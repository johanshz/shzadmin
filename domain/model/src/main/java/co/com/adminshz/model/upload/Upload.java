package co.com.adminshz.model.upload;

import co.com.adminshz.model.factura.Factura;


import reactor.core.publisher.Mono;

import java.io.File;

public interface Upload {
   <T> Mono<String>  cargaArchivo(UploadFile<T> file);
}
