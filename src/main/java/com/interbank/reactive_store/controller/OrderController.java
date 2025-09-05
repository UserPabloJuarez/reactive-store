package com.interbank.reactive_store.controller;

import com.interbank.reactive_store.model.Order;
import com.interbank.reactive_store.model.dto.CreateOrderRequest;
import com.interbank.reactive_store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Flux<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Order>> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Order>> createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request)
                .map(order -> ResponseEntity.status(HttpStatus.CREATED).body(order))
                .onErrorResume(ex -> {
                    if (ex instanceof RuntimeException) {
                        return Mono.just(ResponseEntity.badRequest().build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/status/{status}")
    public Flux<Order> getOrdersByStatus(@PathVariable String status) {
        return orderService.getOrdersByStatus(status);
    }

    @GetMapping("/date-range")
    public Flux<Order> getOrdersByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return orderService.getOrdersByDateRange(start, end);
    }

    @PatchMapping("/{id}/process")
    public Mono<ResponseEntity<Order>> processOrder(@PathVariable Long id) {
        return orderService.processOrder(id)
                .map(ResponseEntity::ok)
                .onErrorResume(error ->
                        Mono.just(ResponseEntity.badRequest().body((Order) null))
                );
    }

    @PatchMapping("/{id}/status")
    public Mono<ResponseEntity<Order>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return orderService.updateOrderStatus(id, status)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}