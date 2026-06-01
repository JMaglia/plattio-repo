package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.exceptions.*;
import com.plattio.plattio_backend.modelo.*;
import com.plattio.plattio_backend.service.*;
import com.plattio.plattio_backend.views.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
//@RequestMapping("/empleados")
public class controllerPlattio {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private ItemPedidoService itemPedidoService;

    @Autowired
    private MesaService mesaService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PlatoService platoService;

    @Autowired
    private SesionMesaService sesionMesaService;

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/")
    public String mensaje() {
        return """
                recetas culinarias
                """;
    }

    // Endpoint para probar excepción
    @GetMapping("/probar-excepcion/{email}")
    public Empleado probarExcepcion(@PathVariable String email) throws EmpleadoException {
        return empleadoService.buscarPorEmail(email);
    }

//    ENDPOINTS MESA --------------------------------------------------------------
//    GET
    @GetMapping("/mesas")
    public ResponseEntity<List<MesaView>> obtenerTodasLasMesas() throws MesaException {
        List<MesaView> views = mesaService.obtenerTodas().stream()
                .map(Mesa::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/mesas/{id}")
    public ResponseEntity<MesaView> obtenerMesaPorId(@PathVariable Long id) throws MesaException {
        return new ResponseEntity<>(mesaService.obtenerPorId(id).toView(), HttpStatus.OK);
    }

    @GetMapping("/mesas/numero/{numero}")
    public ResponseEntity<MesaView> obtenerMesaPorNumero(@PathVariable Integer numero) throws MesaException {
        return new ResponseEntity<>(mesaService.obtenerPorNumero(numero).toView(), HttpStatus.OK);
    }

    @GetMapping("/mesas/qr/{qrToken}")
    public ResponseEntity<MesaView> obtenerMesaPorQr(@PathVariable String qrToken) throws MesaException {
        return new ResponseEntity<>(mesaService.obtenerPorQrToken(qrToken).toView(), HttpStatus.OK);
    }

    @GetMapping("/mesas/estado/{estado}")
    public ResponseEntity<List<MesaView>> obtenerMesasPorEstado(@PathVariable String estado) throws MesaException {
        List<MesaView> views = mesaService.obtenerPorEstado(estado).stream()
                .map(Mesa::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/mesas/{id}/libre")
    public ResponseEntity<Boolean> estaLibre(@PathVariable Long id) throws MesaException {
        return new ResponseEntity<>(mesaService.estaLibre(id), HttpStatus.OK);
    }

    @GetMapping("/mesas/{id}/ocupada")
    public ResponseEntity<Boolean> estaOcupada(@PathVariable Long id) throws MesaException {
        return new ResponseEntity<>(mesaService.estaOcupada(id), HttpStatus.OK);
    }

    @GetMapping("/mesas/{id}/sesion-activa")
    public ResponseEntity<Boolean> tieneSesionActiva(@PathVariable Long id) throws MesaException {
        return new ResponseEntity<>(mesaService.tieneSesionActiva(id), HttpStatus.OK);
    }

    //POST
    @PostMapping("/mesas")
    public ResponseEntity<String> crearMesa(@RequestBody Mesa mesa) throws MesaException {
        mesaService.crearMesa(mesa);
        return new ResponseEntity<>("Mesa creada con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/mesas/{id}/ocupar")
    public ResponseEntity<String> ocuparMesa(@PathVariable Long id) throws MesaException {
        mesaService.ocuparMesa(id);
        return new ResponseEntity<>("Mesa ocupada con éxito", HttpStatus.OK);
    }

    @PostMapping("/mesas/{id}/liberar")
    public ResponseEntity<String> liberarMesa(@PathVariable Long id) throws MesaException {
        mesaService.liberarMesa(id);
        return new ResponseEntity<>("Mesa liberada con éxito", HttpStatus.OK);
    }

    @PostMapping("/mesas/{id}/estado/{nuevoEstado}")
    public ResponseEntity<String> cambiarEstadoMesa(@PathVariable Long id, @PathVariable String nuevoEstado) throws MesaException {
        mesaService.cambiarEstado(id, nuevoEstado);
        return new ResponseEntity<>("Estado de la mesa actualizado con éxito", HttpStatus.OK);
    }

    //    PEDIDO-------------------------
    //    GET ----------------------------------
    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoView>> obtenerTodosLosPedidos() throws PedidoException {
        List<PedidoView> views = pedidoService.obtenerTodos().stream()
                .map(Pedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoView> obtenerPedidoPorId(@PathVariable Long id) throws PedidoException {
        Pedido pedido = pedidoService.obtenerPorId(id);
        return new ResponseEntity<>(pedido.toView(), HttpStatus.OK);
    }

    @GetMapping("/pedidos/sesion/{sesionId}")
    public ResponseEntity<List<PedidoView>> obtenerPedidosPorSesion(@PathVariable Long sesionId) throws PedidoException {
        List<PedidoView> views = pedidoService.obtenerPorSesion(sesionId).stream()
                .map(Pedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }


    @GetMapping("/pedidos/estado/{estado}")
    public ResponseEntity<List<PedidoView>> obtenerPedidosPorEstado(@PathVariable String estado) throws PedidoException {
        List<Pedido> pedidos = pedidoService.obtenerPorEstado(estado);
        List<PedidoView> views = pedidos.stream()
                .map(Pedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/pedidos/{id}/total")
    public ResponseEntity<Double> calcularTotalDelPedido(@PathVariable Long id) throws PedidoException {
        return new ResponseEntity<>(pedidoService.calcularTotalPedido(id), HttpStatus.OK);
    }

    //    POST-------------------------
    @PostMapping("/pedidos/{sesionId}/{categoria}")
    public ResponseEntity<String> crearPedido(@PathVariable Long sesionId, @PathVariable String categoria) throws PedidoException {
        pedidoService.crearPedido(sesionId, categoria);
        return new ResponseEntity<>("Pedido creado con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/pedidos/{id}/iniciar")
    public ResponseEntity<String> iniciarPreparacion(@PathVariable Long id) throws PedidoException {
        pedidoService.iniciarPreparacion(id);
        return new ResponseEntity<>("Preparación del pedido iniciada", HttpStatus.OK);
    }

    @PostMapping("/pedidos/{id}/cambiarEstado/{estado}")
    public ResponseEntity<PedidoView> CambiarEstado(@PathVariable Long id, @PathVariable String estado) throws PedidoException {
        Pedido pedido = pedidoService.cambiarEstado(id, estado);
        return ResponseEntity.ok(pedido.toView());
    }

    @PostMapping("/pedidos/{id}/finalizar")
    public ResponseEntity<String> finalizarPedido(@PathVariable Long id) throws PedidoException {
        pedidoService.finalizarPedido(id);
        return new ResponseEntity<>("Pedido finalizado con éxito", HttpStatus.OK);
    }

    @PostMapping("/pedidos/{id}/cancelar")
    public ResponseEntity<String> cancelarPedido(@PathVariable Long id) throws PedidoException {
        pedidoService.cancelarPedido(id);
        return new ResponseEntity<>("Pedido cancelado con éxito", HttpStatus.OK);
    }

    @GetMapping("/pedidos/activos/{mozoId}")
    public ResponseEntity<List<PedidoView>> obtenerPedidosPendientesMozo(@PathVariable Long mozoId) throws PedidoException {
        List<Pedido> pedidos = pedidoService.buscarPendientesPorMozo(mozoId);
        List<PedidoView> views = pedidos.stream()
                .map(Pedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/pedidos/listos/mozo/{mozoId}")
    public ResponseEntity<List<PedidoView>> obtenerPedidosListosPorMozo(@PathVariable Long mozoId) throws PedidoException {
        List<PedidoView> views = pedidoService.obtenerPedidosListosPorMozo(mozoId)
                .stream()
                .map(Pedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }


    //    TIEM PEDIDO---------------------------------
    //    GET --------------------------------------------------------
    @GetMapping("/items/pedido/{pedidoId}")
    public ResponseEntity<List<ItemPedidoView>> obtenerItemsPorPedido(@PathVariable Long pedidoId) throws ItemPedidoException {
        List<ItemPedidoView> views = itemPedidoService.obtenerPorPedido(pedidoId).stream()
                .map(ItemPedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/items/estado/{estado}")
    public ResponseEntity<List<ItemPedidoView>> obtenerItemsPorEstado(@PathVariable String estado) {
        List<ItemPedidoView> views = itemPedidoService.obtenerPorEstado(estado).stream()
                .map(ItemPedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/items/activos")
    public ResponseEntity<List<ItemPedidoView>> obtenerItemsActivos() {
        List<ItemPedidoView> views = itemPedidoService.obtenerActivos().stream()
                .map(ItemPedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/items/{itemId}/subtotal")
    public ResponseEntity<Double> calcularSubtotalItem(@PathVariable Long itemId) throws ItemPedidoException {
        return new ResponseEntity<>(itemPedidoService.calcularSubtotal(itemId), HttpStatus.OK);
    }

    //    POST
    @PostMapping("/items/{pedidoId}/{platoId}/{cantidad}/{nota}")
    public ResponseEntity<String> agregarItemAPedido(
            @PathVariable Long pedidoId,
            @PathVariable Long platoId,
            @PathVariable int cantidad,
            @PathVariable String nota) throws ItemPedidoException {

        itemPedidoService.agregarItem(pedidoId, platoId, cantidad, nota);
        return new ResponseEntity<>("Ítem agregado al pedido con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/items/{itemId}/iniciar")
    public ResponseEntity<String> iniciarPreparacionItem(@PathVariable Long itemId) throws ItemPedidoException {
        itemPedidoService.iniciarPreparacion(itemId);
        return new ResponseEntity<>("Preparación del ítem iniciada", HttpStatus.OK);
    }

    @PostMapping("/items/{itemId}/listo")
    public ResponseEntity<String> marcarItemListo(@PathVariable Long itemId) throws ItemPedidoException {
        itemPedidoService.marcarListo(itemId);
        return new ResponseEntity<>("Ítem marcado como listo", HttpStatus.OK);
    }

    @PostMapping("/items/{itemId}/entregar")
    public ResponseEntity<String> entregarItem(@PathVariable Long itemId) throws ItemPedidoException {
        itemPedidoService.entregarItem(itemId);
        return new ResponseEntity<>("Ítem entregado", HttpStatus.OK);
    }

    @PostMapping("/items/{itemId}/cancelar")
    public ResponseEntity<String> cancelarItem(@PathVariable Long itemId) throws ItemPedidoException {
        itemPedidoService.cancelarItem(itemId);
        return new ResponseEntity<>("Ítem cancelado", HttpStatus.OK);
    }

    //SESION MESA:
    @GetMapping("/sesiones")
    public ResponseEntity<List<SesionMesaView>> obtenerTodasLasSesiones() throws SesionMesaException {
        List<SesionMesaView> views = sesionMesaService.obtenerTodas().stream()
                .map(SesionMesa::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/sesiones/activas")
    public ResponseEntity<List<SesionMesaView>> obtenerSesionesActivas() throws SesionMesaException {
        List<SesionMesaView> views = sesionMesaService.obtenerSesionesActivas().stream()
                .map(SesionMesa::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/sesiones/{id}")
    public ResponseEntity<SesionMesaView> obtenerSesionPorId(@PathVariable Long id) throws SesionMesaException {
        return new ResponseEntity<>(sesionMesaService.buscarPorId(id).toView(), HttpStatus.OK);
    }

    @GetMapping("/sesiones/numeroMesa/{numeroMesa}/activa")
    public ResponseEntity<SesionMesaView> obtenerSesionActivaNumMesa(@PathVariable Integer numeroMesa) throws MesaException, SesionMesaException {
        return new ResponseEntity<>(mesaService.obtenerSesionActivaNumMesa(numeroMesa).toView(), HttpStatus.OK);
    }

    @GetMapping("/sesiones/mesa/{mesaId}/activa")
    public ResponseEntity<SesionMesaView> obtenerSesionActivaPorMesa(@PathVariable Long mesaId) throws SesionMesaException {
        return new ResponseEntity<>(sesionMesaService.obtenerSesionActivaPorMesa(mesaId).toView(), HttpStatus.OK);
    }

    @GetMapping("/sesiones/mozo/{mozoId}/activas")
    public ResponseEntity<List<SesionMesaView>> obtenerSesionesActivasPorMozo(@PathVariable Long mozoId) throws SesionMesaException {
        List<SesionMesaView> views = sesionMesaService.obtenerSesionesActivasPorMozo(mozoId).stream()
                .map(SesionMesa::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @PostMapping("/sesiones/iniciar/{mesaId}/{tipoComensal}")
    public ResponseEntity<String> iniciarSesionSinMozo(@PathVariable Long mesaId, @PathVariable String tipoComensal) throws SesionMesaException, MesaException {
        sesionMesaService.iniciarSesion(mesaId, null, tipoComensal);
        return new ResponseEntity<>("Sesión iniciada sin mozo", HttpStatus.CREATED);
    }

    @PostMapping("/sesiones/iniciar/{mesaId}/{mozoId}/{tipoComensal}")
    public ResponseEntity<String> iniciarSesionConMozo(@PathVariable Long mesaId, @PathVariable Long mozoId, @PathVariable String tipoComensal) throws SesionMesaException, MesaException {
        sesionMesaService.iniciarSesion(mesaId, mozoId, tipoComensal);
        return new ResponseEntity<>("Sesión iniciada con mozo", HttpStatus.CREATED);
    }

    @PostMapping("/sesiones/{sesionId}/finalizar")
    public ResponseEntity<String> finalizarSesion(@PathVariable Long sesionId) throws SesionMesaException {
        sesionMesaService.finalizarSesion(sesionId);
        return new ResponseEntity<>("Sesión finalizada con éxito", HttpStatus.OK);
    }

    @PostMapping("/sesiones/{sesionId}/cerrarSiSinPedidos")
    public ResponseEntity<String> cerrarSesionSiNoHayPedidos(@PathVariable Long sesionId) throws SesionMesaException {
        sesionMesaService.cerrarSesionSiNoHayPedidos(sesionId);
        return new ResponseEntity<>("Sesión cerrada si no tenía pedidos", HttpStatus.OK);
    }

    @PostMapping("/sesiones/{sesionId}/reasignarMozo/{nuevoMozoId}")
    public ResponseEntity<String> reasignarMozoASesion(@PathVariable Long sesionId, @PathVariable Long nuevoMozoId) throws SesionMesaException {
        sesionMesaService.reasignarMozo(sesionId, nuevoMozoId);
        return new ResponseEntity<>("Mozo reasignado a la sesión con éxito", HttpStatus.OK);
    }

    //    PLATO SERVICE

    @GetMapping("/platos")
    public ResponseEntity<List<PlatoView>> obtenerTodosLosPlatos() throws PlatoException {
        List<PlatoView> views = platoService.obtenerTodos().stream()
                .map(Plato::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/platos/activos")
    public ResponseEntity<List<PlatoView>> obtenerTodosLosPlatosActivos() throws PlatoException {
        List<PlatoView> views = platoService.obtenerTodosLosActivos().stream()
                .map(Plato::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/platos/{id}")
    public ResponseEntity<PlatoView> obtenerPlatoPorId(@PathVariable Long id) throws PlatoException {
        return new ResponseEntity<>(platoService.buscarPorId(id).toView(), HttpStatus.OK);
    }

    @GetMapping("/platos/categoria/{categoria}")
    public ResponseEntity<List<PlatoView>> obtenerPlatosPorCategoria(@PathVariable String categoria) throws PlatoException {
        List<PlatoView> views = platoService.buscarPorCategoria(categoria).stream()
                .map(Plato::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/platos/nombre/{nombreParcial}")
    public ResponseEntity<List<PlatoView>> buscarPlatosPorNombre(@PathVariable String nombreParcial) throws PlatoException {
        List<PlatoView> views = platoService.buscarPorNombre(nombreParcial).stream()
                .map(Plato::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/platos/precioMenorA/{max}")
    public ResponseEntity<List<PlatoView>> buscarPlatosPorPrecio(@PathVariable BigDecimal max) throws PlatoException {
        List<PlatoView> views = platoService.buscarPorPrecioMenorA(max).stream()
                .map(Plato::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/platos/rapidos/{minutosMax}")
    public ResponseEntity<List<PlatoView>> buscarPlatosRapidos(@PathVariable int minutosMax) throws PlatoException {
        List<PlatoView> views = platoService.buscarPlatosRapidos(minutosMax).stream()
                .map(Plato::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @PostMapping("/platos")
    public ResponseEntity<String> crearPlato(@RequestBody Plato plato) throws PlatoException {
        platoService.crearPlato(plato);
        return new ResponseEntity<>("Plato creado con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/platos/{id}/actualizar")
    public ResponseEntity<String> actualizarPlato(
            @PathVariable Long id,
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam BigDecimal precio,
            @RequestParam String categoria,
            @RequestParam Integer tiempoEstimado) throws PlatoException {

        platoService.actualizarPlato(id, nombre, descripcion, precio, categoria, tiempoEstimado);
        return new ResponseEntity<>("Plato actualizado con éxito", HttpStatus.OK);
    }

/*    @PostMapping("/platos/{id}/cambiarPrecio")
    public ResponseEntity<String> cambiarPrecioPlato(
            @PathVariable Long id,
            @RequestParam BigDecimal nuevoPrecio) throws PlatoException {

        platoService.cambiarPrecio(id, nuevoPrecio);
        return new ResponseEntity<>("Precio del plato actualizado con éxito", HttpStatus.OK);
    }*/

    @PostMapping("/platos/{id}/cambiarPrecio/{nuevoPrecio}")
    public ResponseEntity<String> cambiarPrecioPlato(
            @PathVariable Long id,
            @PathVariable BigDecimal nuevoPrecio) throws PlatoException {

        platoService.cambiarPrecio(id, nuevoPrecio);
        return new ResponseEntity<>("Precio del plato actualizado con éxito", HttpStatus.OK);
    }

    @PostMapping("/platos/{id}/toggleActivo")
    public ResponseEntity<String> toggleActivoPlato(@PathVariable Long id) throws PlatoException {
        platoService.toggleActivoEnCarta(id);
        return new ResponseEntity<>("Estado 'activoEnCarta' actualizado correctamente", HttpStatus.OK);
    }


    @DeleteMapping("/platos/{id}")
    public ResponseEntity<String> eliminarPlato(@PathVariable Long id) throws PlatoException {
        platoService.eliminar(id);
        return new ResponseEntity<>("Plato eliminado con éxito", HttpStatus.OK);
    }

    //EMPLEADO SERVICE
    @GetMapping("/empleados")
    public ResponseEntity<List<EmpleadoView>> obtenerTodosLosEmpleados() throws EmpleadoException {
        List<EmpleadoView> views = empleadoService.obtenerTodos().stream()
                .map(Empleado::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/empleados/{id}")
    public ResponseEntity<EmpleadoView> obtenerEmpleadoPorId(@PathVariable Long id) throws EmpleadoException {
        return new ResponseEntity<>(empleadoService.buscarPorId(id).toView(), HttpStatus.OK);
    }

    @GetMapping("/empleados/email/{email}")
    public ResponseEntity<EmpleadoView> obtenerEmpleadoPorEmail(@PathVariable String email) throws EmpleadoException {
        return new ResponseEntity<>(empleadoService.buscarPorEmail(email).toView(), HttpStatus.OK);
    }

    @GetMapping("/empleados/nombre/{nombreParcial}")
    public ResponseEntity<List<EmpleadoView>> buscarEmpleadosPorNombre(@PathVariable String nombreParcial) throws EmpleadoException {
        List<EmpleadoView> views = empleadoService.buscarPorNombre(nombreParcial).stream()
                .map(Empleado::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/empleados/rol/{rol}")
    public ResponseEntity<List<EmpleadoView>> buscarEmpleadosPorRol(@PathVariable String rol) throws EmpleadoException {
        List<EmpleadoView> views = empleadoService.buscarPorRol(rol).stream()
                .map(Empleado::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @PostMapping("/empleados")
    public ResponseEntity<String> registrarEmpleado(@RequestBody Empleado nuevo) throws EmpleadoException {
        empleadoService.registrarEmpleado(nuevo);
        return new ResponseEntity<>("Empleado registrado con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/empleados/login/{email}/{password}")
    public ResponseEntity<EmpleadoView> loginEmpleado(@PathVariable String email, @PathVariable String password) throws EmpleadoException {
        Empleado empleado = empleadoService.login(email, password);
        return new ResponseEntity<>(empleado.toView(), HttpStatus.OK);
    }

/*    @PostMapping("/empleados/{id}/actualizar")
    public ResponseEntity<String> actualizarDatosEmpleado(
            @PathVariable Long id,
            @RequestParam String nuevoNombre,
            @RequestParam String nuevoEmail) throws EmpleadoException {

        empleadoService.actualizarDatos(id, nuevoNombre, nuevoEmail);
        return new ResponseEntity<>("Datos del empleado actualizados con éxito", HttpStatus.OK);
    }*/

    @PostMapping("/empleados/{id}/actualizar/{nuevoNombre}/{nuevoEmail}")
    public ResponseEntity<String> actualizarDatosEmpleado(
            @PathVariable Long id,
            @PathVariable String nuevoNombre,
            @PathVariable String nuevoEmail) throws EmpleadoException {

        empleadoService.actualizarDatos(id, nuevoNombre, nuevoEmail);
        return new ResponseEntity<>("Datos del empleado actualizados con éxito", HttpStatus.OK);
    }

    @PostMapping("/empleados/{id}/cambiarRol/{nuevoRol}")
    public ResponseEntity<String> cambiarRolEmpleado(
            @PathVariable Long id,
            @PathVariable String nuevoRol) throws EmpleadoException {

        empleadoService.cambiarRol(id, nuevoRol);
        return new ResponseEntity<>("Rol del empleado actualizado con éxito", HttpStatus.OK);
    }

    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<String> eliminarEmpleado(@PathVariable Long id) throws EmpleadoException {
        empleadoService.eliminar(id);
        return new ResponseEntity<>("Empleado eliminado con éxito", HttpStatus.OK);
    }

    //    notificacion---------------------------------

    @GetMapping("/notificaciones")
    public ResponseEntity<List<NotificacionView>> obtenerTodasLasNotificaciones() {
        List<NotificacionView> views = notificacionService.obtenerTodas().stream()
                .map(Notificacion::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @PostMapping("/notificaciones/{mensaje}/{tipo}/{mozoId}/{sesionId}")
    public ResponseEntity<NotificacionView> crearNotificacion(
            @PathVariable String mensaje,
            @PathVariable String tipo,
            @PathVariable Long mozoId,
            @PathVariable Long sesionId
    ) throws EmpleadoException, SesionMesaException {
        Notificacion noti = notificacionService.crearNotificacion(mensaje, tipo, mozoId, sesionId);
        return new ResponseEntity<>(noti.toView(), HttpStatus.CREATED);
    }

    @PostMapping("/notificaciones/pedido/{mensaje}/{tipo}/{mesaNum}/{idPedido}")
    public ResponseEntity<NotificacionView> crearNotificacion2(
            @PathVariable String mensaje,
            @PathVariable String tipo,
            @PathVariable Integer mesaNum,
            @PathVariable Long idPedido
    ) throws EmpleadoException, SesionMesaException, PedidoException {
        Notificacion noti = notificacionService.crearNotificacion2(mensaje, tipo, mesaNum, idPedido);
        return new ResponseEntity<>(noti.toView(), HttpStatus.CREATED);
    }

    @GetMapping("/notificaciones/mozo/{mozoId}/estado/{estado}")
    public ResponseEntity<List<NotificacionView>> obtenerNotificacionesPorMozoYEstado(
            @PathVariable Long mozoId,
            @PathVariable String estado
    ) throws NotificacionException {
        List<NotificacionView> views = notificacionService.obtenerPorMozoYEstado(mozoId, estado).stream()
                .map(Notificacion::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @PostMapping("/notificaciones/{id}/toggleEstado")
    public ResponseEntity<String> toggleEstadoNotificacion(@PathVariable Long id) throws NotificacionException {
        notificacionService.toggleEstado(id);
        return new ResponseEntity<>("Estado de notificación actualizado", HttpStatus.OK);
    }

    @PostMapping("/pedido/{pedidoId}/completar")
    public ResponseEntity<Void> completarPorPedido(@PathVariable Long pedidoId) {
        notificacionService.completarNotificacionesPorPedido(pedidoId);
        return ResponseEntity.ok().build();
    }

}
