package com.plattio.plattio_backend.views;

public record NotificacionView(
        Long id,
        String mensaje,
        String estado,
        String tipo,
        Long mozoId,
        Long sesionId,
        Integer numeroMesa,
        Long pedidoId
) {}
