%====================================================================================
% plasticbox description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxwroom, "localhost",  "MQTT", "8030").
 qactor( plasticbox, ctxwroom, "it.unibo.plasticbox.Plasticbox").
