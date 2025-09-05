package com.interbank.reactive_store.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Producto para creación")
public class ProductCreateRequest {
    @NotBlank(message = "El nombre es requerido")
    @Schema(description = "Nombre del producto", example = "Televisor LED", required = true)
    private String name;

    @Positive(message = "El precio debe ser mayor a 0")
    @Schema(description = "Precio del producto", example = "1299.99", required = true)
    private Double price;

    @PositiveOrZero(message = "El stock no puede ser negativo")
    @Schema(description = "Cantidad en stock", example = "10", required = true)
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
