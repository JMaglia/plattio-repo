package com.plattio.plattio_backend.mapper;

import com.plattio.plattio_backend.modelo.Empleado;
import com.plattio.plattio_backend.views.EmpleadoView;

public class EmpleadoMapper {

    private EmpleadoMapper() {}

    public static EmpleadoView toView(Empleado empleado) {
        return new EmpleadoView(
                empleado.getId(),
                empleado.getNombre(),
                empleado.getEmail(),
                empleado.getRol()
        );
    }
}
