package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    // Buscar por email
    Optional<Empleado> findByEmail(String email);

    // Buscar por rol
    List<Empleado> findByRolIgnoreCase(String rol);

    // Buscar por nombre (parcial, sin distinguir mayúsculas)
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT s.mozo FROM SesionMesa s WHERE s.id = :sesionId")
    Optional<Empleado> findMozoBySesionId(@Param("sesionId") Long sesionId);
}
