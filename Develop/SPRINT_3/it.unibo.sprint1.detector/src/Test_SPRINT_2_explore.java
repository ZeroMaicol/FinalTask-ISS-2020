
import static org.junit.Assert.*;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Test;

import itunibo.planner.model.RoomMap;

public class Test_SPRINT_2_explore {

	// coap
	private String detectorBoxRes = "coap://localhost:5683/wroom/detectorBox";
	private String plasticBoxRes = "coap://localhost:5683/wroom/plasticBox";
	private String roomMapRes = "coap://localhost:5683/wroom/roomMap";
	// paho
	private String broker = "tcp://localhost";
	private String detectorTopic = "unibo/qak/detector";
	private String msgContent = "msg(explore,dispatch,js,detector,explore(1),1)";
    private String clientId = "sprint_1";
    private MemoryPersistence persistence = new MemoryPersistence();
    private int qos = 2;
    
    //Test
    private static final long X = 15; //Number of walls "X" in map
    private static final long ONE = 18; //Number of "1" in map
    private static final String BOTTLES = "2"; //Bottles to collect
    private static final String MAP_START = "map()";
	
	@Test
	public void sprint_1_Test() {
		
		//it.unibo.ctxWroom.MainCtxWroomKt.main();
		
		
		CoapClient detectorBoxClient = new CoapClient(detectorBoxRes);
		CoapClient plasticBoxClient = new CoapClient(plasticBoxRes);
		CoapClient roomMapClient = new CoapClient(roomMapRes);
		
		assertTrue("detectorbox is empty?", detectorBoxClient.get().getResponseText().substring(0, 1).equals("0"));
		System.out.println("MAP:"+roomMapClient.get().getResponseText());
		assertTrue("map is correctly initialized?", roomMapClient.get().getResponseText().equals(MAP_START));
		
		
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
			Thread.sleep(2 *   // minutes to sleep
		             60 *   // seconds to a minute
		             1000); // milliseconds to a second
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.println(roomMapClient.get().getResponseText());
		System.out.println(RoomMap.getRoomMap().toString());
		String map = roomMapClient.get().getResponseText();
//		long x_count = map.chars().filter(ch -> ch =='X').count();
//		long one_count = map.chars().filter(ch -> ch =='1').count();
//		long zero_count = map.chars().filter(ch -> ch == '0').count();
		
//		assertTrue("every wall was found?", x_count == X);
//		assertTrue("every cell was explored?", one_count == ONE);

		assertTrue("is the map right?", RoomMap.fromString(map).isFullyExplored());
		assertTrue("bottle emptied?", detectorBoxClient.get().getResponseText().substring(0, 1).equals("0"));
		assertTrue("bottle collected?", plasticBoxClient.get().getResponseText().substring(0, 1).equals(BOTTLES));
		
	}
}
