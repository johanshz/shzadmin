package co.com.adminshz.model.factory;

import co.com.adminshz.model.cliente.Cliente;
import co.com.adminshz.model.factura.Factura;
import co.com.adminshz.model.guia.Guia;
import co.com.adminshz.model.registro.Request;
import co.com.adminshz.model.user.User;
import reactor.core.publisher.Mono;


public interface FactoryObjects {
    default User createUsuario(Request request) {
        return User.builder()
                .correo(request.getCorreo())
                .nombre(request.getNombre())
                .password(request.getPassword())
                .usuario(request.getUsuario())
                .build();
    }
    default Cliente createCliente(Factura request) {
        return Cliente.builder()
                .nombreCliente(request.getNombreCliente())
                .telefono(request.getTelefono())
                .identificacion(request.getIdentificacion())
                .direccion(request.getDireccion())
                .cuidad(request.getCuidad())
                .build();
    }
    default Guia createGuia(Cliente cliente,Integer idGuia){
        return Guia.builder()
                .idGuia(idGuia)
                .cuidad(cliente.getCuidad())
                .direccion(cliente.getDireccion())
                .nombreCliente(cliente.getNombreCliente().toUpperCase())
                .telefono(cliente.getTelefono())
                .build();
    }
    default Factura buildFactura(Factura facturaDB, Factura factura,Boolean isCompraRealizada) {
        return facturaDB.toBuilder()
                .notas(!facturaDB.getNotas().equals(factura.getNotas()) ? factura.getNotas() : facturaDB.getNotas())
                .valorTotalSinDescuento(!facturaDB.getValorTotalSinDescuento().equals(factura.getValorTotalSinDescuento())?factura.getValorTotalSinDescuento():facturaDB.getValorTotalSinDescuento())
                .productos(factura.getProductos())
                .telefono(!facturaDB.getTelefono().equals(factura.getTelefono()) ? factura.getTelefono() : facturaDB.getTelefono())
                .usuario(!facturaDB.getUsuario().equals(factura.getUsuario()) ? factura.getUsuario() : facturaDB.getUsuario())
                .totalFactura(!facturaDB.getTotalFactura().equals(factura.getTotalFactura()) ? factura.getTotalFactura() : facturaDB.getTotalFactura())
                .subTotal(!facturaDB.getSubTotal().equals(factura.getSubTotal()) ? factura.getSubTotal() : facturaDB.getSubTotal())
                .cambio(!facturaDB.getCambio().equals(factura.getCambio()) ? factura.getCambio() : facturaDB.getCambio())
                //.medioPago(!facturaDB.getMedioPago().equals(factura.getMedioPago()) ? factura.getMedioPago() : facturaDB.getMedioPago())
                .efectivo(!facturaDB.getEfectivo().equals(factura.getEfectivo()) ? factura.getEfectivo() : facturaDB.getEfectivo())
                .descuento(!facturaDB.getDescuento().equals(factura.getDescuento()) ? factura.getDescuento() : facturaDB.getDescuento())
                .direccion(!facturaDB.getDireccion().equals(factura.getDireccion()) ? factura.getDireccion() : facturaDB.getDireccion())
                .identificacion(!facturaDB.getIdentificacion().equals(factura.getIdentificacion()) ? factura.getIdentificacion() : facturaDB.getIdentificacion())
                .fechaActual(!facturaDB.getFechaActual().equals(factura.getFechaActual()) ? factura.getFechaActual() : facturaDB.getFechaActual())
                .descuentoPorCompra(!facturaDB.getDescuentoPorCompra().equals(factura.getDescuentoPorCompra()) ? factura.getDescuentoPorCompra() : facturaDB.getDescuentoPorCompra())
                .nombreCliente(!facturaDB.getNombreCliente().equalsIgnoreCase(factura.getNombreCliente()) ? factura.getNombreCliente().toUpperCase() : facturaDB.getNombreCliente().toUpperCase())
                .otroMedioPago(!facturaDB.getOtroMedioPago().equals(factura.getOtroMedioPago()) ? factura.getOtroMedioPago() : facturaDB.getOtroMedioPago())
                .valorDescuentoTotal(!facturaDB.getValorDescuentoTotal().equals(factura.getValorDescuentoTotal()) ? factura.getValorDescuentoTotal() : facturaDB.getValorDescuentoTotal())
                .build();
    }

    default Mono<String> getMedioPago(String medioPagoId){
        String medioPago;
        switch (medioPagoId) {
            case "1":
                medioPago = "Efectivo";
                break;
            case "2":
                medioPago = "Otro";
                break;
            case "3":
                medioPago = "Ambos";
                break;
            default:
                return Mono.error(new RuntimeException("Medio de pago no permitido"));

        }
        return Mono.just(medioPago);
    }
}
