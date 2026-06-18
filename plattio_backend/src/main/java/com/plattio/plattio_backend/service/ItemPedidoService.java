package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.ItemPedidoDAO;
import com.plattio.plattio_backend.dto.request.AgregarItemRequest;
import com.plattio.plattio_backend.exceptions.ItemPedidoException;
import com.plattio.plattio_backend.modelo.ItemPedido;
import com.plattio.plattio_backend.modelo.Pedido;
import com.plattio.plattio_backend.modelo.Plato;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ItemPedidoService {

    private final ItemPedidoDAO itemPedidoDAO;
    private final PedidoService pedidoService;
    private final PlatoService platoService;

    public ItemPedidoService(ItemPedidoDAO itemPedidoDAO, PedidoService pedidoService, PlatoService platoService) {
        this.itemPedidoDAO = itemPedidoDAO;
        this.pedidoService = pedidoService;
        this.platoService = platoService;
    }

    public List<ItemPedido> obtenerPorPedido(Long pedidoId) {
        pedidoService.obtenerPorId(pedidoId);
        return itemPedidoDAO.obtenerPorPedido(pedidoId);
    }

    public List<ItemPedido> obtenerPorEstado(String estado) {
        return itemPedidoDAO.obtenerPorEstado(estado);
    }

    public List<ItemPedido> obtenerActivos() {
        return itemPedidoDAO.obtenerActivos();
    }

    public void agregarItem(Long pedidoId, AgregarItemRequest request) {
        if (request.cantidad() <= 0) {
            throw new ItemPedidoException("La cantidad debe ser mayor a cero.", HttpStatus.BAD_REQUEST);
        }
        Pedido pedido = pedidoService.obtenerPorId(pedidoId);
        Plato plato = platoService.buscarPorId(request.platoId());

        ItemPedido nuevoItem = new ItemPedido(pedido, plato, request.cantidad(), request.nota());
        pedido.agregarItem(nuevoItem);
        itemPedidoDAO.guardar(nuevoItem);
    }

    public void iniciarPreparacion(Long itemId) {
        ItemPedido item = buscarItem(itemId);
        item.iniciarPreparacion();
        itemPedidoDAO.guardar(item);
    }

    public void marcarListo(Long itemId) {
        ItemPedido item = buscarItem(itemId);
        item.marcarListo();
        itemPedidoDAO.guardar(item);
    }

    public void entregarItem(Long itemId) {
        ItemPedido item = buscarItem(itemId);
        item.entregar();
        itemPedidoDAO.guardar(item);
    }

    public void cancelarItem(Long itemId) {
        ItemPedido item = buscarItem(itemId);
        item.cancelar();
        itemPedidoDAO.guardar(item);
    }

    public BigDecimal calcularSubtotal(Long itemId) {
        return buscarItem(itemId).calcularSubtotal();
    }

    private ItemPedido buscarItem(Long id) {
        return itemPedidoDAO.buscarPorId(id)
                .orElseThrow(() -> new ItemPedidoException("ItemPedido no encontrado con ID: " + id, HttpStatus.NOT_FOUND));
    }
}
