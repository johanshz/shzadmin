package co.com.online_trainer.model.cliente.gateway;

import co.com.online_trainer.model.cliente.Cliente;

import reactor.core.publisher.Mono;

public interface ClienteRepository {
    Mono<Cliente> save(Cliente cliente);
    Mono<Cliente> findByIdenficacion(String identificacion);
    Mono<Cliente> findByTelefono(Long telefono);
}
