package ru.aasmc.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

class TestOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(OrderServiceApplication::main)
				.with(KafkaContainerDevMode.class)
				.run(args);
	}

}
