package co.com.adminshz.mongo.contenidoapp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data()
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "contenidoapp")
public class ContenidoAppData {
    private String pathImagenGuia;
    private String nombreEmpresaNavbar;
    private String textoEncabezadoGuia;
    private String textoFooterGuia;
}
