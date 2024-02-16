package co.com.online_trainer.model.descuento.gateway;

import co.com.online_trainer.model.descuento.Descuento;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DescuentoRepository {
    Mono<Descuento> save(Descuento descuento);
    Flux<Descuento> findAll();
    Mono<Descuento> findById(String id);
    Mono<Void> deleteById(String id);
}
