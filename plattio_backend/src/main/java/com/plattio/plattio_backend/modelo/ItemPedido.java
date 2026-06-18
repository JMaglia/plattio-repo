package com.plattio.plattio_backend.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plato_id")
    private Plato plato;

    @Column(nullable = false)
    private int cantidad;

    @Column(length = 50)
    private String nota;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(nullable = false)
    private String estado;

    public ItemPedido() {
        this.fechaInicio = LocalDateTime.now();
        this.estado = "pendiente";
    }

    public ItemPedido(Pedido pedido, Plato plato, int cantidad, String nota) {
        this();
        this.pedido = pedido;
        this.plato = plato;
        this.cantidad = cantidad;
        this.nota = nota;
    }

    public Long getId() { return id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Plato getPlato() { return plato; }
    public void setPlato(Plato plato) { this.plato = plato; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public void iniciarPreparacion() {
        if (!"pendiente".equalsIgnoreCase(this.estado)) {
            throw new IllegalStateException("El ítem ya no está pendiente.");
        }
        this.estado = "en_preparacion";
    }

    public void marcarListo() {
        if (!"en_preparacion".equalsIgnoreCase(this.estado)) {
            throw new IllegalStateException("El ítem debe estar en preparación para marcarse como listo.");
        }
        this.estado = "listo";
        this.fechaFin = LocalDateTime.now();
    }

    public void entregar() {
        if (!"listo".equalsIgnoreCase(this.estado)) {
            throw new IllegalStateException("El ítem debe estar listo para ser entregado.");
        }
        this.estado = "entregado";
    }

    public void cancelar() {
        if (!"pendiente".equalsIgnoreCase(this.estado)) {
            throw new IllegalStateException("Solo se pueden cancelar ítems pendientes.");
        }
        this.estado = "cancelado";
        this.fechaFin = LocalDateTime.now();
    }

    public boolean esModificable() {
        return "pendiente".equalsIgnoreCase(this.estado);
    }

    public BigDecimal calcularSubtotal() {
        return plato.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }

    @Override
    public String toString() {
        return "ItemPedido{id=" + id + ", cantidad=" + cantidad + ", estado='" + estado + "', nota='" + nota + "'}";
    }
}
