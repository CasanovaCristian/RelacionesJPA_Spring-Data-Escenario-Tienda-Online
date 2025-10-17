package com.proyect.tiendaonline.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
//@UniqueConstraint	Evita duplicar el mismo producto en un pedido.
//@cascade = ALL	Guarda automáticamente los ítems al guardar el pedido.
//@calcularTotal()	Mantiene coherente el total sin cálculos manuales.
//@fetch = LAZY	Evita cargar productos innecesarios en memoria.
@Table(name = "items_pedido",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_pedido_producto",
                columnNames = {"pedido_id", "producto_id"}))
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private BigDecimal precioUnitario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    //CONSTRUCTORES
    //vacio
    public ItemPedido() {
    }

    //con parametros
    public ItemPedido(Integer cantidad, BigDecimal precioUnitario, Pedido pedido, Producto producto) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.pedido = pedido;
        this.producto = producto;
    }

    //-----GETTERS AND SETTERS-----

    public Long getId() {
        return id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
