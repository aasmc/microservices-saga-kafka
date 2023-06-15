package ru.aasmc.stockservice;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import ru.aasmc.base.domain.Order;
import ru.aasmc.stockservice.domain.Product;
import ru.aasmc.stockservice.repository.ProductRepository;
import ru.aasmc.stockservice.service.OrderManagerService;

import java.util.Random;

@SpringBootApplication
@EnableKafka
@Slf4j
public class StockServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockServiceApplication.class, args);
	}

	@Autowired
	OrderManagerService orderManagerService;

	@KafkaListener(id = "orders", topics = "orders", groupId = "stock")
	public void onEvent(Order o) {
		log.info("Received: {}", o);
		if (o.getStatus().equals("NEW"))
			orderManagerService.reserve(o);
		else
			orderManagerService.confirm(o);
	}

	@Autowired
	private ProductRepository repository;

	@PostConstruct
	public void generateData() {
		Random r = new Random();
		for (int i = 0; i < 1000; i++) {
			int count = r.nextInt(10, 1000);
			Product p = new Product(null, "Product" + i, count, 0);
			repository.save(p);
		}
	}

}
