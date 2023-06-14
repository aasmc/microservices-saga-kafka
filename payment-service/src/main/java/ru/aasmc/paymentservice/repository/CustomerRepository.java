package ru.aasmc.paymentservice.repository;

import org.springframework.data.repository.CrudRepository;
import ru.aasmc.paymentservice.domain.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
