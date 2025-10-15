package com.proyect.tiendaonline.service;

import com.proyect.tiendaonline.entity.Cliente;
import com.proyect.tiendaonline.entity.ItemPedido;
import com.proyect.tiendaonline.entity.Pedido;
import com.proyect.tiendaonline.entity.Producto;
import com.proyect.tiendaonline.entity.enums.EstadoPedido;
import com.proyect.tiendaonline.repository.ClienteRepository;
import com.proyect.tiendaonline.repository.PedidoRepository;
import com.proyect.tiendaonline.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    //Listar todos los pedidos
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    //Obtener pedido por ID
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    //Crear pedido
    @Transactional
    public Pedido crearPedido(Long clienteId, List<ItemPedido> items) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Pedido pedido = new Pedido(cliente);

        for (ItemPedido item : items) {
            Producto producto = productoRepository.findById(item.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
            pedido.addItem(producto, item.getCantidad());
        }

        pedido.calcularTotal();
        return pedidoRepository.save(pedido);
    }

    //Cambiar estado del pedido
    @Transactional
    public Pedido cambiarEstado(Long pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = obtenerPorId(pedidoId);

        if (pedido.getEstado() == EstadoPedido.ENVIADO && nuevoEstado == EstadoPedido.NUEVO) {
            throw new RuntimeException("No se puede revertir un pedido ENVIADO a NUEVO");
        }

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    //Eliminar pedido
    @Transactional
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}
