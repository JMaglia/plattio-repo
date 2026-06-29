package com.plattio.plattio_backend.exceptions;

import org.springframework.http.HttpStatus;

public class PlatoException extends PlattioException {
    public PlatoException(String mensaje, HttpStatus status) {
        super(mensaje, status);
    }
}
