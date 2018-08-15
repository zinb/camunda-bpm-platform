package org.camunda.bpm.engine.impl.telemetry;

import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandInterceptor;
import org.camunda.bpm.engine.impl.util.ClockUtil;

public class CommandProbe extends CommandInterceptor {

  protected TelemetryManager telemetryManager;

  public CommandProbe(TelemetryManager telemetryManager) {
    this.telemetryManager = telemetryManager;
  }

  @Override
  public <T> T execute(Command<T> command) {

    try {
      CommandInvokedTelemetryEvent commandInvokedTelemetryEvent = new CommandInvokedTelemetryEvent();

      commandInvokedTelemetryEvent.setCommandName(command.getClass().getName());
      commandInvokedTelemetryEvent.setTimestamp(ClockUtil.getCurrentTime().getTime());

      telemetryManager.reportEvent(commandInvokedTelemetryEvent);
    }
    catch (Exception e) {
      e.printStackTrace();
    }


    return next.execute(command);
  }

}
