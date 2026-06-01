package com.plattio.plattio_backend;

import com.plattio.plattio_backend.modelo.*;
import com.plattio.plattio_backend.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IntegracionBDTest {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private MesaService mesaService;

    @Autowired
    private SesionMesaService sesionMesaService;

    @Autowired
    private PlatoService platoService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ItemPedidoService itemPedidoService;

    @Test
    void testInteraccionCompleta() throws Exception {
        // 1. Crear un mozo
        Empleado mozo = new Empleado("Juan Pérez", "juan@plattio.com", "1234", "mozo");
        empleadoService.registrarEmpleado(mozo);
        Empleado mozoPersistido = empleadoService.buscarPorEmail("juan@plattio.com");
        assertNotNull(mozoPersistido.getId());

        // 2. Crear una mesa
        Mesa mesa = new Mesa(10, "qr123token");
        mesaService.cambiarEstado(mesa.getId(), "libre"); // solo si ya está en BD
        mesaService.liberarMesa(mesa.getId());
        mesaService.cambiarEstado(mesa.getId(), "libre"); // por las dudas
        mesaService.ocuparMesa(mesa.getId());

        // 3. Crear sesión en la mesa
        sesionMesaService.iniciarSesion(mesa.getId(), mozoPersistido.getId(), "anonimo");
        SesionMesa sesion = sesionMesaService.obtenerSesionActivaPorMesa(mesa.getId());
        assertTrue(sesion.esActiva());

        // 4. Crear un plato
        Plato plato = new Plato("Milanesa con puré", "Clásica", new BigDecimal("1500.00"), "principal", 20);
        platoService.crearPlato(plato);
        assertNotNull(platoService.buscarPorId(plato.getId()));

        // 5. Crear un pedido en esa sesión
        pedidoService.crearPedido(sesion.getId(), "principal");
        List<Pedido> pedidos = pedidoService.obtenerPorSesion(sesion.getId());
        assertEquals(1, pedidos.size());
        Pedido pedido = pedidos.get(0);

        // 6. Agregar ítem al pedido
        itemPedidoService.agregarItem(pedido.getId(), plato.getId(), 2, "Sin sal");
        List<ItemPedido> items = itemPedidoService.obtenerPorPedido(pedido.getId());
        assertEquals(1, items.size());

        // 7. Validar subtotal del ítem
        double subtotal = itemPedidoService.calcularSubtotal(items.get(0).getId());
        assertEquals(3000.0, subtotal);

        // 8. Entregar ítem
        itemPedidoService.iniciarPreparacion(items.get(0).getId());
        itemPedidoService.marcarListo(items.get(0).getId());
        itemPedidoService.entregarItem(items.get(0).getId());

        // 9. Finalizar pedido
        pedidoService.finalizarPedido(pedido.getId());

        // 10. Finalizar sesión
        sesionMesaService.finalizarSesion(sesion.getId());
        assertFalse(sesionMesaService.buscarPorId(sesion.getId()).esActiva());
    }
}
