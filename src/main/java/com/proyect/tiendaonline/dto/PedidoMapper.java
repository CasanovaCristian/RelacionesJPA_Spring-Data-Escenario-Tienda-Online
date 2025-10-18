package com.proyect.tiendaonline.dto;

import com.proyect.tiendaonline.entity.ItemPedido;
import com.proyect.tiendaonline.entity.Pedido;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoMapper {

    public static ItemPedidoResponseDTO toItemDTO(ItemPedido item) {
        if (item == null) return null;
        ItemPedidoResponseDTO dto = new ItemPedidoResponseDTO();
        dto.setId(item.getId());
        if (item.getProducto() != null) {
            dto.setProductoId(item.getProducto().getId());
            dto.setProductoNombre(item.getProducto().getNombre());
        }
        dto.setCantidad(item.getCantidad());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setSubtotal(item.getPrecioUnitario() != null ? item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())) : BigDecimal.ZERO);
        return dto;
    }

    public static PedidoResponseDTO toDTO(Pedido pedido) {
        if (pedido == null) return null;
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setFecha(pedido.getFecha());
        dto.setEstado(pedido.getEstado());
        dto.setTotal(pedido.getTotal());
        dto.setClienteId(pedido.getCliente() != null ? pedido.getCliente().getId() : null);
        List<ItemPedidoResponseDTO> items = pedido.getItems().stream().map(PedidoMapper::toItemDTO).collect(Collectors.toList());
        dto.setItems(items);
        return dto;
    }
}

