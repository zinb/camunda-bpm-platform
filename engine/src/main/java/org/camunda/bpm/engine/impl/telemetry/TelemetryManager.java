package org.camunda.bpm.engine.impl.telemetry;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;

import org.camunda.bpm.engine.impl.Condition;

public class TelemetryManager implements Runnable {

  private long lastTransmit;

  private ArrayBlockingQueue<TelemetryEvent> events = new ArrayBlockingQueue<TelemetryEvent>(1024);

  private volatile boolean active = true;

  private Map<String, Long> commandCounts = new HashMap<String, Long>();

  public void reportEvent(TelemetryEvent event) {
    events.offer(event);
  }

  @Override
  public void run() {
    while (active && !Thread.interrupted()) {
      try {
        TelemetryEvent event = events.take();
        onEvent(event);

        if (shouldTransmit()) {
          transmit();
        }
      }
      catch (InterruptedException e) {
        // ignore
      }
      catch (Throwable t) {
        t.printStackTrace();
      }
    }
  }

  private void onEvent(TelemetryEvent event) {
    TelemetryEventType type = event.getType();

    switch (type) {
    case COMMAND_INVOKED:
      onCommandInvoked((CommandInvokedTelemetryEvent) event);
      break;

    default:
      break;
    }
  }

  private void onCommandInvoked(CommandInvokedTelemetryEvent event) {
    String commandName = event.getCommandName();

    Long value = commandCounts.get(commandName);

    if (value == null) {
      value = 0L;
    }

    value++;

    commandCounts.put(commandName, value);

  }

  public void shutdown(Thread thread) {
    active = false;
    thread.interrupt();
  }

  private boolean shouldTransmit() {
    return true;
  }

  private void transmit() {
    System.out.println("----");
    for (Entry<String, Long> entry : commandCounts.entrySet()) {
      System.out.format("%s: %d\n", entry.getKey(), entry.getValue());
    }
    System.out.println("----");
  }

}
