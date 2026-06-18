package com.plattio.plattio_backend.mapper;

import com.plattio.plattio_backend.modelo.Notificacion;
import com.plattio.plattio_backend.views.NotificacionView;

public class NotificacionMapper {

    private NotificacionMapper() {}

    public static NotificacionView toView(Notificacion n) {
        return new NotificacionView(
                n.getId(),
                n.getMensaje(),
                n.getEstado(),
                n.getTipo(),
                n.getMozo() != null ? n.getMozo().getId() : null,
                n.getSesion() != null ? n.getSesion().getId() : null,
                n.getSesion() != null ? n.getSesion().getMesa().getNumero() : null,
                n.getPedido() != null ? n.getPedido().getId() : null
        );
    }
}
