package co.com.adminshz.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
public class ContenidoAppDto {
    private String pathImagenGuia;
    private String nombreEmpresaNavbar;
    private String textoEncabezadoGuia;
    private String textoFooterGuia;
}

