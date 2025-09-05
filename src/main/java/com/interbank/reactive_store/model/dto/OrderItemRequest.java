package com.interbank.reactive_store.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Item de orden en la request")
public class OrderItemRequest {
    @Schema(description = "ID del producto", example = "1", required = true)
    private Long productId;
    @Schema(description = "Cantidad del producto", example = "2", required = true)
    private Integer quantity;

    // Constructores
    public OrderItemRequest() {}

    public OrderItemRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters y Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
