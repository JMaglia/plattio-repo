package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.exceptions.MesaException;
import com.plattio.plattio_backend.modelo.Mesa;
import com.plattio.plattio_backend.service.MesaService;
import com.plattio.plattio_backend.views.MesaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mesas")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    @GetMapping
    public ResponseEntity<List<MesaView>> obtenerTodasLasMesas() throws MesaException {
        List<MesaView> views = mesaService.obtenerTodas().stream()
                .map(Mesa::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MesaView> obtenerMesaPorId(@PathVariable Long id) throws MesaException {
        return new ResponseEntity<>(mesaService.obtenerPorId(id).toView(), HttpStatus.OK);
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<MesaView> obtenerMesaPorNumero(@PathVariable Integer numero) throws MesaException {
        return new ResponseEntity<>(mesaService.obtenerPorNumero(numero).toView(), HttpStatus.OK);
    }

    @GetMapping("/qr/{qrToken}")
    public ResponseEntity<MesaView> obtenerMesaPorQr(@PathVariable String qrToken) throws MesaException {
        return new ResponseEntity<>(mesaService.obtenerPorQrToken(qrToken).toView(), HttpStatus.OK);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<MesaView>> obtenerMesasPorEstado(@PathVariable String estado) throws MesaException {
        List<MesaView> views = mesaService.obtenerPorEstado(estado).stream()
                .map(Mesa::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/{id}/libre")
    public ResponseEntity<Boolean> estaLibre(@PathVariable Long id) throws MesaException {
        return new ResponseEntity<>(mesaService.estaLibre(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/ocupada")
    public ResponseEntity<Boolean> estaOcupada(@PathVariable Long id) throws MesaException {
        return new ResponseEntity<>(mesaService.estaOcupada(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/sesion-activa")
    public ResponseEntity<Boolean> tieneSesionActiva(@PathVariable Long id) throws MesaException {
        return new ResponseEntity<>(mesaService.tieneSesionActiva(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> crearMesa(@RequestBody Mesa mesa) throws MesaException {
        mesaService.crearMesa(mesa);
        return new ResponseEntity<>("Mesa creada con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/{id}/ocupar")
    public ResponseEntity<String> ocuparMesa(@PathVariable Long id) throws MesaException {
        mesaService.ocuparMesa(id);
        return new ResponseEntity<>("Mesa ocupada con éxito", HttpStatus.OK);
    }

    @PostMapping("/{id}/liberar")
    public ResponseEntity<String> liberarMesa(@PathVariable Long id) throws MesaException {
        mesaService.liberarMesa(id);
        return new ResponseEntity<>("Mesa liberada con éxito", HttpStatus.OK);
    }

    @PostMapping("/{id}/estado/{nuevoEstado}")
    public ResponseEntity<String> cambiarEstadoMesa(@PathVariable Long id, @PathVariable String nuevoEstado) throws MesaException {
        mesaService.cambiarEstado(id, nuevoEstado);
        return new ResponseEntity<>("Estado de la mesa actualizado con éxito", HttpStatus.OK);
    }
}
