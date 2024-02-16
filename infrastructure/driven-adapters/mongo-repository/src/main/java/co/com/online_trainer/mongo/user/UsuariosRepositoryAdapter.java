package co.com.online_trainer.mongo.user;

import co.com.online_trainer.model.registro.Request;
import co.com.online_trainer.model.user.User;
import co.com.online_trainer.model.user.gateway.UserRepository;
import co.com.online_trainer.mongo.helper.AdapterOperations;
import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Log
public class UsuariosRepositoryAdapter extends AdapterOperations<User,UsuariosData,String,UsuariosDBRepository> implements UserRepository {

    public UsuariosRepositoryAdapter(UsuariosDBRepository repository, ObjectMapper mapper){
        super(repository,mapper,d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> findByCorreo(String correo) {
        return repository.findByCorreo(correo);
    }

    @Override
    public Mono<Request> findByUsuario(String usuario) {
        return repository.findByUsuario(usuario);
    }
}
