package com.interbank.reactive_store.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Request para crear una nueva orden")
public class CreateOrderRequest {
    @Schema(description = "Items de la orden", required = true)
    private List<OrderItemRequest> items;

    // Constructores
    public CreateOrderRequest() {}

    public CreateOrderRequest(List<OrderItemRequest> items) {
        this.items = items;
    }

    // Getters y Setters
    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
