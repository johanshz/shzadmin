package co.com.online_trainer.mongo.factura;

import co.com.online_trainer.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
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
    private String nit;
    private String nombreCliente;
    private String direccion;
    private String fechaActual;
    private String caja;
    private List<Product> productos;
    private Boolean isCompraRealizada;
    private String medioPago;
    private String notas;
    private Double subTotal;
    private Double descuento;
    private Double totalFactura;
    private Double efectivo;
    private Double cambio;
}
