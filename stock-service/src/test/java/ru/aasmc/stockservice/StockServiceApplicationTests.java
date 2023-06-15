package ru.aasmc.stockservice;

import org.springframework.boot.SpringApplication;

class StockServiceApplicationTests {
	public static void main(String[] args) {
		SpringApplication.from(StockServiceApplication::main)
				.with(KafkaContainerDevMode.class)
				.run(args);
	}
}
