package com.plattio.plattio_backend.modelo;

import com.plattio.plattio_backend.views.MesaView;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mesa")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer numero;

    @Column(nullable = false)
    private String estado; // Ej: "libre", "ocupada"

    @Column(name = "qr_token", unique = true)
    private String qrToken;

    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SesionMesa> sesiones = new ArrayList<>();

    // ---------- Constructores ----------

    public Mesa() {
        this.estado = "libre";
    }

    public Mesa(Integer numero, String qrToken) {
        this.numero = numero;
        this.estado = "libre";
        this.qrToken = qrToken;
    }

    // ---------- Getters y Setters ----------

    public Long getId() {
        return id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getQrToken() {
        return qrToken;
    }

    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
    }

    public List<SesionMesa> getSesiones() {
        return sesiones;
    }

    public void agregarSesion(SesionMesa sesion) {
        sesiones.add(sesion);
        sesion.setMesa(this);
    }

    public void quitarSesion(SesionMesa sesion) {
        sesiones.remove(sesion);
        sesion.setMesa(null);
    }

    // ---------- METODOS----------
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

    // ---------- METODOS ----------
    public boolean estaOcupada() {
        return "ocupada".equalsIgnoreCase(this.estado);
    }

    public boolean estaLibre() {
        return "libre".equalsIgnoreCase(this.estado);
    }

    public boolean tieneSesionActiva() {
        return sesiones.stream().anyMatch(s -> s.getFechaFin() == null);
    }

    public SesionMesa getSesionActiva() {
        return sesiones.stream()
                .filter(s -> s.getFechaFin() == null)
                .findFirst()
                .orElse(null);
    }

    public void cambiarEstado(String nuevoEstado) {
        if (!nuevoEstado.equalsIgnoreCase("libre") &&
                !nuevoEstado.equalsIgnoreCase("ocupada") &&
                !nuevoEstado.equalsIgnoreCase("reservada")) {
            throw new IllegalArgumentException("Estado de mesa inválido: " + nuevoEstado);
        }
        this.estado = nuevoEstado.toLowerCase();
    }

    public boolean estaReservada() {
        return "reservada".equalsIgnoreCase(this.estado);
    }

    // ---------- toString ----------

    @Override
    public String toString() {
        return "Mesa{" +
                "id=" + id +
                ", numero=" + numero +
                ", estado='" + estado + '\'' +
                ", qrToken='" + qrToken + '\'' +
                '}';
    }

    public MesaView toView() {
        return new MesaView(this.id, this.numero, this.estado, this.qrToken);
    }

}
