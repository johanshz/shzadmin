package co.com.adminshz.model.generadorpdf;

import co.com.adminshz.model.factura.Factura;
import reactor.core.publisher.Mono;

public interface PdfGenerador {
    Mono<Integer> generarPdf(Factura factura,String filePath);
}
