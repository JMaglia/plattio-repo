package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Empleado;
import com.plattio.plattio_backend.modelo.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByEmail(String email);

    List<Empleado> findByRol(Rol rol);

    List<Empleado> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT s.mozo FROM SesionMesa s WHERE s.id = :sesionId")
    Optional<Empleado> findMozoBySesionId(@Param("sesionId") Long sesionId);
}
