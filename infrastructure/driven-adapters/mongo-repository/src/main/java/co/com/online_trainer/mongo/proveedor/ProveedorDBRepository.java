package co.com.online_trainer.mongo.proveedor;

import co.com.online_trainer.model.proveedor.Proveedor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface ProveedorDBRepository extends ReactiveMongoRepository<ProveedorData,String>, ReactiveQueryByExampleExecutor<ProveedorData> {
    Mono<Proveedor> findByNombre(String nombre);
}
