package com.plattio.plattio_backend.exceptions;

import org.springframework.http.HttpStatus;

public class MesaException extends PlattioException {
    public MesaException(String mensaje, HttpStatus status) {
        super(mensaje, status);
    }
}
