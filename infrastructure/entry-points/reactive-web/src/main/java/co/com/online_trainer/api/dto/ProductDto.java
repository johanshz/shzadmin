package co.com.online_trainer.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
public class ProductDto {
    private String id;
    private Integer cantidad;
    private String nombreProducto; //viene formu
    private String codigo; //viene formu
    private String referencia; //viene formu
    private String medidas;//viene formu
    private String idCategoria;//viene formu tabla solo nombre
    private Double preciosCompras;//viene formu
    private Double precioVenta;//viene formu
    private Double proveedorPorcentaje;//viene formu tabla con un select
    private String idProveedor;//viene formu
    private Double flete;//precio compra x porcentaje segun provedor -1
    private Double costoTotal;//precio compra mas flete -2
    private Double porcentajeRentabilidad;//ganancia dividido precio venta por 100 -4
    private Double ganancia; //precio venta menos costo total -3
    private Double porcentaje15;//precio venta por 0.85 -5
    private Double percentaje10;//precio venta por 0.90 -6
    private Double percentaje5;//0.95 -7
    private Integer stock;
    private Double precioTotal;
    private Integer descuento;
    private Integer existencias;
    private Integer entradas;
    private Integer salidas;


}
