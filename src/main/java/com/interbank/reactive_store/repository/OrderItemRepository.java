package com.interbank.reactive_store.repository;

import com.interbank.reactive_store.model.OrderItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderItemRepository extends ReactiveCrudRepository<OrderItem, Long> {

    @Query("SELECT * FROM order_items WHERE order_id = :orderId")
    Flux<OrderItem> findByOrderId(Long orderId);

    @Query("DELETE FROM order_items WHERE order_id = :orderId")
    Mono<Integer> deleteByOrderId(Long orderId);

    @Query("SELECT COUNT(DISTINCT product_id) FROM order_items WHERE order_id = :orderId")
    Mono<Integer> countDistinctProductsByOrderId(Long orderId);
}