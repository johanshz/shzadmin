package co.com.online_trainer.usecase;

import co.com.online_trainer.model.factory.FactoryObjects;
import co.com.online_trainer.model.factura.Factura;
import co.com.online_trainer.model.factura.gateway.FacturaRepository;
import co.com.online_trainer.model.registro.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Log
public class FacturaUseCase implements FactoryObjects {
    private final FacturaRepository facturaRepository;
    public Mono<Response<Object>> saveFacturaCotizacion(Factura factura){
        //LocalDateTime fechaActual = LocalDateTime.now();
        return  getFacturaDB(factura.getIdCompra())
                        .switchIfEmpty(Mono.defer(() -> saveCotizacion(factura)))
                        .flatMap(facturaDB ->  updateCotizacion(facturaDB,factura))
                        .flatMap(cotizacion -> Mono.just(Response.builder()
                                .description("Cotizacion guardada correctamente guardada correctamente")
                                .results(cotizacion)
                                .build()))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    public Mono<Response<Object>> realizarCompra(Factura factura){
        return getFacturaDB(factura.getIdCompra())
               .flatMap(facturaDB -> facturaRepository.save(buildFactura(facturaDB,factura,Boolean.TRUE)))
                .flatMap(facturaDB -> Mono.just(Response.builder()
                        .description("Factura guardada correctamente")
                        .results(facturaDB)
                        .build()))
                .switchIfEmpty(Mono.defer(()-> Mono.error(new RuntimeException("no se guardo la factura"))));
    }
    private Mono<Factura> updateCotizacion(Factura facturaDB,Factura facturaRequest){
        return  Mono.just(facturaDB)
                        .filter(factura -> Boolean.FALSE.equals(factura.getIsCompraRealizada()))
                        .flatMap(factura -> Mono.defer(()->  facturaRepository.save(buildFactura(factura,facturaRequest,Boolean.FALSE))
                                        .switchIfEmpty(Mono.defer(()-> Mono.error(new RuntimeException("ERROR no se guardo la cotizacion"))))))
                        .switchIfEmpty(Mono.defer(()-> Mono.error(new RuntimeException("factura inabilitada compra ya fue realizar"))));
    }
    private Mono<Factura> saveCotizacion(Factura factura){
        return getIdCompra()
                .flatMap(idCompra -> facturaRepository.save(factura.toBuilder()
                        .idCompra(idCompra)
                        //.fechaActual(fecha)
                        .build()))
                .switchIfEmpty(Mono.defer(()-> Mono.error(new RuntimeException("no se guardo la cotizacion"))));
    }
    public Mono<Response<Object>> getFactura(Integer idCompra){
        return getFacturaDB(idCompra)
                .flatMap(factura -> Mono.just(Response.builder()
                                .description("Consulta realizada con exito")
                                .results(factura)
                        .build()))
                .onErrorResume(error -> Mono.just(Response.builder()
                                .description(error.getMessage())
                        .build()));

    }
    private Mono<Factura> getFacturaDB(Integer idCompra){
        return  facturaRepository.findByIdCompra(idCompra);
    }
    private Mono<Integer> getIdCompra() {
        return facturaRepository.findAll()
                .collectList()
                .flatMap(this::getIdCompraSiguiente)
                .map(idCompra -> idCompra);
    }
    private Mono<Integer> getIdCompraSiguiente(List<Factura> cotizaciones){
        //hacer pruebas borrando todas las cotizaciones
        return Mono.just(cotizaciones.isEmpty() ? 0 : cotizaciones.get(cotizaciones.size() - 1).getIdCompra() )
                //.map(cotizaciones::get)
                .map(cantidadCotizacion -> cantidadCotizacion + 1);

    }
    public Mono<Response<Object>> getCotizacion(String idCompra){
        return getFacturaDB(Integer.parseInt(idCompra))
                .flatMap(facturaDB -> Mono.just(Response.builder()
                        .results(facturaDB)
                        .build()))
               .switchIfEmpty(Mono.defer(()-> Mono.error(new RuntimeException("no existe cotizacion"))))
               .onErrorResume(error -> Mono.just(Response.builder()
                        .description(error.getMessage())
                        .build()));
    }

}
