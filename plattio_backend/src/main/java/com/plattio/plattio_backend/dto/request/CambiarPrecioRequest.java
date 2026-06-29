package com.plattio.plattio_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CambiarPrecioRequest(@NotNull @Positive BigDecimal precio) {}
