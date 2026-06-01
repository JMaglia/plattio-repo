package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.*;
import com.plattio.plattio_backend.exceptions.EmpleadoException;
import com.plattio.plattio_backend.exceptions.NotificacionException;
import com.plattio.plattio_backend.exceptions.PedidoException;
import com.plattio.plattio_backend.exceptions.SesionMesaException;
import com.plattio.plattio_backend.modelo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionDAO notificacionDAO;

    @Autowired
    private SesionMesaDAO sesionMesaDAO;

    @Autowired
    private EmpleadoDAO empleadoDAO;

    @Autowired
    private PedidoDAO pedidoDAO;

    public Notificacion obtenerPorId(Long id) throws NotificacionException {
        return notificacionDAO.buscarPorId(id)
                .orElseThrow(() -> new NotificacionException("Notificación no encontrada con ID: " + id, HttpStatus.NOT_FOUND));
    }

    public List<Notificacion> obtenerPorMozoYEstado(Long mozoId, String estado) throws NotificacionException {
        List<Notificacion> notis;

        if ("completada".equalsIgnoreCase(estado)) {
            notis = notificacionDAO.buscarPorMozoCompletadas(mozoId, estado);
        } else {
            notis = notificacionDAO.buscarPorMozoYEstado(mozoId, estado);
        }
        if (notis.isEmpty()) {
            throw new NotificacionException("No hay notificaciones para este mozo con estado: " + estado, HttpStatus.NOT_FOUND);
        }
        return notis;
    }

    public Notificacion crearNotificacion(String mensaje, String tipo, Long mozoId, Long sesionId) throws EmpleadoException, SesionMesaException {
        Empleado mozo = empleadoDAO.buscarPorId(mozoId)
                .orElseThrow(() -> new EmpleadoException("Mozo no encontrado", HttpStatus.NOT_FOUND));

        SesionMesa sesion = sesionMesaDAO.buscarPorId(sesionId)
                .orElseThrow(() -> new SesionMesaException("Sesión no encontrada", HttpStatus.NOT_FOUND));

        Notificacion n = new Notificacion(mensaje, tipo, mozo, sesion);
        return notificacionDAO.guardar(n);
    }

    public Notificacion crearNotificacion2(String mensaje, String tipo, Integer mesaNum, Long idPedido) throws EmpleadoException, SesionMesaException, PedidoException {

        SesionMesa sesion = sesionMesaDAO.obtenerSesionActivaPorMesaNum(mesaNum)
                .orElseThrow(() -> new SesionMesaException("Sesión no encontrada", HttpStatus.NOT_FOUND));

        Pedido pedido = pedidoDAO.buscarPorId(idPedido)
                .orElseThrow(() -> new PedidoException("Pedido no encontrado", HttpStatus.NOT_FOUND));

        Empleado mozo = empleadoDAO.buscarPorSesionId(sesion.getId())
                .orElseThrow(() -> new EmpleadoException("Mozo no encontrado", HttpStatus.NOT_FOUND));

        Notificacion n = new Notificacion(mensaje, tipo, mozo, sesion, pedido);
        return notificacionDAO.guardar(n);
    }

    public void marcarComoCompletada(Long id) throws NotificacionException {
        Notificacion n = notificacionDAO.buscarPorId(id)
                .orElseThrow(() -> new NotificacionException("Notificación no encontrada", HttpStatus.NOT_FOUND));

        n.setEstado("completada");
        notificacionDAO.guardar(n);
    }

    public List<Notificacion> obtenerTodas() {
        return notificacionDAO.buscarTodas();
    }

    public void toggleEstado(Long id) throws NotificacionException {
        Notificacion noti = obtenerPorId(id);
        if ("completada".equalsIgnoreCase(noti.getEstado())) {
            noti.setEstado("pendiente");
        } else {
            noti.setEstado("completada");
        }
        notificacionDAO.guardar(noti);
    }

    public void completarNotificacionesPorPedido(Long pedidoId) {
        Notificacion noti = notificacionDAO.buscarPrimeraPorPedidoYEstado(pedidoId, "pendiente")
                .orElse(null);

        if (noti != null) {
            noti.setEstado("completada");
            notificacionDAO.guardar(noti);
        }
    }

}
