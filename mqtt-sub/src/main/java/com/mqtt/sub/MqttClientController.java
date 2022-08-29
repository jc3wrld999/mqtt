package com.mqtt.sub;

import java.net.SocketException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;	

@SpringBootConfiguration
@RestController
public class MqttClientController {
	
	@GetMapping("/hello")
	public String hello() throws MqttSecurityException, SocketException, MqttException {
		
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
		
		return "test";
	}
}
