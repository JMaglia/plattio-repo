package com.plattio.plattio_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ActualizarEmpleadoRequest(
        @NotBlank String nombre,
        @NotBlank @Email String email
) {}
