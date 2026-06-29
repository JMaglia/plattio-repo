package com.plattio.plattio_backend.exceptions;

import org.springframework.http.HttpStatus;

public class PedidoException extends PlattioException {
    public PedidoException(String mensaje, HttpStatus status) {
        super(mensaje, status);
    }
}
