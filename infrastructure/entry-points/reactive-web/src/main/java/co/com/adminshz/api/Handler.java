package co.com.adminshz.api;


import co.com.adminshz.api.dto.*;

import co.com.adminshz.model.categoria.Categoria;
import co.com.adminshz.model.cliente.Cliente;
import co.com.adminshz.model.contenidoapp.ContenidoApp;
import co.com.adminshz.model.descuento.Descuento;
import co.com.adminshz.model.factura.Factura;
import co.com.adminshz.model.guia.Guia;
import co.com.adminshz.model.product.Product;
import co.com.adminshz.model.proveedor.Proveedor;
import co.com.adminshz.model.registro.Request;
import co.com.adminshz.model.registro.Response;
import co.com.adminshz.model.upload.UploadFile;
import co.com.adminshz.usecase.*;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@AllArgsConstructor
@Log
public class Handler {

    private final RegisterUseCase registerUseCase;
    private final ProductUseCase productUseCase;
    private final FacturaUseCase facturaUseCase;
    private final ProveedorUseCase proveedorUseCase;
    private final CategoriaUseCase categoriaUseCase;
    private final DescuentoUseCase descuentoUseCase;
    private final ClienteUseCase clienteUseCase;
    private final CargaArchivoUseCase cargaArchivoUseCase;
    private final GuiaUseCase guiaUseCase;
    private final ContenidoAppUseCase contenidoAppUseCase;
    private final ObjectMapper objectMapper;


