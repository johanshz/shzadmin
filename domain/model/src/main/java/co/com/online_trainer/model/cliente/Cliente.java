package co.com.online_trainer.model.cliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Cliente {
    private String id;
    private String identificacion;
    private String nombreCliente;
    private Long telefono;
    private String direccion;
    private String cuidad;

}
