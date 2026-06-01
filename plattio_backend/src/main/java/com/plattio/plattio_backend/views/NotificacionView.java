package com.plattio.plattio_backend.views;

public class NotificacionView {

    private Long id;
    private String mensaje;
    private String estado;
    private String tipo;
    private Long mozoId;
    private Long sesionId;
    private Integer numeroMesa;
    private Long pedidoId;

    public NotificacionView() {}

    public NotificacionView(Long id, String mensaje, String estado, String tipo,
                            Long mozoId, Long sesionId, Integer numeroMesa, Long pedidoId) {
        this.id = id;
        this.mensaje = mensaje;
        this.estado = estado;
        this.tipo = tipo;
        this.mozoId = mozoId;
        this.sesionId = sesionId;
        this.numeroMesa = numeroMesa;
        this.pedidoId = pedidoId;
    }

    // ---------- Getters y Setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getMozoId() {
        return mozoId;
    }

    public void setMozoId(Long mozoId) {
        this.mozoId = mozoId;
    }

    public Long getSesionId() {
        return sesionId;
    }

    public void setSesionId(Long sesionId) {
        this.sesionId = sesionId;
    }

    public Integer getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(Integer numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }
}
