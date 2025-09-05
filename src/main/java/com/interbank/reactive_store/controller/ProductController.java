package com.interbank.reactive_store.controller;

import com.interbank.reactive_store.model.Product;
import com.interbank.reactive_store.model.dto.ProductCreateRequest;
import com.interbank.reactive_store.model.dto.ProductUpdateRequest;
import com.interbank.reactive_store.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nuevo producto")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> createProduct(
            @Parameter(description = "Datos del producto a crear", required = true)
            @RequestBody @Valid ProductCreateRequest request) {

        return productService.createProduct(request);
    }
    @Operation(summary = "Actualizar producto existente")
    @PutMapping("/{id}")
    public Mono<Product> updateProduct(
            @Parameter(description = "ID del producto a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del producto", required = true)
            @RequestBody @Valid ProductUpdateRequest request) {

        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(ex -> {
                    if (ex.getMessage().contains("Producto no encontrado")) {
                        return Mono.just(ResponseEntity.notFound().build());
                    } else if (ex.getMessage().contains("ya fue eliminado")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                                .header("X-Error-Message", ex.getMessage())
                                .body(null));
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/available")
    public Flux<Product> getAvailableProducts() {
        return productService.getAvailableProducts();
    }

    @PatchMapping("/{id}/stock")
    public Mono<ResponseEntity<Product>> updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return productService.updateStock(id, quantity)
                .map(ResponseEntity::ok)
                .onErrorResume(error ->
                        Mono.just(ResponseEntity.badRequest().body((Product) null))
                );
    }
}
