package co.com.adminshz.mongo.guia;

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
@Document(collection = "guia")
public class GuiaData {
    @Id
    private String id;
    private String nombreCliente;
    private String direccion;
    private Long telefono;
    private String cuidad;
    private Integer idGuia;
}
