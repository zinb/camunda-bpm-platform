package org.camunda.bpm.engine.impl.telemetry;

public class CommandInvokedTelemetryEvent extends TelemetryEvent {

  public CommandInvokedTelemetryEvent() {
    super(TelemetryEventType.COMMAND_INVOKED);
  }

  private String commandName;

  public void setCommandName(String commandName) {
    this.commandName = commandName;
  }

  public String getCommandName() {
    return commandName;
  }

}
