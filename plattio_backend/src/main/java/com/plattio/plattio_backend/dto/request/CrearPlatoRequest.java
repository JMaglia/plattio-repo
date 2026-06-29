package com.plattio.plattio_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CrearPlatoRequest(
        @NotBlank String nombre,
        @NotBlank String descripcion,
        @NotNull @Positive BigDecimal precio,
        @NotBlank String categoria,
        @NotNull @Positive Integer tiempoEstimado
) {}
