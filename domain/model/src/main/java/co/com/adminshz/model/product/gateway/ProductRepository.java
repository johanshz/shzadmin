package co.com.adminshz.model.product.gateway;


import co.com.adminshz.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Mono<Product> findByCodigo(String codigo);
    Flux<Product> findAll();
    Mono<Void> deleteById(String id);
    Mono<Product> findById(String id);
}
