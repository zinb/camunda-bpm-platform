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
import org.camunda.bpm.model.bpmn.builder.StartEventBuilder;
import org.camunda.bpm.qa.performance.engine.junit.PerfTestProcessEngine;
import org.camunda.bpm.qa.performance.engine.loadgenerator.LoadGenerator;
import org.camunda.bpm.qa.performance.engine.loadgenerator.LoadGeneratorConfiguration;
import org.camunda.bpm.qa.performance.engine.loadgenerator.tasks.DeployFileTask;
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

  private static final String MULTI_LEVEL_DRG_DMN = "org/camunda/bpm/qa/performance/engine/historycleanup/drdMultiLevelDish.dmn11.xml";

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
    String dmnModelInstancePath = getClasspathResourcePath(MULTI_LEVEL_DRG_DMN);

    Runnable[] setupTasks = new Runnable[] {
        new DeployModelInstancesTask(processEngine, modelInstances),
        new DeployFileTask(processEngine, dmnModelInstancePath)
    };
    config.setSetupTasks(setupTasks);

    // set worker task for starting process instances
    String processDefKey = "nestedProcess" + levels;
    final Runnable[] workerRunnables = {new StartProcessInstanceTask(processEngine, processDefKey)};
    config.setWorkerTasks(workerRunnables);

    // generate load
    new LoadGenerator(config).execute();

    long hpiCount = processEngine.getHistoryService().createHistoricProcessInstanceQuery().count();
    long hdiCount = processEngine.getHistoryService().createHistoricDecisionInstanceQuery().count();
    long totalCount = hpiCount + hdiCount;
    System.out.printf("%d Historic Instances in DB: %d Historic Process Instances, %d Historic Decision Instances\n", totalCount, hpiCount, hdiCount);
  }

  static List<BpmnModelInstance> getHierarchicalProcessesList(int levels) {
    List<BpmnModelInstance> descendantModelInstances = new ArrayList<BpmnModelInstance>(levels);
    if (levels - 2 <= 1) {

      StartEventBuilder modelInstanceBuilder = Bpmn.createExecutableProcess("nestedProcess" + levels)
        .camundaHistoryTimeToLive(levels)
        .startEvent();

      BpmnModelInstance lastModelInstance = (levels < 3)
        ? modelInstanceBuilder
          .endEvent()
          .done()
        : modelInstanceBuilder
          .businessRuleTask("dish-decision")
            .camundaDecisionRef("dish-decision")
            .camundaResultVariable("result")
          .endEvent()
          .done();

      descendantModelInstances.add(lastModelInstance);

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

  protected static String getClasspathResourcePath(String relativePath) {
    Class clazz = HistoryLoadGenerator.class;
    return clazz.getClassLoader().getResource(relativePath).getPath();
  }

}
