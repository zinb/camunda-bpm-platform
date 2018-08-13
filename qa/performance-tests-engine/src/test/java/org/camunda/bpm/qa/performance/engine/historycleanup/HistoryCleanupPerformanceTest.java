package org.camunda.bpm.qa.performance.engine.historycleanup;

import org.camunda.bpm.engine.impl.ProcessEngineImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.metrics.Meter;
import org.camunda.bpm.engine.impl.util.ClockUtil;
import org.camunda.bpm.engine.management.Metrics;
import org.camunda.bpm.qa.performance.engine.framework.aggregate.TabularResultSet;
import org.camunda.bpm.qa.performance.engine.framework.report.HtmlReportBuilder;
import org.camunda.bpm.qa.performance.engine.junit.PerfTestProcessEngine;
import org.camunda.bpm.qa.performance.engine.junit.ProcessEngineJobExecutorPerformanceTestCase;
import org.camunda.bpm.qa.performance.engine.util.CsvUtil;
import org.camunda.bpm.qa.performance.engine.util.FileUtil;
import org.camunda.bpm.qa.performance.engine.util.JsonUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Gather metrics on the HistoryCleanupJob performance.
 *
 * @author Nikola Koevski
 */
public class HistoryCleanupPerformanceTest extends ProcessEngineJobExecutorPerformanceTestCase {

  // Report parameters
  final String reportsFolder = "target"+File.separatorChar+"reports";

  final String csvReportFilename = "history-cleanup-report.csv";
  final String csvReportPath = reportsFolder + File.separatorChar + csvReportFilename;

  final String jsonReportFilename = "history-cleanup-report.json";
  final String jsonReportPath = reportsFolder + File.separatorChar + jsonReportFilename;
  final String htmlReportFilename = reportsFolder + File.separatorChar + "history-cleanup-report.html";

  TabularResultSet resultSet;
  public static final String DESCRIPTION = "Description";
  public static final String DELETED_INSTANCES = "Deleted Instances";
  public static final String ELAPSED_TIME = "Elapsed time (sec)";
  public static final String THROUGHPUT = "Throughput (Deleted Instances/sec)";

  // cleanup check interval
  public static final double INTERVAL = 1000.0;

  protected Date startTime;
  protected Date endTime;
  protected long totalDeletions;
  protected long expectedDeletedInstances;

  ProcessEngineConfigurationImpl engineConfiguration;

  @Before
  public void setUp() throws Exception {
    super.setup();
    this.engineConfiguration = ((ProcessEngineImpl) engine).getProcessEngineConfiguration();

    // load config properties
    Properties properties = PerfTestProcessEngine.loadProperties();

    // make root instances valid for removal
    int levels = Integer.parseInt(properties.getProperty("loadGenerator.processInstanceLevels", "5"));
    Calendar cleanupDays = Calendar.getInstance();
    cleanupDays.add(Calendar.DATE, levels + 1);
    ClockUtil.setCurrentTime(cleanupDays.getTime());

    // set Hierarchical History Cleanup flag
    boolean hierarchicalHistoryCleanup = Boolean.parseBoolean(properties.getProperty("hierarchicalHistoryCleanup", "true"));
    engineConfiguration.setHierarchicalHistoryCleanup(hierarchicalHistoryCleanup);

    int degreeOfParallelism = Integer.parseInt(properties.getProperty("degreeOfParallelism", "3"));
    engineConfiguration.setHistoryCleanupDegreeOfParallelism(degreeOfParallelism);

    // enable metrics
    engineConfiguration.setMetricsEnabled(true);

    // set report data columns
    this.resultSet = new TabularResultSet();
    List<String> columnNames = new ArrayList<String>();
    columnNames.add(DESCRIPTION);
    columnNames.add(DELETED_INSTANCES);
    columnNames.add(ELAPSED_TIME);
    columnNames.add(THROUGHPUT);
    this.resultSet.setResultColumnNames(columnNames);

    // set instances-to-be-deleted number
    this.expectedDeletedInstances = historyService.createHistoricProcessInstanceQuery().count();
  }

  @After
  public void generateReport() {
    List<Object> totalReportRow = new ArrayList<Object>();
    double duration = (endTime.getTime() - startTime.getTime())/1000.0;
    totalReportRow.add("Total");
    totalReportRow.add(totalDeletions);
    totalReportRow.add(duration);
    totalReportRow.add(totalDeletions/duration);
    resultSet.addResultRow(totalReportRow);

    File reportsFolderFile = new File(reportsFolder);
    if(!reportsFolderFile.exists()) {
      reportsFolderFile.mkdir();
    }

    JsonUtil.writeObjectToFile(jsonReportPath, resultSet);
    CsvUtil.saveResultSetToFile(csvReportPath, resultSet);

    String report = new HtmlReportBuilder(resultSet)
      .name("History Cleanup Report")
      .jsonSource(jsonReportFilename)
      .csvSource(csvReportFilename)
      .execute();
    FileUtil.writeStringToFile(report, htmlReportFilename);

    clearMetrics();
  }

  protected void clearMetrics() {
    Collection<Meter> meters = engineConfiguration.getMetricsRegistry().getMeters().values();
    for (Meter meter : meters) {
      meter.getAndClear();
    }
    engine.getManagementService().deleteMetrics(null);
  }

  @Test
  public void performHistoryCleanup() throws InterruptedException {

    startTime = new Date();
    historyService.cleanUpHistoryAsync(true);

    boolean isCleanupDone = false;
    long previousDeletions = 0L;
    int interval = 1;
    while (!isCleanupDone) {

      Thread.sleep((long)INTERVAL);

      totalDeletions = engine.getManagementService()
        .createMetricsQuery()
        .name(Metrics.HISTORY_CLEANUP_REMOVED_PROCESS_INSTANCES)
        .startDate(startTime)
        .sum();

      long intervalDeletions = totalDeletions - previousDeletions;
      previousDeletions = totalDeletions;
      List<Object> reportRow = new ArrayList<Object>();
      reportRow.add("Interval " + interval);
      reportRow.add(intervalDeletions);
      reportRow.add(interval);
      reportRow.add("/");
      resultSet.addResultRow(reportRow);

      interval++;

      isCleanupDone = totalDeletions >= expectedDeletedInstances;
      System.out.println("Deleted instances: " + intervalDeletions);
    }
    endTime = new Date();
  }
}
