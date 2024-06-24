package co.com.adminshz.usecase;


import co.com.adminshz.model.proveedor.Proveedor;
import co.com.adminshz.model.proveedor.gateway.ProveedorRepository;
import co.com.adminshz.model.registro.Response;
import co.com.adminshz.utils.UtilsFuntions;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Log
public class ProveedorUseCase {
    private final ProveedorRepository proveedorRepository;
    public Mono<Response<Object>> saveProveedor(Proveedor proveedor){
        return  existNombre(proveedor.getNombre().toUpperCase())
                .flatMap(proveedorDB -> Mono.just(Response.builder()
                        .description("proveedor ya existe")
                        .results(proveedorDB)
                        .build()))
                .switchIfEmpty(Mono.defer(() ->  proveedorRepository.save(proveedor.toBuilder()
                                .nombre(proveedor.getNombre().toUpperCase())
                                .porcentaje(proveedor.getPorcentaje())
                                .build())
                        .flatMap(proveedorDB -> Mono.just(Response.builder()
                        .description("se guardo correctamente")
                        .results(proveedorDB)
                        .build()))))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no se guardo el proveedor"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    public Mono<Response<Object>> getProveedor(){
        return proveedorRepository.findAll()
                .collectList()
                .flatMap(UtilsFuntions::OrdenarListaDescendente)
                .flatMap(proveedorDB -> Mono.just(Response.builder()
                        .description("busqueda correcta")
                        .results(proveedorDB)
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existen proveedores"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }

    private Mono<Proveedor> existNombre(String nombre){
         return proveedorRepository.findByNombre(nombre);
    }
    public Mono<Response<Object>> updateProveedor(Proveedor proveedor){
          return proveedorRepository.findById(proveedor.getId())
                  .flatMap(proveedorDB -> update(proveedorDB,proveedor))
                  .flatMap(proveedor1 -> Mono.just(Response.builder()
                                  .results(proveedor1)
                                  .description("se actualizo correctamente el proveedor")
                          .build()))
                  .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existen proveedores"))))
                  .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }

    private Mono<Proveedor> update(Proveedor proveedorDB,Proveedor proveedorRequest){
        return proveedorRepository.save(proveedorDB.toBuilder()
                .nombre(!proveedorDB.getNombre().equalsIgnoreCase(proveedorRequest.getNombre()) ?
                        proveedorRequest.getNombre().toUpperCase() : proveedorDB.getNombre().toUpperCase())
                .porcentaje(!proveedorDB.getPorcentaje().equals(proveedorRequest.getPorcentaje()) ?
                        proveedorRequest.getPorcentaje() : proveedorDB.getPorcentaje())
                .build());
    }
    public Mono<Response<Object>> deleteProveedor(String id){
        return proveedorRepository.findById(id)
                .flatMap(proveedor -> eliminar(proveedor.getId()))
                .flatMap(message -> Mono.just(Response.builder()
                        .description(message)
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existen proveedores"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<String> eliminar(String id){
       return proveedorRepository.deleteById(id)
               .thenReturn("elimino correctamente");
    }
    public Mono<Response<Object>> getProveedorById(String id){
        return proveedorRepository.findById(id)
                .flatMap(proveedor -> Mono.just(Response.builder()
                                .results(proveedor)
                        .build()));
    }

}
