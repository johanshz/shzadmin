package co.com.adminshz.model.factura.gateway;

import co.com.adminshz.model.factura.Factura;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FacturaRepository {
    Mono<Factura> save(Factura factura);

    Mono<Factura> findByIdCompra(Integer idCompra);
    Mono<Factura> findByIdenficacion(String identificacion);
    Mono<Factura> findByTelefono(Long telefono);
    Flux<Factura> findAll();
    Mono<Factura> findById(String id);
}
