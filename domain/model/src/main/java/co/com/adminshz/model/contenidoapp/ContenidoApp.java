package co.com.adminshz.model.contenidoapp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class ContenidoApp {
    private String pathImagenGuia;
    private String nombreEmpresaNavbar;
    private String textoEncabezadoGuia;
    private String textoFooterGuia;
}
