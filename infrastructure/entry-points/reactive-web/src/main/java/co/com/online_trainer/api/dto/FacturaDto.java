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
    private String id;
    private Integer idCompra;
    private String identificacion;
    private String nombreCliente;
    private String direccion;
    private String fechaActual;
    private String usuario;
    private List<ProductDto> productos;
    private Boolean isCompraRealizada; //pasar false
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
    private Boolean isEnvio;
}
