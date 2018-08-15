package org.camunda.bpm.engine.impl.telemetry;

import java.util.Map;

public class TelemetryEvent {

  private String type;

  private Map<String, Object> payload;

  public void setType(String type) {
    this.type = type;
  }

  public void setPayload(Map<String, Object> payload) {
    this.payload = payload;
  }

  public Map<String, Object> getPayload() {
    return payload;
  }

  public String getType() {
    return type;
  }
}
