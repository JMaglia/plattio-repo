package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    List<ItemPedido> findByPedidoId(Long pedidoId);

    List<ItemPedido> findByEstado(String estado);

    List<ItemPedido> findByFechaFinIsNull();

    List<ItemPedido> findByPedidoIdAndEstado(Long pedidoId, String estado);
}
