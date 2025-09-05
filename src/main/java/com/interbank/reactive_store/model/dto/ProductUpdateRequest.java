package com.interbank.reactive_store.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Request para actualizar producto")
public class ProductUpdateRequest {
    @Schema(description = "Nombre del producto", example = "Televisor LED 4K", required = true)
    @NotBlank(message = "El nombre es requerido")
    private String name;

    @Schema(description = "Precio del producto", example = "1599.99", required = true)
    @Positive(message = "El precio debe ser mayor a 0")
    private Double price;

    @Schema(description = "Cantidad en stock", example = "5", required = true)
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
