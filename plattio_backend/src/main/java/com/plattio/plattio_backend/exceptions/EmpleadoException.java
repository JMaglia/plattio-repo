package com.plattio.plattio_backend.exceptions;

import org.springframework.http.HttpStatus;

public class EmpleadoException extends PlattioException {
    public EmpleadoException(String mensaje, HttpStatus status) {
        super(mensaje, status);
    }
}
