package co.com.online_trainer.mongo.product;

import co.com.online_trainer.model.product.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface ProductDBRepository extends ReactiveMongoRepository<ProductData,String>, ReactiveQueryByExampleExecutor<ProductData> {
    Mono<Product> findByCodigo(String codigo);
}
