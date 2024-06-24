package co.com.adminshz.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {
    SERVICE_NOT_FOUND("No se encontro el servicio"),
    ERROR_RULES("Error en las reglas de serviceName y catalogId!"),
    ERROR_HEADERS("Error en los headers!"),
    ERROR_DATA_REQUIRED("Internal Server Error"),
    STATUS_CODE("500"),
    MESSAGE_ERROR("No found results were obtained after consulting the database");

    private final String id;
}