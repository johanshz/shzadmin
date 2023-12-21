package co.com.online_trainer.usecase;

import co.com.online_trainer.model.categoria.Categoria;
import co.com.online_trainer.model.categoria.gateway.CategoriaRepository;
import co.com.online_trainer.model.product.Product;
import co.com.online_trainer.model.product.gateway.ProductRepository;
import co.com.online_trainer.model.proveedor.Proveedor;
import co.com.online_trainer.model.proveedor.gateway.ProveedorRepository;
import co.com.online_trainer.model.registro.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log
public class ProductUseCase {
    private final ProductRepository productRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;

    public Mono<Response<Object>> saveProduct(Product product) {
        return getProveedor(product.getIdProveedor())
                .flatMap(proveedor -> getNombreCategoria(product.getIdCategoria())
                        .flatMap(categoria -> getFlete(product, proveedor.getPorcentaje())
                                .flatMap(flete -> getCostoTotal(product, flete)
                                        .flatMap(costoTotal -> getGanancia(product, costoTotal)
                                                .flatMap(ganancia -> getPorcentajeRentabilidad(ganancia, product)
                                                        .flatMap(porcentajeRentabilidad -> getPorcentajeGeneral(product, 0.85)
                                                                .flatMap(porcentaje15 -> getPorcentajeGeneral(product, 0.90)
                                                                        .flatMap(porcentaje10 -> getPorcentajeGeneral(product, 0.95)
                                                                                .flatMap(porcentaje5 -> productRepository.save(product.toBuilder()
                                                                                                .nombreProducto(product.getNombreProducto().toUpperCase())
                                                                                                .flete(flete)
                                                                                                .costoTotal(costoTotal)
                                                                                                .ganancia(ganancia)
                                                                                                .porcentajeRentabilidad(porcentajeRentabilidad)
                                                                                                .porcentaje15(porcentaje15)
                                                                                                .percentaje10(porcentaje10)
                                                                                                .percentaje5(porcentaje5)
                                                                                                .categoria(categoria)
                                                                                                .proveedor(proveedor)
                                                                                                .entradas(0)
                                                                                                .descuento(0)
                                                                                                .build())
                                                                                        .flatMap(productDB -> Mono.just(Response.builder()
                                                                                                .results(product)
                                                                                                .build())))))))))));
    }

    private Mono<Double> getFlete(Product product, Double porcentajeProveedor) {
        return Mono.just(product.getPreciosCompras() * porcentajeProveedor);
    }

    private Mono<Double> getCostoTotal(Product product, Double flete) {
        return Mono.just(flete + product.getPreciosCompras());
    }

    private Mono<Double> getGanancia(Product product, Double costoTotal) {
        return Mono.just(product.getPrecioVenta() > costoTotal ? product.getPrecioVenta() - costoTotal : costoTotal - product.getPrecioVenta());
    }

    private Mono<Double> getPorcentajeRentabilidad(Double ganancia, Product product) {
        return Mono.just((ganancia / product.getPrecioVenta()) * 100);
    }

    private Mono<Double> getPorcentajeGeneral(Product product, Double numeroAmultiplicar) {
        return Mono.just(product.getPrecioVenta() * numeroAmultiplicar);
    }

    public Mono<Response<Object>> getProduct(String codigo) {
        return getProductDB(codigo)
                .flatMap(product -> Mono.just(Response.builder()
                        .results(product)
                        .build()))
                .onErrorResume(error -> Mono.just(Response.builder()
                        .description(error.getMessage())
                        .build()));
    }

    private Mono<Product> getProductDB(String reference) {
        return productRepository.findByCodigo(reference)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existe codigo en la base de datos"))));
    }

