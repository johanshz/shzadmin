package co.com.online_trainer.model.guia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Guia {
    private String id;
    private String nombreCliente;
    private String direccion;
    private Long telefono;
    private String cuidad;
    private Integer idGuia;
}
