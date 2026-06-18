package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Notificacion;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificacionDAO {

    private final NotificacionRepository notificacionRepository;

    public NotificacionDAO(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public Notificacion guardar(Notificacion n) {
        return notificacionRepository.save(n);
    }

    public Optional<Notificacion> buscarPorId(Long id) {
        return notificacionRepository.findById(id);
    }

    public List<Notificacion> obtenerTodas() {
        return notificacionRepository.findAll();
    }

    public void eliminarPorId(Long id) {
        notificacionRepository.deleteById(id);
    }

    public List<Notificacion> buscarPorMozoYEstado(Long mozoId, String estado) {
        return notificacionRepository.findByMozoIdAndEstado(mozoId, estado);
    }

    public List<Notificacion> buscarPorMozoCompletadas(Long mozoId, String estado) {
        return notificacionRepository.findByMozo_IdAndEstadoOrderByIdDesc(mozoId, estado);
    }

    public List<Notificacion> buscarPorPedidoYEstado(Long pedidoId, String estado) {
        return notificacionRepository.findByPedido_IdAndEstado(pedidoId, estado);
    }

    public Optional<Notificacion> buscarPrimeraPorPedidoYEstado(Long pedidoId, String estado) {
        return notificacionRepository.findFirstByPedido_IdAndEstado(pedidoId, estado);
    }
}
