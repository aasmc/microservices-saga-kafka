package ru.aasmc.paymentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.aasmc.base.domain.Order;
import ru.aasmc.paymentservice.domain.Customer;
import ru.aasmc.paymentservice.repository.CustomerRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderManagerService {
    public static final String SOURCE = "payment";
    private final CustomerRepository repository;
    private final KafkaTemplate<Long, Order> template;

    public void confirm(Order order) {
        Customer customer = repository.findById(order.getCustomerId()).orElseThrow();
        log.info("Found: {}", customer);
        if (order.getStatus().equals("CONFIRMED")) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getPrice());
            repository.save(customer);
        } else if (order.getStatus().equals("ROLLBACK") && !order.getSource().equals(SOURCE)) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getPrice());
            customer.setAmountAvailable(customer.getAmountAvailable() + order.getPrice());
            repository.save(customer);
        }
    }

    public void reserve(Order order) {
        Customer customer = repository.findById(order.getCustomerId()).orElseThrow();
        log.info("Found customer: {}", customer);
        if (order.getPrice() < customer.getAmountAvailable()) {
            order.setStatus("ACCEPT");
            customer.setAmountReserved(customer.getAmountReserved() + order.getPrice());
            customer.setAmountAvailable(customer.getAmountAvailable() - order.getPrice());
        } else {
            order.setStatus("REJECT");
        }
        order.setSource(SOURCE);
        repository.save(customer);
        template.send("payment-orders", order.getId(), order);
        log.info("Sent: {}", order);
    }
}
