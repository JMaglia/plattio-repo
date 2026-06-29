package com.plattio.plattio_backend.exceptions;

import org.springframework.http.HttpStatus;

public class ItemPedidoException extends PlattioException {
    public ItemPedidoException(String mensaje, HttpStatus status) {
        super(mensaje, status);
    }
}
