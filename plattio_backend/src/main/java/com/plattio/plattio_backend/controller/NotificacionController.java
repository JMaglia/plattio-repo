package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.dto.request.CrearNotificacionConPedidoRequest;
import com.plattio.plattio_backend.dto.request.CrearNotificacionRequest;
import com.plattio.plattio_backend.mapper.NotificacionMapper;
import com.plattio.plattio_backend.service.NotificacionService;
import com.plattio.plattio_backend.views.NotificacionView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public ResponseEntity<List<NotificacionView>> obtenerTodasLasNotificaciones() {
        List<NotificacionView> views = notificacionService.obtenerTodas().stream()
                .map(NotificacionMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/mozo/{mozoId}/estado/{estado}")
    public ResponseEntity<List<NotificacionView>> obtenerPorMozoYEstado(
            @PathVariable Long mozoId, @PathVariable String estado) {
        List<NotificacionView> views = notificacionService.obtenerPorMozoYEstado(mozoId, estado).stream()
                .map(NotificacionMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @PostMapping
    public ResponseEntity<Void> crearNotificacion(@Valid @RequestBody CrearNotificacionRequest request) {
        notificacionService.crearNotificacion(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/pedido")
    public ResponseEntity<Void> crearNotificacionConPedido(@Valid @RequestBody CrearNotificacionConPedidoRequest request) {
        notificacionService.crearNotificacionConPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> toggleEstadoNotificacion(@PathVariable Long id) {
        notificacionService.toggleEstado(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/pedido/{pedidoId}/completar")
    public ResponseEntity<Void> completarPorPedido(@PathVariable Long pedidoId) {
        notificacionService.completarNotificacionesPorPedido(pedidoId);
        return ResponseEntity.ok().build();
    }
}
