package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.PedidoDAO;
import com.plattio.plattio_backend.datos.SesionMesaDAO;
import com.plattio.plattio_backend.dto.request.CrearPedidoRequest;
import com.plattio.plattio_backend.exceptions.PedidoException;
import com.plattio.plattio_backend.modelo.Pedido;
import com.plattio.plattio_backend.modelo.SesionMesa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    private final PedidoDAO pedidoDAO;
    private final SesionMesaDAO sesionMesaDAO;
    private final NotificacionService notificacionService;

    public PedidoService(PedidoDAO pedidoDAO, SesionMesaDAO sesionMesaDAO, NotificacionService notificacionService) {
        this.pedidoDAO = pedidoDAO;
        this.sesionMesaDAO = sesionMesaDAO;
        this.notificacionService = notificacionService;
    }

    @Transactional(readOnly = true)
    public Page<Pedido> obtenerTodosPaginado(Pageable pageable) {
        Page<Long> idsPage = pedidoDAO.obtenerIds(pageable);
        if (idsPage.isEmpty()) {
            return Page.empty(pageable);
        }
        List<Long> ids = idsPage.getContent();
        Map<Long, Pedido> pedidosPorId = pedidoDAO.obtenerPorIdsConDetalle(ids).stream()
                .collect(Collectors.toMap(Pedido::getId, p -> p));
        List<Pedido> pedidosOrdenados = ids.stream().map(pedidosPorId::get).toList();
        return new PageImpl<>(pedidosOrdenados, pageable, idsPage.getTotalElements());
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoDAO.buscarPorId(id)
                .orElseThrow(() -> new PedidoException("Pedido no encontrado con ID: " + id, HttpStatus.NOT_FOUND));
    }

    public List<Pedido> obtenerPorSesion(Long sesionId) {
        if (sesionMesaDAO.buscarPorId(sesionId).isEmpty()) {
            throw new PedidoException("Sesión no encontrada con ID: " + sesionId, HttpStatus.NOT_FOUND);
        }
        return pedidoDAO.obtenerPorSesion(sesionId);
    }

    public List<Pedido> obtenerPorEstado(String estado) {
        return pedidoDAO.obtenerPorEstado(estado);
    }

    public List<Pedido> buscarPendientesPorMozo(Long mozoId) {
        return pedidoDAO.buscarPendientesMozo(List.of("pendiente", "en_preparacion"), mozoId);
    }

    public List<Pedido> obtenerPedidosListosPorMozo(Long mozoId) {
        return pedidoDAO.obtenerPedidosListosPorMozo(mozoId);
    }

    public void crearPedido(CrearPedidoRequest request) {
        SesionMesa sesion = sesionMesaDAO.buscarPorId(request.sesionId())
                .orElseThrow(() -> new PedidoException("Sesión de mesa no encontrada con ID: " + request.sesionId(), HttpStatus.NOT_FOUND));
        pedidoDAO.guardar(new Pedido(sesion, request.categoria()));
    }

    public void iniciarPreparacion(Long pedidoId) {
        Pedido pedido = obtenerPorId(pedidoId);
        pedido.iniciarPreparacion();
        pedidoDAO.guardar(pedido);
        log.info("Pedido {} cambia a estado {}", pedidoId, pedido.getEstado());
    }

    public void finalizarPedido(Long pedidoId) {
        Pedido pedido = obtenerPorId(pedidoId);
        pedido.finalizar();
        pedidoDAO.guardar(pedido);
        log.info("Pedido {} cambia a estado {}", pedidoId, pedido.getEstado());
    }

    public void cancelarPedido(Long pedidoId) {
        Pedido pedido = obtenerPorId(pedidoId);
        pedido.cancelar();
        pedidoDAO.guardar(pedido);
        log.info("Pedido {} cambia a estado {}", pedidoId, pedido.getEstado());
    }

    @Transactional
    public void cambiarEstado(Long pedidoId, String estado) {
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
        log.info("Pedido {} cambia a estado {}", pedidoId, pedido.getEstado());
    }

    public BigDecimal calcularTotalPedido(Long pedidoId) {
        return obtenerPorId(pedidoId).calcularTotal();
    }
}
