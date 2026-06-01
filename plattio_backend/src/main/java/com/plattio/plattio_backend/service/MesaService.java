package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.MesaDAO;
import com.plattio.plattio_backend.exceptions.MesaException;
import com.plattio.plattio_backend.exceptions.SesionMesaException;
import com.plattio.plattio_backend.modelo.Mesa;
import com.plattio.plattio_backend.modelo.SesionMesa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {

    @Autowired
    private MesaDAO mesaDAO;
    @Autowired
    private SesionMesaService sesionMesaService;

    // Obtener todas las mesas
    public List<Mesa> obtenerTodas() throws MesaException {
        List<Mesa> mesas = mesaDAO.obtenerTodas();
        if (mesas.isEmpty()) {
            throw new MesaException("No hay mesas registradas en el sistema.", HttpStatus.NOT_FOUND);
        }
        return mesas;
    }

    // Buscar por ID
    public Mesa obtenerPorId(Long id) throws MesaException {
        return mesaDAO.buscarPorId(id)
                .orElseThrow(() -> new MesaException("Mesa no encontrada con ID: " + id, HttpStatus.NOT_FOUND));
    }

    // Buscar por número
    public Mesa obtenerPorNumero(Integer numero) throws MesaException {
        return mesaDAO.buscarPorNumero(numero)
                .orElseThrow(() -> new MesaException("Mesa no encontrada con número: " + numero, HttpStatus.NOT_FOUND));
    }

    // Buscar por QR
    public Mesa obtenerPorQrToken(String qrToken) throws MesaException {
        return mesaDAO.buscarPorQrToken(qrToken)
                .orElseThrow(() -> new MesaException("Mesa no encontrada con token QR: " + qrToken, HttpStatus.NOT_FOUND));
    }

    // Obtener mesas por estado
    public List<Mesa> obtenerPorEstado(String estado) throws MesaException {
        List<Mesa> mesas = mesaDAO.obtenerPorEstado(estado);
        if (mesas.isEmpty()) {
            throw new MesaException("No se encontraron mesas con el estado: " + estado, HttpStatus.NOT_FOUND);
        }
        return mesas;
    }

    // Crear nueva mesa
    public void crearMesa(Mesa mesa) throws MesaException {
//        if (mesaDAO.buscarPorNumero(mesa.getNumero()).isPresent()) {
//            throw new MesaException("Ya existe una mesa con ese número.", HttpStatus.BAD_REQUEST);
//        }
        if (mesaDAO.buscarPorQrToken(mesa.getQrToken()).isPresent()) {
            throw new MesaException("Ya existe una mesa con ese token QR.", HttpStatus.BAD_REQUEST);
        }
        mesaDAO.guardar(mesa);
    }

    // Ocupar una mesa
    public void ocuparMesa(Long id) throws MesaException {
        Mesa mesa = obtenerPorId(id);
        if (!mesa.estaLibre()) {
            throw new MesaException("La mesa ya está ocupada.", HttpStatus.BAD_REQUEST);
        }
        mesa.ocupar();
        mesaDAO.guardar(mesa);
    }

    // Liberar una mesa
    public void liberarMesa(Long id) throws MesaException {
        Mesa mesa = obtenerPorId(id);
        if (mesa.estaLibre()) {
            throw new MesaException("La mesa ya está libre.", HttpStatus.BAD_REQUEST);
        }
        mesa.liberar();
        mesaDAO.guardar(mesa);
    }

    // Cambiar estado manualmente
    public void cambiarEstado(Long id, String nuevoEstado) throws MesaException {
        Mesa mesa = obtenerPorId(id);
        mesa.cambiarEstado(nuevoEstado);
        mesaDAO.guardar(mesa);
    }

    // Validar si está libre
    public boolean estaLibre(Long id) throws MesaException {
        return obtenerPorId(id).estaLibre();
    }

    // Validar si está ocupada
    public boolean estaOcupada(Long id) throws MesaException {
        return obtenerPorId(id).estaOcupada();
    }

    // Validar si tiene sesión activa con id mesa
    public boolean tieneSesionActiva(Long id) throws MesaException {
        return obtenerPorId(id).tieneSesionActiva();
    }

    // Validar si tiene sesión activa con el numero de mesa
    public boolean tieneSesionActivaNumMesa(Integer numero) throws MesaException {
        return obtenerPorNumero(numero).tieneSesionActiva();
    }

    // obtener si tiene sesión activa con el numero de mesa
    public SesionMesa obtenerSesionActivaNumMesa(Integer numero) throws MesaException, SesionMesaException {
        Mesa mesa = obtenerPorNumero(numero);
        SesionMesa sesion = sesionMesaService.obtenerSesionActivaPorMesa(mesa.getId());
        return sesion;
    }
}
