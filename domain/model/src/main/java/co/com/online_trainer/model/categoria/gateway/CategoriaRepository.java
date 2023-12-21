package co.com.online_trainer.model.categoria.gateway;

import co.com.online_trainer.model.categoria.Categoria;
import co.com.online_trainer.model.proveedor.Proveedor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoriaRepository {
    Mono<Categoria> save(Categoria categoria);
    Flux<Categoria> findAll();
    Mono<Categoria> findByNombre(String nombre);
    Mono<Categoria> findById(String id);
    Mono<Void> deleteById(String id);
}
