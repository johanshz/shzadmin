package co.com.adminshz.mongo.factura;

import co.com.adminshz.model.factura.Factura;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface FacturaDBRepository  extends ReactiveMongoRepository<FacturaData, String>, ReactiveQueryByExampleExecutor<FacturaData> {

    Mono<Factura> findByIdCompra(Integer idCompra);
    Mono<Factura> findByIdentificacion(String identificacion);
    Mono<Factura> findByTelefono(Long telefono);
}