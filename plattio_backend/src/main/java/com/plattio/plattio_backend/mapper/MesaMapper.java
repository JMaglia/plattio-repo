package com.plattio.plattio_backend.mapper;

import com.plattio.plattio_backend.modelo.Mesa;
import com.plattio.plattio_backend.views.MesaView;

public class MesaMapper {

    private MesaMapper() {}

    public static MesaView toView(Mesa mesa) {
        return new MesaView(
                mesa.getId(),
                mesa.getNumero(),
                mesa.getEstado(),
                mesa.getQrToken()
        );
    }
}
