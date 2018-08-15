package org.camunda.bpm.engine.impl.telemetry;

public class DeploymentProbeEvent extends ProbeEvent {

  public DeploymentProbeEvent() {
    super(ProbeEventType.DEPLOYMENT);
  }

  private byte[] resource;

  public void setResource(byte[] resource) {
    this.resource = resource;
  }

  public byte[] getResource() {
    return resource;
  }

}
