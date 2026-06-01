package com.plattio.plattio_backend.views;

public class MesaView {

    private Long id;
    private int numero;
    private String estado;
    private String qrToken;

    public MesaView() {}

    public MesaView(Long id, int numero, String estado, String qrToken) {
        this.id = id;
        this.numero = numero;
        this.estado = estado;
        this.qrToken = qrToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
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
}
