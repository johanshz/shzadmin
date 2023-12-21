package co.com.online_trainer.model.user.gateway;

import co.com.online_trainer.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> save(User usuario);
    Mono<User> findByCorreo(String correo);
}
