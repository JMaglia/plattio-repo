package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.exceptions.ItemPedidoException;
import com.plattio.plattio_backend.exceptions.PedidoException;
import com.plattio.plattio_backend.modelo.ItemPedido;
import com.plattio.plattio_backend.modelo.Pedido;
import com.plattio.plattio_backend.service.ItemPedidoService;
import com.plattio.plattio_backend.service.PedidoService;
import com.plattio.plattio_backend.views.ItemPedidoView;
import com.plattio.plattio_backend.views.PedidoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ItemPedidoService itemPedidoService;

    // PEDIDOS

    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoView>> obtenerTodosLosPedidos() throws PedidoException {
        List<PedidoView> views = pedidoService.obtenerTodos().stream()
                .map(Pedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoView> obtenerPedidoPorId(@PathVariable Long id) throws PedidoException {
        Pedido pedido = pedidoService.obtenerPorId(id);
        return new ResponseEntity<>(pedido.toView(), HttpStatus.OK);
    }

    @GetMapping("/pedidos/sesion/{sesionId}")
    public ResponseEntity<List<PedidoView>> obtenerPedidosPorSesion(@PathVariable Long sesionId) throws PedidoException {
        List<PedidoView> views = pedidoService.obtenerPorSesion(sesionId).stream()
                .map(Pedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/pedidos/estado/{estado}")
    public ResponseEntity<List<PedidoView>> obtenerPedidosPorEstado(@PathVariable String estado) throws PedidoException {
        List<Pedido> pedidos = pedidoService.obtenerPorEstado(estado);
        List<PedidoView> views = pedidos.stream()
                .map(Pedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/pedidos/{id}/total")
    public ResponseEntity<Double> calcularTotalDelPedido(@PathVariable Long id) throws PedidoException {
        return new ResponseEntity<>(pedidoService.calcularTotalPedido(id), HttpStatus.OK);
    }

    @GetMapping("/pedidos/activos/{mozoId}")
    public ResponseEntity<List<PedidoView>> obtenerPedidosPendientesMozo(@PathVariable Long mozoId) throws PedidoException {
        List<Pedido> pedidos = pedidoService.buscarPendientesPorMozo(mozoId);
        List<PedidoView> views = pedidos.stream()
                .map(Pedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/pedidos/listos/mozo/{mozoId}")
    public ResponseEntity<List<PedidoView>> obtenerPedidosListosPorMozo(@PathVariable Long mozoId) throws PedidoException {
        List<PedidoView> views = pedidoService.obtenerPedidosListosPorMozo(mozoId)
                .stream()
                .map(Pedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @PostMapping("/pedidos/{sesionId}/{categoria}")
    public ResponseEntity<String> crearPedido(@PathVariable Long sesionId, @PathVariable String categoria) throws PedidoException {
        pedidoService.crearPedido(sesionId, categoria);
        return new ResponseEntity<>("Pedido creado con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/pedidos/{id}/iniciar")
    public ResponseEntity<String> iniciarPreparacion(@PathVariable Long id) throws PedidoException {
        pedidoService.iniciarPreparacion(id);
        return new ResponseEntity<>("Preparación del pedido iniciada", HttpStatus.OK);
    }

    @PostMapping("/pedidos/{id}/cambiarEstado/{estado}")
    public ResponseEntity<PedidoView> CambiarEstado(@PathVariable Long id, @PathVariable String estado) throws PedidoException {
        Pedido pedido = pedidoService.cambiarEstado(id, estado);
        return ResponseEntity.ok(pedido.toView());
    }

    @PostMapping("/pedidos/{id}/finalizar")
    public ResponseEntity<String> finalizarPedido(@PathVariable Long id) throws PedidoException {
        pedidoService.finalizarPedido(id);
        return new ResponseEntity<>("Pedido finalizado con éxito", HttpStatus.OK);
    }

    @PostMapping("/pedidos/{id}/cancelar")
    public ResponseEntity<String> cancelarPedido(@PathVariable Long id) throws PedidoException {
        pedidoService.cancelarPedido(id);
        return new ResponseEntity<>("Pedido cancelado con éxito", HttpStatus.OK);
    }

    // ITEMS DE PEDIDO

    @GetMapping("/items/pedido/{pedidoId}")
    public ResponseEntity<List<ItemPedidoView>> obtenerItemsPorPedido(@PathVariable Long pedidoId) throws ItemPedidoException {
        List<ItemPedidoView> views = itemPedidoService.obtenerPorPedido(pedidoId).stream()
                .map(ItemPedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/items/estado/{estado}")
    public ResponseEntity<List<ItemPedidoView>> obtenerItemsPorEstado(@PathVariable String estado) {
        List<ItemPedidoView> views = itemPedidoService.obtenerPorEstado(estado).stream()
                .map(ItemPedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/items/activos")
    public ResponseEntity<List<ItemPedidoView>> obtenerItemsActivos() {
        List<ItemPedidoView> views = itemPedidoService.obtenerActivos().stream()
                .map(ItemPedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/items/{itemId}/subtotal")
    public ResponseEntity<Double> calcularSubtotalItem(@PathVariable Long itemId) throws ItemPedidoException {
        return new ResponseEntity<>(itemPedidoService.calcularSubtotal(itemId), HttpStatus.OK);
    }

    @PostMapping("/items/{pedidoId}/{platoId}/{cantidad}/{nota}")
    public ResponseEntity<String> agregarItemAPedido(
            @PathVariable Long pedidoId,
            @PathVariable Long platoId,
            @PathVariable int cantidad,
            @PathVariable String nota) throws ItemPedidoException {
        itemPedidoService.agregarItem(pedidoId, platoId, cantidad, nota);
        return new ResponseEntity<>("Ítem agregado al pedido con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/items/{itemId}/iniciar")
    public ResponseEntity<String> iniciarPreparacionItem(@PathVariable Long itemId) throws ItemPedidoException {
        itemPedidoService.iniciarPreparacion(itemId);
        return new ResponseEntity<>("Preparación del ítem iniciada", HttpStatus.OK);
    }

    @PostMapping("/items/{itemId}/listo")
    public ResponseEntity<String> marcarItemListo(@PathVariable Long itemId) throws ItemPedidoException {
        itemPedidoService.marcarListo(itemId);
        return new ResponseEntity<>("Ítem marcado como listo", HttpStatus.OK);
    }

    @PostMapping("/items/{itemId}/entregar")
    public ResponseEntity<String> entregarItem(@PathVariable Long itemId) throws ItemPedidoException {
        itemPedidoService.entregarItem(itemId);
        return new ResponseEntity<>("Ítem entregado", HttpStatus.OK);
    }

    @PostMapping("/items/{itemId}/cancelar")
    public ResponseEntity<String> cancelarItem(@PathVariable Long itemId) throws ItemPedidoException {
        itemPedidoService.cancelarItem(itemId);
        return new ResponseEntity<>("Ítem cancelado", HttpStatus.OK);
    }
}
