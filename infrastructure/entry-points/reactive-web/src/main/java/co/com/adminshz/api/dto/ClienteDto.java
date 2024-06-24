package co.com.adminshz.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
public class ClienteDto {
    private String id;
    private String identificacion;
    private String nombreCliente;
    private Long telefono;
    private String direccion;
    private String cuidad;
}
