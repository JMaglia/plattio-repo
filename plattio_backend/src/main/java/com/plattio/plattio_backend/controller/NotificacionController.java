package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.exceptions.EmpleadoException;
import com.plattio.plattio_backend.exceptions.NotificacionException;
import com.plattio.plattio_backend.exceptions.PedidoException;
import com.plattio.plattio_backend.exceptions.SesionMesaException;
import com.plattio.plattio_backend.modelo.Notificacion;
import com.plattio.plattio_backend.service.NotificacionService;
import com.plattio.plattio_backend.views.NotificacionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/notificaciones")
    public ResponseEntity<List<NotificacionView>> obtenerTodasLasNotificaciones() {
        List<NotificacionView> views = notificacionService.obtenerTodas().stream()
                .map(Notificacion::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/notificaciones/mozo/{mozoId}/estado/{estado}")
    public ResponseEntity<List<NotificacionView>> obtenerNotificacionesPorMozoYEstado(
            @PathVariable Long mozoId,
            @PathVariable String estado
    ) throws NotificacionException {
        List<NotificacionView> views = notificacionService.obtenerPorMozoYEstado(mozoId, estado).stream()
                .map(Notificacion::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @PostMapping("/notificaciones/{mensaje}/{tipo}/{mozoId}/{sesionId}")
    public ResponseEntity<NotificacionView> crearNotificacion(
            @PathVariable String mensaje,
            @PathVariable String tipo,
            @PathVariable Long mozoId,
            @PathVariable Long sesionId
    ) throws EmpleadoException, SesionMesaException {
        Notificacion noti = notificacionService.crearNotificacion(mensaje, tipo, mozoId, sesionId);
        return new ResponseEntity<>(noti.toView(), HttpStatus.CREATED);
    }

    @PostMapping("/notificaciones/pedido/{mensaje}/{tipo}/{mesaNum}/{idPedido}")
    public ResponseEntity<NotificacionView> crearNotificacion2(
            @PathVariable String mensaje,
            @PathVariable String tipo,
            @PathVariable Integer mesaNum,
            @PathVariable Long idPedido
    ) throws EmpleadoException, SesionMesaException, PedidoException {
        Notificacion noti = notificacionService.crearNotificacion2(mensaje, tipo, mesaNum, idPedido);
        return new ResponseEntity<>(noti.toView(), HttpStatus.CREATED);
    }

    @PostMapping("/notificaciones/{id}/toggleEstado")
    public ResponseEntity<String> toggleEstadoNotificacion(@PathVariable Long id) throws NotificacionException {
        notificacionService.toggleEstado(id);
        return new ResponseEntity<>("Estado de notificación actualizado", HttpStatus.OK);
    }

    @PostMapping("/pedido/{pedidoId}/completar")
    public ResponseEntity<Void> completarPorPedido(@PathVariable Long pedidoId) {
        notificacionService.completarNotificacionesPorPedido(pedidoId);
        return ResponseEntity.ok().build();
    }
}
