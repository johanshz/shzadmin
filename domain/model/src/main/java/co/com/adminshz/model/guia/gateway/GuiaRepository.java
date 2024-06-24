package co.com.adminshz.model.guia.gateway;

import co.com.adminshz.model.guia.Guia;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GuiaRepository {
    Mono<Guia> save(Guia guia);
    Flux<Guia> findAll();
    Mono<Guia> findById(String id);
    Mono<Void> deleteById(String id);
}
