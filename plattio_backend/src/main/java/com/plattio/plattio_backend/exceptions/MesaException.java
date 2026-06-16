package com.plattio.plattio_backend.exceptions;

import org.springframework.http.HttpStatus;

public class MesaException extends RuntimeException {

    private final HttpStatus status;

    public MesaException(String mensaje, HttpStatus status) {
        super(mensaje);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
