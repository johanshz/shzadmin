package co.com.online_trainer.usecase;

import co.com.online_trainer.model.guia.Guia;
import co.com.online_trainer.model.guia.gateway.GuiaRepository;
import co.com.online_trainer.model.proveedor.Proveedor;
import co.com.online_trainer.model.registro.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;
import static java.util.Objects.isNull;

import java.util.List;

@RequiredArgsConstructor
@Log
public class GuiaUseCase {
    private final GuiaRepository guiaRepository;
    public Mono<Response<Object>> saveGuia(Guia guiaRequest){
        return  validarDatosObligatorios(guiaRequest) //realizar esta prueba
                .flatMap(guia ->  getIdGuia()
                        .flatMap(idGuia -> guiaRepository.save(guia.toBuilder()
                                        .idGuia(idGuia)
                                        .nombreCliente(guia.getNombreCliente().toUpperCase())
                                .build()))
                        .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("No se pudo guardar guia"))))
                        .flatMap(guiaDB -> Mono.just(Response.builder()
                                .results(guiaDB)
                                .build())))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<Guia> validarDatosObligatorios(Guia guiaRequest){
         return Mono.just(guiaRequest)
                 .filter(guia -> !isNull(guia.getDireccion()) && !guia.getDireccion().isEmpty())
                 .filter(guia -> !isNull(guia.getTelefono()))
                 .filter((guia -> !isNull(guia.getNombreCliente()) && !guia.getNombreCliente().isEmpty()))
                 .filter((guia -> !isNull(guia.getCuidad()) && !guia.getCuidad().isEmpty()))
                 .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("error en los datos del request"))));
    }
    private Mono<Integer> getIdGuia() {
        return guiaRepository.findAll()
                .collectList()
                .flatMap(this::getIdGuiaSiguiente)
                .map(idCompra -> idCompra);
    }
    private Mono<Integer> getIdGuiaSiguiente(List<Guia> guias){
        return Mono.just(guias.isEmpty() ? 0 : guias.get(guias.size() - 1).getIdGuia())
                .map(cantidadGuia -> cantidadGuia + 1);

    }

    public Mono<Response<Object>> getGuia(){
        return guiaRepository.findAll()
                .collectList()
                .flatMap(guiaDB -> Mono.just(Response.builder()
                        .description("busqueda correcta")
                        .results(guiaDB)
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existen guias"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    public Mono<Response<Object>> getGuiaId(String id){
        return guiaRepository.findById(id)
                .flatMap(guia -> Mono.just(Response.builder()
                        .results(guia)
                        .build()));
    }
    public Mono<Response<Object>> updateGuia(Guia guia){
        return guiaRepository.findById(guia.getId())
                .flatMap(guiaDB -> update(guiaDB,guia))
                .flatMap(guia1 -> Mono.just(Response.builder()
                        .results(guia1)
                        .description("se actualizo correctamente la guia")
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existen guia"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }

    private Mono<Guia> update(Guia guiaDB,Guia guiaRequest){
        return guiaRepository.save(guiaDB.toBuilder()
                .direccion(!guiaDB.getDireccion().equals(guiaRequest.getDireccion()) ?
                        guiaRequest.getDireccion() : guiaDB.getDireccion())
                .nombreCliente(!guiaDB.getNombreCliente().equalsIgnoreCase(guiaRequest.getNombreCliente()) ?
                        guiaRequest.getNombreCliente().toUpperCase() : guiaDB.getNombreCliente().toUpperCase())
                .cuidad(!guiaDB.getCuidad().equals(guiaRequest.getCuidad()) ?
                        guiaRequest.getCuidad() : guiaDB.getCuidad())
                .telefono(!guiaDB.getTelefono().equals(guiaRequest.getTelefono()) ?
                                guiaRequest.getTelefono() : guiaDB.getTelefono())
                .build());
    }
    public Mono<Response<Object>> deleteGuia(String id){
        return guiaRepository.findById(id)
                .flatMap(proveedor -> eliminar(proveedor.getId()))
                .flatMap(message -> Mono.just(Response.builder()
                        .description(message)
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existen proveedores"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<String> eliminar(String id){
        return guiaRepository.deleteById(id)
                .thenReturn("elimino correctamente");
    }

}
