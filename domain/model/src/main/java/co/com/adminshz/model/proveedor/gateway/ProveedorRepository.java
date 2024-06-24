package co.com.adminshz.model.proveedor.gateway;


import co.com.adminshz.model.proveedor.Proveedor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProveedorRepository {
    Mono<Proveedor> save(Proveedor proveedor);
    Flux<Proveedor> findAll();
    Mono<Proveedor> findByNombre(String nombre);
    Mono<Proveedor> findById(String id);
     Mono<Void> deleteById(String id);
}
