package com.plattio.plattio_backend.mapper;

import com.plattio.plattio_backend.modelo.SesionMesa;
import com.plattio.plattio_backend.views.SesionMesaView;

import java.time.format.DateTimeFormatter;

public class SesionMesaMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private SesionMesaMapper() {}

    public static SesionMesaView toView(SesionMesa sesion) {
        return new SesionMesaView(
                sesion.getId(),
                sesion.getMesa().getId(),
                sesion.getMesa().getNumero(),
                sesion.getTipoComensal(),
                sesion.getFechaInicio().format(FORMATTER),
                sesion.getFechaFin() != null ? sesion.getFechaFin().format(FORMATTER) : null,
                sesion.getMozo() != null ? EmpleadoMapper.toView(sesion.getMozo()) : null,
                sesion.getPedidos().stream().map(PedidoMapper::toView).toList()
        );
    }
}
