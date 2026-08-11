package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.EmpleadoDAO;
import com.plattio.plattio_backend.datos.MesaDAO;
import com.plattio.plattio_backend.datos.PedidoDAO;
import com.plattio.plattio_backend.datos.SesionMesaDAO;
import com.plattio.plattio_backend.dto.request.IniciarSesionRequest;
import com.plattio.plattio_backend.dto.request.ReasignarMozoRequest;
import com.plattio.plattio_backend.exceptions.EmpleadoException;
import com.plattio.plattio_backend.exceptions.MesaException;
import com.plattio.plattio_backend.exceptions.SesionMesaException;
import com.plattio.plattio_backend.modelo.Empleado;
import com.plattio.plattio_backend.modelo.Mesa;
import com.plattio.plattio_backend.modelo.SesionMesa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SesionMesaService {

    private static final Logger log = LoggerFactory.getLogger(SesionMesaService.class);

    private final SesionMesaDAO sesionMesaDAO;
    private final MesaDAO mesaDAO;
    private final EmpleadoDAO empleadoDAO;
    private final PedidoDAO pedidoDAO;

    public SesionMesaService(SesionMesaDAO sesionMesaDAO, MesaDAO mesaDAO, EmpleadoDAO empleadoDAO, PedidoDAO pedidoDAO) {
        this.sesionMesaDAO = sesionMesaDAO;
        this.mesaDAO = mesaDAO;
        this.empleadoDAO = empleadoDAO;
        this.pedidoDAO = pedidoDAO;
    }

    @Transactional(readOnly = true)
    public List<SesionMesa> obtenerTodas() {
        List<SesionMesa> sesiones = sesionMesaDAO.obtenerTodas();
        hidratarItemsDePedidos(sesiones);
        return sesiones;
    }

    @Transactional(readOnly = true)
    public List<SesionMesa> obtenerSesionesActivas() {
        List<SesionMesa> sesiones = sesionMesaDAO.obtenerActivas();
        hidratarItemsDePedidos(sesiones);
        return sesiones;
    }

    @Transactional(readOnly = true)
    public SesionMesa buscarPorId(Long id) {
        SesionMesa sesion = sesionMesaDAO.buscarPorId(id)
                .orElseThrow(() -> new SesionMesaException("Sesión de mesa no encontrada con ID: " + id, HttpStatus.NOT_FOUND));
        hidratarItemsDePedidos(List.of(sesion));
        return sesion;
    }

    @Transactional(readOnly = true)
    public SesionMesa obtenerSesionActivaPorMesa(Long mesaId) {
        SesionMesa sesion = sesionMesaDAO.obtenerSesionActivaPorMesa(mesaId)
                .orElseThrow(() -> new SesionMesaException("No hay sesión activa para la mesa con ID: " + mesaId, HttpStatus.NOT_FOUND));
        hidratarItemsDePedidos(List.of(sesion));
        return sesion;
    }

    @Transactional(readOnly = true)
    public SesionMesa obtenerSesionActivaPorNumeroMesa(Integer numeroMesa) {
        SesionMesa sesion = sesionMesaDAO.obtenerSesionActivaPorMesaNum(numeroMesa)
                .orElseThrow(() -> new SesionMesaException("No hay sesión activa para la mesa número: " + numeroMesa, HttpStatus.NOT_FOUND));
        hidratarItemsDePedidos(List.of(sesion));
        return sesion;
    }

    @Transactional(readOnly = true)
    public List<SesionMesa> obtenerSesionesActivasPorMozo(Long mozoId) {
        empleadoDAO.buscarPorId(mozoId)
                .orElseThrow(() -> new EmpleadoException("Mozo no encontrado con ID: " + mozoId, HttpStatus.NOT_FOUND));
        List<SesionMesa> sesiones = sesionMesaDAO.obtenerSesionesActivasPorMozo(mozoId);
        hidratarItemsDePedidos(sesiones);
        return sesiones;
    }

    // Los Pedido de esta query ya están en la persistence context (cargados junto con las sesiones),
    // así que Hibernate fusiona sus items en esas mismas entidades sin duplicarlas.
    private void hidratarItemsDePedidos(List<SesionMesa> sesiones) {
        List<Long> sesionIds = sesiones.stream().map(SesionMesa::getId).toList();
        if (sesionIds.isEmpty()) {
            return;
        }
        pedidoDAO.obtenerPorSesionIdsConItems(sesionIds);
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
        log.info("Sesión {} iniciada en mesa {} con mozo {}", sesion.getId(), mesa.getNumero(),
                mozo != null ? mozo.getId() : "sin asignar");
    }

    public void finalizarSesion(Long sesionId) {
        SesionMesa sesion = buscarPorId(sesionId);
        if (!sesion.esActiva()) {
            throw new SesionMesaException("La sesión ya fue finalizada.", HttpStatus.CONFLICT);
        }
        sesion.finalizar();
        sesionMesaDAO.guardar(sesion);
        log.info("Sesión {} finalizada", sesionId);
    }

    public void cerrarSesionSiNoHayPedidos(Long sesionId) {
        SesionMesa sesion = buscarPorId(sesionId);
        sesion.cerrarSiNoHayPedidos();
        sesionMesaDAO.guardar(sesion);
        log.info("Sesión {} cerrada por falta de pedidos", sesionId);
    }

    public void reasignarMozo(Long sesionId, ReasignarMozoRequest request) {
        SesionMesa sesion = buscarPorId(sesionId);
        Empleado mozo = empleadoDAO.buscarPorId(request.mozoId())
                .orElseThrow(() -> new EmpleadoException("Mozo no encontrado con ID: " + request.mozoId(), HttpStatus.NOT_FOUND));
        sesion.asignarMozo(mozo);
        sesionMesaDAO.guardar(sesion);
    }
}
