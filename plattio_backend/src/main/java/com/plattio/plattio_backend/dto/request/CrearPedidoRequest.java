package com.plattio.plattio_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearPedidoRequest(@NotNull Long sesionId, @NotBlank String categoria) {}
