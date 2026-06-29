package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.dto.request.ActualizarEmpleadoRequest;
import com.plattio.plattio_backend.dto.request.CambiarRolRequest;
import com.plattio.plattio_backend.dto.request.LoginRequest;
import com.plattio.plattio_backend.dto.request.RegistrarEmpleadoRequest;
import com.plattio.plattio_backend.exceptions.EmpleadoException;
import com.plattio.plattio_backend.mapper.EmpleadoMapper;
import com.plattio.plattio_backend.modelo.Rol;
import com.plattio.plattio_backend.service.EmpleadoService;
import com.plattio.plattio_backend.views.EmpleadoView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoView>> obtenerTodosLosEmpleados() {
        List<EmpleadoView> views = empleadoService.obtenerTodos().stream()
                .map(EmpleadoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoView> obtenerEmpleadoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(EmpleadoMapper.toView(empleadoService.buscarPorId(id)));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<EmpleadoView> obtenerEmpleadoPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(EmpleadoMapper.toView(empleadoService.buscarPorEmail(email)));
    }

    @GetMapping("/nombre/{nombreParcial}")
    public ResponseEntity<List<EmpleadoView>> buscarEmpleadosPorNombre(@PathVariable String nombreParcial) {
        List<EmpleadoView> views = empleadoService.buscarPorNombre(nombreParcial).stream()
                .map(EmpleadoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<EmpleadoView>> buscarEmpleadosPorRol(@PathVariable String rol) {
        Rol rolEnum;
        try {
            rolEnum = Rol.fromString(rol);
        } catch (IllegalArgumentException e) {
            throw new EmpleadoException("Rol no válido: " + rol, HttpStatus.BAD_REQUEST);
        }
        List<EmpleadoView> views = empleadoService.buscarPorRol(rolEnum).stream()
                .map(EmpleadoMapper::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    @PostMapping
    public ResponseEntity<String> registrarEmpleado(@Valid @RequestBody RegistrarEmpleadoRequest request) {
        empleadoService.registrarEmpleado(request);
        return new ResponseEntity<>("Empleado registrado con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<EmpleadoView> loginEmpleado(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(EmpleadoMapper.toView(empleadoService.login(request.email(), request.password())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarDatosEmpleado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEmpleadoRequest request) {
        empleadoService.actualizarDatos(id, request.nombre(), request.email());
        return ResponseEntity.ok("Datos del empleado actualizados con éxito");
    }

    @PatchMapping("/{id}/rol")
    public ResponseEntity<String> cambiarRolEmpleado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarRolRequest request) {
        empleadoService.cambiarRol(id, request.rol());
        return ResponseEntity.ok("Rol del empleado actualizado con éxito");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
