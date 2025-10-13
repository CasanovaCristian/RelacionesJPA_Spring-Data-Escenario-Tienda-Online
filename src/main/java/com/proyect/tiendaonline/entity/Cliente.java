package com.proyect.tiendaonline.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Para autoincrementar
    private Long id;

    @Column(nullable = false) //NO puede ser nulo = No puede estar vacio
    private String nombre;

    @Column(nullable = false, unique = true) //No nulo y que sea único
    private String email;

    /**
     * @mappedBy = "cliente" - Indica que la FK está en Direccion.
     * @cascade = CascadeType.ALL Al guardar/eliminar un cliente, también se afecta su dirección.
     * @orphanRemoval = true - Si una dirección se quita del cliente, se elimina de la BD.
     * @fetch = FetchType.LAZY - Carga diferida: evita traer la dirección hasta que se necesite.
     */
    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private Direccion direccion;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();

    //CONSTRUCTORES
    //vacio
    public Cliente() {
    }

    //con parametros
    public Cliente(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    //Metodos de conveniencia para mantener sincronia en la relacion
    //tanto addPedido como removePedido mantiene la consistencia bidireccional de la relacion.
    public void addPedido(Pedido pedido) {
        pedidos.add(pedido);
        pedido.setCliente(this);
    }

    public void removePedido(Pedido pedido) {
        pedidos.remove(pedido);
        pedido.setCliente(null);
    }

    //-----GETTERS AND SETTERS-----
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

    public Direccion getDireccion() {
        return direccion;
    }

    //@setDireccion() - Metodo para mantener sincronizadas ambas entidades (buena práctica).
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
        if (direccion != null) {
            direccion.setCliente(this);
        }
    }

    //@fetch = FetchType.Lazy = evita traer todos los pedidos al consultar un cliente (optimiza el rendimiento)
    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
