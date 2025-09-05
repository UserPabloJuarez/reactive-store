package com.interbank.reactive_store.service;

import com.interbank.reactive_store.model.Product;
import com.interbank.reactive_store.model.dto.ProductCreateRequest;
import com.interbank.reactive_store.model.dto.ProductUpdateRequest;
import com.interbank.reactive_store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.retry.Retry;
import java.time.Duration;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Mono<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Mono<Product> createProduct(ProductCreateRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        return productRepository.save(product);
    }

    public Mono<Product> updateProduct(Long id, ProductUpdateRequest request) {
        return productRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setName(request.getName());
                    existingProduct.setPrice(request.getPrice());
                    existingProduct.setStock(request.getStock());
                    return productRepository.save(existingProduct);
                })
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .onErrorResume(OptimisticLockingFailureException.class, ex ->
                        Mono.error(new RuntimeException("El producto fue modificado por otro usuario. Por favor, recargue e intente nuevamente."))
                );
    }

    @Transactional
    public Mono<Void> deleteProduct(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado")))
                .flatMap(product -> {
                    if (Boolean.TRUE.equals(product.getDeleted())) {
                        return Mono.error(new RuntimeException("El producto ya fue eliminado"));
                    }
                    product.setDeleted(true);
                    return productRepository.save(product);
                })
                .then();
    }

    @Transactional
    public Mono<Product> updateStock(Long productId, Integer quantity) {
        return productRepository.findById(productId)
                .flatMap(product -> {
                    if (product.getStock() >= quantity) {
                        product.setStock(product.getStock() - quantity);
                        return productRepository.save(product)
                                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                                .onErrorResume(OptimisticLockingFailureException.class,
                                ex -> Mono.error(new RuntimeException("Conflicto de concurrencia. Intente nuevamente.")));
                    }
                    return Mono.error(new RuntimeException("Stock insuficiente para el producto: " + product.getName()));
                });
    }

    public Flux<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts();
    }
}

