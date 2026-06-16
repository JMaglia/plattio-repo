package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.PlatoDAO;
import com.plattio.plattio_backend.dto.request.ActualizarPlatoRequest;
import com.plattio.plattio_backend.dto.request.CrearPlatoRequest;
import com.plattio.plattio_backend.exceptions.PlatoException;
import com.plattio.plattio_backend.modelo.Plato;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PlatoService {

    private final PlatoDAO platoDAO;

    public PlatoService(PlatoDAO platoDAO) {
        this.platoDAO = platoDAO;
    }

    public List<Plato> obtenerTodos() {
        return platoDAO.obtenerTodos();
    }

    public List<Plato> obtenerTodosLosActivos() {
        return platoDAO.obtenerTodosLosActivos();
    }

    public Plato buscarPorId(Long id) {
        return platoDAO.buscarPorId(id)
                .orElseThrow(() -> new PlatoException("Plato no encontrado con ID: " + id, HttpStatus.NOT_FOUND));
    }

    public List<Plato> buscarPorCategoria(String categoria) {
        return platoDAO.buscarPorCategoria(categoria);
    }

    public List<Plato> buscarPorNombre(String nombreParcial) {
        return platoDAO.buscarPorNombre(nombreParcial);
    }

    public List<Plato> buscarPorPrecioMenorA(BigDecimal max) {
        return platoDAO.buscarPorPrecioMenorA(max);
    }

    public List<Plato> buscarPlatosRapidos(int minutosMaximos) {
        return platoDAO.buscarPlatosRapidos(minutosMaximos);
    }

    public void crearPlato(CrearPlatoRequest request) {
        if (request.precio() == null || request.precio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlatoException("El precio del plato debe ser mayor a cero.", HttpStatus.BAD_REQUEST);
        }
        platoDAO.guardar(new Plato(
                request.nombre(),
                request.descripcion(),
                request.precio(),
                request.categoria(),
                request.tiempoEstimado()
        ));
    }

    public void actualizarPlato(Long id, ActualizarPlatoRequest request) {
        if (request.precio() == null || request.precio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlatoException("El precio del plato debe ser mayor a cero.", HttpStatus.BAD_REQUEST);
        }
        Plato plato = buscarPorId(id);
        plato.actualizarDatos(request.nombre(), request.descripcion(), request.precio(), request.categoria(), request.tiempoEstimado());
        platoDAO.guardar(plato);
    }

    public void cambiarPrecio(Long id, BigDecimal nuevoPrecio) {
        if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlatoException("El precio debe ser mayor a cero.", HttpStatus.BAD_REQUEST);
        }
        Plato plato = buscarPorId(id);
        plato.cambiarPrecio(nuevoPrecio);
        platoDAO.guardar(plato);
    }

    public void toggleActivoEnCarta(Long id) {
        Plato plato = buscarPorId(id);
        plato.setActivoEnCarta(!plato.isActivoEnCarta());
        platoDAO.guardar(plato);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        platoDAO.eliminar(id);
    }
}
