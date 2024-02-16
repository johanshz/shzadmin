package co.com.online_trainer.api.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
public class RequestDto {
    private String nombre;
    private String correo;
    private String password;
    private String usuario;
}