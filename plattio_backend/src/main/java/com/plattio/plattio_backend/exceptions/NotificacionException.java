package com.plattio.plattio_backend.exceptions;

import org.springframework.http.HttpStatus;

public class NotificacionException extends PlattioException {
    public NotificacionException(String mensaje, HttpStatus status) {
        super(mensaje, status);
    }
}
