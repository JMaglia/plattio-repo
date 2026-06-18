package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.dto.request.AgregarItemRequest;
import com.plattio.plattio_backend.mapper.ItemPedidoMapper;
import com.plattio.plattio_backend.service.ItemPedidoService;
import com.plattio.plattio_backend.views.ItemPedidoView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemPedidoController {

    private final ItemPedidoService itemPedidoService;

    public ItemPedidoController(ItemPedidoService itemPedidoService) {
        this.itemPedidoService = itemPedidoService;
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<ItemPedidoView>> obtenerItemsPorPedido(@PathVariable Long pedidoId) {
        List<ItemPedidoView> views = itemPedidoService.obtenerPorPedido(pedidoId).stream()
                .map(ItemPedidoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ItemPedidoView>> obtenerItemsPorEstado(@PathVariable String estado) {
        List<ItemPedidoView> views = itemPedidoService.obtenerPorEstado(estado).stream()
                .map(ItemPedidoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ItemPedidoView>> obtenerItemsActivos() {
        List<ItemPedidoView> views = itemPedidoService.obtenerActivos().stream()
                .map(ItemPedidoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/{itemId}/subtotal")
    public ResponseEntity<BigDecimal> calcularSubtotalItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemPedidoService.calcularSubtotal(itemId));
    }

    @PostMapping("/pedido/{pedidoId}")
    public ResponseEntity<Void> agregarItemAPedido(@PathVariable Long pedidoId, @RequestBody AgregarItemRequest request) {
        itemPedidoService.agregarItem(pedidoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{itemId}/iniciar")
    public ResponseEntity<Void> iniciarPreparacionItem(@PathVariable Long itemId) {
        itemPedidoService.iniciarPreparacion(itemId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{itemId}/listo")
    public ResponseEntity<Void> marcarItemListo(@PathVariable Long itemId) {
        itemPedidoService.marcarListo(itemId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{itemId}/entregar")
    public ResponseEntity<Void> entregarItem(@PathVariable Long itemId) {
        itemPedidoService.entregarItem(itemId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{itemId}/cancelar")
    public ResponseEntity<Void> cancelarItem(@PathVariable Long itemId) {
        itemPedidoService.cancelarItem(itemId);
        return ResponseEntity.ok().build();
    }
}
