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

    /**
     * Crea una categoría con el nombre indicado.
     * Se añadió porque otras partes del código instancian `new Categoria(nombre)`.
     * Tener este constructor evita errores de compilación y facilita pruebas/creación rápida.
     */
    public Categoria(String nombre) {
        this.nombre = nombre;
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

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }
}
