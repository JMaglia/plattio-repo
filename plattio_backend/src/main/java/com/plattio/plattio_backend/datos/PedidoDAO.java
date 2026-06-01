package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PedidoDAO {

    @Autowired
    private PedidoRepository pedidoRepository;

    // CRUD básico
    public void guardar(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    public void eliminar(Long idPedido) {
        pedidoRepository.deleteById(idPedido);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    // Consultas útiles
    public List<Pedido> obtenerPorSesion(Long sesionId) {
        return pedidoRepository.findBySesionId(sesionId);
    }

    public List<Pedido> obtenerPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public List<Pedido> obtenerPedidosActivos() {
        return pedidoRepository.findByFechaFinIsNull();
    }

    public List<Pedido> obtenerPorSesionYEstado(Long sesionId, String estado) {
        return pedidoRepository.findBySesionIdAndEstado(sesionId, estado);
    }

    public List<Pedido> buscarPendientesMozo(List<String> estados, Long mozoId) {
        return pedidoRepository.findByEstadoInAndSesion_Mozo_Id(estados, mozoId);
    }

    public List<Pedido> obtenerPedidosListosPorMozo(Long mozoId) {
        return pedidoRepository.findByEstadoAndSesion_Mozo_Id("listo", mozoId);
    }



}

