package com.plattio.plattio_backend.modelo;

import com.plattio.plattio_backend.views.EmpleadoView;
import jakarta.persistence.*;

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol;  // Ej: "mozo", "cocinero", "admin", etc.

    // ---------- Constructores ----------

    public Empleado() {}

    public Empleado(String nombre, String email, String password, String rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    // ---------- Getters y Setters ----------

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // ---------- METODOS ----------
    public boolean validarPassword(String passwordIngresada) {
        return this.password.equals(passwordIngresada);
    }

    public boolean esMozo() {
        return "mozo".equalsIgnoreCase(this.rol);
    }

    public boolean esCocinero() {
        return "cocinero".equalsIgnoreCase(this.rol);
    }

    public boolean esAdmin() {
        return "admin".equalsIgnoreCase(this.rol);
    }

    public void actualizarDatos(String nuevoNombre, String nuevoEmail) {
        this.nombre = nuevoNombre;
        this.email = nuevoEmail;
    }

    // ---------- To String ----------

    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }

    public EmpleadoView toView() {
        return new EmpleadoView(this.id, this.nombre, this.email, this.password, this.rol);
    }

}
