package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.SesionMesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SesionMesaRepository extends JpaRepository<SesionMesa, Long> {

    List<SesionMesa> findByFechaFinIsNull();

    Optional<SesionMesa> findByMesaIdAndFechaFinIsNull(Long mesaId);

    Optional<SesionMesa> findByMesaNumeroAndFechaFinIsNull(Integer numeroMesa);

    List<SesionMesa> findByMozoIdAndFechaFinIsNull(Long mozoId);

    List<SesionMesa> findByMesaIdAndFechaFinIsNotNull(Long mesaId);

    @Query("SELECT DISTINCT s FROM SesionMesa s " +
           "JOIN FETCH s.mesa LEFT JOIN FETCH s.mozo LEFT JOIN FETCH s.pedidos")
    List<SesionMesa> findAllConMesaYMozoYPedidos();

    @Query("SELECT DISTINCT s FROM SesionMesa s " +
           "JOIN FETCH s.mesa LEFT JOIN FETCH s.mozo LEFT JOIN FETCH s.pedidos " +
           "WHERE s.id = :id")
    Optional<SesionMesa> findByIdConMesaYMozoYPedidos(@Param("id") Long id);

    @Query("SELECT DISTINCT s FROM SesionMesa s " +
           "JOIN FETCH s.mesa LEFT JOIN FETCH s.mozo LEFT JOIN FETCH s.pedidos " +
           "WHERE s.fechaFin IS NULL")
    List<SesionMesa> findByFechaFinIsNullConMesaYMozoYPedidos();

    @Query("SELECT DISTINCT s FROM SesionMesa s " +
           "JOIN FETCH s.mesa LEFT JOIN FETCH s.mozo LEFT JOIN FETCH s.pedidos " +
           "WHERE s.mesa.id = :mesaId AND s.fechaFin IS NULL")
    Optional<SesionMesa> findByMesaIdAndFechaFinIsNullConMesaYMozoYPedidos(@Param("mesaId") Long mesaId);

    @Query("SELECT DISTINCT s FROM SesionMesa s " +
           "JOIN FETCH s.mesa m LEFT JOIN FETCH s.mozo LEFT JOIN FETCH s.pedidos " +
           "WHERE m.numero = :numeroMesa AND s.fechaFin IS NULL")
    Optional<SesionMesa> findByMesaNumeroAndFechaFinIsNullConMesaYMozoYPedidos(@Param("numeroMesa") Integer numeroMesa);

    @Query("SELECT DISTINCT s FROM SesionMesa s " +
           "JOIN FETCH s.mesa LEFT JOIN FETCH s.mozo LEFT JOIN FETCH s.pedidos " +
           "WHERE s.mozo.id = :mozoId AND s.fechaFin IS NULL")
    List<SesionMesa> findByMozoIdAndFechaFinIsNullConMesaYMozoYPedidos(@Param("mozoId") Long mozoId);
}
