package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.SesionMesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SesionMesaDAO {

    private final SesionMesaRepository sesionMesaRepository;

    public SesionMesaDAO(SesionMesaRepository sesionMesaRepository) {
        this.sesionMesaRepository = sesionMesaRepository;
    }

    public SesionMesa guardar(SesionMesa sesion) {
        return sesionMesaRepository.save(sesion);
    }

    public void eliminar(Long id) {
        sesionMesaRepository.deleteById(id);
    }

    public Optional<SesionMesa> buscarPorId(Long id) {
        return sesionMesaRepository.findByIdConMesaYMozoYPedidos(id);
    }

    public List<SesionMesa> obtenerActivas() {
        return sesionMesaRepository.findByFechaFinIsNullConMesaYMozoYPedidos();
    }

    public Optional<SesionMesa> obtenerSesionActivaPorMesa(Long mesaId) {
        return sesionMesaRepository.findByMesaIdAndFechaFinIsNullConMesaYMozoYPedidos(mesaId);
    }

    public Optional<SesionMesa> obtenerSesionActivaPorMesaNum(Integer mesaNum) {
        return sesionMesaRepository.findByMesaNumeroAndFechaFinIsNullConMesaYMozoYPedidos(mesaNum);
    }

    public List<SesionMesa> obtenerSesionesActivasPorMozo(Long mozoId) {
        return sesionMesaRepository.findByMozoIdAndFechaFinIsNullConMesaYMozoYPedidos(mozoId);
    }

    public List<SesionMesa> obtenerSesionesFinalizadasPorMesa(Long mesaId) {
        return sesionMesaRepository.findByMesaIdAndFechaFinIsNotNull(mesaId);
    }

    public Page<Long> obtenerIds(Pageable pageable) {
        return sesionMesaRepository.findAllIds(pageable);
    }

    public List<SesionMesa> obtenerPorIdsConMesaYMozoYPedidos(List<Long> ids) {
        return sesionMesaRepository.findByIdInConMesaYMozoYPedidos(ids);
    }
}
