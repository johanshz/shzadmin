package co.com.online_trainer.model.proveedor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Proveedor {
    private String id;
    private String nombre;
    private Double porcentaje;
}
