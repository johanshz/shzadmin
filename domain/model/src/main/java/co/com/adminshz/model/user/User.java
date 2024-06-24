package co.com.adminshz.model.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor

public class User {
    private String nombre;
    private String correo;
    private String password;
    private String token;
    private String usuario;

}
