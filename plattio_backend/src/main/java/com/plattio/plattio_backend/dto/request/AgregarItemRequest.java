package com.plattio.plattio_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AgregarItemRequest(@NotNull Long platoId, @Positive int cantidad, String nota) {}
