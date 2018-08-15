package org.camunda.bpm.engine.impl.telemetry;

import java.util.Map;
import java.util.Properties;

public class StartupProbe {

  private TelemetryManager telemetryManager;

  public StartupProbe(TelemetryManager telemetryManager) {
    this.telemetryManager = telemetryManager;
  }

  public void onStartup() {
    Properties properties = System.getProperties();

    EnvironmentProbeEvent event = new EnvironmentProbeEvent();

    Map<String, String> envVars = event.getValues();
    envVars
      .put("java.version", properties.getProperty("java.version"));
    envVars
      .put("java.vendor", properties.getProperty("java.vendor"));
    envVars
      .put("os.name", properties.getProperty("os.name"));

    telemetryManager.reportEvent(event);
  }

}
