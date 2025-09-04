package com.interbank.reactive_store.service;

import com.interbank.reactive_store.model.Product;
import com.interbank.reactive_store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.transaction.annotation.Transactional;

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

    public Mono<Product> createProduct(Product product) {
        return productRepository.save(product);
    }

    public Mono<Product> updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setStock(product.getStock());
                    return productRepository.save(existingProduct);
                });
    }

    public Mono<Void> deleteProduct(Long id) {
        return productRepository.deleteById(id);
    }

    @Transactional
    public Mono<Product> updateStock(Long productId, Integer quantity) {
        return productRepository.findById(productId)
                .flatMap(product -> {
                    if (product.getStock() >= quantity) {
                        product.setStock(product.getStock() - quantity);
                        return productRepository.save(product)
                                .retry(3)
                                .onErrorMap(OptimisticLockingFailureException.class,
                                        ex -> new RuntimeException("Conflicto de concurrencia. Intente nuevamente."));
                    }
                    return Mono.error(new RuntimeException("Stock insuficiente para el producto: " + product.getName()));
                });
    }

    public Flux<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts();
    }
}

