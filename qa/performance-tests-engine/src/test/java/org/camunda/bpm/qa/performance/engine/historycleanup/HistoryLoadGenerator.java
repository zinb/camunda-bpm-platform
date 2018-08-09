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
package org.camunda.bpm.qa.performance.engine.historycleanup;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.qa.performance.engine.junit.PerfTestProcessEngine;
import org.camunda.bpm.qa.performance.engine.loadgenerator.LoadGenerator;
import org.camunda.bpm.qa.performance.engine.loadgenerator.LoadGeneratorConfiguration;
import org.camunda.bpm.qa.performance.engine.loadgenerator.tasks.DeployModelInstancesTask;
import org.camunda.bpm.qa.performance.engine.loadgenerator.tasks.GenerateMetricsTask;
import org.camunda.bpm.qa.performance.engine.loadgenerator.tasks.StartProcessInstanceTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Daniel Meyer
 *
 */
public class HistoryLoadGenerator {

  /**
   * The reported ID for the metrics.
   */
  protected static final String REPORTER_ID = "REPORTER_ID";

  public static void main(String[] args) throws InterruptedException {

    final Properties properties = PerfTestProcessEngine.loadProperties();
    final ProcessEngine processEngine = PerfTestProcessEngine.getInstance();

    final LoadGeneratorConfiguration config = new LoadGeneratorConfiguration();
    config.setColor(Boolean.parseBoolean(properties.getProperty("loadGenerator.colorOutput", "false")));
    config.setNumberOfIterations(Integer.parseInt(properties.getProperty("loadGenerator.numberOfIterations", "10000")));
    int levels = Integer.valueOf(properties.getProperty("loadGenerator.processInstanceLevels", "5"));

    final List<BpmnModelInstance> modelInstances = getHierarchicalProcessesList(levels);

    Runnable[] setupTasks = new Runnable[] {
        new DeployModelInstancesTask(processEngine, modelInstances)
    };
    config.setSetupTasks(setupTasks);

    ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
    processEngineConfiguration.setMetricsEnabled(true);
    processEngineConfiguration.getDbMetricsReporter().setReporterId(REPORTER_ID);
    final Runnable[] workerRunnables = new Runnable[2];
    Process process = modelInstances.get(modelInstances.size() - 1).getModelElementsByType(Process.class).iterator().next();
    String processDefKey = process.getId();
    workerRunnables[0] = new StartProcessInstanceTask(processEngine, processDefKey);
    workerRunnables[1] = new GenerateMetricsTask(processEngine);
    config.setWorkerTasks(workerRunnables);

    new LoadGenerator(config).execute();

    System.out.println(processEngine.getHistoryService().createHistoricProcessInstanceQuery().count()+ " Process Instances in DB");
    processEngineConfiguration.setMetricsEnabled(false);
  }

  static List<BpmnModelInstance> getHierarchicalProcessesList(int levels) {
    List<BpmnModelInstance> descendantModelInstances = new ArrayList<BpmnModelInstance>(levels);
    if (levels == 1) {
      descendantModelInstances.add(Bpmn.createExecutableProcess("nestedProcess" + levels)
        .startEvent()
        .endEvent()
        .done());
      return descendantModelInstances;
    }

    int nextLevel = levels - 1;
    descendantModelInstances = getHierarchicalProcessesList(nextLevel);
    descendantModelInstances.add(Bpmn.createExecutableProcess("nestedProcess" + levels)
      .startEvent()
      .callActivity()
        .calledElement("nestedProcess" + nextLevel)
      .endEvent()
      .done());

    return descendantModelInstances;
  }

}
