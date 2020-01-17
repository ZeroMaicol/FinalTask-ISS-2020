%====================================================================================
% wroomserver description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxwroom, "localhost",  "MQTT", "8040").
 qactor( wroomserver, ctxwroom, "it.unibo.wroomserver.Wroomserver").
