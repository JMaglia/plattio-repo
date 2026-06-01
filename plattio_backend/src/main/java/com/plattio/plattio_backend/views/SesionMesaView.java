package com.plattio.plattio_backend.views;

import java.util.List;

public class SesionMesaView {

    private Long id;
    private Long idMesa;
    private int numeroMesa;
    private String tipoComensal;
    private String fechaInicio;
    private String fechaFin;
    private EmpleadoView mozoId;
    private List<PedidoView> pedidos;

    public SesionMesaView() {}

    public SesionMesaView(Long id, Long idMesa, int numeroMesa, String tipoComensal, String fechaInicio, String fechaFin, EmpleadoView mozoId, List<PedidoView> pedidos) {
        this.id = id;
        this.idMesa = idMesa;
        this.numeroMesa = numeroMesa;
        this.tipoComensal = tipoComensal;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.mozoId = mozoId;
        this.pedidos = pedidos;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(Long idMesa) {
        this.idMesa = idMesa;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public String getTipoComensal() {
        return tipoComensal;
    }

    public void setTipoComensal(String tipoComensal) {
        this.tipoComensal = tipoComensal;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EmpleadoView getMozoId() {
        return mozoId;
    }

    public void setMozoId(EmpleadoView mozoId) {
        this.mozoId = mozoId;
    }

    public List<PedidoView> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<PedidoView> pedidos) {
        this.pedidos = pedidos;
    }
}
