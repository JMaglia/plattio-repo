package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.dto.request.ActualizarPlatoRequest;
import com.plattio.plattio_backend.dto.request.CambiarPrecioRequest;
import com.plattio.plattio_backend.dto.request.CrearPlatoRequest;
import com.plattio.plattio_backend.mapper.PlatoMapper;
import com.plattio.plattio_backend.service.PlatoService;
import com.plattio.plattio_backend.views.PlatoView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/platos")
public class PlatoController {

    private final PlatoService platoService;

    public PlatoController(PlatoService platoService) {
        this.platoService = platoService;
    }

    @GetMapping
    public ResponseEntity<List<PlatoView>> obtenerTodosLosPlatos() {
        List<PlatoView> views = platoService.obtenerTodos().stream()
                .map(PlatoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<PlatoView>> obtenerTodosLosPlatosActivos() {
        List<PlatoView> views = platoService.obtenerTodosLosActivos().stream()
                .map(PlatoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatoView> obtenerPlatoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PlatoMapper.toView(platoService.buscarPorId(id)));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<PlatoView>> obtenerPlatosPorCategoria(@PathVariable String categoria) {
        List<PlatoView> views = platoService.buscarPorCategoria(categoria).stream()
                .map(PlatoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/nombre/{nombreParcial}")
    public ResponseEntity<List<PlatoView>> buscarPlatosPorNombre(@PathVariable String nombreParcial) {
        List<PlatoView> views = platoService.buscarPorNombre(nombreParcial).stream()
                .map(PlatoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/precioMenorA/{max}")
    public ResponseEntity<List<PlatoView>> buscarPlatosPorPrecio(@PathVariable BigDecimal max) {
        List<PlatoView> views = platoService.buscarPorPrecioMenorA(max).stream()
                .map(PlatoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/rapidos/{minutosMax}")
    public ResponseEntity<List<PlatoView>> buscarPlatosRapidos(@PathVariable int minutosMax) {
        List<PlatoView> views = platoService.buscarPlatosRapidos(minutosMax).stream()
                .map(PlatoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @PostMapping
    public ResponseEntity<String> crearPlato(@Valid @RequestBody CrearPlatoRequest request) {
        platoService.crearPlato(request);
        return new ResponseEntity<>("Plato creado con éxito", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarPlato(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarPlatoRequest request) {
        platoService.actualizarPlato(id, request);
        return ResponseEntity.ok("Plato actualizado con éxito");
    }

    @PatchMapping("/{id}/precio")
    public ResponseEntity<String> cambiarPrecioPlato(
            @PathVariable Long id,
            @Valid @RequestBody CambiarPrecioRequest request) {
        platoService.cambiarPrecio(id, request.precio());
        return ResponseEntity.ok("Precio del plato actualizado con éxito");
    }

    @PatchMapping("/{id}/activo")
    public ResponseEntity<String> toggleActivoPlato(@PathVariable Long id) {
        platoService.toggleActivoEnCarta(id);
        return ResponseEntity.ok("Estado 'activoEnCarta' actualizado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPlato(@PathVariable Long id) {
        platoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
