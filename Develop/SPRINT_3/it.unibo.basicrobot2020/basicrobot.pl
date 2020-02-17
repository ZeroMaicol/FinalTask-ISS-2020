%====================================================================================
% basicrobot description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxbasicrobot, "localhost",  "MQTT", "8018").
 qactor( robotadapter, ctxbasicrobot, "itunibo.robot.robotAdapterQaStream").
  qactor( basicrobot, ctxbasicrobot, "it.unibo.basicrobot.Basicrobot").
