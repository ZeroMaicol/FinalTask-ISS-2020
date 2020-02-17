%====================================================================================
% plasticbox description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxwroom, "localhost",  "MQTT", "8030").
 qactor( plasticboxadapter, ctxwroom, "plasticBox.plasticBoxAdapter").
  qactor( plasticbox, ctxwroom, "it.unibo.plasticbox.Plasticbox").
