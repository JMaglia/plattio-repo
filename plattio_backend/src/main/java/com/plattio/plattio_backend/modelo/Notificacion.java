package com.plattio.plattio_backend.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.plattio.plattio_backend.views.NotificacionView;
import jakarta.persistence.*;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    private String estado; // "pendiente" o "completada"

    private String tipo; // "asistir", "entregar_cuenta", "pedido_listo"

    @ManyToOne
    @JoinColumn(name = "mozo_id")
    private Empleado mozo;

    @ManyToOne
    @JoinColumn(name = "sesion_id")
    @JsonIgnore
    private SesionMesa sesion;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = true)
    private Pedido pedido;

    // ---------- Constructores ----------

    public Notificacion() {}

    public Notificacion(String mensaje, String tipo, Empleado mozo, SesionMesa sesion) {
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.mozo = mozo;
        this.sesion = sesion;
        this.estado = "pendiente";
        this.pedido = null; // explícito, aunque no necesario
    }

    public Notificacion(String mensaje, String tipo, Empleado mozo, SesionMesa sesion, Pedido pedido) {
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.estado = "pendiente";
        this.mozo = mozo;
        this.sesion = sesion;
        this.pedido = pedido;
    }

    // ---------- Getters y Setters ----------

    public Long getId() {
        return id;
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

    public Empleado getMozo() {
        return mozo;
    }

    public void setMozo(Empleado mozo) {
        this.mozo = mozo;
    }

    public SesionMesa getSesion() {
        return sesion;
    }

    public void setSesion(SesionMesa sesion) {
        this.sesion = sesion;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    // ---------- toView ----------

    public NotificacionView toView() {
        return new NotificacionView(
                id,
                mensaje,
                estado,
                tipo,
                mozo != null ? mozo.getId() : null,
                sesion != null ? sesion.getId() : null,
                sesion != null ? sesion.getMesa().getNumero() : null,
                pedido != null ? pedido.getId() : null // 👈 nuevo campo
        );
    }
}
