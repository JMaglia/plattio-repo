package com.plattio.plattio_backend.exceptions;

import org.springframework.http.HttpStatus;

public abstract class PlattioException extends RuntimeException {

    private final HttpStatus status;

    public PlattioException(String mensaje, HttpStatus status) {
        super(mensaje);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
