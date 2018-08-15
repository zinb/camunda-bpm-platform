package org.camunda.bpm.engine.impl.telemetry;

public class CommandErrorProbeEvent extends ProbeEvent {

  public CommandErrorProbeEvent() {
    super(ProbeEventType.COMMAND_FAILED);
  }

  private String stacktrace;

  public void setStacktrace(String stacktrace) {
    this.stacktrace = stacktrace;
  }

  public String getStacktrace() {
    return stacktrace;
  }

}
