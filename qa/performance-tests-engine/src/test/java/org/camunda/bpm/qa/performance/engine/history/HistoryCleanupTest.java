/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.qa.performance.engine.history;

import org.camunda.bpm.engine.impl.util.ClockUtil;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.qa.performance.engine.junit.ProcessEnginePerformanceTestCase;
import org.camunda.bpm.qa.performance.engine.steps.HistoryCleanupStep;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tassilo Weidner
 */
public class HistoryCleanupTest extends ProcessEnginePerformanceTestCase {

  @Before
  public void setUp() {
    testConfigurationRule.getPerformanceTestConfiguration().setHistoryLevel("full");
    testConfigurationRule.getPerformanceTestConfiguration().setNumberOfThreads(1);
    testConfigurationRule.getPerformanceTestConfiguration().setNumberOfRuns(1);
  }

  @Test
  @Deployment
  public void historyCleanup() throws Exception {
    ClockUtil.setCurrentTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("13/11/2036 11:12:30"));

    Map<String, Object> variables = new HashMap<String, Object>();
    int variablesCount = 50;
    for (int i = 0; i < variablesCount; i++) {
      variables.put("varName" + i, "value");
    }

    int processInstancesCount = 1000;
    int activitiesCount = 20;
    for (int i = 0; i < processInstancesCount; i++) {
      runtimeService.startProcessInstanceByKey("process", variables);

      for (int j = 0; j < activitiesCount; j++) {
        String jobId = engine.getManagementService().createJobQuery()
          .processDefinitionKey("process").singleResult().getId();
        engine.getManagementService().executeJob(jobId);
      }
    }

    ClockUtil.setCurrentTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("15/11/2036 11:12:30"));

    String jobId = historyService.cleanUpHistoryAsync(true).getId();

    Assert.assertEquals(processInstancesCount, historyService.createHistoricProcessInstanceQuery().list().size());

    performanceTest().step(new HistoryCleanupStep(engine, jobId, processInstancesCount)).run();

    Assert.assertEquals(0, historyService.createHistoricProcessInstanceQuery().list().size());
  }
  
}
