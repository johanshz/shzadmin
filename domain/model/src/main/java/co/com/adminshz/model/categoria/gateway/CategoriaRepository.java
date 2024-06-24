package co.com.adminshz.model.categoria.gateway;

import co.com.adminshz.model.categoria.Categoria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoriaRepository {
    Mono<Categoria> save(Categoria categoria);
    Flux<Categoria> findAll();
    Mono<Categoria> findByNombre(String nombre);
    Mono<Categoria> findById(String id);
    Mono<Void> deleteById(String id);
}
