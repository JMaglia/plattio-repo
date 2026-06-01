package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.EmpleadoDAO;
import com.plattio.plattio_backend.datos.MesaDAO;
import com.plattio.plattio_backend.datos.SesionMesaDAO;
import com.plattio.plattio_backend.exceptions.MesaException;
import com.plattio.plattio_backend.exceptions.SesionMesaException;
import com.plattio.plattio_backend.modelo.Empleado;
import com.plattio.plattio_backend.modelo.Mesa;
import com.plattio.plattio_backend.modelo.SesionMesa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SesionMesaService {

    @Autowired
    private SesionMesaDAO sesionMesaDAO;

    @Autowired
    private MesaDAO mesaDAO;

    @Autowired
    private EmpleadoDAO empleadoDAO;

    // Obtener todas
    public List<SesionMesa> obtenerTodas() throws SesionMesaException {
        List<SesionMesa> sesiones = sesionMesaDAO.obtenerTodas();
        if (sesiones.isEmpty()) {
            throw new SesionMesaException("No se encontraron sesiones de mesa.", HttpStatus.NOT_FOUND);
        }
        return sesiones;
    }

    public List<SesionMesa> obtenerSesionesActivas() throws SesionMesaException {
        List<SesionMesa> sesiones = sesionMesaDAO.obtenerActivas();
        if (sesiones.isEmpty()) {
            throw new SesionMesaException("No hay sesiones activas.", HttpStatus.NOT_FOUND);
        }
        return sesiones;
    }

    // Buscar sesión por ID
    public SesionMesa buscarPorId(Long id) throws SesionMesaException {
        return sesionMesaDAO.buscarPorId(id)
                .orElseThrow(() -> new SesionMesaException("Sesión de mesa no encontrada con ID: " + id, HttpStatus.NOT_FOUND));
    }

    // Crear sesión nueva
    public void iniciarSesion(Long mesaId, Long mozoId, String tipoComensal) throws SesionMesaException, MesaException {
        Mesa mesa = mesaDAO.buscarPorId(mesaId)
                .orElseThrow(() -> new MesaException("Mesa no encontrada", HttpStatus.NOT_FOUND));

        if (mesa.tieneSesionActiva()) {
            throw new SesionMesaException("La mesa ya tiene una sesión activa.", HttpStatus.NOT_FOUND);
        }

        Empleado mozo = null;
        if (mozoId != null) {
            mozo = empleadoDAO.buscarPorId(mozoId)
                    .orElseThrow(() -> new SesionMesaException("Mozo no encontrado", HttpStatus.NOT_FOUND));
        }

        SesionMesa sesion = new SesionMesa(mesa, mozo, tipoComensal);
        mesa.ocupar();
        mesa.agregarSesion(sesion);
        sesionMesaDAO.guardar(sesion);
        mesaDAO.guardar(mesa);
    }

    // Finalizar sesión
    public void finalizarSesion(Long sesionId) throws SesionMesaException {
        SesionMesa sesion = buscarPorId(sesionId);
        if (!sesion.esActiva()) {
            throw new SesionMesaException("La sesión ya fue finalizada.", HttpStatus.NOT_FOUND);
        }

        sesion.finalizar();
        sesionMesaDAO.guardar(sesion);
    }

    // Obtener sesión activa por mesa
    public SesionMesa obtenerSesionActivaPorMesa(Long mesaId) throws SesionMesaException {
        return sesionMesaDAO.obtenerSesionActivaPorMesa(mesaId)
                .orElseThrow(() -> new SesionMesaException("No hay sesión activa para la mesa con ID: " + mesaId, HttpStatus.NOT_FOUND));
    }

    // Obtener sesiones activas por mozo
    public List<SesionMesa> obtenerSesionesActivasPorMozo(Long mozoId) throws SesionMesaException {
        empleadoDAO.buscarPorId(mozoId)
                .orElseThrow(() -> new SesionMesaException("Mozo no encontrado con ID: " + mozoId, HttpStatus.NOT_FOUND));

        List<SesionMesa> sesiones = sesionMesaDAO.obtenerSesionesActivasPorMozo(mozoId);
        if (sesiones.isEmpty()) {
            throw new SesionMesaException("No se encontraron sesiones activas para el mozo con ID: " + mozoId, HttpStatus.NOT_FOUND);
        }
        return sesiones;
    }

    // Cerrar sesión si no tiene pedidos
    public void cerrarSesionSiNoHayPedidos(Long sesionId) throws SesionMesaException {
        SesionMesa sesion = buscarPorId(sesionId);
        sesion.cerrarSiNoHayPedidos();
        sesionMesaDAO.guardar(sesion);
    }

    // Asignar o cambiar mozo
    public void reasignarMozo(Long sesionId, Long nuevoMozoId) throws SesionMesaException {
        SesionMesa sesion = buscarPorId(sesionId);
        Empleado mozo = empleadoDAO.buscarPorId(nuevoMozoId)
                .orElseThrow(() -> new SesionMesaException("Mozo no encontrado.", HttpStatus.NOT_FOUND));
        sesion.asignarMozo(mozo);
        sesionMesaDAO.guardar(sesion);
    }
}

