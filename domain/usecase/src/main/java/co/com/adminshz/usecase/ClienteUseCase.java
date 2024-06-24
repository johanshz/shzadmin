package co.com.adminshz.usecase;

import co.com.adminshz.model.cliente.Cliente;
import co.com.adminshz.model.cliente.gateway.ClienteRepository;
import co.com.adminshz.model.registro.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log
public class ClienteUseCase {
    private final ClienteRepository clienteRepository;
    public Mono<Response<Object>> saveCliente(Cliente cliente){
        return  clienteRepository.findByIdenficacion(cliente.getIdentificacion())
                .switchIfEmpty(Mono.defer(()-> clienteRepository.save(cliente)))
                .flatMap(clienteDB -> updateCliente(clienteDB,cliente))
                .flatMap(clienteDB -> Mono.just(Response.builder()
                        .description("se guardo correctamente")
                        .results(clienteDB)
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no se guardo el cliente"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<Cliente> updateCliente(Cliente clienteDB, Cliente clienteRequest){
        return clienteRepository.save(clienteDB.toBuilder()
                        .direccion(!clienteDB.getDireccion().equals(clienteRequest.getDireccion()) ?
                                clienteRequest.getDireccion() :
                                clienteDB.getDireccion())
                        .telefono(!clienteDB.getTelefono().equals(clienteRequest.getTelefono()) ?
                                clienteRequest.getTelefono() :
                                clienteDB.getTelefono())
                        .nombreCliente(!clienteDB.getNombreCliente().equals(clienteRequest.getNombreCliente()) ?
                                clienteRequest.getNombreCliente() :
                                clienteDB.getNombreCliente())
                .build());
    }
}
