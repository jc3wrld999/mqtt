package com.mqtt.sub;

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
