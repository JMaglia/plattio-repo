package com.plattio.plattio_backend.modelo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Rol {
    MOZO, COCINERO, ADMIN;

    @JsonValue
    public String toLowercase() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static Rol fromString(String valor) {
        return valueOf(valor.toUpperCase());
    }
}
