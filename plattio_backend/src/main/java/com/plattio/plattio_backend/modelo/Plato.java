package com.plattio.plattio_backend.modelo;

import com.plattio.plattio_backend.views.PlatoView;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "plato")
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column
    private String categoria;

    @Column(name = "tiempo_estimado")
    private Integer tiempoEstimado;

    @Column(name = "activo_en_carta", nullable = false)
    private boolean activoEnCarta = true;

    public Plato() {}

    public Plato(String nombre, String descripcion, BigDecimal precio, String categoria, Integer tiempoEstimado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.tiempoEstimado = tiempoEstimado;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(Integer tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public boolean isActivoEnCarta() {
        return activoEnCarta;
    }

    public void setActivoEnCarta(boolean activoEnCarta) {
        this.activoEnCarta = activoEnCarta;
    }

    public boolean esCategoria(String cat) {
        return this.categoria != null && this.categoria.equalsIgnoreCase(cat);
    }

    public void cambiarPrecio(BigDecimal nuevoPrecio) {
        if (nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }
        this.precio = nuevoPrecio;
    }

    public boolean esPrecioValido() {
        return this.precio != null && this.precio.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean esRapido() {
        return tiempoEstimado != null && tiempoEstimado <= 10;
    }

    public void actualizarDatos(String nombre, String descripcion, BigDecimal precio, String categoria, Integer tiempoEstimado) {
        setNombre(nombre);
        setDescripcion(descripcion);
        setPrecio(precio);
        setCategoria(categoria);
        setTiempoEstimado(tiempoEstimado);
    }

    @Override
    public String toString() {
        return "Plato{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", categoria='" + categoria + '\'' +
                ", tiempoEstimado=" + tiempoEstimado +
                ", activoEnCarta=" + activoEnCarta +
                '}';
    }

    public PlatoView toView() {
        return new PlatoView(
                this.id,
                this.nombre,
                this.descripcion,
                this.precio,
                this.categoria,
                this.tiempoEstimado,
                this.activoEnCarta
        );
    }
}
