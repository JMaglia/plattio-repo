package com.plattio.plattio_backend.dto.request;

import com.plattio.plattio_backend.modelo.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrarEmpleadoRequest(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotNull Rol rol
) {}
