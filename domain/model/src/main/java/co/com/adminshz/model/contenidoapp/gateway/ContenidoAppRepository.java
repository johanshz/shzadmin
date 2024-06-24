package co.com.adminshz.model.contenidoapp.gateway;

import co.com.adminshz.model.contenidoapp.ContenidoApp;
import reactor.core.publisher.Mono;

public interface ContenidoAppRepository {
    Mono<ContenidoApp> save(ContenidoApp contenidoApp);
}
