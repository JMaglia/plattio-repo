package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.EmpleadoDAO;
import com.plattio.plattio_backend.dto.request.RegistrarEmpleadoRequest;
import com.plattio.plattio_backend.exceptions.EmpleadoException;
import com.plattio.plattio_backend.modelo.Empleado;
import com.plattio.plattio_backend.modelo.Rol;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {

    private final EmpleadoDAO empleadoDAO;

    public EmpleadoService(EmpleadoDAO empleadoDAO) {
        this.empleadoDAO = empleadoDAO;
    }

    public List<Empleado> obtenerTodos() {
        return empleadoDAO.obtenerTodos();
    }

    public Empleado buscarPorId(Long id) {
        return empleadoDAO.buscarPorId(id)
                .orElseThrow(() -> new EmpleadoException("Empleado no encontrado con ID: " + id, HttpStatus.NOT_FOUND));
    }

    public Empleado buscarPorEmail(String email) {
        return empleadoDAO.buscarPorEmail(email)
                .orElseThrow(() -> new EmpleadoException("No existe un empleado con ese email", HttpStatus.NOT_FOUND));
    }

    public List<Empleado> buscarPorNombre(String nombreParcial) {
        return empleadoDAO.buscarPorNombre(nombreParcial);
    }

    public List<Empleado> buscarPorRol(Rol rol) {
        return empleadoDAO.buscarPorRol(rol);
    }

    public void registrarEmpleado(RegistrarEmpleadoRequest request) {
        if (empleadoDAO.buscarPorEmail(request.email()).isPresent()) {
            throw new EmpleadoException("Ya existe un empleado con ese email.", HttpStatus.BAD_REQUEST);
        }
        if (request.rol() == null) {
            throw new EmpleadoException("El rol es requerido.", HttpStatus.BAD_REQUEST);
        }
        empleadoDAO.guardar(new Empleado(request.nombre(), request.email(), request.password(), request.rol()));
    }

    public Empleado login(String email, String password) {
        Empleado empleado = buscarPorEmail(email);
        if (!empleado.validarPassword(password)) {
            throw new EmpleadoException("Contraseña incorrecta", HttpStatus.UNAUTHORIZED);
        }
        return empleado;
    }

    public void actualizarDatos(Long id, String nuevoNombre, String nuevoEmail) {
        Empleado empleado = buscarPorId(id);
        Optional<Empleado> existente = empleadoDAO.buscarPorEmail(nuevoEmail);
        if (existente.isPresent() && !existente.get().getId().equals(id)) {
            throw new EmpleadoException("Ya existe otro empleado con ese email.", HttpStatus.BAD_REQUEST);
        }
        empleado.actualizarDatos(nuevoNombre, nuevoEmail);
        empleadoDAO.guardar(empleado);
    }

    public void cambiarRol(Long id, Rol nuevoRol) {
        Empleado empleado = buscarPorId(id);
        empleado.setRol(nuevoRol);
        empleadoDAO.guardar(empleado);
    }

    public void eliminar(Long id) {
        if (empleadoDAO.buscarPorId(id).isEmpty()) {
            throw new EmpleadoException("No se puede eliminar: no existe un empleado con ID " + id, HttpStatus.NOT_FOUND);
        }
        empleadoDAO.eliminar(id);
    }
}