    private Mono<Categoria> getNombreCategoria(String id) {
        return categoriaRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("No existe categoria"))));
    }

    private Mono<Proveedor> getProveedor(String id) {
        return proveedorRepository.findById(id)
                .flatMap(Mono::just)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("No existe proveedor"))));
    }
    public Mono<Response<Object>> getProductos(){
        return productRepository.findAll()
                .collectList()
                .flatMap(productoDB -> Mono.just(Response.builder()
                        .description("busqueda correcta")
                        .results(productoDB)
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existen productos"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    public Mono<Response<Object>> deleteProducto(String id){
        return productRepository.findById(id)
                .flatMap(producto -> eliminar(producto.getId()))
                .flatMap(message -> Mono.just(Response.builder()
                        .description(message)
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existen productos"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<String> eliminar(String id){
        return productRepository.deleteById(id)
                .thenReturn("se elimino correctamente");
    }
    public Mono<Response<Object>> updateProducto(Product producto){
        return productRepository.findById(producto.getId())
                .flatMap(productoDB -> update(productoDB,producto))
                .flatMap(producto1 -> Mono.just(Response.builder()
                        .results(producto1)
                        .description("se actualizo correctamente el categoria")
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("no existen productos"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
    private Mono<Product> update(Product productoDB,Product productoRequest){
        return getProveedor(productoRequest.getIdProveedor())
                .flatMap(proveedor -> getNombreCategoria(productoRequest.getIdCategoria())
                        .flatMap(categoria-> getFlete(productoRequest, proveedor.getPorcentaje())
                                .flatMap(flete -> getCostoTotal(productoRequest, flete)
                                        .flatMap(costoTotal -> getGanancia(productoRequest, costoTotal)
                                                .flatMap(ganancia -> getPorcentajeRentabilidad(ganancia, productoRequest)
                                                        .flatMap(porcentajeRentabilidad -> getPorcentajeGeneral(productoRequest, 0.85)
                                                                .flatMap(porcentaje15 -> getPorcentajeGeneral(productoRequest, 0.90)
                                                                        .flatMap(porcentaje10 -> getPorcentajeGeneral(productoRequest, 0.95)
                                                                                .flatMap(porcentaje5 -> productRepository.save(productoDB.toBuilder()
                                                                                                .nombreProducto(!productoDB.getNombreProducto().equalsIgnoreCase(productoRequest.getNombreProducto()) ?
                                                                                                        productoRequest.getNombreProducto().toUpperCase() : productoDB.getNombreProducto().toUpperCase())
                                                                                                .codigo(!productoDB.getCodigo().equals(productoRequest.getCodigo()) ?
                                                                                                        productoRequest.getCodigo() : productoDB.getCodigo())
                                                                                                .referencia(!productoDB.getReferencia().equals(productoRequest.getReferencia()) ?
                                                                                                        productoRequest.getReferencia() : productoDB.getReferencia())
                                                                                                .medidas(!productoDB.getMedidas().equals(productoRequest.getMedidas()) ?
                                                                                                        productoRequest.getMedidas() : productoDB.getMedidas())
                                                                                                .categoria(!productoDB.getCategoria().getNombre().equals(categoria.getNombre()) ?
                                                                                                        categoria : productoDB.getCategoria())
                                                                                                .preciosCompras(!productoDB.getPreciosCompras().equals(productoRequest.getPreciosCompras()) ?
                                                                                                        productoRequest.getPreciosCompras() : productoDB.getPreciosCompras())
                                                                                                .precioVenta(!productoDB.getPrecioVenta().equals(productoRequest.getPrecioVenta()) ?
                                                                                                        productoRequest.getPrecioVenta() : productoDB.getPrecioVenta())
                                                                                               // .proveedorPorcentaje(!productoDB.getProveedorPorcentaje().equals(productoRequest.getProveedorPorcentaje()) ?
                                                                                               //         productoRequest.getProveedorPorcentaje() : productoDB.getProveedorPorcentaje())
                                                                                                .proveedor(!productoDB.getProveedor().getNombre().equals(proveedor.getNombre()) ?
                                                                                                        proveedor : productoDB.getProveedor())
                                                                                                .flete(!productoDB.getFlete().equals(flete) ?
                                                                                                        flete : productoDB.getFlete())
                                                                                                .costoTotal(!productoDB.getCostoTotal().equals(costoTotal) ?
                                                                                                        costoTotal : productoDB.getCostoTotal())
                                                                                                .porcentajeRentabilidad(!productoDB.getPorcentajeRentabilidad().equals(porcentajeRentabilidad) ?
                                                                                                        porcentajeRentabilidad : productoDB.getPorcentajeRentabilidad())
                                                                                                .ganancia(!productoDB.getGanancia().equals(ganancia) ?
                                                                                                        ganancia : productoDB.getGanancia())
                                                                                                .porcentaje15(!productoDB.getPorcentaje15().equals(porcentaje15) ?
                                                                                                        porcentaje15 : productoDB.getPorcentaje15())
                                                                                                .percentaje10(!productoDB.getPercentaje10().equals(porcentaje10) ?
                                                                                                        porcentaje10 : productoDB.getPercentaje10())
                                                                                                .percentaje5(!productoDB.getPercentaje5().equals(porcentaje5) ?
                                                                                                        porcentaje5 : productoDB.getPercentaje5())
                                                                                                .stock(productoDB.getStock() + productoRequest.getEntradas())
                                                                                                .descuento(!productoDB.getDescuento().equals(productoRequest.getDescuento()) ?
                                                                                                        productoRequest.getDescuento() : productoDB.getDescuento())
                                                                                                .build())
                                                                                     )))))))));
    }
    public Mono<Response<Object>> getProductoById(String id){
        return productRepository.findById(id)
                .flatMap(pruducto -> Mono.just(Response.builder()
                        .results(pruducto)
                        .build()));
    }


}
