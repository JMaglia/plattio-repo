package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findBySesionId(Long sesionId);

    List<Pedido> findByEstado(String estado);

    List<Pedido> findByFechaFinIsNull();

    List<Pedido> findBySesionIdAndEstado(Long sesionId, String estado);

    List<Pedido> findByEstadoIn(List<String> estados);

    List<Pedido> findByEstadoInAndSesion_Mozo_Id(List<String> estados, Long mozoId);

    List<Pedido> findByEstadoAndSesion_Mozo_Id(String estado, Long mozoId);
}
