package co.com.online_trainer.mongo.categoria;

import co.com.online_trainer.model.categoria.Categoria;
import co.com.online_trainer.model.proveedor.Proveedor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface CategoriaDBRepository extends ReactiveMongoRepository<CategoriaData,String>, ReactiveQueryByExampleExecutor<CategoriaData> {
    Mono<Categoria> findByNombre(String nombre);
}
