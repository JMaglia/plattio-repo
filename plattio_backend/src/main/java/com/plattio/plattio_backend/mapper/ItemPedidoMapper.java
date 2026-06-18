package com.plattio.plattio_backend.mapper;

import com.plattio.plattio_backend.modelo.ItemPedido;
import com.plattio.plattio_backend.views.ItemPedidoView;

public class ItemPedidoMapper {

    private ItemPedidoMapper() {}

    public static ItemPedidoView toView(ItemPedido item) {
        return new ItemPedidoView(
                item.getId(),
                item.getPlato().getNombre(),
                item.getPlato().getDescripcion(),
                item.getPlato().getTiempoEstimado(),
                item.getNota(),
                "entregado".equalsIgnoreCase(item.getEstado()),
                item.getEstado(),
                item.getCantidad(),
                PlatoMapper.toView(item.getPlato())
        );
    }
}
