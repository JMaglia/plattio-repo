package com.plattio.plattio_backend.views;

public class ItemPedidoView {

    private Long id;
    private String nombre;
    private String detalle;
    private int tiempo;
    private String nota;
    private boolean finalizado;
    private String estado;
    private int cantidad;
    private PlatoView plato;

    public ItemPedidoView() {}

    public ItemPedidoView(Long id, String nombre, String detalle, int tiempo, String nota, boolean finalizado, String estado, int cantidad, PlatoView plato) {
        this.id = id;
        this.nombre = nombre;
        this.detalle = detalle;
        this.tiempo = tiempo;
        this.nota = nota;
        this.finalizado = finalizado;
        this.estado = estado;
        this.cantidad = cantidad;
        this.plato = plato;
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

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public PlatoView getPlato() {
        return plato;
    }

    public void setPlato(PlatoView plato) {
        this.plato = plato;
    }
}
