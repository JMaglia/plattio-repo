package com.plattio.plattio_backend.views;

public record ItemPedidoView(
        Long id,
        String nombre,
        String detalle,
        Integer tiempo,
        String nota,
        boolean finalizado,
        String estado,
        int cantidad,
        PlatoView plato
) {}
