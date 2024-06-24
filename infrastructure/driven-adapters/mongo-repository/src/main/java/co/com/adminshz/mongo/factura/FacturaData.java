package co.com.adminshz.mongo.factura;


import co.com.adminshz.mongo.product.ProductData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data()
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "factura")
public class FacturaData {
    @Id
    private String id;
    private Integer idCompra;
    private String identificacion;
    private String nombreCliente;
    private String direccion;
    private String fechaActual;
    private String usuario;
    private List<ProductData> productos;
    private Boolean isCompraRealizada;
    private String medioPago;
    private String notas;
    private Double subTotal;
    private Double descuento;
    private Double totalFactura;
    private Double efectivo;
    private Double otroMedioPago;
    private Double cambio;
    private Long telefono;
    private Double valorDescuentoTotal; //agregar en el resto del flujo
    private Double descuentoPorCompra;//agregar en el resto del flujo
    private Double valorTotalSinDescuento;//agregar en el resto del flujo
    private String cuidad;
}
