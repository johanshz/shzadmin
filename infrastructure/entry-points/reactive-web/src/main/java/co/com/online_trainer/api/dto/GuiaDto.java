package co.com.online_trainer.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
public class GuiaDto {
    private String id;
    private String nombreCliente;
    private String direccion;
    private Long telefono;
    private String cuidad;
    private Integer idGuia;
}
