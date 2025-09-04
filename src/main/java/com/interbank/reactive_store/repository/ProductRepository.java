package com.interbank.reactive_store.repository;

import com.interbank.reactive_store.model.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    @Query("SELECT * FROM products WHERE name = :name")
    Mono<Product> findByName(String name);

    @Query("UPDATE products SET stock = stock - :quantity WHERE id = :id")
    Mono<Integer> updateStock(Long id, Integer quantity);

    @Query("SELECT * FROM products WHERE stock > 0")
    Flux<Product> findAvailableProducts();
}