package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.dto.request.CambiarEstadoPedidoRequest;
import com.plattio.plattio_backend.dto.request.CrearPedidoRequest;
import com.plattio.plattio_backend.mapper.PedidoMapper;
import com.plattio.plattio_backend.service.PedidoService;
import com.plattio.plattio_backend.views.PedidoView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<PedidoView>> obtenerTodosLosPedidos() {
        List<PedidoView> views = pedidoService.obtenerTodos().stream()
                .map(PedidoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoView> obtenerPedidoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PedidoMapper.toView(pedidoService.obtenerPorId(id)));
    }

    @GetMapping("/sesion/{sesionId}")
    public ResponseEntity<List<PedidoView>> obtenerPedidosPorSesion(@PathVariable Long sesionId) {
        List<PedidoView> views = pedidoService.obtenerPorSesion(sesionId).stream()
                .map(PedidoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PedidoView>> obtenerPedidosPorEstado(@PathVariable String estado) {
        List<PedidoView> views = pedidoService.obtenerPorEstado(estado).stream()
                .map(PedidoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<BigDecimal> calcularTotalDelPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.calcularTotalPedido(id));
    }

    @GetMapping("/activos/{mozoId}")
    public ResponseEntity<List<PedidoView>> obtenerPedidosPendientesMozo(@PathVariable Long mozoId) {
        List<PedidoView> views = pedidoService.buscarPendientesPorMozo(mozoId).stream()
                .map(PedidoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/listos/mozo/{mozoId}")
    public ResponseEntity<List<PedidoView>> obtenerPedidosListosPorMozo(@PathVariable Long mozoId) {
        List<PedidoView> views = pedidoService.obtenerPedidosListosPorMozo(mozoId).stream()
                .map(PedidoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @PostMapping
    public ResponseEntity<Void> crearPedido(@Valid @RequestBody CrearPedidoRequest request) {
        pedidoService.crearPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<Void> iniciarPreparacion(@PathVariable Long id) {
        pedidoService.iniciarPreparacion(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambiarEstadoPedidoRequest request) {
        pedidoService.cambiarEstado(id, request.estado());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarPedido(@PathVariable Long id) {
        pedidoService.finalizarPedido(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.ok().build();
    }
}
