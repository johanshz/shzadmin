package co.com.adminshz.model.descuento.gateway;

import co.com.adminshz.model.descuento.Descuento;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DescuentoRepository {
    Mono<Descuento> save(Descuento descuento);
    Flux<Descuento> findAll();
    Mono<Descuento> findById(String id);
    Mono<Void> deleteById(String id);
}
