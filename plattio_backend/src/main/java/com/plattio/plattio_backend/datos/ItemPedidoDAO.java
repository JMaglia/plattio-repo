package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.ItemPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return itemPedidoRepository.findByPedidoIdConPlato(pedidoId);
    }

    public Page<ItemPedido> obtenerPorEstado(String estado, Pageable pageable) {
        return itemPedidoRepository.findByEstadoConPlato(estado, pageable);
    }

    public List<ItemPedido> obtenerActivos() {
        return itemPedidoRepository.findByFechaFinIsNullConPlato();
    }

    public List<ItemPedido> obtenerPorPedidoYEstado(Long pedidoId, String estado) {
        return itemPedidoRepository.findByPedidoIdAndEstado(pedidoId, estado);
    }
}
