%====================================================================================
% smartrobot description   
%====================================================================================
context(ctxsmartrobot, "localhost",  "TCP", "8020").
context(ctxbasicrobot, "192.168.1.6",  "TCP", "8018").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( smartrobot, ctxsmartrobot, "it.unibo.smartrobot.Smartrobot").
  qactor( perceiver, ctxsmartrobot, "it.unibo.perceiver.Perceiver").
