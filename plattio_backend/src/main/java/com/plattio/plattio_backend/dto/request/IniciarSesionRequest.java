package com.plattio.plattio_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IniciarSesionRequest(@NotNull Long mesaId, Long mozoId, @NotBlank String tipoComensal) {}
