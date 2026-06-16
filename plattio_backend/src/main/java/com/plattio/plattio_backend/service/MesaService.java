package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.MesaDAO;
import com.plattio.plattio_backend.dto.request.CrearMesaRequest;
import com.plattio.plattio_backend.exceptions.MesaException;
import com.plattio.plattio_backend.modelo.Mesa;
import com.plattio.plattio_backend.modelo.SesionMesa;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {

    private final MesaDAO mesaDAO;
    private final SesionMesaService sesionMesaService;

    public MesaService(MesaDAO mesaDAO, SesionMesaService sesionMesaService) {
        this.mesaDAO = mesaDAO;
        this.sesionMesaService = sesionMesaService;
    }

    public List<Mesa> obtenerTodas() {
        return mesaDAO.obtenerTodas();
    }

    public Mesa obtenerPorId(Long id) {
        return mesaDAO.buscarPorId(id)
                .orElseThrow(() -> new MesaException("Mesa no encontrada con ID: " + id, HttpStatus.NOT_FOUND));
    }

    public Mesa obtenerPorNumero(Integer numero) {
        return mesaDAO.buscarPorNumero(numero)
                .orElseThrow(() -> new MesaException("Mesa no encontrada con número: " + numero, HttpStatus.NOT_FOUND));
    }

    public Mesa obtenerPorQrToken(String qrToken) {
        return mesaDAO.buscarPorQrToken(qrToken)
                .orElseThrow(() -> new MesaException("Mesa no encontrada con token QR: " + qrToken, HttpStatus.NOT_FOUND));
    }

    public List<Mesa> obtenerPorEstado(String estado) {
        return mesaDAO.obtenerPorEstado(estado);
    }

    public void crearMesa(CrearMesaRequest request) {
        if (mesaDAO.buscarPorNumero(request.numero()).isPresent()) {
            throw new MesaException("Ya existe una mesa con el número: " + request.numero(), HttpStatus.BAD_REQUEST);
        }
        if (mesaDAO.buscarPorQrToken(request.qrToken()).isPresent()) {
            throw new MesaException("Ya existe una mesa con ese token QR.", HttpStatus.BAD_REQUEST);
        }
        mesaDAO.guardar(new Mesa(request.numero(), request.qrToken()));
    }

    public void ocuparMesa(Long id) {
        Mesa mesa = obtenerPorId(id);
        if (!mesa.estaLibre()) {
            throw new MesaException("La mesa ya está ocupada.", HttpStatus.BAD_REQUEST);
        }
        mesa.ocupar();
        mesaDAO.guardar(mesa);
    }

    public void liberarMesa(Long id) {
        Mesa mesa = obtenerPorId(id);
        if (mesa.estaLibre()) {
            throw new MesaException("La mesa ya está libre.", HttpStatus.BAD_REQUEST);
        }
        mesa.liberar();
        mesaDAO.guardar(mesa);
    }

    public void cambiarEstado(Long id, String nuevoEstado) {
        Mesa mesa = obtenerPorId(id);
        mesa.cambiarEstado(nuevoEstado);
        mesaDAO.guardar(mesa);
    }

    public boolean estaLibre(Long id) {
        return obtenerPorId(id).estaLibre();
    }

    public boolean estaOcupada(Long id) {
        return obtenerPorId(id).estaOcupada();
    }

    public boolean tieneSesionActiva(Long id) {
        return obtenerPorId(id).tieneSesionActiva();
    }

    public boolean tieneSesionActivaNumMesa(Integer numero) {
        return obtenerPorNumero(numero).tieneSesionActiva();
    }

    public SesionMesa obtenerSesionActivaNumMesa(Integer numero) {
        return sesionMesaService.obtenerSesionActivaPorMesa(obtenerPorNumero(numero).getId());
    }

    public void eliminar(Long id) {
        obtenerPorId(id);
        mesaDAO.eliminar(id);
    }
}
