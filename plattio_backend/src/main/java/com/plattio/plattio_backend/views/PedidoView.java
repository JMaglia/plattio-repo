package com.plattio.plattio_backend.views;

import java.util.List;

public record PedidoView(
        Long id,
        String fechaInicio,
        String fechaFin,
        String fechaPreparacion,
        String estado,
        String categoria,
        Integer numMesa,
        List<ItemPedidoView> items
) {}
