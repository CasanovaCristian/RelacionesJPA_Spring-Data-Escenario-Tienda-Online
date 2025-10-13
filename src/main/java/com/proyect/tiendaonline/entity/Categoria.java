package com.proyect.tiendaonline.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "categorias",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_categoria_nombre", columnNames = "nombre")
        }
)
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @ManyToMany(mappedBy = "categorias")
    private Set<Producto> productos = new HashSet<>();

    //CONSTRUCTORES
    //vacio
    public Categoria() {
    }

    //con parametros
    public Categoria(String nombre, Set<Producto> productos) {
        this.nombre = nombre;
        this.productos = productos;
    }

    //-----GETTERS AND SETTERS
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Producto> getProductos() {
        return productos;
    }
}
