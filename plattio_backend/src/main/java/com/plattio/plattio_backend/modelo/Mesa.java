package com.plattio.plattio_backend.modelo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "mesa")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer numero;

    @Column(nullable = false)
    private String estado;

    @Column(name = "qr_token", unique = true)
    private String qrToken;

    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SesionMesa> sesiones = new ArrayList<>();

    public Mesa() {
        this.estado = "libre";
    }

    public Mesa(Integer numero, String qrToken) {
        this.numero = numero;
        this.estado = "libre";
        this.qrToken = qrToken;
    }

    public Long getId() { return id; }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getQrToken() { return qrToken; }
    public void setQrToken(String qrToken) { this.qrToken = qrToken; }

    public List<SesionMesa> getSesiones() { return sesiones; }

    public void agregarSesion(SesionMesa sesion) {
        sesiones.add(sesion);
        sesion.setMesa(this);
    }

    public void quitarSesion(SesionMesa sesion) {
        sesiones.remove(sesion);
        sesion.setMesa(null);
    }

    public void ocupar() {
        if (!this.estado.equalsIgnoreCase("libre")) {
            throw new IllegalStateException("La mesa ya está ocupada o reservada.");
        }
        this.estado = "ocupada";
    }

    public void liberar() {
        if (this.estado.equalsIgnoreCase("libre")) {
            throw new IllegalStateException("La mesa ya está libre.");
        }
        this.estado = "libre";
    }

    public boolean estaOcupada() { return "ocupada".equalsIgnoreCase(this.estado); }
    public boolean estaLibre() { return "libre".equalsIgnoreCase(this.estado); }
    public boolean estaReservada() { return "reservada".equalsIgnoreCase(this.estado); }

    public boolean tieneSesionActiva() {
        return sesiones.stream().anyMatch(s -> s.getFechaFin() == null);
    }

    public Optional<SesionMesa> getSesionActiva() {
        return sesiones.stream()
                .filter(s -> s.getFechaFin() == null)
                .findFirst();
    }

    public void cambiarEstado(String nuevoEstado) {
        if (!nuevoEstado.equalsIgnoreCase("libre") &&
                !nuevoEstado.equalsIgnoreCase("ocupada") &&
                !nuevoEstado.equalsIgnoreCase("reservada")) {
            throw new IllegalArgumentException("Estado de mesa inválido: " + nuevoEstado);
        }
        this.estado = nuevoEstado.toLowerCase();
    }

    @Override
    public String toString() {
        return "Mesa{id=" + id + ", numero=" + numero + ", estado='" + estado + "', qrToken='" + qrToken + "'}";
    }
}
