package com.plattio.plattio_backend.views;

import com.plattio.plattio_backend.modelo.Rol;

public record EmpleadoView(Long id, String nombre, String email, Rol rol) {}
