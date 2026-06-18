package com.plattio.plattio_backend.dto.request;

public record CrearNotificacionRequest(String mensaje, String tipo, Long mozoId, Long sesionId) {}
