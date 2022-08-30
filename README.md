## MQTT-JAVA


![image](https://user-images.githubusercontent.com/61821641/187131455-b6ef639e-82d3-4b7d-9a96-33f4a543eb0d.png)

## 1. 기본 연결

### broker (mosquitto)

```sh
mosquitto
```

![image](https://user-images.githubusercontent.com/61821641/187130577-e9a5416b-1f4a-421c-9f08-332e529f6b52.png)

### publisher

```xml
<repositories>
    <repository>
        <id>Eclipse Paho Repo</id>
        <url>https://repo.eclipse.org/content/repositories/paho-releases/</url>
    </repository>
</repositories>
    
<dependencies>
    <dependency>
        <groupId>org.eclipse.paho</groupId>
        <artifactId>org.eclipse.paho.client.mqttv3</artifactId>
        <version>1.0.2</version>
    </dependency>
</dependencies>
```

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

### subscriber

```gradle
implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5'
```
```java
import java.net.SocketException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;		


@Component
@Slf4j
public class ConstructBean {

	private MqttClient client;
	private int qos             = 2;
	private String broker       = "tcp://192.168.0.141:1883";
	private String clientId     = "client22";
	
    @PostConstruct
    public void init() throws MqttSecurityException, SocketException, MqttException {
    	log.info("-----------------------------------------");
        log.info("spring tomcat start");
        log.info("-----------------------------------------");
		String topic        = "topic/test";

		MemoryPersistence persistence = new MemoryPersistence();

		client = new MqttClient(broker, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		
		log.info("Connecting to broker: ", broker);
		client.connect(connOpts);
		log.info("Connected");
		log.info("-----------------------------------------");


		client.setCallback(new MqttCallback() {

		    @Override
		    public void connectionLost(Throwable cause) { 
		    	log.info("클라이언트가 브로커에 대한 연결이 끊겼습니다.");
		    }

		    @Override
		    public void messageArrived(String topic, MqttMessage message) throws Exception {
		    	log.info(topic, ": ", new String(message.getPayload()));
		    }

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {	
			}
		});
		        
		client.subscribe(topic, qos);


    }
    
    @PreDestroy
    public void preDestroy() throws MqttException {
		client.disconnect();
		log.info("-----------------------------------------");
		log.info("Disconnected");
		System.exit(0); 
    }
}

```

![image](https://user-images.githubusercontent.com/61821641/187131288-642ef447-8c62-4577-8771-6436a29b057e.png)
- 좌: publisher
- 우: subscriver

## 

## References
- [pub-sub example(온도)](https://www.genuitec.com/introduction-to-mqtt-protocol/)
- [spring boot mqtt demo](https://github.com/frensley/springboot-mqtt-demo)
- [hivemq eclipse paho java](https://www.hivemq.com/blog/mqtt-client-library-encyclopedia-eclipse-paho-java/)
- [hivemq topic, wild card example](https://www.hivemq.com/blog/mqtt-essentials-part-5-mqtt-topics-best-practices/)
- [spring 시작시 로직 수행](https://www.baeldung.com/running-setup-logic-on-startup-in-spring)