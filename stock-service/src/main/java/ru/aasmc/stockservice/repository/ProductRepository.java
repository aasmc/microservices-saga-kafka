package ru.aasmc.stockservice.repository;

import org.springframework.data.repository.CrudRepository;
import ru.aasmc.stockservice.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
