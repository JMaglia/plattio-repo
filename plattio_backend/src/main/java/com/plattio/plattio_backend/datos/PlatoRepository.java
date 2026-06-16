package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {

    List<Plato> findByCategoriaIgnoreCase(String categoria);

    List<Plato> findByNombreContainingIgnoreCase(String nombre);

    List<Plato> findByPrecioLessThan(BigDecimal precio);

    List<Plato> findByTiempoEstimadoLessThanEqual(Integer minutos);

    List<Plato> findByActivoEnCartaTrue();
}
