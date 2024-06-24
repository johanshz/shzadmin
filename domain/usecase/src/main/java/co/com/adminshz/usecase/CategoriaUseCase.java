package co.com.adminshz.usecase;

import co.com.adminshz.model.categoria.Categoria;
import co.com.adminshz.model.categoria.gateway.CategoriaRepository;
import co.com.adminshz.model.registro.Response;
import co.com.adminshz.utils.UtilsFuntions;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log
public class CategoriaUseCase {
    private final CategoriaRepository categoriaRepository;
    public Mono<Response<Object>> saveCategoria(Categoria categoria){
        return  existNombre(categoria.getNombre().toUpperCase())
                .flatMap(proveedorDB -> Mono.just(Response.builder()
                        .description("categoria ya existe")
                        .results(proveedorDB)
                        .build()))
                .switchIfEmpty(Mono.defer(() ->  categoriaRepository.save(categoria.toBuilder()
                                .nombre(categoria.getNombre().toUpperCase())
                                .build())
                        .flatMap(proveedorDB -> Mono.just(Response.builder()
                                .description("se guardo correctamente")
                                .results(proveedorDB)
                                .build()))))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no se guardo el proveedor"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<Categoria> existNombre(String nombre){
        return categoriaRepository.findByNombre(nombre);
    }
    public Mono<Response<Object>> getCategoria(){
        return categoriaRepository.findAll()
                .collectList()
                .flatMap(UtilsFuntions::OrdenarListaDescendente)
                .flatMap(categoriaDB -> Mono.just(Response.builder()
                        .description("busqueda correcta")
                        .results(categoriaDB)
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existen proveedores"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    public Mono<Response<Object>> updateCategoria(Categoria categoria){
        return categoriaRepository.findById(categoria.getId())
                .flatMap(categoriaDB -> update(categoriaDB,categoria))
                .flatMap(categoria1 -> Mono.just(Response.builder()
                        .results(categoria1)
                        .description("se actualizo correctamente el categoria")
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existen categorias"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<Categoria> update(Categoria categoriaDB,Categoria categoriaRequest){
        return categoriaRepository.save(categoriaDB.toBuilder()
                .nombre(!categoriaDB.getNombre().equalsIgnoreCase(categoriaRequest.getNombre()) ?
                        categoriaRequest.getNombre().toUpperCase() : categoriaDB.getNombre().toUpperCase())
                .build());
    }
    public Mono<Response<Object>> deleteCategoria(String id){
        return categoriaRepository.findById(id)
                .flatMap(categoria -> eliminar(categoria.getId()))
                .flatMap(message -> Mono.just(Response.builder()
                        .description(message)
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existen categorias"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<String> eliminar(String id){
        return categoriaRepository.deleteById(id)
                .thenReturn("se elimino correctamente");
    }
    public Mono<Response<Object>> getCategoriaById(String id){
        return categoriaRepository.findById(id)
                .flatMap(categoria -> Mono.just(Response.builder()
                        .results(categoria)
                        .build()));
    }

}
