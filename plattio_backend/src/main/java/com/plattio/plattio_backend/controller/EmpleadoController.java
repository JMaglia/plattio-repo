package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.exceptions.EmpleadoException;
import com.plattio.plattio_backend.modelo.Empleado;
import com.plattio.plattio_backend.service.EmpleadoService;
import com.plattio.plattio_backend.views.EmpleadoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping("/")
    public String mensaje() {
        return """
                recetas culinarias
                """;
    }

    @GetMapping("/probar-excepcion/{email}")
    public Empleado probarExcepcion(@PathVariable String email) throws EmpleadoException {
        return empleadoService.buscarPorEmail(email);
    }

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
}
