
import static org.junit.Assert.*;

import java.util.HashMap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Test;
import alice.tuprolog.Term;
import alice.tuprolog.Struct;

public class Test_SPRINT_2_suspend {

	// coap
	private String resDetectorPosition = "coap://localhost:5683/wroom/detectorPosition";
	// paho
	private String broker = "tcp://localhost";
	private String detectorTopic = "unibo/qak/detector";
	private String msgContentExplore = "msg(explore,dispatch,js,detector,explore(1),1)";
	private String msgContentSuspend = "msg(suspend,dispatch,js,detector,explore(1),1)";
    private String clientId = "sprint_1";
    private MemoryPersistence persistence = new MemoryPersistence();
    private int qos = 2;
    //Test
	
    
	@Test
	public void sprint_1_Test() {
		
		CoapClient detectorPosition = new CoapClient(resDetectorPosition);
		
		try {
			MqttClient publisherClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            publisherClient.connect(connOpts);
            assertTrue("client is connected?", publisherClient.isConnected());
            MqttMessage message = new MqttMessage(msgContentExplore.getBytes());
            message.setQos(qos);
            publisherClient.publish(detectorTopic, message);
            
            Thread.sleep(  // minutes to sleep
		             10 *   // seconds to a minute
		             1000); // milliseconds to a second
            
            
            String v = "state("+detectorPosition.get().getResponseText()+")";
            
    		String coordinates = ( (Struct) Term.createTerm(v)).getArg(0).toString();
    		String idle = ( (Struct) Term.createTerm(v)).getArg(2).toString();
    		System.out.println(v+" "+coordinates+" "+idle);
    		
    		assertTrue("is the detector exploring?", !coordinates.equals("pos(0,0)"));
    		//assertTrue("is the detector moving?", idle!="moving(idle)" );
    		
            MqttMessage messageSuspend = new MqttMessage(msgContentSuspend.getBytes());
            messageSuspend.setQos(qos);
            publisherClient.publish(detectorTopic, messageSuspend);
            publisherClient.disconnect();
            publisherClient.close();
		} catch (MqttException | InterruptedException me) {
            me.printStackTrace();
		}
		
		try {
			Thread.sleep(  // minutes to sleep
			             20 *   // seconds to a minute
			             1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String v = "state("+detectorPosition.get().getResponseText()+")";
        
		String coordinates = ( (Struct) Term.createTerm(v)).getArg(0).toString();
		//String idle = ( (Struct) Term.createTerm(v)).getArg(2).toString();
		System.out.println("coord: "+coordinates);
		assertTrue("is the detector back at home?", coordinates.equals("pos(0,0)"));
		//assertTrue("is the detector not moving?", idle=="moving(idle)" );
		
	}

}
