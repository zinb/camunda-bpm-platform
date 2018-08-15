package org.camunda.bpm.engine.impl.telemetry;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentProbeEvent extends ProbeEvent {

  public EnvironmentProbeEvent() {
    super(ProbeEventType.ENVIRONMENT);
  }

  private Map<String, String> values = new HashMap<String, String>();

  public Map<String, String> getValues() {
    return values;
  }

  public void setValues(Map<String, String> values) {
    this.values = values;
  }

}
