package com.proyect.tiendaonline.entity;

import com.proyect.tiendaonline.entity.enums.EstadoPedido;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) //no puede ser nulo
    private EstadoPedido estado;

    @Column(nullable = false)
    private BigDecimal total;

    //@fetch = FetchType.LAZY - Carga diferida: evita traer el cliente hasta que se necesite.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    //@orphanRemoval = true - Si el item se quita del pedido, se elimina de la BD.
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> items = new ArrayList<>();

    //CONSTRUCTORES
    //vacio
    public Pedido() {
        this.estado = EstadoPedido.NUEVO; //Valor por defecto del estado del pedido (si no se asigna)
        this.fecha = LocalDateTime.now(); //Hora del servidor del cliente
        this.total = BigDecimal.ZERO; //Por defecto a la hora de crearlo lo asigna en 0
    }
    //con parametros


    public Pedido(Cliente cliente) {
        this();
        this.cliente = cliente;
    }

    //Metodos de conveniencia y para agregar un item correctamente
    public void addItem(Producto producto, int cantidad) {
        ItemPedido item = new ItemPedido();
        item.setProducto(producto);
        item.setCantidad(cantidad);
        item.setPrecioUnitario(producto.getPrecio());
        item.setPedido(this);

        items.add(item);

        //actualizamos el total del pedido
        calcularTotal();
    }

    //Metodos de conveniencia
    public void removeItem(ItemPedido item) {
        items.remove(item);
        item.setPedido(null);
        calcularTotal();
    }

    public void calcularTotal() {
        this.total = items.stream()
                .map(it -> it.getPrecioUnitario().multiply(BigDecimal.valueOf(it.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //-----GETTERS AND SETTERS-----
    public Long getId() {
        return id;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemPedido> getItems() {
        return items;
    }
}
