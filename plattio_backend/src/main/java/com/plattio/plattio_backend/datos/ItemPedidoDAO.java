package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.ItemPedido;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ItemPedidoDAO {

    private final ItemPedidoRepository itemPedidoRepository;

    public ItemPedidoDAO(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public ItemPedido guardar(ItemPedido item) {
        return itemPedidoRepository.save(item);
    }

    public void eliminar(Long id) {
        itemPedidoRepository.deleteById(id);
    }

    public Optional<ItemPedido> buscarPorId(Long id) {
        return itemPedidoRepository.findById(id);
    }

    public List<ItemPedido> obtenerTodos() {
        return itemPedidoRepository.findAll();
    }

    public List<ItemPedido> obtenerPorPedido(Long pedidoId) {
        return itemPedidoRepository.findByPedidoId(pedidoId);
    }

    public List<ItemPedido> obtenerPorEstado(String estado) {
        return itemPedidoRepository.findByEstado(estado);
    }

    public List<ItemPedido> obtenerActivos() {
        return itemPedidoRepository.findByFechaFinIsNull();
    }

    public List<ItemPedido> obtenerPorPedidoYEstado(Long pedidoId, String estado) {
        return itemPedidoRepository.findByPedidoIdAndEstado(pedidoId, estado);
    }
}
