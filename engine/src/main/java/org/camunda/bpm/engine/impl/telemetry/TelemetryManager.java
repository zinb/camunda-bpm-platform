package org.camunda.bpm.engine.impl.telemetry;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.camunda.bpm.engine.impl.util.json.JSONObject;

public class TelemetryManager implements Runnable {

  private long lastTransmit;

  private ArrayBlockingQueue<ProbeEvent> events = new ArrayBlockingQueue<ProbeEvent>(1024);

  private volatile boolean active = true;

  private Map<String, Long> commandCounts = new HashMap<String, Long>();

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
      }
      catch (InterruptedException e) {
        // ignore
      }
      catch (Throwable t) {
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

    default:
      break;
    }
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
    return true;
  }

  private void transmit() {

    try {
      final URL url = new URL("http://192.168.178.43:8080/events");
      final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      connection.setRequestProperty("Accept", "application/json");

      connection.setDoOutput(true);

      DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

      TelemetryEvent event = new TelemetryEvent();
      event.setType("CommandInvocation");
      event.setPayload((Map) commandCounts);

      commandCounts.clear();

      TelemetryRequest telemetryRequest = new TelemetryRequest();
      telemetryRequest.getEvents()
        .add(event);

      wr.writeBytes(new JSONObject(telemetryRequest).toString());
      wr.flush();
      wr.close();

      int responseCode = connection.getResponseCode();
      System.out.println(responseCode);

      BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      //print result
      System.out.println(response.toString());


    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("----");
    for (Entry<String, Long> entry : commandCounts.entrySet()) {
      System.out.format("%s: %d\n", entry.getKey(), entry.getValue());
    }
    System.out.println("----");
  }

}
