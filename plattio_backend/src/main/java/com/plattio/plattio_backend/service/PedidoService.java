package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.PedidoDAO;
import com.plattio.plattio_backend.datos.SesionMesaDAO;
import com.plattio.plattio_backend.exceptions.PedidoException;
import com.plattio.plattio_backend.modelo.Pedido;
import com.plattio.plattio_backend.modelo.SesionMesa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoDAO pedidoDAO;

    @Autowired
    private SesionMesaDAO sesionMesaDAO;

    @Autowired
    private NotificacionService notificacionService;

    // Obtener todos los pedidos
    public List<Pedido> obtenerTodos() throws PedidoException {
        List<Pedido> pedidos = pedidoDAO.obtenerTodos();
        if (pedidos.isEmpty()) {
            throw new PedidoException("No se encontraron pedidos en el sistema.", HttpStatus.NOT_FOUND);
        }
        return pedidos;
    }

    // Obtener pedido por ID
    public Pedido obtenerPorId(Long id) throws PedidoException {
        return pedidoDAO.buscarPorId(id)
                .orElseThrow(() -> new PedidoException("Pedido no encontrado con ID: " + id, HttpStatus.NOT_FOUND));
    }

    // Obtener pedidos por sesión
    public List<Pedido> obtenerPorSesion(Long sesionId) throws PedidoException {
        if (sesionMesaDAO.buscarPorId(sesionId).isEmpty()) {
            throw new PedidoException("Sesión no encontrada con ID: " + sesionId, HttpStatus.NOT_FOUND);
        }
        List<Pedido> pedidos = pedidoDAO.obtenerPorSesion(sesionId);
        if (pedidos.isEmpty()) {
            throw new PedidoException("La sesión con ID " + sesionId + " no tiene pedidos registrados.", HttpStatus.NOT_FOUND);
        }
        return pedidos;
    }

    // Obtener pedidos por estado
    public List<Pedido> obtenerPorEstado(String estado) throws PedidoException {
        List<Pedido> pedidos = pedidoDAO.obtenerPorEstado(estado);
        return pedidos;
    }

    public List<Pedido> buscarPendientesPorMozo(Long mozoId) {
        List<String> estados = List.of("pendiente", "en_preparacion");
        return pedidoDAO.buscarPendientesMozo(estados, mozoId);
    }

    public List<Pedido> obtenerPedidosListosPorMozo(Long mozoId) throws PedidoException {
        List<Pedido> pedidos = pedidoDAO.obtenerPedidosListosPorMozo(mozoId);
        return pedidos;
    }



    // Crear nuevo pedido
    public void crearPedido(Long sesionId, String categoria) throws PedidoException {
        SesionMesa sesion = sesionMesaDAO.buscarPorId(sesionId)
                .orElseThrow(() -> new PedidoException("Sesión de mesa no encontrada con ID: " + sesionId, HttpStatus.NOT_FOUND));

        Pedido nuevoPedido = new Pedido(sesion, categoria);
        pedidoDAO.guardar(nuevoPedido);
        sesion.agregarPedido(nuevoPedido);
        sesionMesaDAO.guardar(sesion);
    }

    // Cambiar estado del pedido
    public void iniciarPreparacion(Long pedidoId) throws PedidoException {
        Pedido pedido = obtenerPorId(pedidoId);
        pedido.iniciarPreparacion();
        pedidoDAO.guardar(pedido);
    }

    public void finalizarPedido(Long pedidoId) throws PedidoException {
        Pedido pedido = obtenerPorId(pedidoId);
        pedido.finalizar();
        pedidoDAO.guardar(pedido);
    }

    public void cancelarPedido(Long pedidoId) throws PedidoException {
        Pedido pedido = obtenerPorId(pedidoId);
        pedido.cancelar();
        pedidoDAO.guardar(pedido);
    }

    public Pedido cambiarEstado(Long pedidoId, String estado) throws PedidoException {
        Pedido pedido = obtenerPorId(pedidoId);
        if ("en_preparacion".equalsIgnoreCase(estado)) {
            pedido.iniciarPreparacion();
        } else {
            pedido.cambiarEstado(estado);
        }
        if ("finalizado".equalsIgnoreCase(estado)) {
            notificacionService.completarNotificacionesPorPedido(pedidoId);
        }
        pedidoDAO.guardar(pedido);
        return pedido;
    }

    // Calcular total
    public double calcularTotalPedido(Long pedidoId) throws PedidoException {
        Pedido pedido = obtenerPorId(pedidoId);
        return pedido.calcularTotal().doubleValue();
    }

}
