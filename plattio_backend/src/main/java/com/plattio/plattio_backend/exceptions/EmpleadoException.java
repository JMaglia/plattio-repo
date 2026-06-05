package com.plattio.plattio_backend.exceptions;

import org.springframework.http.HttpStatus;

public class EmpleadoException extends RuntimeException {

    private final HttpStatus status;

    public EmpleadoException(String mensaje, HttpStatus status) {
        super(mensaje);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
