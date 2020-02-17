
import static org.junit.Assert.*;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Test;

public class Test_SPRINT_2_plasticBox_empty {

	// coap
	private String detectorBoxRes = "coap://localhost:5683/wroom/detectorBox";
	private String plasticBoxRes = "coap://localhost:5683/wroom/plasticBox";
	// paho
	private String broker = "tcp://localhost";
	private String detectorTopic = "unibo/qak/detector";
	private String msgContent = "msg(explore,request,js,detector,explore(1),1)";
    private String clientId = "sprint_1";
    private MemoryPersistence persistence = new MemoryPersistence();
    private int qos = 2;
	
	@Test
	public void sprint_1_Test() {
		CoapClient detectorBoxClient = new CoapClient(detectorBoxRes);
		CoapClient plasticBoxClient = new CoapClient(plasticBoxRes);
		assertTrue("detectorBox is empty?", detectorBoxClient.get().getResponseText().substring(0, 1).equals("0"));
		assertTrue("plasticBox is empty?", plasticBoxClient.get().getResponseText().substring(0, 1).equals("0"));
		
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
		assertTrue("detector has emptied his box?", detectorBoxClient.get().getResponseText().substring(0, 1).equals("0"));
		System.out.println(plasticBoxClient.get().getResponseText());
		System.out.println(plasticBoxClient.get().getResponseText().substring(0, 1));
		assertTrue("plastic box has received a bottle?", plasticBoxClient.get().getResponseText().substring(0, 1).equals("1"));
	}

}
