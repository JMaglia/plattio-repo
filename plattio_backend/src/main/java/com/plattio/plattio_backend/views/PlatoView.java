package com.plattio.plattio_backend.views;

import java.math.BigDecimal;

public record PlatoView(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        String categoria,
        Integer tiempoEstimado,
        boolean activoEnCarta
) {}
