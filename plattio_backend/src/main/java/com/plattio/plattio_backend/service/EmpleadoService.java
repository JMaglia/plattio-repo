package com.plattio.plattio_backend.service;

import com.plattio.plattio_backend.datos.EmpleadoDAO;
import com.plattio.plattio_backend.exceptions.EmpleadoException;
import com.plattio.plattio_backend.modelo.Empleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.HashMap;


import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoDAO empleadoDAO;

    // Obtener todos los empleados
    public List<Empleado> obtenerTodos() throws EmpleadoException {
        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        if (empleados.isEmpty()) {
            throw new EmpleadoException("No hay empleados registrados en el sistema.", HttpStatus.NOT_FOUND);
        }
        return empleados;
    }

    // Buscar por ID
    public Empleado buscarPorId(Long id) throws EmpleadoException {
        return empleadoDAO.buscarPorId(id)
                .orElseThrow(() -> new EmpleadoException("Empleado no encontrado con ID: " + id, HttpStatus.NOT_FOUND));
    }

    // Buscar por email
    public Empleado buscarPorEmail(String email) throws EmpleadoException {
        return empleadoDAO.buscarPorEmail(email)
                .orElseThrow(() -> new EmpleadoException("No existe un empleado con ese email", HttpStatus.NOT_FOUND));
    }

    public List<Empleado> buscarPorNombre(String nombreParcial) throws EmpleadoException {
        List<Empleado> encontrados = empleadoDAO.buscarPorNombre(nombreParcial);
        if (encontrados.isEmpty()) {
            throw new EmpleadoException("No se encontraron empleados con ese nombre.", HttpStatus.NOT_FOUND);
        }
        return encontrados;
    }

    // Buscar por rol
    public List<Empleado> buscarPorRol(String rol) throws EmpleadoException {
        List<Empleado> encontrados = empleadoDAO.buscarPorRol(rol);
        if (encontrados.isEmpty()) {
            throw new EmpleadoException("No se encontraron empleados con el rol: " + rol, HttpStatus.NOT_FOUND);
        }
        return encontrados;
    }

    // Registrar nuevo empleado
    public void registrarEmpleado(Empleado nuevo) throws EmpleadoException {
        if (empleadoDAO.buscarPorEmail(nuevo.getEmail()).isPresent()) {
            throw new EmpleadoException("Ya existe un empleado con ese email.", HttpStatus.BAD_REQUEST);
        }
        if (!nuevo.esMozo() && !nuevo.esAdmin() && !nuevo.esCocinero()) {
            throw new EmpleadoException("Rol no válido: " + nuevo.getRol(), HttpStatus.BAD_REQUEST);
        }
        empleadoDAO.guardar(nuevo);
    }

    // Validar login (simple)
    public Empleado login(String email, String password) throws EmpleadoException {
        Empleado empleado = buscarPorEmail(email);
        if (!empleado.validarPassword(password)) {
            throw new EmpleadoException("Contraseña incorrecta", HttpStatus.BAD_REQUEST);
        }
        return empleado;
    }

    // Actualizar nombre y email
    public void actualizarDatos(Long id, String nuevoNombre, String nuevoEmail) throws EmpleadoException {
        Empleado empleado = buscarPorId(id); // ya lanza excepción si no existe

        // Verificamos si el nuevo email ya lo usa otro empleado
        Optional<Empleado> existente = empleadoDAO.buscarPorEmail(nuevoEmail);
        if (existente.isPresent() && !existente.get().getId().equals(id)) {
            throw new EmpleadoException("Ya existe otro empleado con ese email.", HttpStatus.BAD_REQUEST);
        }

        empleado.actualizarDatos(nuevoNombre, nuevoEmail);
        empleadoDAO.guardar(empleado);
    }

    // Cambiar rol
    public void cambiarRol(Long id, String nuevoRol) throws EmpleadoException {
        Empleado empleado = buscarPorId(id); // lanza excepción si no existe
        empleado.setRol(nuevoRol);
        empleadoDAO.guardar(empleado);
    }

    // Eliminar
    public void eliminar(Long id) throws EmpleadoException {
        if (empleadoDAO.buscarPorId(id).isEmpty()) {
            throw new EmpleadoException("No se puede eliminar: no existe un empleado con ID " + id, HttpStatus.BAD_REQUEST);
        }
        empleadoDAO.eliminar(id);
    }
}
