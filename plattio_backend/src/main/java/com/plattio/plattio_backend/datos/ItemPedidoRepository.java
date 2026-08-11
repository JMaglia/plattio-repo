package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.ItemPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    List<ItemPedido> findByPedidoId(Long pedidoId);

    List<ItemPedido> findByEstado(String estado);

    List<ItemPedido> findByFechaFinIsNull();

    List<ItemPedido> findByPedidoIdAndEstado(Long pedidoId, String estado);

    @Query("SELECT i FROM ItemPedido i JOIN FETCH i.plato WHERE i.pedido.id = :pedidoId")
    List<ItemPedido> findByPedidoIdConPlato(@Param("pedidoId") Long pedidoId);

    @Query(value = "SELECT i FROM ItemPedido i JOIN FETCH i.plato WHERE i.estado = :estado",
           countQuery = "SELECT count(i) FROM ItemPedido i WHERE i.estado = :estado")
    Page<ItemPedido> findByEstadoConPlato(@Param("estado") String estado, Pageable pageable);

    @Query("SELECT i FROM ItemPedido i JOIN FETCH i.plato WHERE i.fechaFin IS NULL")
    List<ItemPedido> findByFechaFinIsNullConPlato();
}
