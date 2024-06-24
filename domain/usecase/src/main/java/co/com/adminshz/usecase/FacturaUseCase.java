package co.com.adminshz.usecase;

import co.com.adminshz.model.cliente.Cliente;
import co.com.adminshz.model.cliente.gateway.ClienteRepository;
import co.com.adminshz.model.factory.FactoryObjects;
import co.com.adminshz.model.factura.Factura;
import co.com.adminshz.model.factura.gateway.FacturaRepository;
import co.com.adminshz.model.generadorpdf.PdfGenerador;
import co.com.adminshz.model.guia.Guia;
import co.com.adminshz.model.guia.gateway.GuiaRepository;
import co.com.adminshz.model.product.Product;
import co.com.adminshz.model.registro.Response;
import co.com.adminshz.utils.UtilsFuntions;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Log
public class FacturaUseCase implements FactoryObjects {
    private final FacturaRepository facturaRepository;
    private final ClienteRepository clienteRepository;
    private final GuiaRepository guiaRepository;
    private final PdfGenerador pdfGenerador;
    private final String DIRECTORIO_FACTURA = "C:\\Facturas";
    private final String DIRECTORIO_COTIZACION = "C:\\cotizaciones";
    public Mono<Response<Object>> saveFacturaCotizacion(Factura factura){
        return  saveCliente(createCliente(factura))
                .flatMap(cliente -> getFacturaDB(factura.getIdCompra())
                        .flatMap(facturaDB ->  updateCotizacion(facturaDB,factura))
                        .switchIfEmpty(Mono.defer(() -> saveCotizacion(factura)))
                        .flatMap(cotizacion -> imprimirFactura(cotizacion,DIRECTORIO_COTIZACION))
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
                                .flatMap(facturaDB -> Boolean.TRUE.equals(factura.getIsEnvio()) ?
                                        saveGuia(cliente).thenReturn(facturaDB) : Mono.just(facturaDB))
                                        .flatMap(facturaDB -> imprimirFactura(facturaDB,DIRECTORIO_FACTURA))
                                      ))
                .flatMap(idCompra -> Mono.just(Response.builder().results(idCompra).build()))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<Integer> imprimirFactura(Factura factura,String directorio){
        String nombreArchivo = "compra_#_" + factura.getIdCompra() + ".pdf";
        String directorioFacturas = directorio;
        String filePath = Paths.get(directorioFacturas, nombreArchivo).toString();
        return  ordenarProductosPorCategoria(factura)
                .flatMap(productsOrdenados -> pdfGenerador.generarPdf(factura.toBuilder().productos(productsOrdenados).build(), filePath)
                        .retry(2))
                .onErrorResume(error -> Mono.error(new RuntimeException(error.getMessage())));
    }

    private Mono<List<Product>> ordenarProductosPorCategoria(Factura factura){
        return  getCategoriasLista(factura.getProductos())
                .flatMap(categorias -> getProductosPorCatgoria(categorias,factura.getProductos()));
    }
    private Mono<List<Product>> getProductosPorCatgoria(List<String> categorias, List<Product> productos){
        return Flux.fromIterable(categorias)
                        .flatMap(categoria ->
                                  Flux.fromIterable(productos)
                                        .filter(product -> categoria.equals(product.getCategoria().getNombre()))
                        ).collectList();
    }

    private Mono<List<String>> getCategoriasLista(List<Product> productos){
        return Flux.fromIterable(productos)
                .map(product -> product.getCategoria().getNombre())
                .distinct()
                .collectList();
    }
    private Mono<List<String>> getCategoriasLista1(String categoria,List<String> categorias){
        return Flux.fromIterable(categorias)
                .filter(categoriaAdd -> !categorias.contains(categoriaAdd))//revisar que este contains cumpla con el objetivo que es verificar que exista no exista esa categoria en esa lista
                .collectList()
                .flatMap(categoriaList -> Mono.just(categoriaList.add(categoria))
                        .flatMap(b -> Mono.just(categoriaList)));
    }

    private Mono<Guia> saveGuia(Cliente cliente){
        return getIdGuia()
                .flatMap(idGuia -> guiaRepository.save(createGuia(cliente,idGuia)))
                .onErrorResume(error -> Mono.error(new RuntimeException(error.getMessage())));
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
                .flatMap(factura ->  facturaRepository.save(factura.toBuilder()
                        .nombreCliente(factura.getNombreCliente().toUpperCase())
                        .build()));
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
                                        .nombreCliente(!factura.getNombreCliente().isEmpty() ? factura.getNombreCliente().toUpperCase():"")
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
                                .flatMap(clienteDB ->  updateCliente(clienteDB,cliente).switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no se guardo el cliente")))))
                                        .switchIfEmpty(Mono.defer(()-> clienteRepository.save(cliente.toBuilder()
                                                        .nombreCliente(cliente.getNombreCliente().toUpperCase())
                                                .build())))
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
                .nombreCliente(!clienteDB.getNombreCliente().equalsIgnoreCase(clienteRequest.getNombreCliente()) ?
                        clienteRequest.getNombreCliente().toUpperCase() :
                        clienteDB.getNombreCliente().toUpperCase())
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
                .nombreCliente(!clienteDB.getNombreCliente().equalsIgnoreCase(clienteRequest.getNombreCliente()) ?
                        clienteRequest.getNombreCliente().toUpperCase() :
                        clienteDB.getNombreCliente().toUpperCase())
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
    public Mono<Response<Object>> getFacturas(){
        return facturaRepository.findAll()
                .collectList()
                .flatMap(UtilsFuntions::OrdenarListaDescendente)
                .flatMap(facturas -> Mono.just(Response.builder()
                                .description("Busqueda Exitosa")
                                .results(facturas)
                        .build()))
                .switchIfEmpty(Mono.defer(()-> Mono.error(new RuntimeException("No existen facturas"))))
                .onErrorResume(error -> Mono.just(Response.builder()
                                .description(error.getMessage())
                        .build()));
    }
    public Mono<Response<Object>> getFacturaById(String id){
        return facturaRepository.findById(id)
                .flatMap(factura -> Mono.just(Response.builder()
                                .description("Busqueda exitosa")
                                .results(factura)
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("No existe factura por id"))))
                .onErrorResume(error -> Mono.just(Response.builder()
                                .description(error.getMessage())
                        .build()));
    }
}
