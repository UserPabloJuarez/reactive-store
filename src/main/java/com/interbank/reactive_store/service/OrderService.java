package com.interbank.reactive_store.service;

import com.interbank.reactive_store.model.Order;
import com.interbank.reactive_store.model.OrderItem;
import com.interbank.reactive_store.repository.OrderItemRepository;
import com.interbank.reactive_store.repository.OrderRepository;
import com.interbank.reactive_store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductRepository productRepository,
                        ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll()
                .flatMap(this::loadOrderItems);
    }

    public Mono<Order> getOrderById(Long id) {
        return orderRepository.findById(id)
                .flatMap(this::loadOrderItems);
    }

    @Transactional
    public Mono<Order> createOrder(Order order) {
        order.setDate(LocalDateTime.now());
        order.setStatus("PENDING");

        return calculateTotal(order.getItems())
                .flatMap(total -> {
                    order.setTotal(applyDiscounts(total, order.getItems()));
                    return orderRepository.save(order);
                })
                .flatMap(savedOrder ->
                        saveOrderItems(savedOrder.getId(), order.getItems())
                                .then(updateOrderStatus(savedOrder.getId(), "CONFIRMED"))
                )
                .flatMap(this::loadOrderItems)
                .onErrorResume(error -> {
                    // Rollback automático en caso de error
                    return Mono.error(new RuntimeException("Error al crear el pedido: " + error.getMessage()));
                });
    }

    private Mono<Double> calculateTotal(List<OrderItem> items) {
        return Flux.fromIterable(items)
                .flatMap(item -> productRepository.findById(item.getProductId())
                        .map(product -> {
                            item.setSubtotal(product.getPrice() * item.getQuantity());
                            return item.getSubtotal();
                        }))
                .reduce(0.0, Double::sum);
    }

    private Double applyDiscounts(Double total, List<OrderItem> items) {
        // Descuento del 10% si total > 1000
        if (total > 1000) {
            total = total * 0.9;
        }

        // Descuento adicional del 5% si más de 5 productos diferentes
        long distinctProducts = items.stream()
                .map(OrderItem::getProductId)
                .distinct()
                .count();

        if (distinctProducts > 5) {
            total = total * 0.95;
        }

        return Math.round(total * 100.0) / 100.0; // Redondear a 2 decimales
    }

    private Mono<Void> saveOrderItems(Long orderId, List<OrderItem> items) {
        return Flux.fromIterable(items)
                .map(item -> {
                    item.setOrderId(orderId);
                    return item;
                })
                .flatMap(orderItemRepository::save)
                .then();
    }

    private Mono<Order> loadOrderItems(Order order) {
        return orderItemRepository.findByOrderId(order.getId())
                .collectList()
                .map(items -> {
                    order.setItems(items);
                    return order;
                });
    }

    public Mono<Order> updateOrderStatus(Long orderId, String status) {
        return orderRepository.updateStatus(orderId, status)
                .then(orderRepository.findById(orderId))
                .flatMap(this::loadOrderItems);
    }

    @Transactional
    public Mono<Order> processOrder(Long orderId) {
        return getOrderById(orderId)
                .flatMap(order -> {
                    if (!"PENDING".equals(order.getStatus())) {
                        return Mono.error(new RuntimeException("El pedido ya fue procesado"));
                    }

                    return Flux.fromIterable(order.getItems())
                            .flatMap(item -> productService.updateStock(item.getProductId(), item.getQuantity()))
                            .then(updateOrderStatus(orderId, "PROCESSED"));
                });
    }

    public Flux<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status)
                .flatMap(this::loadOrderItems);
    }

    public Flux<Order> getOrdersByDateRange(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByDateBetween(start, end)
                .flatMap(this::loadOrderItems);
    }
}

