package ru.aasmc.paymentservice;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import ru.aasmc.base.domain.Order;
import ru.aasmc.paymentservice.domain.Customer;
import ru.aasmc.paymentservice.repository.CustomerRepository;
import ru.aasmc.paymentservice.service.OrderManagerService;

import java.util.Random;

@SpringBootApplication
@EnableKafka
@Slf4j
public class PaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceApplication.class, args);
	}

	@Autowired
	OrderManagerService orderManagerService;

	@KafkaListener(id = "orders", topics = "orders", groupId = "payment")
	public void onEvent(Order o) {
		log.info("Received: {}", o);
		if (o.getStatus().equals("NEW")) {
			orderManagerService.reserve(o);
		} else {
			orderManagerService.confirm(o);
		}
	}

	@Autowired
	private CustomerRepository repository;

	@PostConstruct
	public void generateData() {
		Random r = new Random();
		Faker faker = new Faker();
		for (int i = 0; i < 100; i++) {
			int count = r.nextInt(100, 1000);
			Customer c = new Customer(null, faker.name().fullName(), count, 0);
			repository.save(c);
		}
	}

}
