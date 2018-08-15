package org.camunda.bpm.engine.impl.telemetry;

public class CommandErrorProbeEvent extends ProbeEvent {

  public CommandErrorProbeEvent() {
    super(ProbeEventType.COMMAND_FAILED);
  }

  private String stacktrace;
  private String exceptionClass;

  public void setStacktrace(String stacktrace) {
    this.stacktrace = stacktrace;
  }

  public String getStacktrace() {
    return stacktrace;
  }

  public String getExceptionClass() {
    return exceptionClass;
  }

  public void setExceptionClass(String exceptionClass) {
    this.exceptionClass = exceptionClass;
  }

}
