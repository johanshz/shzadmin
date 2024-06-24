package co.com.adminshz.model.cliente.gateway;

import co.com.adminshz.model.cliente.Cliente;

import reactor.core.publisher.Mono;

public interface ClienteRepository {
    Mono<Cliente> save(Cliente cliente);
    Mono<Cliente> findByIdenficacion(String identificacion);
    Mono<Cliente> findByTelefono(Long telefono);
}
