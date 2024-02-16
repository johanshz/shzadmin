package co.com.online_trainer.usecase;

import co.com.online_trainer.model.cliente.Cliente;
import co.com.online_trainer.model.cliente.gateway.ClienteRepository;
import co.com.online_trainer.model.factory.FactoryObjects;
import co.com.online_trainer.model.factura.Factura;
import co.com.online_trainer.model.factura.gateway.FacturaRepository;
import co.com.online_trainer.model.guia.Guia;
import co.com.online_trainer.model.guia.gateway.GuiaRepository;
import co.com.online_trainer.model.registro.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;
import java.util.List;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Log
public class FacturaUseCase implements FactoryObjects {
    private final FacturaRepository facturaRepository;
    private final ClienteRepository clienteRepository;
    private final GuiaRepository guiaRepository;

    public Mono<Response<Object>> saveFacturaCotizacion(Factura factura){
        return  saveCliente(createCliente(factura))
                .flatMap(cliente -> getFacturaDB(factura.getIdCompra())
                        .flatMap(facturaDB ->  updateCotizacion(facturaDB,factura))
                        .switchIfEmpty(Mono.defer(() -> saveCotizacion(factura)))
                        .flatMap(cotizacion -> Mono.just(Response.builder()
                                .description("Cotizacion guardada correctamente guardada correctamente")
                                .results(cotizacion)
                                .build())))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    public Mono<Response<Object>> realizarCompra(Factura facturaRequest){
        return validarDatosObligatorios(facturaRequest)
                .flatMap(factura -> saveCliente(createCliente(factura))
                        .flatMap(cliente -> saveFacturaCompraRealiza(facturaRequest)
                                .flatMap(facturaDB -> Boolean.TRUE.equals(factura.getIsEnvio()) ? saveGuia(cliente) : Mono.just(factura))))
                .flatMap(guia -> Mono.just(Response.builder().results(guia).build()))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<Guia> saveGuia(Cliente cliente){
        return getIdGuia()
                .flatMap(idGuia -> guiaRepository.save(createGuia(cliente,idGuia)));
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
    private Mono<Factura> saveFacturaCompraRealiza(Factura facturaRequest){
        return Mono.just(facturaRequest)
                .flatMap(factura -> isNull(factura.getIdCompra()) || factura.getIdCompra() == 0 ?
                        getIdCompra()
                                .flatMap(idCompra ->
                                        Mono.just(factura.toBuilder().idCompra(idCompra).build()))  : Mono.just(factura))
                .flatMap(facturaRepository::save);
    }
    private Mono<Factura> validarDatosObligatorios(Factura facturaRequest){
        return  validarMediodePago(facturaRequest)
                .flatMap(medioPagoValido -> Boolean.TRUE.equals(medioPagoValido) ? Mono.just(facturaRequest) : Mono.empty())
                .filter(factura -> !isNull(factura.getProductos()) && factura.getProductos().size() > 0)
                .filter(factura -> !isNull(factura.getSubTotal()) && factura.getSubTotal() > 0)
                .filter(factura -> !isNull(factura.getTotalFactura()) && factura.getTotalFactura() > 0)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("Erros en los datos del request"))));
    }
    private Mono<Boolean> validarMediodePago(Factura facturaRequets){
        return Mono.just(facturaRequets)
                .filter(factura -> !isNull(factura.getMedioPago()))
                .flatMap(factura -> {
                    switch (factura.getMedioPago()){
                        case "1" :
                            return  Mono.just(!isNull(factura.getEfectivo()) && factura.getEfectivo() > 0);
                        case "2" :
                            return Mono.just(!isNull(factura.getEfectivo()) && factura.getOtroMedioPago() > 0);
                        case "3" :
                            return Mono.just(!isNull(factura.getEfectivo()) && factura.getOtroMedioPago() > 0 && factura.getEfectivo() > 0);
                        default:
                            return Mono.just(Boolean.FALSE);
                    }
                });
    }
    private Mono<Factura> updateCotizacion(Factura facturaDB,Factura facturaRequest){
        return  Mono.just(facturaDB)
                        .filter(factura -> Boolean.FALSE.equals(factura.getIsCompraRealizada()))
                        .flatMap(factura -> Mono.defer(()->  facturaRepository.save(buildFactura(factura,facturaRequest,Boolean.FALSE))
                                        .switchIfEmpty(Mono.defer(()-> Mono.error(new RuntimeException("ERROR no se guardo la cotizacion"))))))
                        .switchIfEmpty(Mono.defer(()-> Mono.error(new RuntimeException("factura inabilitada compra ya fue realizar"))));
    }
    private Mono<Factura> saveCotizacion(Factura factura){
        return   getIdCompra()
                        .flatMap(idCompra -> facturaRepository.save(factura.toBuilder()
                                        .id(null)
                                .idCompra(idCompra)
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
        return  facturaRepository.findByIdCompra(idCompra)
                .filter(factura -> !factura.getIsCompraRealizada());
    }
    private Mono<Cliente> getFacturaDBIdentificacion(String identificacion){
        return  clienteRepository.findByIdenficacion(identificacion);
    }
    private Mono<Cliente> getFacturaDBTelefono(String telefono){
        return  clienteRepository.findByTelefono(Long.parseLong(telefono));
    }
    private Mono<Integer> getIdCompra() {
        return facturaRepository.findAll()
                .collectList()
                .flatMap(this::getIdCompraSiguiente)
                .map(idCompra -> idCompra);
    }
    private Mono<Integer> getIdCompraSiguiente(List<Factura> cotizaciones){
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
    public Mono<Response<Object>> getDatosClienteIdentificacion(String identificacion){
        return getFacturaDBIdentificacion(identificacion)
                .flatMap(clienteDB -> Mono.just(Response.builder()
                        .results(clienteDB)
                        .build()))
                .switchIfEmpty(Mono.defer(()-> Mono.error(new RuntimeException("no hay datos cliente"))))
                .onErrorResume(error -> Mono.just(Response.builder()
                        .description(error.getMessage())
                        .build()));
    }
    public Mono<Response<Object>> getFacturaTelefono(String telefono){
        return getFacturaDBTelefono(telefono)
                .flatMap(clienteDB -> Mono.just(Response.builder()
                        .results(clienteDB)
                        .build()))
                    .switchIfEmpty(Mono.defer(()-> Mono.error(new RuntimeException("no hay datos cliente"))))
                .onErrorResume(error -> Mono.just(Response.builder()
                        .description(error.getMessage())
                        .build()));
    }
    private Mono<Cliente> saveCliente(Cliente cliente){
        return  validarDatosVaciosCliente(cliente)
                .flatMap(clienteValidado -> isTelefonoUnico(cliente.getTelefono())
                        .filter(existeTelefono -> existeTelefono)
                        .flatMap(telefonoValido ->  clienteRepository.findByIdenficacion(cliente.getIdentificacion())
                                //.filter(clienteDB -> validarDatosVaciosCliente(cliente))
                                .flatMap(clienteDB ->  updateCliente(clienteDB,cliente).switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no se guardo el cliente")))))
                                        .switchIfEmpty(Mono.defer(()-> clienteRepository.save(cliente)))
                                )
                        .switchIfEmpty(Mono.defer(() -> clienteRepository.findByIdenficacion(cliente.getIdentificacion())
                                .flatMap(clienteDB -> updateClienteSinTelefono(clienteDB,cliente)))))
                .switchIfEmpty(Mono.defer(() -> Mono.just(cliente)))
                .onErrorResume(error -> Mono.error(new RuntimeException(error.getMessage())));
    }
    private Mono<Cliente> validarDatosVaciosCliente(Cliente cliente){
        return  Mono.just(cliente)
                .filter(cliente1 -> !cliente.getNombreCliente().isEmpty()
                        && !cliente.getDireccion().isEmpty()
                        && !cliente.getCuidad().isEmpty());
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
                .cuidad(!clienteDB.getCuidad().equals(clienteRequest.getCuidad()) ?
                        clienteRequest.getCuidad() :
                        clienteDB.getCuidad())
                .build());
    }
    private Mono<Cliente> updateClienteSinTelefono(Cliente clienteDB, Cliente clienteRequest){
        return clienteRepository.save(clienteDB.toBuilder()
                .direccion(!clienteDB.getDireccion().equals(clienteRequest.getDireccion()) ?
                        clienteRequest.getDireccion() :
                        clienteDB.getDireccion())
                .nombreCliente(!clienteDB.getNombreCliente().equals(clienteRequest.getNombreCliente()) ?
                        clienteRequest.getNombreCliente() :
                        clienteDB.getNombreCliente())
                .cuidad(!clienteDB.getCuidad().equals(clienteRequest.getCuidad()) ?
                        clienteRequest.getCuidad() :
                        clienteDB.getCuidad())
                .build())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("error al actualizar el cliente"))));
    }
    private Mono<Boolean> isTelefonoUnico(Long telefono){
        return clienteRepository.findByTelefono(telefono)
                .flatMap(cliente -> Mono.just(Boolean.FALSE))
                .switchIfEmpty(Mono.defer(() -> Mono.just(Boolean.TRUE)));
    }
}
