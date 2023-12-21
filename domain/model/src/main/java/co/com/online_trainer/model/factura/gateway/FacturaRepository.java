package co.com.online_trainer.model.factura.gateway;

import co.com.online_trainer.model.factura.Factura;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FacturaRepository {
    Mono<Factura> save(Factura factura);

    Mono<Factura> findByIdCompra(Integer idCompra);
    Flux<Factura> findAll();
}
