package co.com.online_trainer.model.factory;

import co.com.online_trainer.model.factura.Factura;
import co.com.online_trainer.model.registro.Request;
import co.com.online_trainer.model.user.User;

import java.time.LocalDateTime;


public interface FactoryObjects {
    default User createUsuario(Request request) {
        return User.builder()
                .correo(request.getCorreo())
                .nombre(request.getNombre())
                .password(request.getPassword())
                .build();
    }
    default Factura buildFactura(Factura facturaDB, Factura factura,Boolean isCompraRealizada) {
        LocalDateTime fechaActual = LocalDateTime.now();
        return facturaDB.toBuilder()
                .build();
    }

}
