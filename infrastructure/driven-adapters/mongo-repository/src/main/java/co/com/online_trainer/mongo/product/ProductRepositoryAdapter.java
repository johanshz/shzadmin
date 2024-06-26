package co.com.online_trainer.mongo.product;

import co.com.online_trainer.model.product.Product;
import co.com.online_trainer.model.product.gateway.ProductRepository;
import co.com.online_trainer.mongo.helper.AdapterOperations;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Log
public class ProductRepositoryAdapter extends AdapterOperations<Product,ProductData,String,ProductDBRepository> implements ProductRepository {
    public  ProductRepositoryAdapter(ProductDBRepository repository, ObjectMapper mapper){
        super(repository,mapper,d->mapper.map(d,Product.class));
    }

    @Override
    public Mono<Product> findByCodigo(String reference) {
        return repository.findByCodigo(reference);
    }
}
