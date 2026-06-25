package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.SesionMesa;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
