%====================================================================================
% wroom description   
%====================================================================================
context(ctxwroom, "localhost",  "TCP", "8020").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8018").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( detector, ctxwroom, "it.unibo.detector.Detector").
  qactor( detectorbox, ctxwroom, "it.unibo.detectorbox.Detectorbox").
