package org.camunda.bpm.engine.impl.telemetry;

public class ProbeEvent {

  private ProbeEventType type;

  private long timestamp;

  public ProbeEvent(ProbeEventType type) {
    this.type = type;
  }

  public void setType(ProbeEventType type) {
    this.type = type;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public ProbeEventType getType() {
    return type;
  }
}
