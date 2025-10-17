package com.proyect.tiendaonline.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "direcciones")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Autoincrementar
    private Long id;

    private String calle;
    private String ciudad;
    private String pais;
    private String zip;

    //FK hacia cliente
    @OneToOne
    @JoinColumn(name = "cliente_id", unique = true)
    private Cliente cliente;

    //CONSTRUCTORES
    //vacio
    public Direccion() {
    }

    //con parametros
    public Direccion(String calle, String ciudad, String pais, String zip) {
        this.calle = calle;
        this.ciudad = ciudad;
        this.pais = pais;
        this.zip = zip;
    }

    //-----GETTERS AND SETTERS-----

    public Long getId() {
        return id;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
