package com.plattio.plattio_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CrearMesaRequest(@NotNull @Positive Integer numero, @NotBlank String qrToken) {}
