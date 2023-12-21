package co.com.online_trainer.api.dto;

import co.com.online_trainer.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
public class FacturaDto {
    private Integer idCompra;
    private String nit;
    private String nombreCliente;
    private String direccion;
    private String fechaActual;
    private String caja;
    private List<Product> productos;
    private Boolean isCompraRealizada; //pasar false
    private String medioPago;
    private String notas;
    private Double subTotal;
    private Double descuento; //seria descuentoPorCompra
    private Double totalFactura;
    private Double efectivo;
    private Double cambio;
}
