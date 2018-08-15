package org.camunda.bpm.engine.impl.telemetry;

public class TelemetryEvent {

  private TelemetryEventType type;

  private long timestamp;

  public TelemetryEvent(TelemetryEventType type) {
    this.type = type;
  }

  public void setType(TelemetryEventType type) {
    this.type = type;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public TelemetryEventType getType() {
    return type;
  }
}
