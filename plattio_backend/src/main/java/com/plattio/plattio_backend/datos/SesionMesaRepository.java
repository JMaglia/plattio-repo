package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.SesionMesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SesionMesaRepository extends JpaRepository<SesionMesa, Long> {

    // Sesiones activas (sin fecha de fin)
    List<SesionMesa> findByFechaFinIsNull();

    // Buscar sesiones activas por mesa
    Optional<SesionMesa> findByMesaIdAndFechaFinIsNull(Long mesaId);

    Optional<SesionMesa> findByMesaNumeroAndFechaFinIsNull(Integer numeroMesa);

    // Buscar sesiones activas por mozo
    List<SesionMesa> findByMozoIdAndFechaFinIsNull(Long mozoId);

    // Buscar sesiones finalizadas por mesa
    List<SesionMesa> findByMesaIdAndFechaFinIsNotNull(Long mesaId);

}
