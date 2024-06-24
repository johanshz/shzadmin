package co.com.adminshz.usecase;

import co.com.adminshz.model.registro.*;

import co.com.adminshz.model.factory.FactoryObjects;
import co.com.adminshz.model.user.User;
import co.com.adminshz.model.user.gateway.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log
public class RegisterUseCase implements FactoryObjects {

    private final UserRepository usuariosRepository;


    public Mono<Response<Object>> registerUser(Request request){
        return validarUsuario(request)
                    .flatMap(object -> usuariosRepository.save(createUsuario(request))
                            .flatMap(user -> getToken().map(token -> Response.builder()
                                    .description("usuario creado de forma exitosa")
                                    .results(User.builder().nombre(user.getNombre()).token(token).usuario(user.getUsuario()).build())
                                    .build()))
                            .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("Error al crear el usuario")))))
                .onErrorResume(error -> Mono.just(Response.builder()
                        .description(error.getMessage())
                        .build()));
    }
    private Mono<Object> validarUsuario(Request request){
      return usuariosRepository.findByUsuario(request.getUsuario())
              .flatMap(usuario -> Mono.error(new RuntimeException("usuario ya existe")))
              .switchIfEmpty(Mono.defer(() -> Mono.just(request)));
    }
    public Mono<Response<Object>> login(Request user){

          return usuariosRepository.findByCorreo(user.getCorreo())
                  .filter(userDB -> user.getCorreo().equals(userDB.getCorreo()) && user.getPassword().equals(userDB.getPassword()))
                  .flatMap(userDB -> getToken().map(token -> Response.builder()
                                  .description("acceso correcto")
                                  .results(User.builder().nombre(userDB.getNombre()).token(token).usuario(userDB.getUsuario()).build())
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

