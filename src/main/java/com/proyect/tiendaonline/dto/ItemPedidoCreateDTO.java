package com.proyect.tiendaonline.dto;

public class ItemPedidoCreateDTO {

    private Long productoId;

    private Integer cantidad;

    public ItemPedidoCreateDTO() {
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
