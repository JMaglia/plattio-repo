package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {

    // Buscar platos por categoría
    List<Plato> findByCategoriaIgnoreCase(String categoria);

    // Buscar platos por nombre (parcial, contiene)
    List<Plato> findByNombreContainingIgnoreCase(String nombre);

    // Buscar platos por precio menor a
    List<Plato> findByPrecioLessThan(BigDecimal precio);

    // Buscar platos por tiempo estimado (rápidos)
    List<Plato> findByTiempoEstimadoLessThanEqual(Integer minutos);

    List<Plato> findByActivoEnCartaTrue();
}