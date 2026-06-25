package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.dto.request.IniciarSesionRequest;
import com.plattio.plattio_backend.dto.request.ReasignarMozoRequest;
import com.plattio.plattio_backend.mapper.SesionMesaMapper;
import com.plattio.plattio_backend.service.SesionMesaService;
import com.plattio.plattio_backend.views.SesionMesaView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sesiones")
public class SesionMesaController {

    private final SesionMesaService sesionMesaService;

    public SesionMesaController(SesionMesaService sesionMesaService) {
        this.sesionMesaService = sesionMesaService;
    }

    @GetMapping
    public ResponseEntity<List<SesionMesaView>> obtenerTodasLasSesiones() {
        List<SesionMesaView> views = sesionMesaService.obtenerTodas().stream()
                .map(SesionMesaMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<SesionMesaView>> obtenerSesionesActivas() {
        List<SesionMesaView> views = sesionMesaService.obtenerSesionesActivas().stream()
                .map(SesionMesaMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SesionMesaView> obtenerSesionPorId(@PathVariable Long id) {
        return ResponseEntity.ok(SesionMesaMapper.toView(sesionMesaService.buscarPorId(id)));
    }

    @GetMapping("/numeroMesa/{numeroMesa}/activa")
    public ResponseEntity<SesionMesaView> obtenerSesionActivaPorNumeroMesa(@PathVariable Integer numeroMesa) {
        return ResponseEntity.ok(SesionMesaMapper.toView(sesionMesaService.obtenerSesionActivaPorNumeroMesa(numeroMesa)));
    }

    @GetMapping("/mesa/{mesaId}/activa")
    public ResponseEntity<SesionMesaView> obtenerSesionActivaPorMesa(@PathVariable Long mesaId) {
        return ResponseEntity.ok(SesionMesaMapper.toView(sesionMesaService.obtenerSesionActivaPorMesa(mesaId)));
    }

    @GetMapping("/mozo/{mozoId}/activas")
    public ResponseEntity<List<SesionMesaView>> obtenerSesionesActivasPorMozo(@PathVariable Long mozoId) {
        List<SesionMesaView> views = sesionMesaService.obtenerSesionesActivasPorMozo(mozoId).stream()
                .map(SesionMesaMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @PostMapping("/iniciar")
    public ResponseEntity<Void> iniciarSesion(@RequestBody IniciarSesionRequest request) {
        sesionMesaService.iniciarSesion(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{sesionId}/finalizar")
    public ResponseEntity<Void> finalizarSesion(@PathVariable Long sesionId) {
        sesionMesaService.finalizarSesion(sesionId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{sesionId}/cerrar")
    public ResponseEntity<Void> cerrarSesionSiNoHayPedidos(@PathVariable Long sesionId) {
        sesionMesaService.cerrarSesionSiNoHayPedidos(sesionId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{sesionId}/mozo")
    public ResponseEntity<Void> reasignarMozo(@PathVariable Long sesionId, @RequestBody ReasignarMozoRequest request) {
        sesionMesaService.reasignarMozo(sesionId, request);
        return ResponseEntity.ok().build();
    }
}
