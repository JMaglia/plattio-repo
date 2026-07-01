package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.EmpleadoDAO;
import com.plattio.plattio_backend.datos.MesaDAO;
import com.plattio.plattio_backend.datos.SesionMesaDAO;
import com.plattio.plattio_backend.dto.request.IniciarSesionRequest;
import com.plattio.plattio_backend.dto.request.ReasignarMozoRequest;
import com.plattio.plattio_backend.exceptions.EmpleadoException;
import com.plattio.plattio_backend.exceptions.MesaException;
import com.plattio.plattio_backend.exceptions.SesionMesaException;
import com.plattio.plattio_backend.modelo.Empleado;
import com.plattio.plattio_backend.modelo.Mesa;
import com.plattio.plattio_backend.modelo.SesionMesa;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SesionMesaService {

    private final SesionMesaDAO sesionMesaDAO;
    private final MesaDAO mesaDAO;
    private final EmpleadoDAO empleadoDAO;

    public SesionMesaService(SesionMesaDAO sesionMesaDAO, MesaDAO mesaDAO, EmpleadoDAO empleadoDAO) {
        this.sesionMesaDAO = sesionMesaDAO;
        this.mesaDAO = mesaDAO;
        this.empleadoDAO = empleadoDAO;
    }

    public List<SesionMesa> obtenerTodas() {
        return sesionMesaDAO.obtenerTodas();
    }

    public List<SesionMesa> obtenerSesionesActivas() {
        return sesionMesaDAO.obtenerActivas();
    }

    public SesionMesa buscarPorId(Long id) {
        return sesionMesaDAO.buscarPorId(id)
                .orElseThrow(() -> new SesionMesaException("Sesión de mesa no encontrada con ID: " + id, HttpStatus.NOT_FOUND));
    }

    public SesionMesa obtenerSesionActivaPorMesa(Long mesaId) {
        return sesionMesaDAO.obtenerSesionActivaPorMesa(mesaId)
                .orElseThrow(() -> new SesionMesaException("No hay sesión activa para la mesa con ID: " + mesaId, HttpStatus.NOT_FOUND));
    }

    public SesionMesa obtenerSesionActivaPorNumeroMesa(Integer numeroMesa) {
        return sesionMesaDAO.obtenerSesionActivaPorMesaNum(numeroMesa)
                .orElseThrow(() -> new SesionMesaException("No hay sesión activa para la mesa número: " + numeroMesa, HttpStatus.NOT_FOUND));
    }

    public List<SesionMesa> obtenerSesionesActivasPorMozo(Long mozoId) {
        empleadoDAO.buscarPorId(mozoId)
                .orElseThrow(() -> new EmpleadoException("Mozo no encontrado con ID: " + mozoId, HttpStatus.NOT_FOUND));
        return sesionMesaDAO.obtenerSesionesActivasPorMozo(mozoId);
    }

    @Transactional
    public void iniciarSesion(IniciarSesionRequest request) {
        Mesa mesa = mesaDAO.buscarPorId(request.mesaId())
                .orElseThrow(() -> new MesaException("Mesa no encontrada con ID: " + request.mesaId(), HttpStatus.NOT_FOUND));

        if (mesa.tieneSesionActiva()) {
            throw new SesionMesaException("La mesa ya tiene una sesión activa.", HttpStatus.CONFLICT);
        }

        Empleado mozo = null;
        if (request.mozoId() != null) {
            mozo = empleadoDAO.buscarPorId(request.mozoId())
                    .orElseThrow(() -> new EmpleadoException("Mozo no encontrado con ID: " + request.mozoId(), HttpStatus.NOT_FOUND));
        }

        SesionMesa sesion = new SesionMesa(mesa, mozo, request.tipoComensal());
        mesa.ocupar();
        mesa.agregarSesion(sesion);
        sesionMesaDAO.guardar(sesion);
        mesaDAO.guardar(mesa);
    }

    public void finalizarSesion(Long sesionId) {
        SesionMesa sesion = buscarPorId(sesionId);
        if (!sesion.esActiva()) {
            throw new SesionMesaException("La sesión ya fue finalizada.", HttpStatus.CONFLICT);
        }
        sesion.finalizar();
        sesionMesaDAO.guardar(sesion);
    }

    public void cerrarSesionSiNoHayPedidos(Long sesionId) {
        SesionMesa sesion = buscarPorId(sesionId);
        sesion.cerrarSiNoHayPedidos();
        sesionMesaDAO.guardar(sesion);
    }

    public void reasignarMozo(Long sesionId, ReasignarMozoRequest request) {
        SesionMesa sesion = buscarPorId(sesionId);
        Empleado mozo = empleadoDAO.buscarPorId(request.mozoId())
                .orElseThrow(() -> new EmpleadoException("Mozo no encontrado con ID: " + request.mozoId(), HttpStatus.NOT_FOUND));
        sesion.asignarMozo(mozo);
        sesionMesaDAO.guardar(sesion);
    }
}
