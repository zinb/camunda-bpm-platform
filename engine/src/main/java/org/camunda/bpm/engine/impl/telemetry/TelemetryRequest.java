package org.camunda.bpm.engine.impl.telemetry;

import java.util.ArrayList;
import java.util.List;

public class TelemetryRequest {

  private String cid;

  private List<TelemetryEvent> events = new ArrayList<TelemetryEvent>();

  public String getCid() {
    return cid;
  }

  public void setCid(String cid) {
    this.cid = cid;
  }

  public List<TelemetryEvent> getEvents() {
    return events;
  }

  public void setEvents(List<TelemetryEvent> events) {
    this.events = events;
  }


}
