package org.camunda.bpm.qa.performance.engine.historycleanuplog;

import org.camunda.bpm.qa.performance.engine.framework.PerfTest;
import org.camunda.bpm.qa.performance.engine.framework.PerfTestPass;
import org.camunda.bpm.qa.performance.engine.framework.PerfTestRun;
import org.camunda.bpm.qa.performance.engine.framework.PerfTestStep;
import org.camunda.bpm.qa.performance.engine.framework.PerfTestWatcher;

/**
 * @author Nikola Koevski
 */
public class HistoryCleanupLogPerfTestWatcher implements PerfTestWatcher {

  @Override
  public void beforePass(PerfTestPass pass) {
    // nothing to do
  }

  @Override
  public void beforeRun(PerfTest test, PerfTestRun run) {
    // nothing to do
  }

  @Override
  public void beforeStep(PerfTestStep step, PerfTestRun run) {
    // nothing to do
  }

  @Override
  public void afterStep(PerfTestStep step, PerfTestRun run) {
    // nothing to do
  }

  @Override
  public void afterRun(PerfTest test, PerfTestRun run) {
    // TODO?
  }

  @Override
  public void afterPass(PerfTestPass pass) {
    // nothing to do
  }
}
