package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Mesa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MesaDAO {

    private final MesaRepository mesaRepository;

    public MesaDAO(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    public Mesa guardar(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public void eliminar(Long id) {
        mesaRepository.deleteById(id);
    }

    public Optional<Mesa> buscarPorId(Long id) {
        return mesaRepository.findById(id);
    }

    public List<Mesa> obtenerTodas() {
        return mesaRepository.findAll();
    }

    public Optional<Mesa> buscarPorNumero(Integer numero) {
        return mesaRepository.findByNumero(numero);
    }

    public Optional<Mesa> buscarPorQrToken(String qrToken) {
        return mesaRepository.findByQrToken(qrToken);
    }

    public List<Mesa> obtenerPorEstado(String estado) {
        return mesaRepository.findByEstado(estado);
    }
}
