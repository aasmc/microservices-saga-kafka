package ru.aasmc.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

class TestPaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(PaymentServiceApplication::main)
				.with(KafkaContainerDevMode.class)
				.run(args);
	}

}
