package com.plattio.plattio_backend.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    private String estado;

    private String tipo;

    @ManyToOne
    @JoinColumn(name = "mozo_id")
    private Empleado mozo;

    @ManyToOne
    @JoinColumn(name = "sesion_id")
    private SesionMesa sesion;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    public Notificacion() {}

    public Notificacion(String mensaje, String tipo, Empleado mozo, SesionMesa sesion) {
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.mozo = mozo;
        this.sesion = sesion;
        this.estado = "pendiente";
    }

    public Notificacion(String mensaje, String tipo, Empleado mozo, SesionMesa sesion, Pedido pedido) {
        this(mensaje, tipo, mozo, sesion);
        this.pedido = pedido;
    }

    public Long getId() { return id; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Empleado getMozo() { return mozo; }
    public void setMozo(Empleado mozo) { this.mozo = mozo; }

    public SesionMesa getSesion() { return sesion; }
    public void setSesion(SesionMesa sesion) { this.sesion = sesion; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public void completar() {
        if ("completada".equalsIgnoreCase(this.estado)) {
            throw new IllegalStateException("La notificación ya está completada.");
        }
        this.estado = "completada";
    }

    public void alternarEstado() {
        this.estado = "completada".equalsIgnoreCase(this.estado) ? "pendiente" : "completada";
    }
}
