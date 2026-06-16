package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Plato;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class PlatoDAO {

    private final PlatoRepository platoRepository;

    public PlatoDAO(PlatoRepository platoRepository) {
        this.platoRepository = platoRepository;
    }

    public Plato guardar(Plato plato) {
        return platoRepository.save(plato);
    }

    public void eliminar(Long id) {
        platoRepository.deleteById(id);
    }

    public Optional<Plato> buscarPorId(Long id) {
        return platoRepository.findById(id);
    }

    public List<Plato> obtenerTodos() {
        return platoRepository.findAll();
    }

    public List<Plato> buscarPorCategoria(String categoria) {
        return platoRepository.findByCategoriaIgnoreCase(categoria);
    }

    public List<Plato> buscarPorNombre(String nombre) {
        return platoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Plato> buscarPorPrecioMenorA(BigDecimal precioMaximo) {
        return platoRepository.findByPrecioLessThan(precioMaximo);
    }

    public List<Plato> buscarPlatosRapidos(Integer minutosMaximos) {
        return platoRepository.findByTiempoEstimadoLessThanEqual(minutosMaximos);
    }

    public List<Plato> obtenerTodosLosActivos() {
        return platoRepository.findByActivoEnCartaTrue();
    }
}
