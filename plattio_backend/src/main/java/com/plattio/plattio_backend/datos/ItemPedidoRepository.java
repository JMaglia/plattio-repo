package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    // Buscar todos los ítems de un pedido
    List<ItemPedido> findByPedidoId(Long pedidoId);

    // Buscar ítems por estado (ej. "en_preparacion", "listo")
    List<ItemPedido> findByEstado(String estado);

    // Buscar ítems activos (sin fecha de fin)
    List<ItemPedido> findByFechaFinIsNull();

    // Buscar ítems por pedido y estado
    List<ItemPedido> findByPedidoIdAndEstado(Long pedidoId, String estado);
}
