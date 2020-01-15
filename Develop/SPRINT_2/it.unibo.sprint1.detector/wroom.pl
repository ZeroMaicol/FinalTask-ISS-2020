%====================================================================================
% wroom description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxwroom, "localhost",  "MQTT", "8020").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8018").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( detector, ctxwroom, "it.unibo.detector.Detector").
  qactor( detectorbox, ctxwroom, "it.unibo.detectorbox.Detectorbox").
