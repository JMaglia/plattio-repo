package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.exceptions.PlatoException;
import com.plattio.plattio_backend.modelo.Plato;
import com.plattio.plattio_backend.service.PlatoService;
import com.plattio.plattio_backend.views.PlatoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/platos")
public class PlatoController {

    @Autowired
    private PlatoService platoService;

    @GetMapping
    public ResponseEntity<List<PlatoView>> obtenerTodosLosPlatos() throws PlatoException {
        List<PlatoView> views = platoService.obtenerTodos().stream()
                .map(Plato::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<PlatoView>> obtenerTodosLosPlatosActivos() throws PlatoException {
        List<PlatoView> views = platoService.obtenerTodosLosActivos().stream()
                .map(Plato::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatoView> obtenerPlatoPorId(@PathVariable Long id) throws PlatoException {
        return new ResponseEntity<>(platoService.buscarPorId(id).toView(), HttpStatus.OK);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<PlatoView>> obtenerPlatosPorCategoria(@PathVariable String categoria) throws PlatoException {
        List<PlatoView> views = platoService.buscarPorCategoria(categoria).stream()
                .map(Plato::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombreParcial}")
    public ResponseEntity<List<PlatoView>> buscarPlatosPorNombre(@PathVariable String nombreParcial) throws PlatoException {
        List<PlatoView> views = platoService.buscarPorNombre(nombreParcial).stream()
                .map(Plato::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/precioMenorA/{max}")
    public ResponseEntity<List<PlatoView>> buscarPlatosPorPrecio(@PathVariable BigDecimal max) throws PlatoException {
        List<PlatoView> views = platoService.buscarPorPrecioMenorA(max).stream()
                .map(Plato::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/rapidos/{minutosMax}")
    public ResponseEntity<List<PlatoView>> buscarPlatosRapidos(@PathVariable int minutosMax) throws PlatoException {
        List<PlatoView> views = platoService.buscarPlatosRapidos(minutosMax).stream()
                .map(Plato::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> crearPlato(@RequestBody Plato plato) throws PlatoException {
        platoService.crearPlato(plato);
        return new ResponseEntity<>("Plato creado con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/{id}/actualizar")
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

/*    @PostMapping("/{id}/cambiarPrecio")
    public ResponseEntity<String> cambiarPrecioPlato(
            @PathVariable Long id,
            @RequestParam BigDecimal nuevoPrecio) throws PlatoException {

        platoService.cambiarPrecio(id, nuevoPrecio);
        return new ResponseEntity<>("Precio del plato actualizado con éxito", HttpStatus.OK);
    }*/

    @PostMapping("/{id}/cambiarPrecio/{nuevoPrecio}")
    public ResponseEntity<String> cambiarPrecioPlato(
            @PathVariable Long id,
            @PathVariable BigDecimal nuevoPrecio) throws PlatoException {
        platoService.cambiarPrecio(id, nuevoPrecio);
        return new ResponseEntity<>("Precio del plato actualizado con éxito", HttpStatus.OK);
    }

    @PostMapping("/{id}/toggleActivo")
    public ResponseEntity<String> toggleActivoPlato(@PathVariable Long id) throws PlatoException {
        platoService.toggleActivoEnCarta(id);
        return new ResponseEntity<>("Estado 'activoEnCarta' actualizado correctamente", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPlato(@PathVariable Long id) throws PlatoException {
        platoService.eliminar(id);
        return new ResponseEntity<>("Plato eliminado con éxito", HttpStatus.OK);
    }
}
