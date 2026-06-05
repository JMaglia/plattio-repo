package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Empleado;
import com.plattio.plattio_backend.modelo.Rol;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmpleadoDAO {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoDAO(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public Empleado guardar(Empleado empleado) {
        return empleadoRepository.save(empleado);
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

    public Optional<Empleado> buscarPorEmail(String email) {
        return empleadoRepository.findByEmail(email);
    }

    public List<Empleado> buscarPorRol(Rol rol) {
        return empleadoRepository.findByRol(rol);
    }

    public List<Empleado> buscarPorNombre(String nombreParcial) {
        return empleadoRepository.findByNombreContainingIgnoreCase(nombreParcial);
    }

    public Optional<Empleado> buscarPorSesionId(Long sesionId) {
        return empleadoRepository.findMozoBySesionId(sesionId);
    }
}
