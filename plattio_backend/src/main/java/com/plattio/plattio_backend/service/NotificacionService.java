package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.EmpleadoDAO;
import com.plattio.plattio_backend.datos.NotificacionDAO;
import com.plattio.plattio_backend.datos.PedidoDAO;
import com.plattio.plattio_backend.datos.SesionMesaDAO;
import com.plattio.plattio_backend.dto.request.CrearNotificacionConPedidoRequest;
import com.plattio.plattio_backend.dto.request.CrearNotificacionRequest;
import com.plattio.plattio_backend.exceptions.NotificacionException;
import com.plattio.plattio_backend.modelo.Empleado;
import com.plattio.plattio_backend.modelo.Notificacion;
import com.plattio.plattio_backend.modelo.Pedido;
import com.plattio.plattio_backend.modelo.SesionMesa;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionDAO notificacionDAO;
    private final SesionMesaDAO sesionMesaDAO;
    private final EmpleadoDAO empleadoDAO;
    private final PedidoDAO pedidoDAO;

    public NotificacionService(NotificacionDAO notificacionDAO, SesionMesaDAO sesionMesaDAO,
                               EmpleadoDAO empleadoDAO, PedidoDAO pedidoDAO) {
        this.notificacionDAO = notificacionDAO;
        this.sesionMesaDAO = sesionMesaDAO;
        this.empleadoDAO = empleadoDAO;
        this.pedidoDAO = pedidoDAO;
    }

    public Notificacion obtenerPorId(Long id) {
        return notificacionDAO.buscarPorId(id)
                .orElseThrow(() -> new NotificacionException("Notificación no encontrada con ID: " + id, HttpStatus.NOT_FOUND));
    }

    public List<Notificacion> obtenerTodas() {
        return notificacionDAO.obtenerTodas();
    }

    public List<Notificacion> obtenerPorMozoYEstado(Long mozoId, String estado) {
        if ("completada".equalsIgnoreCase(estado)) {
            return notificacionDAO.buscarPorMozoCompletadas(mozoId, estado);
        }
        return notificacionDAO.buscarPorMozoYEstado(mozoId, estado);
    }

    public void crearNotificacion(CrearNotificacionRequest request) {
        Empleado mozo = empleadoDAO.buscarPorId(request.mozoId())
                .orElseThrow(() -> new NotificacionException("Mozo no encontrado con ID: " + request.mozoId(), HttpStatus.NOT_FOUND));
        SesionMesa sesion = sesionMesaDAO.buscarPorId(request.sesionId())
                .orElseThrow(() -> new NotificacionException("Sesión no encontrada con ID: " + request.sesionId(), HttpStatus.NOT_FOUND));
        notificacionDAO.guardar(new Notificacion(request.mensaje(), request.tipo(), mozo, sesion));
    }

    public void crearNotificacionConPedido(CrearNotificacionConPedidoRequest request) {
        SesionMesa sesion = sesionMesaDAO.obtenerSesionActivaPorMesaNum(request.mesaNum())
                .orElseThrow(() -> new NotificacionException("Sesión activa no encontrada para mesa: " + request.mesaNum(), HttpStatus.NOT_FOUND));
        Pedido pedido = pedidoDAO.buscarPorId(request.pedidoId())
                .orElseThrow(() -> new NotificacionException("Pedido no encontrado con ID: " + request.pedidoId(), HttpStatus.NOT_FOUND));
        Empleado mozo = empleadoDAO.buscarPorSesionId(sesion.getId())
                .orElseThrow(() -> new NotificacionException("Mozo no encontrado para la sesión: " + sesion.getId(), HttpStatus.NOT_FOUND));
        notificacionDAO.guardar(new Notificacion(request.mensaje(), request.tipo(), mozo, sesion, pedido));
    }

    public void marcarComoCompletada(Long id) {
        Notificacion n = obtenerPorId(id);
        n.completar();
        notificacionDAO.guardar(n);
    }

    public void toggleEstado(Long id) {
        Notificacion n = obtenerPorId(id);
        n.alternarEstado();
        notificacionDAO.guardar(n);
    }

    public void completarNotificacionesPorPedido(Long pedidoId) {
        notificacionDAO.buscarPrimeraPorPedidoYEstado(pedidoId, "pendiente").ifPresent(n -> {
            n.completar();
            notificacionDAO.guardar(n);
        });
    }
}
