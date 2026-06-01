package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByMozoIdAndEstado(Long mozoId, String estado);

    List<Notificacion> findByMozo_IdAndEstadoOrderByIdDesc(Long mozoId, String estado);

    List<Notificacion> findByPedido_IdAndEstado(Long pedidoId, String estado);

    Optional<Notificacion> findFirstByPedido_IdAndEstado(Long pedidoId, String estado);


}
