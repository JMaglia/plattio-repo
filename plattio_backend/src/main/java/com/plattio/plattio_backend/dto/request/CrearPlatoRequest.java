package com.plattio.plattio_backend.dto.request;

import java.math.BigDecimal;

public record CrearPlatoRequest(
        String nombre,
        String descripcion,
        BigDecimal precio,
        String categoria,
        Integer tiempoEstimado
) {}
