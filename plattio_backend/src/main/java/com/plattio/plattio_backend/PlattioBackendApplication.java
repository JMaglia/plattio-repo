package com.plattio.plattio_backend;

import com.plattio.plattio_backend.modelo.*;
import com.plattio.plattio_backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class PlattioBackendApplication implements CommandLineRunner {

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

    public static void main(String[] args) {
        SpringApplication.run(PlattioBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //Empleado mozo = empleadoService.buscarPorEmail("juan@plattio.com.ar");
//        System.out.println("===== INICIANDO PRUEBA DE INTEGRACIÓN =====");

        // 1. Crear mozo
//        Empleado mozo = new Empleado("Lionel Messi", "messi@plattio.com", "1234", "mozo");
//        empleadoService.registrarEmpleado(mozo);
//        Empleado mozoPersistido = empleadoService.buscarPorEmail("messi@plattio.com");
//        System.out.println("✔ Mozo registrado: " + mozoPersistido.getId());

//         2. Crear mesa
//        Mesa mesa = new Mesa(null, "qr12456790 token");
//        mesaService.crearMesa(mesa);
//        mesaService.ocuparMesa(mesa.getId());
//        System.out.println("✔ Mesa ocupada: " + mesa.getId());

//        Mesa mesa2 = mesaService.obtenerPorId(2L);
//        System.out.println(mesa2.getId());
//        mesaService.ocuparMesa(mesa.getId());
//        mesaService.liberarMesa(mesa.getId());

//         3. Crear sesión
//        Mesa mesa3 = mesaService.obtenerPorId(4L);
//        Empleado mozoPersistido = empleadoService.buscarPorEmail("messi@plattio.com");
//        sesionMesaService.iniciarSesion(mesa3.getId(), mozoPersistido.getId(), "anonimo");
//        SesionMesa sesion = sesionMesaService.obtenerSesionActivaPorMesa(mesa3.getId());
//        System.out.println("✔ Sesión activa: " + sesion.getId());

        // 4. Crear plato
//        Plato plato = new Plato("Milanesa con puré", "Clásica", new BigDecimal("1500.00"), "principal", 20);
//        platoService.crearPlato(plato);
//        System.out.println("✔ Plato creado: " + plato.getId());
//
//        Plato plato2 = new Plato("Pollo al Curry", "Pollito al curry", new BigDecimal("10000.00"), "principal", 30);
//        platoService.crearPlato(plato2);
//
//        Plato plato3 = new Plato("Ravioles de calabaza", "Ravioles de calabaza con salsa", new BigDecimal("15000.00"), "principal", 25);
//        platoService.crearPlato(plato3);
//
//        Plato plato4 = new Plato("Porción de Papas Fritas", "Porción de Papas Fritas", new BigDecimal("5000.00"), "entrada", 1);
//        platoService.crearPlato(plato4);
//
//        Plato plato5 = new Plato("Flan con DDL", "Flan con dulce de leche", new BigDecimal("3500.00"), "entrada", 5);
//        platoService.crearPlato(plato5);
//
//        Plato plato6 = new Plato("Coca Cola 500cc", "Coca Cola 500cc sin azúcar", new BigDecimal("3500.00"), "gaseosa", 1);
//        platoService.crearPlato(plato6);

//        Plato plato = new Plato("Provoleta", "Con salsa criolla", new BigDecimal("10500.00"), "entrada", 20);
//        platoService.crearPlato(plato);
//
//        Plato plato2 = new Plato("Cordero braseado", "Con papas españolas", new BigDecimal("18000.00"), "principal", 30);
//        platoService.crearPlato(plato2);
//
//        Plato plato3 = new Plato("Hamburguesa smash", "Con cheddar, panceta y cebolla", new BigDecimal("15000.00"), "principal", 25);
//        platoService.crearPlato(plato3);
//
//        Plato plato4 = new Plato("Porción de rabas", "Para 2 personas", new BigDecimal("11000.00"), "entrada", 1);
//        platoService.crearPlato(plato4);
//
//        Plato plato5 = new Plato("Cheesecake de frutos rojos", "Con base de vainilla", new BigDecimal("8000.00"), "entrada", 5);
//        platoService.crearPlato(plato5);

        // 5. Crear pedido
//        Mesa mesa5 = new Mesa(18, "qr4567898token");
//        mesaService.crearMesa(mesa5);
//        Empleado mozo = empleadoService.buscarPorEmail("messi@plattio.com");
//        Mesa mesa5 = mesaService.obtenerPorId(9L);
//        sesionMesaService.iniciarSesion(mesa5.getId(), mozo.getId(), "anonimo");
//        SesionMesa sesion1 = sesionMesaService.obtenerSesionActivaPorMesa(mesa5.getId());

//        pedidoService.crearPedido(sesion1.getId(), "principal");
//        List<Pedido> pedidos = pedidoService.obtenerPorSesion(sesion1.getId());
//        Pedido pedido = pedidos.get(0);
//        System.out.println("✔ Pedido creado: " + pedido.getId());

        // 6. Agregar ítem
//        Mesa mesa2 = mesaService.obtenerPorId(9L);
//        SesionMesa sesion = sesionMesaService.obtenerSesionActivaPorMesa(mesa2.getId());
//
//        List<Pedido> pedidos2 = pedidoService.obtenerPorSesion(sesion.getId());
//        Pedido pedido2 = pedidos2.get(0);
//
//        Plato plato = platoService.buscarPorId(4L);
//        Plato plato2 = platoService.buscarPorId(3L);
//        Plato plato3 = platoService.buscarPorId(5L);
//        Plato plato4 = platoService.buscarPorId(1L);
//
//        itemPedidoService.agregarItem(pedido2.getId(), plato.getId(), 1, "Sin sal");
//        itemPedidoService.agregarItem(pedido2.getId(), plato2.getId(), 2, "");
//        itemPedidoService.agregarItem(pedido2.getId(), plato3.getId(), 3, "Para compartir");
//        itemPedidoService.agregarItem(pedido2.getId(), plato4.getId(), 1, "");
//        List<ItemPedido> items = itemPedidoService.obtenerPorPedido(pedido2.getId());
//        ItemPedido item = items.get(0);
//        System.out.println("✔ Ítem agregado al pedido: " + item.getId());

        // 7. Calcular subtotal
//        double subtotal = itemPedidoService.calcularSubtotal(item.getId());
//        System.out.println("✔ Subtotal del ítem: " + subtotal);

        // 8. Entregar ítem
//        itemPedidoService.iniciarPreparacion(item.getId());
//        itemPedidoService.marcarListo(item.getId());
//        itemPedidoService.entregarItem(item.getId());
//        System.out.println("✔ Ítem entregado.");

        // 9. Finalizar pedido
//        pedidoService.finalizarPedido(pedido.getId());
//        System.out.println("✔ Pedido finalizado.");

        // 10. Finalizar sesión
//        sesionMesaService.finalizarSesion(sesion.getId());
//        SesionMesa sesionFinalizada = sesionMesaService.buscarPorId(sesion.getId());
//        System.out.println("✔ Sesión finalizada: " + !sesionFinalizada.esActiva());
//
//        System.out.println("===== FIN DE PRUEBA =====");

//        ---------------------------------------------------
//        Mesa mesa = new Mesa(3, "qrtokenmesa3");
//        mesaService.crearMesa(mesa);
//        Empleado mozo = empleadoService.buscarPorEmail("messi@plattio.com");
//        Mesa mesa2 = mesaService.obtenerPorId(7L);
//        sesionMesaService.iniciarSesion(mesa2.getId(), mozo.getId(), "anonimo");
//        SesionMesa sesion1 = sesionMesaService.obtenerSesionActivaPorMesa(mesa2.getId());
//
//        pedidoService.crearPedido(sesion1.getId(), "principal");
//        List<Pedido> pedidos = pedidoService.obtenerPorSesion(sesion1.getId());
//        Pedido pedido = pedidos.get(0);
//        System.out.println("✔ Pedido creado: " + pedido.getId());

//        Mesa mesa2 = mesaService.obtenerPorId(9L);
//        SesionMesa sesion = sesionMesaService.obtenerSesionActivaPorMesa(mesa2.getId());
//
//        List<Pedido> pedidos2 = pedidoService.obtenerPorSesion(sesion.getId());
//        Pedido pedido2 = pedidos2.get(0);
//
//        Plato plato = platoService.buscarPorId(4L);
//        Plato plato2 = platoService.buscarPorId(10L);
//        Plato plato3 = platoService.buscarPorId(9L);
//        Plato plato4 = platoService.buscarPorId(8L);
//        Plato plato5 = platoService.buscarPorId(11L);
//        Plato plato6 = platoService.buscarPorId(2L);
//
//        itemPedidoService.agregarItem(pedido2.getId(), plato.getId(), 1, "Sin sal");
//        itemPedidoService.agregarItem(pedido2.getId(), plato2.getId(), 2, "");
//        itemPedidoService.agregarItem(pedido2.getId(), plato3.getId(), 3, "Para compartir");
//        itemPedidoService.agregarItem(pedido2.getId(), plato4.getId(), 1, "");
//        itemPedidoService.agregarItem(pedido2.getId(), plato5.getId(), 1, "");
//        itemPedidoService.agregarItem(pedido2.getId(), plato6.getId(), 1, "pure de batata");
//        List<ItemPedido> items = itemPedidoService.obtenerPorPedido(pedido2.getId());
//        ItemPedido item = items.get(0);
//        System.out.println("✔ Ítem agregado al pedido: " + item.getId());
    }
}
