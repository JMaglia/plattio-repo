package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Notificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Notificacion> obtenerTodas(Pageable pageable) {
        return notificacionRepository.findAllConSesionYMesa(pageable);
    }

    public void eliminarPorId(Long id) {
        notificacionRepository.deleteById(id);
    }

    public Page<Notificacion> buscarPorMozoYEstado(Long mozoId, String estado, Pageable pageable) {
        return notificacionRepository.findByMozoIdAndEstadoConSesionYMesa(mozoId, estado, pageable);
    }

    public Page<Notificacion> buscarPorMozoCompletadas(Long mozoId, String estado, Pageable pageable) {
        return notificacionRepository.findByMozoIdAndEstadoConSesionYMesaOrderByIdDesc(mozoId, estado, pageable);
    }

    public List<Notificacion> buscarPorPedidoYEstado(Long pedidoId, String estado) {
        return notificacionRepository.findByPedido_IdAndEstado(pedidoId, estado);
    }

    public Optional<Notificacion> buscarPrimeraPorPedidoYEstado(Long pedidoId, String estado) {
        return notificacionRepository.findFirstByPedido_IdAndEstado(pedidoId, estado);
    }
}
