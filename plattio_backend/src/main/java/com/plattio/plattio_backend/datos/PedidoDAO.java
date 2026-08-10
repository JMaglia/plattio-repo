package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Pedido;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PedidoDAO {

    private final PedidoRepository pedidoRepository;

    public PedidoDAO(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public void eliminar(Long idPedido) {
        pedidoRepository.deleteById(idPedido);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findByIdConDetalle(id);
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAllConDetalle();
    }

    public List<Pedido> obtenerPorSesion(Long sesionId) {
        return pedidoRepository.findBySesionIdConDetalle(sesionId);
    }

    public List<Pedido> obtenerPorEstado(String estado) {
        return pedidoRepository.findByEstadoConDetalle(estado);
    }

    public List<Pedido> obtenerPedidosActivos() {
        return pedidoRepository.findByFechaFinIsNull();
    }

    public List<Pedido> obtenerPorSesionYEstado(Long sesionId, String estado) {
        return pedidoRepository.findBySesionIdAndEstado(sesionId, estado);
    }

    public List<Pedido> buscarPendientesMozo(List<String> estados, Long mozoId) {
        return pedidoRepository.findByEstadoInAndSesionMozoIdConDetalle(estados, mozoId);
    }

    public List<Pedido> obtenerPedidosListosPorMozo(Long mozoId) {
        return pedidoRepository.findByEstadoAndSesionMozoIdConDetalle("listo", mozoId);
    }

    public List<Pedido> obtenerPorSesionIdsConItems(List<Long> sesionIds) {
        return pedidoRepository.findBySesionIdInConItems(sesionIds);
    }
}
