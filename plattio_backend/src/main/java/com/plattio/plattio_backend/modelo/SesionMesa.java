package com.plattio.plattio_backend.modelo;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sesion_mesa")
public class SesionMesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mozo_id")
    private Empleado mozo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "tipo_comensal")
    private String tipoComensal;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sesion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos = new ArrayList<>();

    public SesionMesa() {
        this.fechaInicio = LocalDateTime.now();
        this.tipoComensal = "anónimo";
    }

    public SesionMesa(Mesa mesa, Empleado mozo, String tipoComensal) {
        this();
        this.mesa = mesa;
        this.mozo = mozo;
        this.tipoComensal = tipoComensal;
    }

    public Long getId() { return id; }

    public Mesa getMesa() { return mesa; }
    public void setMesa(Mesa mesa) { this.mesa = mesa; }

    public Empleado getMozo() { return mozo; }
    public void setMozo(Empleado mozo) { this.mozo = mozo; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public String getTipoComensal() { return tipoComensal; }
    public void setTipoComensal(String tipoComensal) { this.tipoComensal = tipoComensal; }

    public List<Pedido> getPedidos() { return pedidos; }

    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
        pedido.setSesion(this);
    }

    public void quitarPedido(Pedido pedido) {
        pedidos.remove(pedido);
        pedido.setSesion(null);
    }

    public void finalizar() {
        if (this.fechaFin != null) {
            throw new IllegalStateException("La sesión ya fue finalizada.");
        }
        this.fechaFin = LocalDateTime.now();
        if (mesa != null) {
            mesa.liberar();
        }
    }

    public boolean esActiva() {
        return this.fechaFin == null;
    }

    public boolean tienePedidosActivos() {
        return pedidos.stream().anyMatch(p -> p.getFechaFin() == null);
    }

    public List<Pedido> getPedidosEnCurso() {
        return pedidos.stream()
                .filter(p -> p.getFechaFin() == null)
                .toList();
    }

    public void asignarMozo(Empleado nuevoMozo) {
        if (!esActiva()) {
            throw new IllegalStateException("No se puede asignar mozo a una sesión finalizada.");
        }
        this.mozo = nuevoMozo;
    }

    public Duration duracion() {
        if (fechaFin == null) {
            throw new IllegalStateException("La sesión aún está activa.");
        }
        return Duration.between(fechaInicio, fechaFin);
    }

    public void cerrarSiNoHayPedidos() {
        if (!pedidos.isEmpty()) {
            throw new IllegalStateException("No se puede cerrar la sesión: hay pedidos registrados.");
        }
        finalizar();
    }

    @Override
    public String toString() {
        return "SesionMesa{id=" + id + ", mesa=" + (mesa != null ? mesa.getNumero() : "null") +
                ", mozo=" + (mozo != null ? mozo.getNombre() : "null") +
                ", tipoComensal='" + tipoComensal + "', fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin + '}';
    }
}
