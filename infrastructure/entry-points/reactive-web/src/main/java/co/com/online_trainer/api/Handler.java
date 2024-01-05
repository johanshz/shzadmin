package co.com.online_trainer.api;


import co.com.online_trainer.api.dto.*;

import co.com.online_trainer.model.categoria.Categoria;
import co.com.online_trainer.model.factura.Factura;
import co.com.online_trainer.model.product.Product;
import co.com.online_trainer.model.proveedor.Proveedor;
import co.com.online_trainer.model.registro.Request;
import co.com.online_trainer.model.registro.Response;
import co.com.online_trainer.usecase.*;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Log
public class Handler {

    private final RegisterUseCase registerUseCase;
    private final ProductUseCase productUseCase;
    private final FacturaUseCase facturaUseCase;
    private final ProveedorUseCase proveedorUseCase;
    private final CategoriaUseCase categoriaUseCase;
    private final ObjectMapper objectMapper;

    public Mono<ServerResponse> registerUsers(ServerRequest serverRequest){
        return serverRequest.bodyToMono(RequestDto.class)
                .map(requestDto -> objectMapper.map(requestDto, Request.class))
                .flatMap(registerUseCase::registerUser)
                .flatMap(response -> ServerResponse.ok().body(Mono.just(response), Response.class));
    }
    public Mono<ServerResponse> login(ServerRequest serverRequest){
        return serverRequest.bodyToMono(RequestDto.class)
                .map(requestDto -> objectMapper.map(requestDto, Request.class))
                .flatMap(registerUseCase::login)
                .flatMap(response -> ServerResponse.ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> saveProveedor(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ProveedorDto.class)
                .map(proveedorDto -> objectMapper.map(proveedorDto , Proveedor.class))
                .flatMap(proveedorUseCase::saveProveedor)
                .flatMap(resonse -> ServerResponse.ok().body(Mono.just(resonse),Response.class));

    }
    public Mono<ServerResponse> saveCategoria(ServerRequest serverRequest){
        return serverRequest.bodyToMono(CategoriaDto.class)
                .map(categoriaDto -> objectMapper.map(categoriaDto , Categoria.class))
                .flatMap(categoriaUseCase::saveCategoria)
                .flatMap(resonse -> ServerResponse.ok().body(Mono.just(resonse),Response.class));

    }
    public Mono<ServerResponse> saveProduct(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ProductDto.class)
                .map(productDto -> objectMapper.map(productDto , Product.class))
                .flatMap(productUseCase::saveProduct)
                .flatMap(response -> ServerResponse.ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getProveedor(ServerRequest serverRequest){
        return proveedorUseCase.getProveedor()
                        .flatMap(response -> ServerResponse.ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> getCategoria(ServerRequest serverRequest){
        return categoriaUseCase.getCategoria()
                .flatMap(response -> ServerResponse.ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> getProduct(ServerRequest serverRequest){
        return serverRequest.queryParam("codigo")
                .map(productUseCase::getProduct)
                .orElse(Mono.just(Response.builder()
                                .description("parametros invalidos")
                        .build()))
                .flatMap(response -> ServerResponse.ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getCotizacion(ServerRequest serverRequest){
        return serverRequest.queryParam("idCompra")
                .map(facturaUseCase::getCotizacion)
                .orElse(Mono.just(Response.builder()
                        .description("parametros invalidos")
                        .build()))
                .flatMap(response -> ServerResponse.ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getProductos(ServerRequest serverRequest){
        return productUseCase.getProductos()
                .flatMap(response -> ServerResponse.ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> updateProduct(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ProveedorDto.class)
                .map(proveedorDto -> objectMapper.map(proveedorDto , Proveedor.class))
                .flatMap(proveedorUseCase::updateProveedor)
                .flatMap(resonse -> ServerResponse.ok().body(Mono.just(resonse),Response.class));
    }
    public Mono<ServerResponse> updateCategoria(ServerRequest serverRequest){
        return serverRequest.bodyToMono(CategoriaDto.class)
                .map(categoriaDto -> objectMapper.map(categoriaDto , Categoria.class))
                .flatMap(categoriaUseCase::updateCategoria)
                .flatMap(resonse -> ServerResponse.ok().body(Mono.just(resonse),Response.class));
    }
    public Mono<ServerResponse> updateProducto(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ProductDto.class)
                .map(productoDto -> objectMapper.map(productoDto , Product.class))
                .flatMap(productUseCase::updateProducto)
                .flatMap(resonse -> ServerResponse.ok().body(Mono.just(resonse),Response.class));
    }
    public Mono<ServerResponse> saveFactura(ServerRequest serverRequest){
        return serverRequest.bodyToMono(FacturaDto.class)
                .map(facturaDto -> objectMapper.map(facturaDto , Factura.class))
                .flatMap(facturaUseCase::saveFacturaCotizacion)
                .flatMap(resonse -> ServerResponse.ok().body(Mono.just(resonse),Response.class));

    }
    public Mono<ServerResponse> deleteProveedor(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(proveedorUseCase::deleteProveedor)
                .orElse(Mono.just(Response.builder()
                        .description("parametros invalidos")
                        .build()))
                .flatMap(response -> ServerResponse.ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getProveedorById(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(proveedorUseCase::getProveedorById)
                .orElse(Mono.just(Response.builder()
                        .description("parametros invalidos")
                        .build()))
                .flatMap(response -> ServerResponse.ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> getCategoriaById(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(categoriaUseCase::getCategoriaById)
                .orElse(Mono.just(Response.builder()
                        .description("parametros invalidos")
                        .build()))
                .flatMap(response -> ServerResponse.ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> getProductoById(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(productUseCase::getProductoById)
                .orElse(Mono.just(Response.builder()
                        .description("parametros invalidos")
                        .build()))
                .flatMap(response -> ServerResponse.ok().body(Mono.just(response),Response.class));
    }
    public Mono<ServerResponse> deleteCategoria(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(categoriaUseCase::deleteCategoria)
                .orElse(Mono.just(Response.builder()
                        .description("parametros invalidos")
                        .build()))
                .flatMap(response -> ServerResponse.ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> deleteProducto(ServerRequest serverRequest){
        return serverRequest.queryParam("id")
                .map(productUseCase::deleteProducto)
                .orElse(Mono.just(Response.builder()
                        .description("parametros invalidos")
                        .build()))
                .flatMap(response -> ServerResponse.ok().body(Mono.just(response),Response.class));

    }
    public Mono<ServerResponse> realizarCompra(ServerRequest serverRequest){
        return serverRequest.bodyToMono(FacturaDto.class)
                .map(facturaDto -> objectMapper.map(facturaDto , Factura.class))
                .flatMap(facturaUseCase::realizarCompra)
                .flatMap(resonse -> ServerResponse.ok().body(Mono.just(resonse),Response.class));

    }

}

