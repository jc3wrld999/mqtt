## MQTT-JAVA


![image](https://user-images.githubusercontent.com/61821641/187130487-9ac58163-53c4-4d5f-868f-ad2fa8bf2787.png)

## Broker (mosquitto)

```sh
mosquitto
```

![image](https://user-images.githubusercontent.com/61821641/187130577-e9a5416b-1f4a-421c-9f08-332e529f6b52.png)

## web server (publisher)

```java
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
			 
//			    if (!client.isConnected()) {
//			        client.publish("topic", new MqttMessage("Message".getBytes()));
//				}
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

```

## Client (subscriber)

```java
		String topic        = "topic/test";
		int qos             = 2;
		String broker       = "tcp://192.168.0.141:1883";
		String clientId     = "client22";
		MemoryPersistence persistence = new MemoryPersistence();

		MqttClient client = new MqttClient(broker, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		System.out.println("Connecting to broker: "+broker);
		client.connect(connOpts);
		System.out.println("Connected");


		client.setCallback(new MqttCallback() {

		    @Override
		    public void connectionLost(Throwable cause) { //Called when the client lost the connection to the broker 
		    }

		    @Override
		    public void messageArrived(String topic, MqttMessage message) throws Exception {
		        System.out.println(topic + ": " + new String(message.getPayload()));
		    }


			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {	
			}
		});
		        
		client.subscribe(topic, qos);

//		client.disconnect();
//		System.out.println("Disconnected");
//		System.exit(0);
```

## References
- [_](https://www.genuitec.com/introduction-to-mqtt-protocol/)