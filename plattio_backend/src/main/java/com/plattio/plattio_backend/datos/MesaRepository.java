package com.plattio.plattio_backend.datos;

import com.plattio.plattio_backend.modelo.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    Optional<Mesa> findByNumero(Integer numero);

    Optional<Mesa> findByQrToken(String qrToken);

    List<Mesa> findByEstado(String estado);
}
