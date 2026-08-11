package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.dto.request.CrearNotificacionConPedidoRequest;
import com.plattio.plattio_backend.dto.request.CrearNotificacionRequest;
import com.plattio.plattio_backend.mapper.NotificacionMapper;
import com.plattio.plattio_backend.service.NotificacionService;
import com.plattio.plattio_backend.views.NotificacionView;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public ResponseEntity<Page<NotificacionView>> obtenerTodasLasNotificaciones(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(notificacionService.obtenerTodas(pageable).map(NotificacionMapper::toView));
    }

    @GetMapping("/mozo/{mozoId}/estado/{estado}")
    public ResponseEntity<Page<NotificacionView>> obtenerPorMozoYEstado(
            @PathVariable Long mozoId, @PathVariable String estado,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(notificacionService.obtenerPorMozoYEstado(mozoId, estado, pageable).map(NotificacionMapper::toView));
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
