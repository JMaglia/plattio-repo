package com.plattio.plattio_backend.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.plattio.plattio_backend.mapper.EmpleadoMapper;
import com.plattio.plattio_backend.mapper.PedidoMapper;
import com.plattio.plattio_backend.views.PedidoView;
import com.plattio.plattio_backend.views.SesionMesaView;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private String tipoComensal; // anónimo o registrado

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sesion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // ← esto rompe el ciclo
    private List<Pedido> pedidos = new ArrayList<>();

    // ---------- Constructores ----------

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

    // ---------- Getters y Setters ----------

    public Long getId() {
        return id;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Empleado getMozo() {
        return mozo;
    }

    public void setMozo(Empleado mozo) {
        this.mozo = mozo;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getTipoComensal() {
        return tipoComensal;
    }

    public void setTipoComensal(String tipoComensal) {
        this.tipoComensal = tipoComensal;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
        pedido.setSesion(this);
    }

    public void quitarPedido(Pedido pedido) {
        pedidos.remove(pedido);
        pedido.setSesion(null);
    }

    // ---------- METODOS ----------
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
        if (pedidos.isEmpty()) {
            finalizar();
        } else {
            throw new IllegalStateException("No se puede cerrar la sesión: hay pedidos registrados.");
        }
    }

    // ---------- toString ----------

    @Override
    public String toString() {
        return "SesionMesa{" +
                "id=" + id +
                ", mesa=" + (mesa != null ? mesa.getNumero() : "null") +
                ", mozo=" + (mozo != null ? mozo.getNombre() : "null") +
                ", tipoComensal='" + tipoComensal + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                '}';
    }

    public SesionMesaView toView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        List<PedidoView> pedidosView = this.pedidos.stream()
                .map(PedidoMapper::toView)
                .collect(Collectors.toList());

        return new SesionMesaView(
                this.id,
                this.mesa.getId(),
                this.mesa.getNumero(),
                this.tipoComensal,
                this.fechaInicio.format(formatter),
                this.fechaFin != null ? this.fechaFin.format(formatter) : null,
                this.mozo != null ? EmpleadoMapper.toView(this.mozo) : null,
                pedidosView
        );
    }


}