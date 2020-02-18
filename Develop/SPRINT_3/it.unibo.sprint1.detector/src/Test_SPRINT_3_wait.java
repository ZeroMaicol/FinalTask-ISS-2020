
import static org.junit.Assert.*;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Test;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;

public class Test_SPRINT_3_wait {

	// coap
	private String resDetectorPosition = "coap://localhost:5683/wroom/detectorPosition";
	private String resDetectorBox = "coap://localhost:5683/wroom/detectorBox";
	private String resPlasticBox = "coap://localhost:5683/wroom/plasticBox";
	// paho
	private String broker = "tcp://localhost";
	private String detectorTopic = "unibo/qak/detector";
	private String msgContentExplore = "msg(explore,dispatch,js,detector,explore(1),1)";
	private String msgContentSuspend = "msg(suspend,dispatch,js,detector,suspend(1),1)";
    private String clientId = "sprint_1";
    private MemoryPersistence persistence = new MemoryPersistence();
    private int qos = 2;
    
    //Test
	
    
	@Test
	public void sprint_1_Test() {
		
		CoapClient detectorPosition = new CoapClient(resDetectorPosition);
		CoapClient detectorBox = new CoapClient(resDetectorBox);
		CoapClient plasticBox = new CoapClient(resPlasticBox);
		
		plasticBox.put("2", MediaTypeRegistry.TEXT_PLAIN);
		detectorBox.put("1", MediaTypeRegistry.TEXT_PLAIN);
		detectorBox.put("1", MediaTypeRegistry.TEXT_PLAIN);
		detectorBox.put("1", MediaTypeRegistry.TEXT_PLAIN);
		
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
		             40 *   // seconds to a minute
		             1000);
            Thread.sleep(200);
            
            System.out.println("box: "+detectorBox.get().getResponseText().substring(0, 1));
            assertTrue("detectorbox is full?", detectorBox.get().getResponseText().substring(0, 1).equals("5"));
            assertTrue("plasticbox is full?", plasticBox.get().getResponseText().substring(0, 1).equals("2"));
            
    		String v = "state("+detectorPosition.get().getResponseText()+")";
    		String coordinates = ( (Struct) Term.createTerm(v)).getArg(0).toString();
    		String lastCoordinates = coordinates;
    		assertTrue("is the detector exploring?", !coordinates.equals("pos(0,0)"));
    		
    		Thread.sleep(200);
    		
    		v = "state("+detectorPosition.get().getResponseText()+")";
    		coordinates = ( (Struct) Term.createTerm(v)).getArg(0).toString();
    		assertTrue("is the detector still in the same position?", coordinates.equals(lastCoordinates));
    		
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
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		String v = "state("+detectorPosition.get().getResponseText()+")";
        
		String coordinates = ( (Struct) Term.createTerm(v)).getArg(0).toString();
		System.out.println("box: "+detectorBox.get().getResponseText().substring(0, 1));
		
		assertTrue("is the detector back at home?", coordinates.equals("pos(0,0)"));
			
	}

}
