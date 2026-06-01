package com.plattio.plattio_backend.exceptions;

import org.springframework.http.HttpStatus;

public class PlatoException extends Exception {

    private final HttpStatus status;

    public PlatoException(String mensaje, HttpStatus status) {
        super(mensaje);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
