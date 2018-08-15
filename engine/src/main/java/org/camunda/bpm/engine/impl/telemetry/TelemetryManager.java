package org.camunda.bpm.engine.impl.telemetry;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;

public class TelemetryManager implements Runnable {

  private long lastTransmit;

  private long counter = 0;

  private ArrayBlockingQueue<ProbeEvent> events = new ArrayBlockingQueue<ProbeEvent>(1024);

  private volatile boolean active = true;

  private Map<String, Long> commandCounts = new HashMap<String, Long>();
  private Map<String, String> environmentEvents = new HashMap<String, String>();
  private Map<String, Long> errorLog = new HashMap<String, Long>();
  private Map<String, Long> bpmnSymbolCounts = new HashMap<String, Long>();

  public void reportEvent(ProbeEvent event) {
    events.offer(event);
  }

  @Override
  public void run() {
    while (active && !Thread.interrupted()) {
      try {
        ProbeEvent event = events.poll(5, TimeUnit.SECONDS);

        if (event != null) {
          onEvent(event);
        }

        if (shouldTransmit()) {
          transmit();
        }
      } catch (InterruptedException e) {
        // ignore
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
  }

  private void onEvent(ProbeEvent event) {
    ProbeEventType type = event.getType();

    switch (type) {
    case COMMAND_INVOKED:
      onCommandInvoked((CommandInvokedProbeEvent) event);
      break;
    case ENVIRONMENT:
      onEnvironmentEvent((EnvironmentProbeEvent) event);
      break;
    case COMMAND_FAILED:
      onCommandFailed((CommandErrorProbeEvent) event);
      break;
    case DEPLOYMENT:
      onDeploymentEvent((DeploymentProbeEvent) event);
      break;

    default:
      break;
    }
  }

  private void onDeploymentEvent(DeploymentProbeEvent event) {
    BpmnModelInstance instance = Bpmn.readModelFromStream(new ByteArrayInputStream(event.getResource()));

    Collection<FlowNode> flowNodes = instance.getModelElementsByType(FlowNode.class);

    for (FlowNode flowNode : flowNodes) {
      String type = flowNode.getElementType().getTypeName();

      Long value = bpmnSymbolCounts.get(type);

      if (value == null) {
        value = 0L;
      }

      value++;

      bpmnSymbolCounts.put(type, value);
    }
  }

  private void onCommandFailed(CommandErrorProbeEvent event) {
    String stacktrace = event.getExceptionClass();

    Long value = errorLog.get(stacktrace);

    if (value == null) {
      value = 0L;
    }

    value++;

    errorLog.put(stacktrace, value);
  }

  private void onEnvironmentEvent(EnvironmentProbeEvent event) {
    environmentEvents.putAll(event.getValues());
  }

  private void onCommandInvoked(CommandInvokedProbeEvent event) {
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
    counter++;
    return counter % 2 == 0;
  }

  private void transmit() {

    TelemetryRequest telemetryRequest = new TelemetryRequest();

    // command invocations
    if (!commandCounts.isEmpty()) {
      TelemetryEvent commandInvocationsEvent = new TelemetryEvent();
      commandInvocationsEvent.setType("CommandInvocation");
      commandInvocationsEvent.setPayload(new HashMap<String, Object>(commandCounts));
      telemetryRequest.getEvents().add(commandInvocationsEvent);
      commandCounts.clear();
    }

    // environment
    if (!environmentEvents.isEmpty()) {
      TelemetryEvent environmentEvent = new TelemetryEvent();
      environmentEvent.setType("Environment");
      environmentEvent.setPayload(new HashMap<String, Object>(environmentEvents));
      telemetryRequest.getEvents().add(environmentEvent);
      environmentEvents.clear();
    }

    // error log
    if (!errorLog.isEmpty()) {
      TelemetryEvent environmentEvent = new TelemetryEvent();
      environmentEvent.setType("ExceptionLog");
      environmentEvent.setPayload(new HashMap<String, Object>(errorLog));
      telemetryRequest.getEvents().add(environmentEvent);
      errorLog.clear();
    }

    // symbol log
    if (!bpmnSymbolCounts.isEmpty()) {
      TelemetryEvent environmentEvent = new TelemetryEvent();
      environmentEvent.setType("BpmnSymbolCount");
      environmentEvent.setPayload(new HashMap<String, Object>(bpmnSymbolCounts));
      telemetryRequest.getEvents().add(environmentEvent);
      bpmnSymbolCounts.clear();
    }

    if (!telemetryRequest.getEvents().isEmpty()) {
      try {
        final URL url = new URL("http://localhost:8080/events");
//        final URL url = new URL("http://192.168.178.43:8080/events");
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");

        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

        String json = new JSONObject(telemetryRequest).toString();
        System.out.println(json);
        wr.writeBytes(json);
        wr.flush();
        wr.close();

        connection.getResponseCode();

      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
