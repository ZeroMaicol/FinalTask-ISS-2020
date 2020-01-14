package sprinttests;

import static org.junit.Assert.*;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Test;

public class Sprint_1 {

	// coap
	private String coapResource = "coap://localhost:5683/wroom/detectorBox";
	// paho
	private String broker = "tcp://localhost";
	private String detectorTopic = "unibo/qak/detector";
	private String msgContent = "msg(explore,request,js,detector,explore(1),1)";
    private String clientId = "sprint_1";
    private MemoryPersistence persistence = new MemoryPersistence();
    private int qos = 2;
	
	@Test
	public void sprint_1_Test() {
		CoapClient readerClient = new CoapClient(coapResource);
		assertTrue("detectorbox is empty?", readerClient.get().getResponseText().equals("0"));

		try {
			MqttClient publisherClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            publisherClient.connect(connOpts);
            assertTrue("client is connected?", publisherClient.isConnected());
            MqttMessage message = new MqttMessage(msgContent.getBytes());
            message.setQos(qos);
            publisherClient.publish(detectorTopic, message);
            publisherClient.disconnect();
            publisherClient.close();
		} catch (MqttException me) {
			System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
		}
		
		
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue("detector has found the bottle in 5 seconds?", readerClient.get().getResponseText().equals("1"));
	}

}
