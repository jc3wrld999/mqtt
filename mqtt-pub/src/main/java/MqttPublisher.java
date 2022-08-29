import java.net.SocketException;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class MqttPublisher {

	private static final String BROKER_URL = "tcp://192.168.0.141:1883";

	private static final String TOPIC = "topic/test";
	
	public static void main(String[] args) throws MqttSecurityException, SocketException, MqttException {
		String clientId = "client00";
		// 1. MQTT client instance 생성
		MqttClient client = new MqttClient(BROKER_URL, clientId);
		// 2. Start
		start(client);
	}
	
	private static void start(MqttClient client) throws MqttSecurityException,
	MqttException {
		try {
			// 3. 새 연결 생성
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			if (client != null) {
				client.connect(options);
			 
			    if (!client.isConnected()) {
			        client.publish("topic", new MqttMessage("Message".getBytes()));
				}
				
	//			client.setKeepAlive(100);
				while (true) {
					// 4. 매초마다 publish
					publishRandom(client);
					Thread.sleep(5000);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		
	private static void publishRandom(MqttClient client)
		throws MqttException {
		// 5. topic 가져오기
		MqttTopic topic = client.getTopic(TOPIC);
		
		// 4. 랜덤값 생성
		Random random = new Random();
		String topicValue = String.valueOf(random.nextInt(10) + 20);
		
		// 6. 브로커에게 pub
		topic.publish(new MqttMessage(topicValue.getBytes()));
		
		System.out.println("published: " + topicValue);
	}
}
