package com.plattio.plattio_backend.dto.request;

import com.plattio.plattio_backend.modelo.Rol;

public record RegistrarEmpleadoRequest(String nombre, String email, String password, Rol rol) {}
