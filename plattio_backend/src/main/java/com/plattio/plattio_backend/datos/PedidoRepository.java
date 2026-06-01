package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Buscar todos los pedidos de una sesión específica
    List<Pedido> findBySesionId(Long sesionId);

    // Buscar por estado (ej. "pendiente", "entregado")
    List<Pedido> findByEstado(String estado);

    // Buscar pedidos activos (sin fecha_fin)
    List<Pedido> findByFechaFinIsNull();

    // Opcional: buscar por sesión y estado
    List<Pedido> findBySesionIdAndEstado(Long sesionId, String estado);

    List<Pedido> findByEstadoIn(List<String> estados);

    List<Pedido> findByEstadoInAndSesion_Mozo_Id(List<String> estados, Long mozoId);

    List<Pedido> findByEstadoAndSesion_Mozo_Id(String estado, Long mozoId);

}
