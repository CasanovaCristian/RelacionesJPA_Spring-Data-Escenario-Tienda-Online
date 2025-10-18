package com.proyect.tiendaonline.controller;

import com.proyect.tiendaonline.dto.ItemPedidoCreateDTO;
import com.proyect.tiendaonline.dto.PedidoCreateDTO;
import com.proyect.tiendaonline.dto.PedidoMapper;
import com.proyect.tiendaonline.dto.PedidoResponseDTO;
import com.proyect.tiendaonline.entity.ItemPedido;
import com.proyect.tiendaonline.entity.Pedido;
import com.proyect.tiendaonline.entity.Producto;
import com.proyect.tiendaonline.entity.enums.EstadoPedido;
import com.proyect.tiendaonline.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> obtenerTodos() {
        List<Pedido> pedidos = pedidoService.listarTodos();
        List<PedidoResponseDTO> dtos = new ArrayList<>();
        for (Pedido p : pedidos) dtos.add(PedidoMapper.toDTO(p));
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtenerPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.obtenerPorId(id);
        return ResponseEntity.ok(PedidoMapper.toDTO(pedido));
    }

    // Crear pedido a partir de DTO
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crear(@Validated @RequestBody PedidoCreateDTO pedidoDto) {
        // Convertir ItemPedidoCreateDTO -> ItemPedido
        List<ItemPedido> items = new ArrayList<>();
        if (pedidoDto.getItems() != null) {
            for (ItemPedidoCreateDTO itDto : pedidoDto.getItems()) {
                ItemPedido it = new ItemPedido();
                Producto prod = new Producto();
                prod.setId(itDto.getProductoId());
                it.setProducto(prod);
                it.setCantidad(itDto.getCantidad());
                items.add(it);
            }
        }
        Pedido nuevoPedido = pedidoService.crearPedido(pedidoDto.getClienteId(), items);
        return ResponseEntity.status(HttpStatus.CREATED).body(PedidoMapper.toDTO(nuevoPedido));
    }

    // Crear pedido con items para un cliente (POST /api/pedidos/clientes/{clienteId})
    @PostMapping("/clientes/{clienteId}")
    public ResponseEntity<PedidoResponseDTO> crearPedido(@PathVariable Long clienteId, @Validated @RequestBody List<ItemPedidoCreateDTO> itemsDto) {
        List<ItemPedido> items = new ArrayList<>();
        if (itemsDto != null) {
            for (ItemPedidoCreateDTO itDto : itemsDto) {
                ItemPedido it = new ItemPedido();
                Producto prod = new Producto();
                prod.setId(itDto.getProductoId());
                it.setProducto(prod);
                it.setCantidad(itDto.getCantidad());
                items.add(it);
            }
        }
        Pedido nuevoPedido = pedidoService.crearPedido(clienteId, items);
        return ResponseEntity.status(HttpStatus.CREATED).body(PedidoMapper.toDTO(nuevoPedido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> actualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
        Pedido pedidoActualizado = pedidoService.actualizar(id, pedido);
        return ResponseEntity.ok(PedidoMapper.toDTO(pedidoActualizado));
    }

    // Cambiar estado del pedido
    @PutMapping("/{id}/estado")
    public ResponseEntity<PedidoResponseDTO> cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado) {
        Pedido pedidoActualizado = pedidoService.cambiarEstado(id, estado);
        return ResponseEntity.ok(PedidoMapper.toDTO(pedidoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
