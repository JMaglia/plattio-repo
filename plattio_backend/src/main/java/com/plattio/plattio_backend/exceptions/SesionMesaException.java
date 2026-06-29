package com.plattio.plattio_backend.exceptions;

import org.springframework.http.HttpStatus;

public class SesionMesaException extends PlattioException {
    public SesionMesaException(String mensaje, HttpStatus status) {
        super(mensaje, status);
    }
}
