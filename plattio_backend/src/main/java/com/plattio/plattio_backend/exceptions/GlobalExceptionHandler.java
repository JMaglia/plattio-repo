package com.plattio.plattio_backend.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmpleadoException.class)
    public ResponseEntity<String> handleEmpleadoException(EmpleadoException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(SesionMesaException.class)
    public ResponseEntity<String> handleSesionMesaException(SesionMesaException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(MesaException.class)
    public ResponseEntity<String> handleMesaException(MesaException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(PlatoException.class)
    public ResponseEntity<String> handlePlatoException(PlatoException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(ItemPedidoException.class)
    public ResponseEntity<String> handleItemPedidoException(ItemPedidoException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(PedidoException.class)
    public ResponseEntity<String> handlePedidoException(PedidoException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }
}
