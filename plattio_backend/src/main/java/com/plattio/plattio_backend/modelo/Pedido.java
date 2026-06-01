package com.plattio.plattio_backend.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.plattio.plattio_backend.views.ItemPedidoView;
import com.plattio.plattio_backend.views.PedidoView;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_id")
    @JsonIgnore
    private SesionMesa sesion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "fecha_preparacion")
    private LocalDateTime fechaPreparacion;

    @Column(nullable = false)
    private String estado;

    @Column
    private String categoria;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ItemPedido> items = new ArrayList<>();

    public Pedido() {
        this.fechaInicio = LocalDateTime.now();
        this.estado = "pendiente";
    }

    public Pedido(SesionMesa sesion, String categoria) {
        this();
        this.sesion = sesion;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public SesionMesa getSesion() {
        return sesion;
    }

    public void setSesion(SesionMesa sesion) {
        this.sesion = sesion;
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

    public LocalDateTime getFechaPreparacion() {
        return fechaPreparacion;
    }

    public void setFechaPreparacion(LocalDateTime fechaPreparacion) {
        this.fechaPreparacion = fechaPreparacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<ItemPedido> getItems() {
        return items;
    }

    public void agregarItem(ItemPedido item) {
        items.add(item);
        item.setPedido(this);
    }

    public void quitarItem(ItemPedido item) {
        items.remove(item);
        item.setPedido(null);
    }

    public void finalizar() {
        if (this.fechaFin != null) {
            throw new IllegalStateException("El pedido ya está finalizado.");
        }
        this.fechaFin = LocalDateTime.now();
        this.estado = "finalizado";
    }

    public boolean tieneItemsPendientes() {
        return items.stream().anyMatch(item -> !"finalizado".equalsIgnoreCase(item.getEstado()));
    }

    public void iniciarPreparacion() {
        if (!this.estado.equalsIgnoreCase("pendiente")) {
            throw new IllegalStateException("El pedido ya está en preparación o finalizado.");
        }
        this.estado = "en_preparacion";
        this.fechaPreparacion = LocalDateTime.now();
    }

    public void cancelar() {
        if ("finalizado".equalsIgnoreCase(this.estado)) {
            throw new IllegalStateException("No se puede cancelar un pedido entregado.");
        }
        this.estado = "cancelado";
        this.fechaFin = LocalDateTime.now();
    }

    public void cambiarEstado(String estado) {
        if (this.fechaFin != null) {
            throw new IllegalStateException("El pedido ya está finalizado.");
        }
        this.estado = estado;

        if ("pendiente".equalsIgnoreCase(estado)) {
            this.fechaPreparacion = null;  // <<--- Limpia el timestamp viejo
        }
    }

    public BigDecimal calcularTotal() {
        return items.stream()
                .map(item -> item.getPlato().getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", estado='" + estado + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", categoria='" + categoria + '\'' +
                '}';
    }

    public PedidoView toView() {
        PedidoView view = new PedidoView();
        view.setId(this.id);
        view.setMesaId(this.sesion.getMesa().getNumero());
        view.setEstado(this.estado);
        view.setCategoria(this.categoria);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        if (this.fechaInicio != null) view.setFechaInicio(this.fechaInicio.format(formatter));
        if (this.fechaFin != null) view.setFechaFin(this.fechaFin.format(formatter));
        if (this.fechaPreparacion != null) view.setFechaPreparacion(this.fechaPreparacion.format(formatter));

        List<ItemPedidoView> itemViews = this.items.stream()
                .map(ItemPedido::toView)
                .toList();
        view.setItems(itemViews);

        return view;
    }
}
