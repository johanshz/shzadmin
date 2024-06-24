package co.com.adminshz.usecase;


import co.com.adminshz.model.registro.Response;
import co.com.adminshz.model.upload.Upload;
import co.com.adminshz.model.upload.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.io.File;




@RequiredArgsConstructor
@Log
public class CargaArchivoUseCase {
    private final Upload upload;
    public <T> Mono<Response<Object>> cargaArchivo(UploadFile<T> file) {
       return Mono.just(file)
               .flatMap(upload::cargaArchivo)
               .flatMap(response -> Mono.just(Response.builder().results(response).build()))
               .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }

}
