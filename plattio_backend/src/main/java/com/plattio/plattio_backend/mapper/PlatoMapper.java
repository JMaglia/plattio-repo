package com.plattio.plattio_backend.mapper;

import com.plattio.plattio_backend.modelo.Plato;
import com.plattio.plattio_backend.views.PlatoView;

public class PlatoMapper {

    private PlatoMapper() {}

    public static PlatoView toView(Plato plato) {
        return new PlatoView(
                plato.getId(),
                plato.getNombre(),
                plato.getDescripcion(),
                plato.getPrecio(),
                plato.getCategoria(),
                plato.getTiempoEstimado(),
                plato.isActivoEnCarta()
        );
    }
}
