package org.camunda.bpm.engine.impl.telemetry;

import java.io.PrintWriter;
import java.io.StringWriter;

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
      CommandInvokedProbeEvent commandInvokedTelemetryEvent = new CommandInvokedProbeEvent();

      commandInvokedTelemetryEvent.setCommandName(command.getClass().getName());
      commandInvokedTelemetryEvent.setTimestamp(ClockUtil.getCurrentTime().getTime());

      telemetryManager.reportEvent(commandInvokedTelemetryEvent);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    try {
      return next.execute(command);
    }
    catch (RuntimeException e) {
      try {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        CommandErrorProbeEvent event = new CommandErrorProbeEvent();
        event.setStacktrace(sw.toString());
        event.setExceptionClass(e.getClass().getCanonicalName());

        telemetryManager.reportEvent(event);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      throw e;
    }
  }

}
