package com.proyect.tiendaonline.dto;

import com.proyect.tiendaonline.entity.enums.EstadoPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDTO {
    private Long id;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private BigDecimal total;
    private Long clienteId;
    private List<ItemPedidoResponseDTO> items;

    public PedidoResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public List<ItemPedidoResponseDTO> getItems() { return items; }
    public void setItems(List<ItemPedidoResponseDTO> items) { this.items = items; }
}

