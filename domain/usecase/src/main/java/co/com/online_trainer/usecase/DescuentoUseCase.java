package co.com.online_trainer.usecase;

import co.com.online_trainer.model.descuento.Descuento;
import co.com.online_trainer.model.descuento.gateway.DescuentoRepository;

import co.com.online_trainer.model.proveedor.Proveedor;
import co.com.online_trainer.model.registro.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Log
public class DescuentoUseCase {
    private final DescuentoRepository descuentoRepository;
    public Mono<Response<Object>> saveDescuento(Descuento descuento){
        return  validarDescuento(descuento)
                 .flatMap(descuentoValido -> descuentoRepository.save(descuentoValido)
                         .flatMap(descuentoDB -> Mono.just(Response.builder()
                                 .description("se guardo correctamente")
                                 .results(descuentoDB)
                                 .build()))
                         .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no se guardo el descuento")))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<Descuento> validarDescuento(Descuento descuento){
        return descuentoRepository.findAll()
                .collectList()
                .flatMap(descuentos -> descuentos.isEmpty() ? Mono.empty() : isValidoDescuento(descuentos.get(descuentos.size() - 1),descuento))
                .flatMap(valid -> Boolean.TRUE.equals(valid) ? Mono.just(descuento) : Mono.error(new RuntimeException("Descuento no valido")))
                .switchIfEmpty(Mono.just(descuento));
    }
    private Mono<Boolean> isValidoDescuento(Descuento descuento,Descuento descuentoRequest){
        return Mono.just(descuento)
                .filter(descuentoDB -> !descuentoDB.getValorMax().equals(0))
                .filter(descuentoDB -> descuentoRequest.getValorMin() > descuentoDB.getValorMax())
                .filter(descuentoDB -> descuentoRequest.getValorMax() > descuentoRequest.getValorMin() ||
                                       descuentoRequest.getValorMax() == 0 )
                .flatMap(descuentoDB -> Mono.just(Boolean.TRUE))
                .switchIfEmpty(Mono.just(Boolean.FALSE));
    }
    public Mono<Response<Object>> getDescuentos(){
        return descuentoRepository.findAll()
                .collectList()
                .flatMap(descuentos -> Mono.just(Response.builder()
                                .results(descuentos)
                        .build()))
                .switchIfEmpty(Mono.error(new RuntimeException("no hay descuentos")))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));

    }
    public Mono<Response<Object>> getDescuentoById(String id){
        return descuentoRepository.findById(id)
                .flatMap(descuento -> Mono.just(Response.builder()
                        .results(descuento)
                        .build()))
                .switchIfEmpty(Mono.error(new RuntimeException("no hay descuentos")))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    public Mono<Response<Object>> getDescuentoCompra(String valorTotalSinDescuento){
        return  descuentoRepository.findAll()
                .collectList()
                .flatMap(descuentos -> getPorcentajeDescuento(descuentos,valorTotalSinDescuento))
                .flatMap(porcentajeDescuento -> Mono.just(Response.builder().results(porcentajeDescuento).build()))
                .switchIfEmpty(Mono.error(new RuntimeException("no hay descuentos")))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<Integer> getPorcentajeDescuento(List<Descuento> descuentos, String valorTotalSinDescuento){
        return Flux.fromIterable(descuentos)
                .filter(descuento -> Integer.parseInt(valorTotalSinDescuento) >= descuento.getValorMin())
                .filter(descuento -> Integer.parseInt(valorTotalSinDescuento) <= descuento.getValorMax() || descuento.getValorMax()  == 0)
                .collectList()
                .flatMap(descuento -> Mono.just(descuento.get(0).getValorDescuento()));
    }
    public Mono<Response<Object>> deleteDescuento(String id){
        return descuentoRepository.findById(id)
                .flatMap(descuento -> eliminar(descuento.getId()))
                .flatMap(message -> Mono.just(Response.builder()
                        .description(message)
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existen descuentos"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<String> eliminar(String id){
        return descuentoRepository.deleteById(id)
                .thenReturn("elimino correctamente");
    }
}
