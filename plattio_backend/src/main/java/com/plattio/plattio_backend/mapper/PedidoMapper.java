package com.plattio.plattio_backend.mapper;

import com.plattio.plattio_backend.modelo.Pedido;
import com.plattio.plattio_backend.views.PedidoView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PedidoMapper {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private PedidoMapper() {}

    public static PedidoView toView(Pedido pedido) {
        return new PedidoView(
                pedido.getId(),
                formatear(pedido.getFechaInicio()),
                formatear(pedido.getFechaFin()),
                formatear(pedido.getFechaPreparacion()),
                pedido.getEstado(),
                pedido.getCategoria(),
                pedido.getSesion().getMesa().getNumero(),
                pedido.getItems().stream().map(ItemPedidoMapper::toView).toList()
        );
    }

    private static String formatear(LocalDateTime fecha) {
        return fecha != null ? fecha.format(FORMATTER) : null;
    }
}
