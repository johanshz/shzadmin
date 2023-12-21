package co.com.online_trainer.model.registro;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Request implements Serializable {
    private String nombre;
    private String correo;
    private String password;
}

