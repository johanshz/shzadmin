package co.com.online_trainer.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/registerUser"), handler::registerUsers)
                .andRoute(POST("/saveProduct"),handler::saveProduct)
                .andRoute(GET("/getProduct"),handler::getProduct)
                .andRoute(GET("/getProductos"),handler::getProductos)
                .andRoute(POST("/updateProducto"),handler::updateProducto)
                .andRoute(GET("/deleteProducto"),handler::deleteProducto)
                .andRoute(GET("/getProductoId"),handler::getProductoById)
                .andRoute(POST("/cotizar"),handler::saveFactura)
                .andRoute(GET("/getCotizacion"),handler::getCotizacion)
                .andRoute(GET("/getDatosClienteIdentificacion"),handler::getDatosClienteIdentificacion)
                .andRoute(GET("/getDatosClientePorTelefono"),handler::getDatosClientePorTelefono)
                .andRoute(POST("/realizarCompra"),handler::realizarCompra)
                .andRoute(POST("/login"),handler::login)
                .andRoute(POST("/saveProveedor"),handler::saveProveedor)
                .andRoute(GET("/getProveedor"),handler::getProveedor)
                .andRoute(GET("/getProveedorId"),handler::getProveedorById)
                .andRoute(POST("/updateProveedor"),handler::updateProvedor)
                .andRoute(GET("/deleteProveedor"),handler::deleteProveedor)
                .andRoute(POST("/saveCatagoria"),handler::saveCategoria)
                .andRoute(GET("/getCategoria"),handler::getCategoria)
                .andRoute(POST("/updateCategoria"),handler::updateCategoria)
                .andRoute(GET("/deleteCategoria"),handler::deleteCategoria)
                .andRoute(GET("/getCategoriaId"),handler::getCategoriaById)
                .andRoute(POST("/saveDescuento"),handler::saveDescuento)
                .andRoute(GET("/getDescuento"),handler::getDescuento)
                .andRoute(GET("/getDescuentoId"),handler::getDescuentoId)
                .andRoute(GET("/deleteDescuento"),handler::deleteDescuento)
                .andRoute(GET("/getDescuentoCompra"),handler::getDescuentoCompra)
                .andRoute(POST("/saveCliente"),handler::saveCliente)
                .andRoute(POST("/saveGuia"),handler::saveGuia)
                .andRoute(GET("/getGuia"),handler::getGuia)
                .andRoute(GET("/getGuiaId"),handler::getGuiaId)
                .andRoute(POST("/updateGuia"),handler::updateGuia)
                .andRoute(GET("/deleteGuia"),handler::deleteGuia);
    }
}
