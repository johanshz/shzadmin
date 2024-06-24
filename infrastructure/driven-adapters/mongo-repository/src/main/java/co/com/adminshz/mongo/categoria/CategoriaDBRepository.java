package co.com.adminshz.mongo.categoria;

import co.com.adminshz.model.categoria.Categoria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface CategoriaDBRepository extends ReactiveMongoRepository<CategoriaData,String>, ReactiveQueryByExampleExecutor<CategoriaData> {
    Mono<Categoria> findByNombre(String nombre);
}
