package com.plattio.plattio_backend.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReasignarMozoRequest(@NotNull Long mozoId) {}
