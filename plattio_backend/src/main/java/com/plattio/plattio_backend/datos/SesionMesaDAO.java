package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.SesionMesa;
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
        return sesionMesaRepository.findById(id);
    }

    public List<SesionMesa> obtenerTodas() {
        return sesionMesaRepository.findAll();
    }

    public List<SesionMesa> obtenerActivas() {
        return sesionMesaRepository.findByFechaFinIsNull();
    }

    public Optional<SesionMesa> obtenerSesionActivaPorMesa(Long mesaId) {
        return sesionMesaRepository.findByMesaIdAndFechaFinIsNull(mesaId);
    }

    public Optional<SesionMesa> obtenerSesionActivaPorMesaNum(Integer mesaNum) {
        return sesionMesaRepository.findByMesaNumeroAndFechaFinIsNull(mesaNum);
    }

    public List<SesionMesa> obtenerSesionesActivasPorMozo(Long mozoId) {
        return sesionMesaRepository.findByMozoIdAndFechaFinIsNull(mozoId);
    }

    public List<SesionMesa> obtenerSesionesFinalizadasPorMesa(Long mesaId) {
        return sesionMesaRepository.findByMesaIdAndFechaFinIsNotNull(mesaId);
    }
}
