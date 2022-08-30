package com.mqtt.sub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@SpringBootApplication
@EnableScheduling
public class MqttSubApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqttSubApplication.class, args);
	}
	
    @Bean
    public TaskScheduler taskScheduler() {
        // 단일 스레드 구현
        return new ConcurrentTaskScheduler();
    }
}
