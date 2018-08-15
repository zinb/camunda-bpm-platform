package org.camunda.bpm.engine.impl.telemetry;

public class CommandInvokedProbeEvent extends ProbeEvent {

  public CommandInvokedProbeEvent() {
    super(ProbeEventType.COMMAND_INVOKED);
  }

  private String commandName;

  public void setCommandName(String commandName) {
    this.commandName = commandName;
  }

  public String getCommandName() {
    return commandName;
  }

}