    private final String PARAMETROS_INVALIDOS = "parametros invalidos";
    public Mono<ServerResponse> registerUsers(ServerRequest serverRequest){
        return serverRequest.bodyToMono(RequestDto.class)
                .map(requestDto -> objectMapper.map(requestDto, Request.class))
                .flatMap(registerUseCase::registerUser)
                .flatMap(response -> ok().body(Mono.just(response), Response.class));
    }
    public Mono<ServerResponse> login(ServerRequest serverRequest){
        return serverRequest.bodyToMono(RequestDto.class)
                .map(requestDto -> objectMapper.map(requestDto, Request.class))
                .flatMap(registerUseCase::login)
                .flatMap(response -> ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> saveProveedor(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ProveedorDto.class)
                .map(proveedorDto -> objectMapper.map(proveedorDto , Proveedor.class))
                .flatMap(proveedorUseCase::saveProveedor)
                .flatMap(resonse -> ok().body(Mono.just(resonse),Response.class));

    }
    public Mono<ServerResponse> saveDescuento(ServerRequest serverRequest){
        return serverRequest.bodyToMono(DescuentoDto.class)
                .map(descuentoDto -> objectMapper.map(descuentoDto , Descuento.class))
                .flatMap(descuentoUseCase::saveDescuento)
                .flatMap(resonse -> ok().body(Mono.just(resonse),Response.class));
    }
    public Mono<ServerResponse> saveGuia(ServerRequest serverRequest){
        return serverRequest.bodyToMono(GuiaDto.class)
                .map(guiaDto -> objectMapper.map(guiaDto, Guia.class))
                .flatMap(guiaUseCase::saveGuia)
                .flatMap(resonse -> ok().body(Mono.just(resonse),Response.class));
    }
    public Mono<ServerResponse> saveContenidoApp(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ContenidoAppDto.class)
                .map(contenidoDto -> objectMapper.map(contenidoDto, ContenidoApp.class))
                .flatMap(contenidoAppUseCase::saveContenidoApp)
                .flatMap(resonse -> ok().body(Mono.just(resonse),Response.class));
    }
    public Mono<ServerResponse> saveCategoria(ServerRequest serverRequest){
        return serverRequest.bodyToMono(CategoriaDto.class)
                .map(categoriaDto -> objectMapper.map(categoriaDto , Categoria.class))
                .flatMap(categoriaUseCase::saveCategoria)
                .flatMap(resonse -> ok().body(Mono.just(resonse),Response.class));

    }
    public Mono<ServerResponse> saveProduct(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ProductDto.class)
                .map(productDto -> objectMapper.map(productDto , Product.class))
                .flatMap(productUseCase::saveProduct)
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getProveedor(ServerRequest serverRequest){
        return proveedorUseCase.getProveedor()
                        .flatMap(response -> ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> getFacturas(ServerRequest serverRequest){
        return facturaUseCase.getFacturas()
                        .flatMap(response -> ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> getGuia(ServerRequest serverRequest){
        return guiaUseCase.getGuia()
                .flatMap(response -> ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> getDescuento(ServerRequest serverRequest){
        return descuentoUseCase.getDescuentos()
                        .flatMap(response -> ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> getCategoria(ServerRequest serverRequest){
        return categoriaUseCase.getCategoria()
                .flatMap(response -> ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> getProduct(ServerRequest serverRequest){
        return serverRequest.queryParam("codigo")
                .map(productUseCase::getProduct)
                .orElse(Mono.just(Response.builder()
                                .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> getCotizacion(ServerRequest serverRequest){
        return serverRequest.queryParam("idCompra")
                .map(facturaUseCase::getCotizacion)
                .orElse(Mono.just(Response.builder()
                        .description("parametros invalidos")
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getDatosClienteIdentificacion(ServerRequest serverRequest){
        return serverRequest.queryParam("identificacion")
                .map(facturaUseCase::getDatosClienteIdentificacion)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getDatosClientePorTelefono(ServerRequest serverRequest){
        return serverRequest.queryParam("telefono")
                .map(facturaUseCase::getFacturaTelefono)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getProductos(ServerRequest serverRequest){
        return productUseCase.getProductos()
                .flatMap(response -> ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> updateProvedor(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ProveedorDto.class)
                .map(proveedorDto -> objectMapper.map(proveedorDto , Proveedor.class))
                .flatMap(proveedorUseCase::updateProveedor)
                .flatMap(resonse -> ok().body(Mono.just(resonse),Response.class));
    }
    public Mono<ServerResponse> updateGuia(ServerRequest serverRequest){
        return serverRequest.bodyToMono(GuiaDto.class)
                .map(guiaDto -> objectMapper.map(guiaDto , Guia.class))
                .flatMap(guiaUseCase::updateGuia)
                .flatMap(resonse -> ok().body(Mono.just(resonse),Response.class));
    }
    public Mono<ServerResponse> updateCategoria(ServerRequest serverRequest){
        return serverRequest.bodyToMono(CategoriaDto.class)
                .map(categoriaDto -> objectMapper.map(categoriaDto , Categoria.class))
                .flatMap(categoriaUseCase::updateCategoria)
                .flatMap(resonse -> ok().body(Mono.just(resonse),Response.class));
    }
    public Mono<ServerResponse> updateProducto(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ProductDto.class)
                .map(productoDto -> objectMapper.map(productoDto , Product.class))
                .flatMap(productUseCase::updateProducto)
                .flatMap(resonse -> ok().body(Mono.just(resonse),Response.class));
    }
    public Mono<ServerResponse> saveFactura(ServerRequest serverRequest){
        return serverRequest.bodyToMono(FacturaDto.class)
                .map(facturaDto -> objectMapper.map(facturaDto , Factura.class))
                .flatMap(facturaUseCase::saveFacturaCotizacion)
                .flatMap(resonse -> ok().body(Mono.just(resonse),Response.class));
    }
    public Mono<ServerResponse> saveCliente(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ClienteDto.class)
                .map(clienteDto -> objectMapper.map(clienteDto , Cliente.class))
                .flatMap(clienteUseCase::saveCliente)
                .flatMap(resonse -> ok().body(Mono.just(resonse),Response.class));

    }
    public Mono<ServerResponse> deleteProveedor(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(proveedorUseCase::deleteProveedor)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> deleteGuia(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(guiaUseCase::deleteGuia)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> deleteDescuento(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(descuentoUseCase::deleteDescuento)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getProveedorById(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(proveedorUseCase::getProveedorById)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getFacturaById(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(facturaUseCase::getFacturaById)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getGuiaId(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(guiaUseCase::getGuiaId)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getDescuentoId(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(descuentoUseCase::getDescuentoById)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getCategoriaById(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(categoriaUseCase::getCategoriaById)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> getProductoById(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(productUseCase::getProductoById)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> deleteCategoria(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(categoriaUseCase::deleteCategoria)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> deleteProducto(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(productUseCase::deleteProducto)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> realizarCompra(ServerRequest serverRequest){
        return serverRequest.bodyToMono(FacturaDto.class)
                .map(facturaDto -> objectMapper.map(facturaDto , Factura.class))
                .flatMap(facturaUseCase::realizarCompra)
                .flatMap(resonse -> ok().body(Mono.just(resonse),Response.class));

    }
    public Mono<ServerResponse> getDescuentoCompra(ServerRequest serverRequest){
        return serverRequest.queryParam("valorTotalSinDescuento")
                .map(descuentoUseCase::getDescuentoCompra)
                .orElse(Mono.just(Response.builder()
                        .description(PARAMETROS_INVALIDOS)
                        .build()))
                .flatMap(response -> ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> handleCargaArchivo(ServerRequest serverRequest){
        return serverRequest.body(BodyExtractors.toMultipartData())
                .flatMap(this::extraerFilePart)
                .map(uploadDto -> objectMapper.map(uploadDto, UploadFile.class))
                .flatMap(cargaArchivoUseCase::cargaArchivo)
                .flatMap(response -> ok().body(Mono.just(response),Response.class));
    }

    private  Mono<UploadFileDto<Object>> extraerFilePart(MultiValueMap<String, Part> parts) {
        Part filePart = parts.toSingleValueMap().get("file");
        if (filePart instanceof FilePart) {
            FilePart file = (FilePart) filePart;
            return Mono.just(UploadFileDto.builder().file(file).build());
        } else {
            return Mono.error(new IllegalArgumentException("La parte no es un archivo"));
        }
    }


    private Mono<File> filePartToFile(FilePart filePart) {
        try {
            Path tempFile = Files.createTempFile("temp", filePart.filename());
            return DataBufferUtils.write(filePart.content(), tempFile)
                    .then(Mono.just(tempFile.toFile()));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

}

