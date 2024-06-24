package co.com.adminshz.mongo.proveedor;

import co.com.adminshz.model.proveedor.Proveedor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface ProveedorDBRepository extends ReactiveMongoRepository<ProveedorData,String>, ReactiveQueryByExampleExecutor<ProveedorData> {
    Mono<Proveedor> findByNombre(String nombre);
}
