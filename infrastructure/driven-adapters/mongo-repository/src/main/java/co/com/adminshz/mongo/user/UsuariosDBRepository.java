package co.com.adminshz.mongo.user;

import co.com.adminshz.model.registro.Request;
import co.com.adminshz.model.user.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface UsuariosDBRepository extends ReactiveMongoRepository<UsuariosData,String>, ReactiveQueryByExampleExecutor<UsuariosData> {
    Mono<User> findByCorreo(String email);
    Mono<Request> findByUsuario(String usuario);
}
