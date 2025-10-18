package com.proyect.tiendaonline.dto;

import java.util.List;

public class PedidoCreateDTO {

    private Long clienteId;

    private List<ItemPedidoCreateDTO> items;

    public PedidoCreateDTO() {
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemPedidoCreateDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemPedidoCreateDTO> items) {
        this.items = items;
    }
}
