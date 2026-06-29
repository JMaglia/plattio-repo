package com.plattio.plattio_backend.dto.request;

import com.plattio.plattio_backend.modelo.Rol;
import jakarta.validation.constraints.NotNull;

public record CambiarRolRequest(@NotNull Rol rol) {}
