package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.dto.request.CambiarEstadoMesaRequest;
import com.plattio.plattio_backend.dto.request.CrearMesaRequest;
import com.plattio.plattio_backend.mapper.MesaMapper;
import com.plattio.plattio_backend.service.MesaService;
import com.plattio.plattio_backend.views.MesaView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @GetMapping
    public ResponseEntity<List<MesaView>> obtenerTodasLasMesas() {
        List<MesaView> views = mesaService.obtenerTodas().stream()
                .map(MesaMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MesaView> obtenerMesaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(MesaMapper.toView(mesaService.obtenerPorId(id)));
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<MesaView> obtenerMesaPorNumero(@PathVariable Integer numero) {
        return ResponseEntity.ok(MesaMapper.toView(mesaService.obtenerPorNumero(numero)));
    }

    @GetMapping("/qr/{qrToken}")
    public ResponseEntity<MesaView> obtenerMesaPorQr(@PathVariable String qrToken) {
        return ResponseEntity.ok(MesaMapper.toView(mesaService.obtenerPorQrToken(qrToken)));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<MesaView>> obtenerMesasPorEstado(@PathVariable String estado) {
        List<MesaView> views = mesaService.obtenerPorEstado(estado).stream()
                .map(MesaMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/{id}/libre")
    public ResponseEntity<Boolean> estaLibre(@PathVariable Long id) {
        return ResponseEntity.ok(mesaService.estaLibre(id));
    }

    @GetMapping("/{id}/ocupada")
    public ResponseEntity<Boolean> estaOcupada(@PathVariable Long id) {
        return ResponseEntity.ok(mesaService.estaOcupada(id));
    }

    @GetMapping("/{id}/sesion-activa")
    public ResponseEntity<Boolean> tieneSesionActiva(@PathVariable Long id) {
        return ResponseEntity.ok(mesaService.tieneSesionActiva(id));
    }

    @PostMapping
    public ResponseEntity<String> crearMesa(@Valid @RequestBody CrearMesaRequest request) {
        mesaService.crearMesa(request);
        return new ResponseEntity<>("Mesa creada con éxito", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/ocupar")
    public ResponseEntity<String> ocuparMesa(@PathVariable Long id) {
        mesaService.ocuparMesa(id);
        return ResponseEntity.ok("Mesa ocupada con éxito");
    }

    @PatchMapping("/{id}/liberar")
    public ResponseEntity<String> liberarMesa(@PathVariable Long id) {
        mesaService.liberarMesa(id);
        return ResponseEntity.ok("Mesa liberada con éxito");
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstadoMesa(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoMesaRequest request) {
        mesaService.cambiarEstado(id, request.estado());
        return ResponseEntity.ok("Estado de la mesa actualizado con éxito");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMesa(@PathVariable Long id) {
        mesaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
