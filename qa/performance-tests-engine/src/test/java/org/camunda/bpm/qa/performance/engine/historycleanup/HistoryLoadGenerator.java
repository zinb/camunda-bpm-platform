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
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.qa.performance.engine.junit.PerfTestProcessEngine;
import org.camunda.bpm.qa.performance.engine.loadgenerator.LoadGenerator;
import org.camunda.bpm.qa.performance.engine.loadgenerator.LoadGeneratorConfiguration;
import org.camunda.bpm.qa.performance.engine.loadgenerator.tasks.DeployModelInstancesTask;
import org.camunda.bpm.qa.performance.engine.loadgenerator.tasks.StartProcessInstanceTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Daniel Meyer
 * @author Nikola Koevski
 *
 */
public class HistoryLoadGenerator {

  public static void main(String[] args) throws InterruptedException {

    final Properties properties = PerfTestProcessEngine.loadProperties();
    final ProcessEngine processEngine = PerfTestProcessEngine.getInstance();

    // set config properties
    final LoadGeneratorConfiguration config = new LoadGeneratorConfiguration();
    config.setColor(Boolean.parseBoolean(properties.getProperty("loadGenerator.colorOutput", "false")));
    config.setNumberOfIterations(Integer.parseInt(properties.getProperty("loadGenerator.numberOfIterations", "10000")));

    // generate & deploy models
    int levels = Integer.parseInt(properties.getProperty("loadGenerator.processInstanceLevels", "5"));
    final List<BpmnModelInstance> modelInstances = getHierarchicalProcessesList(levels);
    Runnable[] setupTasks = new Runnable[] {
        new DeployModelInstancesTask(processEngine, modelInstances)
    };
    config.setSetupTasks(setupTasks);

    // set worker task for starting process instances
    final Runnable[] workerRunnables = new Runnable[1];
    String processDefKey = modelInstances
      .get(modelInstances.size() - 1)
      .getModelElementsByType(Process.class)
      .iterator()
      .next()
      .getId();
    workerRunnables[0] = new StartProcessInstanceTask(processEngine, processDefKey);
    config.setWorkerTasks(workerRunnables);

    // generate load
    new LoadGenerator(config).execute();
    System.out.println(processEngine.getHistoryService().createHistoricProcessInstanceQuery().count()+ " Process Instances in DB");
  }

  static List<BpmnModelInstance> getHierarchicalProcessesList(int levels) {
    List<BpmnModelInstance> descendantModelInstances = new ArrayList<BpmnModelInstance>(levels);
    if (levels <= 1) {
      descendantModelInstances.add(Bpmn.createExecutableProcess("nestedProcess" + levels)
        .camundaHistoryTimeToLive(levels)
        .startEvent()
        .endEvent()
        .done());
      return descendantModelInstances;
    }

    int nextLevel = levels - 1;
    descendantModelInstances = getHierarchicalProcessesList(nextLevel);
    descendantModelInstances.add(Bpmn.createExecutableProcess("nestedProcess" + levels)
      .camundaHistoryTimeToLive(levels)
      .startEvent()
      .callActivity()
        .calledElement("nestedProcess" + nextLevel)
      .endEvent()
      .done());

    return descendantModelInstances;
  }

}
