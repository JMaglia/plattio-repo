package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.PlatoDAO;
import com.plattio.plattio_backend.exceptions.PlatoException;
import com.plattio.plattio_backend.modelo.Plato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PlatoService {

    @Autowired
    private PlatoDAO platoDAO;

    // Obtener todos los platos
    public List<Plato> obtenerTodos() throws PlatoException {
        List<Plato> platos = platoDAO.obtenerTodos();
        if (platos.isEmpty()) {
            throw new PlatoException("No hay platos cargados en el sistema.", HttpStatus.NOT_FOUND);
        }
        return platos;
    }

    public List<Plato> obtenerTodosLosActivos() throws PlatoException {
        List<Plato> activos = platoDAO.obtenerTodosLosActivos();
        if (activos.isEmpty()) {
            throw new PlatoException("No hay platos activos en la carta", HttpStatus.NOT_FOUND);
        }
        return activos;
    }


    // Buscar plato por ID
    public Plato buscarPorId(Long id) throws PlatoException {
        return platoDAO.buscarPorId(id)
                .orElseThrow(() -> new PlatoException("Plato no encontrado con ID: " + id, HttpStatus.NOT_FOUND));
    }

    // Buscar por categoría
    public List<Plato> buscarPorCategoria(String categoria) throws PlatoException {
        List<Plato> encontrados = platoDAO.buscarPorCategoria(categoria);
        if (encontrados.isEmpty()) {
            throw new PlatoException("No se encontraron platos en la categoría: " + categoria, HttpStatus.NOT_FOUND);
        }
        return encontrados;
    }

    // Buscar por nombre parcial
    public List<Plato> buscarPorNombre(String nombreParcial) throws PlatoException {
        List<Plato> encontrados = platoDAO.buscarPorNombre(nombreParcial);
        if (encontrados.isEmpty()) {
            throw new PlatoException("No se encontraron platos que coincidan con: " + nombreParcial, HttpStatus.NOT_FOUND);
        }
        return encontrados;
    }

    // Buscar por precio menor a un valor
    public List<Plato> buscarPorPrecioMenorA(BigDecimal max) throws PlatoException {
        List<Plato> encontrados = platoDAO.buscarPorPrecioMenorA(max);
        if (encontrados.isEmpty()) {
            throw new PlatoException("No se encontraron platos con precio menor a: $" + max, HttpStatus.NOT_FOUND);
        }
        return encontrados;
    }

    // Buscar platos rápidos (por tiempo estimado)
    public List<Plato> buscarPlatosRapidos(int minutosMaximos) throws PlatoException {
        List<Plato> encontrados = platoDAO.buscarPlatosRapidos(minutosMaximos);
        if (encontrados.isEmpty()) {
            throw new PlatoException("No se encontraron platos que puedan prepararse en menos de " + minutosMaximos + " minutos.", HttpStatus.NOT_FOUND);
        }
        return encontrados;
    }

    // Crear nuevo plato
    public void crearPlato(Plato plato) throws PlatoException {
        if (!plato.esPrecioValido()) {
            throw new PlatoException("El precio del plato debe ser mayor a cero.", HttpStatus.BAD_REQUEST);
        }
        platoDAO.guardar(plato);
    }

    public void toggleActivoEnCarta(Long id) throws PlatoException {
        Plato plato = platoDAO.buscarPorId(id)
                .orElseThrow(() -> new PlatoException("Plato no encontrado con ID: " + id, HttpStatus.NOT_FOUND));

        plato.setActivoEnCarta(!plato.isActivoEnCarta());
        platoDAO.guardar(plato);
    }

    // Actualizar plato completo
    public void actualizarPlato(Long id, String nombre, String descripcion, BigDecimal precio, String categoria, Integer tiempoEstimado)
            throws PlatoException {
        Plato plato = buscarPorId(id);
        if (!plato.esPrecioValido()) {
            throw new PlatoException("El precio del plato debe ser mayor a cero.", HttpStatus.BAD_GATEWAY);
        }
        plato.actualizarDatos(nombre, descripcion, precio, categoria, tiempoEstimado);
        platoDAO.guardar(plato);
    }

    // Cambiar solo el precio
    public void cambiarPrecio(Long id, BigDecimal nuevoPrecio) throws PlatoException {
        if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlatoException("El precio debe ser mayor a cero.", HttpStatus.BAD_REQUEST);
        }
        Plato plato = buscarPorId(id);
        plato.cambiarPrecio(nuevoPrecio);
        platoDAO.guardar(plato);
    }

    // Eliminar
    public void eliminar(Long id) throws PlatoException {
        if (platoDAO.buscarPorId(id).isEmpty()) {
            throw new PlatoException("No se puede eliminar: no existe un plato con ID " + id, HttpStatus.NOT_FOUND);
        }
        platoDAO.eliminar(id);
    }
}