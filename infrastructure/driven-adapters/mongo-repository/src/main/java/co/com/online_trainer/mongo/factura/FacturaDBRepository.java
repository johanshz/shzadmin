package co.com.online_trainer.mongo.factura;

import co.com.online_trainer.model.factura.Factura;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface FacturaDBRepository  extends ReactiveMongoRepository<FacturaData, String>, ReactiveQueryByExampleExecutor<FacturaData> {

    Mono<Factura> findByIdCompra(Integer idCompra);
    Mono<Factura> findByIdentificacion(String identificacion);
    Mono<Factura> findByTelefono(Long telefono);
}
