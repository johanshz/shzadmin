package co.com.adminshz.model.descuento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Descuento {
    private String id;
    private Integer valorMin;
    private Integer valorMax;
    private Integer valorDescuento;
}
