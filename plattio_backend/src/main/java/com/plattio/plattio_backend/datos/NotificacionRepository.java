package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByMozoIdAndEstado(Long mozoId, String estado);

    List<Notificacion> findByMozo_IdAndEstadoOrderByIdDesc(Long mozoId, String estado);

    List<Notificacion> findByPedido_IdAndEstado(Long pedidoId, String estado);

    Optional<Notificacion> findFirstByPedido_IdAndEstado(Long pedidoId, String estado);

    @Query("SELECT n FROM Notificacion n LEFT JOIN FETCH n.sesion s LEFT JOIN FETCH s.mesa")
    List<Notificacion> findAllConSesionYMesa();

    @Query("SELECT n FROM Notificacion n LEFT JOIN FETCH n.sesion s LEFT JOIN FETCH s.mesa " +
           "WHERE n.mozo.id = :mozoId AND n.estado = :estado")
    List<Notificacion> findByMozoIdAndEstadoConSesionYMesa(@Param("mozoId") Long mozoId, @Param("estado") String estado);

    @Query("SELECT n FROM Notificacion n LEFT JOIN FETCH n.sesion s LEFT JOIN FETCH s.mesa " +
           "WHERE n.mozo.id = :mozoId AND n.estado = :estado ORDER BY n.id DESC")
    List<Notificacion> findByMozoIdAndEstadoConSesionYMesaOrderByIdDesc(@Param("mozoId") Long mozoId, @Param("estado") String estado);
}
