package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.ItemPedidoDAO;
import com.plattio.plattio_backend.datos.PedidoDAO;
import com.plattio.plattio_backend.datos.PlatoDAO;
import com.plattio.plattio_backend.exceptions.ItemPedidoException;
import com.plattio.plattio_backend.modelo.ItemPedido;
import com.plattio.plattio_backend.modelo.Pedido;
import com.plattio.plattio_backend.modelo.Plato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemPedidoService {

    @Autowired
    private ItemPedidoDAO itemPedidoDAO;

    @Autowired
    private PedidoDAO pedidoDAO;

    @Autowired
    private PlatoDAO platoDAO;

    // Obtener todos los ítems de un pedido
    public List<ItemPedido> obtenerPorPedido(Long pedidoId) throws ItemPedidoException {
        pedidoDAO.buscarPorId(pedidoId)
                .orElseThrow(() -> new ItemPedidoException("Pedido no encontrado con ID: " + pedidoId, HttpStatus.NOT_FOUND));
        return itemPedidoDAO.obtenerPorPedido(pedidoId);
    }

    // Obtener ítems por estado
    public List<ItemPedido> obtenerPorEstado(String estado) {
        return itemPedidoDAO.obtenerPorEstado(estado);
    }

    // Obtener ítems activos (sin fechaFin)
    public List<ItemPedido> obtenerActivos() {
        return itemPedidoDAO.obtenerActivos();
    }

    // Obtener ítems por mesa
//    public List<ItemPedido> obtenerPorMesa(String estado) {
//        return itemPedidoDAO.obtenerPorEstado(estado);
//    }


    // Crear un nuevo ítem dentro de un pedido
    public void agregarItem(Long pedidoId, Long platoId, int cantidad, String nota) throws ItemPedidoException {
        Pedido pedido = pedidoDAO.buscarPorId(pedidoId)
                .orElseThrow(() -> new ItemPedidoException("Pedido no encontrado con ID: " + pedidoId, HttpStatus.NOT_FOUND));
        Plato plato = platoDAO.buscarPorId(platoId)
                .orElseThrow(() -> new ItemPedidoException("Plato no encontrado con ID: " + platoId, HttpStatus.NOT_FOUND));

        if (cantidad <= 0) {
            throw new ItemPedidoException("La cantidad debe ser mayor a cero.", HttpStatus.BAD_REQUEST);
        }

        ItemPedido nuevoItem = new ItemPedido(pedido, plato, cantidad, nota);
        pedido.agregarItem(nuevoItem);
        pedidoDAO.guardar(pedido);
    }

    // Cambiar estado a "en preparación"
    public void iniciarPreparacion(Long itemId) throws ItemPedidoException {
        ItemPedido item = buscarItem(itemId);
        item.iniciarPreparacion();
        itemPedidoDAO.guardar(item);
    }

    // Marcar como listo
    public void marcarListo(Long itemId) throws ItemPedidoException {
        ItemPedido item = buscarItem(itemId);
        item.marcarListo();
        itemPedidoDAO.guardar(item);
    }

    // Entregar ítem
    public void entregarItem(Long itemId) throws ItemPedidoException {
        ItemPedido item = buscarItem(itemId);
        item.entregar();
        itemPedidoDAO.guardar(item);
    }

    // Cancelar ítem
    public void cancelarItem(Long itemId) throws ItemPedidoException {
        ItemPedido item = buscarItem(itemId);
        item.cancelar();
        itemPedidoDAO.guardar(item);
    }

    // Subtotal de un ítem
    public double calcularSubtotal(Long itemId) throws ItemPedidoException {
        return buscarItem(itemId).calcularSubtotal().doubleValue();
    }

    // ---------- Helper ----------
    private ItemPedido buscarItem(Long id) throws ItemPedidoException {
        return itemPedidoDAO.buscarPorId(id)
                .orElseThrow(() -> new ItemPedidoException("ItemPedido no encontrado con ID: " + id, HttpStatus.NOT_FOUND));
    }
}
