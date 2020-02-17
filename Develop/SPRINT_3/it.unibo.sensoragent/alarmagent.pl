%====================================================================================
% alarmagent description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxalarmagent, "localhost",  "MQTT", "8060").
context(ctxwroom, "127.0.0.1",  "TCP", "8020").
 qactor( detector, ctxwroom, "external").
  qactor( alarmagent, ctxalarmagent, "it.unibo.alarmagent.Alarmagent").
  qactor( perceiver, ctxalarmagent, "it.unibo.perceiver.Perceiver").
