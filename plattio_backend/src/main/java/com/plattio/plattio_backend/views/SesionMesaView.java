package com.plattio.plattio_backend.views;

import java.util.List;

public record SesionMesaView(
        Long id,
        Long mesaId,
        Integer numeroMesa,
        String tipoComensal,
        String fechaInicio,
        String fechaFin,
        EmpleadoView mozo,
        List<PedidoView> pedidos
) {}
