package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Empleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmpleadoDAO {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    // CRUD básico
    public void guardar(Empleado empleado) {
        empleadoRepository.save(empleado);
    }

    public void eliminar(Long id) {
        empleadoRepository.deleteById(id);
    }

    public Optional<Empleado> buscarPorId(Long id) {
        return empleadoRepository.findById(id);
    }

    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    // Consultas útiles
    public Optional<Empleado> buscarPorEmail(String email) {
        return empleadoRepository.findByEmail(email);
    }

    public List<Empleado> buscarPorRol(String rol) {
        return empleadoRepository.findByRolIgnoreCase(rol);
    }

    public List<Empleado> buscarPorNombre(String nombreParcial) {
        return empleadoRepository.findByNombreContainingIgnoreCase(nombreParcial);
    }

    public Optional<Empleado> buscarPorSesionId(Long sesionId) {
        return empleadoRepository.findMozoBySesionId(sesionId);
    }
}
