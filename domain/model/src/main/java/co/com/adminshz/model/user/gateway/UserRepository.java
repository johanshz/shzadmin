package co.com.adminshz.model.user.gateway;

import co.com.adminshz.model.registro.Request;
import co.com.adminshz.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> save(User usuario);
    Mono<User> findByCorreo(String correo);
    Mono<Request> findByUsuario(String usuario);

}
