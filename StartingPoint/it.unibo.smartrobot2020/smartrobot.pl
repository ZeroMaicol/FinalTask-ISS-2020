%====================================================================================
% smartrobot description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxsmartrobot, "localhost",  "MQTT", "8020").
context(ctxbasicrobot, "127.0.0.1",  "MQTT", "8018").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( smartrobot, ctxsmartrobot, "it.unibo.smartrobot.Smartrobot").
  qactor( walker, ctxsmartrobot, "it.unibo.walker.Walker").
