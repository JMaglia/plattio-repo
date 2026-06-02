package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.exceptions.MesaException;
import com.plattio.plattio_backend.exceptions.SesionMesaException;
import com.plattio.plattio_backend.modelo.SesionMesa;
import com.plattio.plattio_backend.service.MesaService;
import com.plattio.plattio_backend.service.SesionMesaService;
import com.plattio.plattio_backend.views.SesionMesaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sesiones")
public class SesionMesaController {

    @Autowired
    private SesionMesaService sesionMesaService;

    @Autowired
    private MesaService mesaService;

    @GetMapping
    public ResponseEntity<List<SesionMesaView>> obtenerTodasLasSesiones() throws SesionMesaException {
        List<SesionMesaView> views = sesionMesaService.obtenerTodas().stream()
                .map(SesionMesa::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<SesionMesaView>> obtenerSesionesActivas() throws SesionMesaException {
        List<SesionMesaView> views = sesionMesaService.obtenerSesionesActivas().stream()
                .map(SesionMesa::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SesionMesaView> obtenerSesionPorId(@PathVariable Long id) throws SesionMesaException {
        return new ResponseEntity<>(sesionMesaService.buscarPorId(id).toView(), HttpStatus.OK);
    }

    @GetMapping("/numeroMesa/{numeroMesa}/activa")
    public ResponseEntity<SesionMesaView> obtenerSesionActivaNumMesa(@PathVariable Integer numeroMesa) throws MesaException, SesionMesaException {
        return new ResponseEntity<>(mesaService.obtenerSesionActivaNumMesa(numeroMesa).toView(), HttpStatus.OK);
    }

    @GetMapping("/mesa/{mesaId}/activa")
    public ResponseEntity<SesionMesaView> obtenerSesionActivaPorMesa(@PathVariable Long mesaId) throws SesionMesaException {
        return new ResponseEntity<>(sesionMesaService.obtenerSesionActivaPorMesa(mesaId).toView(), HttpStatus.OK);
    }

    @GetMapping("/mozo/{mozoId}/activas")
    public ResponseEntity<List<SesionMesaView>> obtenerSesionesActivasPorMozo(@PathVariable Long mozoId) throws SesionMesaException {
        List<SesionMesaView> views = sesionMesaService.obtenerSesionesActivasPorMozo(mozoId).stream()
                .map(SesionMesa::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @PostMapping("/iniciar/{mesaId}/{tipoComensal}")
    public ResponseEntity<String> iniciarSesionSinMozo(@PathVariable Long mesaId, @PathVariable String tipoComensal) throws SesionMesaException, MesaException {
        sesionMesaService.iniciarSesion(mesaId, null, tipoComensal);
        return new ResponseEntity<>("Sesión iniciada sin mozo", HttpStatus.CREATED);
    }

    @PostMapping("/iniciar/{mesaId}/{mozoId}/{tipoComensal}")
    public ResponseEntity<String> iniciarSesionConMozo(@PathVariable Long mesaId, @PathVariable Long mozoId, @PathVariable String tipoComensal) throws SesionMesaException, MesaException {
        sesionMesaService.iniciarSesion(mesaId, mozoId, tipoComensal);
        return new ResponseEntity<>("Sesión iniciada con mozo", HttpStatus.CREATED);
    }

    @PostMapping("/{sesionId}/finalizar")
    public ResponseEntity<String> finalizarSesion(@PathVariable Long sesionId) throws SesionMesaException {
        sesionMesaService.finalizarSesion(sesionId);
        return new ResponseEntity<>("Sesión finalizada con éxito", HttpStatus.OK);
    }

    @PostMapping("/{sesionId}/cerrarSiSinPedidos")
    public ResponseEntity<String> cerrarSesionSiNoHayPedidos(@PathVariable Long sesionId) throws SesionMesaException {
        sesionMesaService.cerrarSesionSiNoHayPedidos(sesionId);
        return new ResponseEntity<>("Sesión cerrada si no tenía pedidos", HttpStatus.OK);
    }

    @PostMapping("/{sesionId}/reasignarMozo/{nuevoMozoId}")
    public ResponseEntity<String> reasignarMozoASesion(@PathVariable Long sesionId, @PathVariable Long nuevoMozoId) throws SesionMesaException {
        sesionMesaService.reasignarMozo(sesionId, nuevoMozoId);
        return new ResponseEntity<>("Mozo reasignado a la sesión con éxito", HttpStatus.OK);
    }
}
