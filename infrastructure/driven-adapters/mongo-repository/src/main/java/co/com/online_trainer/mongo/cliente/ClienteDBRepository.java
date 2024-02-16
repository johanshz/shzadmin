package co.com.online_trainer.mongo.cliente;

import co.com.online_trainer.model.cliente.Cliente;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface ClienteDBRepository extends ReactiveMongoRepository<ClienteData,String>, ReactiveQueryByExampleExecutor<ClienteData> {
   Mono<Cliente> findByIdentificacion(String identificacion);
   Mono<Cliente> findByTelefono(Long telefono);
}
