package com.plattio.plattio_backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CambiarEstadoMesaRequest(@NotBlank String estado) {}
