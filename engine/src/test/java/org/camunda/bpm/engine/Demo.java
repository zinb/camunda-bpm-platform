package org.camunda.bpm.engine;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

public class Demo {

  public static void main(String[] args) {


    ProcessEngineConfigurationImpl createStandaloneInMemProcessEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngineConfigurationImpl.createStandaloneInMemProcessEngineConfiguration();

    ProcessEngine processEngine = createStandaloneInMemProcessEngineConfiguration
      .buildProcessEngine();

    RepositoryService repositoryService = processEngine.getRepositoryService();
    RuntimeService runtimeService = processEngine.getRuntimeService();

    BpmnModelInstance modelInstance = Bpmn.createExecutableProcess("testProcess")
      .startEvent()
      .endEvent()
      .done();

    repositoryService.createDeployment()
      .addModelInstance("testProcess.bpmn", modelInstance)
      .deploy();

    runtimeService.startProcessInstanceByKey("testProcess");
    runtimeService.startProcessInstanceByKey("testProcess");
//    runtimeService.startProcessInstanceByKey("foo");
    runtimeService.startProcessInstanceByKey("testProcess");

    System.out.println("foo");

    processEngine.close();


  }

}
