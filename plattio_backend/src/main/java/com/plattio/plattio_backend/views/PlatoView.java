package com.plattio.plattio_backend.views;

import java.math.BigDecimal;

public class PlatoView {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String categoria;
    private int tiempoEstimado;
    private boolean activoEnCarta;

    public PlatoView() {}

    public PlatoView(Long id, String nombre, String descripcion, BigDecimal precio, String categoria, int tiempoEstimado, boolean activoEnCarta) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.tiempoEstimado = tiempoEstimado;
        this.activoEnCarta = activoEnCarta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public boolean isActivoEnCarta() {
        return activoEnCarta;
    }

    public void setActivoEnCarta(boolean activoEnCarta) {
        this.activoEnCarta = activoEnCarta;
    }
}
