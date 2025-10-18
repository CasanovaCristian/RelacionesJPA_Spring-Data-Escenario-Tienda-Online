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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        return pedidoRepository.findWithItemsById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    // Crear pedido con validación de stock y cálculo de totales (a partir de items y clienteId)
    @Transactional
    public Pedido crearPedido(Long clienteId, List<ItemPedido> items) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEstado(EstadoPedido.NUEVO);
        pedido.setFecha(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        Set<Long> productoIds = new HashSet<>();

        for (ItemPedido item : items) {
            Long productoId = item.getProducto().getId();
            if (productoIds.contains(productoId)) {
                throw new RuntimeException("Producto repetido en el pedido: " + productoId);
            }
            productoIds.add(productoId);

            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            item.setPedido(pedido);
            item.setPrecioUnitario(producto.getPrecio());
            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        pedido.setTotal(total);
        pedido.getItems().addAll(items);

        return pedidoRepository.save(pedido);
    }

    // Nuevo: crear pedido a partir de un objeto Pedido (controlador POST /api/pedidos)
    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getItems() == null || pedido.getItems().isEmpty()) {
            throw new RuntimeException("Pedido debe contener cliente y al menos un item");
        }

        Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        pedido.setCliente(cliente);
        pedido.setEstado(EstadoPedido.NUEVO);
        pedido.setFecha(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        Set<Long> productoIds = new HashSet<>();

        for (ItemPedido item : pedido.getItems()) {
            if (item.getProducto() == null || item.getProducto().getId() == null) {
                throw new RuntimeException("Cada item debe contener un producto con id");
            }
            Long productoId = item.getProducto().getId();
            if (productoIds.contains(productoId)) {
                throw new RuntimeException("Producto repetido en el pedido: " + productoId);
            }
            productoIds.add(productoId);

            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            item.setPedido(pedido);
            item.setPrecioUnitario(producto.getPrecio());
            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    // Cambiar estado del pedido
    @Transactional
    public Pedido cambiarEstado(Long pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = obtenerPorId(pedidoId);
        EstadoPedido estadoAnterior = pedido.getEstado();

        // Si se cancela, revertir stock
        if (nuevoEstado == EstadoPedido.CANCELADO && estadoAnterior != EstadoPedido.CANCELADO) {
            for (ItemPedido item : pedido.getItems()) {
                Producto producto = productoRepository.findById(item.getProducto().getId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado al revertir stock"));
                producto.setStock(producto.getStock() + item.getCantidad());
                productoRepository.save(producto);
            }
        }

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    //Eliminar pedido
    @Transactional
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }

    // Actualizar pedido (solo campos sencillos: estado y fecha). No actualiza items aquí.
    @Transactional
    public Pedido actualizar(Long id, Pedido datos) {
        Pedido pedido = obtenerPorId(id);
        if (datos.getEstado() != null) {
            pedido.setEstado(datos.getEstado());
        }
        if (datos.getFecha() != null) {
            pedido.setFecha(datos.getFecha());
        }
        // Recalcular total si viene vacío (aunque normalmente se gestiona al crear items)
        pedido.calcularTotal();
        return pedidoRepository.save(pedido);
    }
}
