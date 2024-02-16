package co.com.online_trainer.mongo.cliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data()
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cliente")
public class ClienteData {
    @Id
    private String id;
    private String identificacion;
    private String nombreCliente;
    private Long   telefono;
    private String direccion;
    private String cuidad;
}
