package co.com.adminshz.usecase;

import co.com.adminshz.model.contenidoapp.ContenidoApp;
import co.com.adminshz.model.contenidoapp.gateway.ContenidoAppRepository;
import co.com.adminshz.model.registro.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log
public class ContenidoAppUseCase {
    private final ContenidoAppRepository contenidoAppRepository;

    public Mono<Response<Object>> saveContenidoApp(ContenidoApp contenidoApp){
        return contenidoAppRepository.save(contenidoApp)
                .flatMap(contenidoDB -> Mono.just(Response.builder()
                                .results(contenidoApp)
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("error al guardar"))))
                .onErrorResume(error -> Mono.just(Response.builder().description(error.getMessage()).build()));
    }
}
