package org.camunda.bpm.qa.performance.engine.historycleanup;

import org.camunda.bpm.engine.impl.ProcessEngineImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.qa.performance.engine.framework.PerfTestRunContext;
import org.camunda.bpm.qa.performance.engine.framework.PerfTestStepBehavior;
import org.camunda.bpm.qa.performance.engine.junit.ProcessEngineJobExecutorPerformanceTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

/**
 * Gather metrics on the HistoryCleanupJob performance.
 *
 * @author Nikola Koevski
 */
@RunWith(Parameterized.class)
public class HistoryCleanupPerformanceTest extends ProcessEngineJobExecutorPerformanceTestCase {

  @Parameter(0)
  public static boolean isHierarchicalCleanup;

  @Parameters(name = "With Hierarchical History Cleanup: {0}")
  public static Iterable<Object[]> params() {
    return Arrays.asList(new Object[][] {{true}, {false}});
  }

  @Before
  public void setUp() throws Exception {
    super.setup();
    ProcessEngineConfigurationImpl engineConfiguration = ((ProcessEngineImpl) engine).getProcessEngineConfiguration();
    engineConfiguration.setHierarchicalHistoryCleanup(isHierarchicalCleanup);
  }

  @Test
  public void performHistoryCleanup() {
    performanceTest()
      .step(new PerfTestStepBehavior() {
        @Override
        public void execute(PerfTestRunContext context) {
          historyService.cleanUpHistoryAsync(true);
        }
      })
      .run();
  }
}
