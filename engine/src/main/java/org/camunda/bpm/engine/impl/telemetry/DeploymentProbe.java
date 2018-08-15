package org.camunda.bpm.engine.impl.telemetry;

public class DeploymentProbe {

  private TelemetryManager telemetryManager;

  public DeploymentProbe(TelemetryManager telemetryManager) {
    this.telemetryManager = telemetryManager;
  }

  public void onBpmnProcessDeployed(byte[] bytes) {
    DeploymentProbeEvent event = new DeploymentProbeEvent();
    event.setResource(bytes);
    telemetryManager.reportEvent(event);
  }

}
