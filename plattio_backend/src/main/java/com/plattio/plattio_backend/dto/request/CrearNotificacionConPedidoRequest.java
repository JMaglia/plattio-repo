package com.plattio.plattio_backend.dto.request;

public record CrearNotificacionConPedidoRequest(String mensaje, String tipo, Integer mesaNum, Long pedidoId) {}
