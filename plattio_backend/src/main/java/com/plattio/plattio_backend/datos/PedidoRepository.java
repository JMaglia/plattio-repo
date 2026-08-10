package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findBySesionId(Long sesionId);

    List<Pedido> findByEstado(String estado);

    List<Pedido> findByFechaFinIsNull();

    List<Pedido> findBySesionIdAndEstado(Long sesionId, String estado);

    List<Pedido> findByEstadoIn(List<String> estados);

    List<Pedido> findByEstadoInAndSesion_Mozo_Id(List<String> estados, Long mozoId);

    List<Pedido> findByEstadoAndSesion_Mozo_Id(String estado, Long mozoId);

    @Query("SELECT DISTINCT p FROM Pedido p " +
           "LEFT JOIN FETCH p.sesion s LEFT JOIN FETCH s.mesa " +
           "LEFT JOIN FETCH p.items i LEFT JOIN FETCH i.plato")
    List<Pedido> findAllConDetalle();

    @Query("SELECT DISTINCT p FROM Pedido p " +
           "LEFT JOIN FETCH p.sesion s LEFT JOIN FETCH s.mesa " +
           "LEFT JOIN FETCH p.items i LEFT JOIN FETCH i.plato " +
           "WHERE p.id = :id")
    Optional<Pedido> findByIdConDetalle(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Pedido p " +
           "LEFT JOIN FETCH p.sesion s LEFT JOIN FETCH s.mesa " +
           "LEFT JOIN FETCH p.items i LEFT JOIN FETCH i.plato " +
           "WHERE p.sesion.id = :sesionId")
    List<Pedido> findBySesionIdConDetalle(@Param("sesionId") Long sesionId);

    @Query("SELECT DISTINCT p FROM Pedido p " +
           "LEFT JOIN FETCH p.sesion s LEFT JOIN FETCH s.mesa " +
           "LEFT JOIN FETCH p.items i LEFT JOIN FETCH i.plato " +
           "WHERE p.estado = :estado")
    List<Pedido> findByEstadoConDetalle(@Param("estado") String estado);

    @Query("SELECT DISTINCT p FROM Pedido p " +
           "LEFT JOIN FETCH p.sesion s LEFT JOIN FETCH s.mesa " +
           "LEFT JOIN FETCH p.items i LEFT JOIN FETCH i.plato " +
           "WHERE p.estado IN :estados AND s.mozo.id = :mozoId")
    List<Pedido> findByEstadoInAndSesionMozoIdConDetalle(@Param("estados") List<String> estados, @Param("mozoId") Long mozoId);

    @Query("SELECT DISTINCT p FROM Pedido p " +
           "LEFT JOIN FETCH p.sesion s LEFT JOIN FETCH s.mesa " +
           "LEFT JOIN FETCH p.items i LEFT JOIN FETCH i.plato " +
           "WHERE p.estado = :estado AND s.mozo.id = :mozoId")
    List<Pedido> findByEstadoAndSesionMozoIdConDetalle(@Param("estado") String estado, @Param("mozoId") Long mozoId);

    @Query("SELECT DISTINCT p FROM Pedido p " +
           "LEFT JOIN FETCH p.items i LEFT JOIN FETCH i.plato " +
           "WHERE p.sesion.id IN :sesionIds")
    List<Pedido> findBySesionIdInConItems(@Param("sesionIds") List<Long> sesionIds);
}
