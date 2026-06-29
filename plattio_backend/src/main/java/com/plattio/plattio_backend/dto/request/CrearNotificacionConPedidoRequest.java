package com.plattio.plattio_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CrearNotificacionConPedidoRequest(
        @NotBlank String mensaje,
        @NotBlank String tipo,
        @NotNull @Positive Integer mesaNum,
        @NotNull Long pedidoId
) {}
