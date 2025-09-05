package com.interbank.reactive_store.repository;

import com.interbank.reactive_store.model.Order;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

    @Query("SELECT * FROM orders WHERE status = :status")
    Flux<Order> findByStatus(String status);

    @Query("SELECT * FROM orders WHERE date BETWEEN :start AND :end")
    Flux<Order> findByDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("UPDATE orders SET status = :status WHERE id = :id")
    Mono<Integer> updateStatus(@Param("id") Long id, @Param("status") String status);

}
