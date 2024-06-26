package co.com.online_trainer.usecase;

import co.com.online_trainer.model.registro.*;

import co.com.online_trainer.model.factory.FactoryObjects;
import co.com.online_trainer.model.user.User;
import co.com.online_trainer.model.user.gateway.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log
public class RegisterUseCase implements FactoryObjects {

    private final UserRepository usuariosRepository;


    public Mono<Response<Object>> registerUser(Request request){
        return usuariosRepository.save(createUsuario(request))
                .flatMap(user -> getToken().map(token -> Response.builder()
                        .description("usuario creado de forma exitosa")
                        .results(User.builder().nombre(user.getNombre()).token(token).build())
                        .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("Error al crear el usuario"))))
                .onErrorResume(error -> Mono.just(Response.builder()
                        .description(error.getMessage())
                        .build()));
    }
    public Mono<Response<Object>> login(Request user){

          return usuariosRepository.findByCorreo(user.getCorreo())
                  .filter(userDB -> user.getCorreo().equals(userDB.getCorreo()) && user.getPassword().equals(userDB.getPassword()))
                  .flatMap(userDB -> getToken().map(token -> Response.builder()
                                  .description("acceso correcto")
                                  .results(User.builder().nombre(userDB.getNombre()).token(token).build())
                                  .build())
                          )
                  .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("Correo o clave invalidos"))))
                  .onErrorResume(error -> Mono.just(Response.builder()
                          .description(error.getMessage())
                          .build()));
    }
    private Mono<String> getToken(){
        return Mono.just("Token de prueba");//despues se rempleza por un token de verdad
    }
}

