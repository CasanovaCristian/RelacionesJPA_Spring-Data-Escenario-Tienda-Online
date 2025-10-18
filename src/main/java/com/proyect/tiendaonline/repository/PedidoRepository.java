package com.proyect.tiendaonline.repository;

import com.proyect.tiendaonline.entity.Cliente;
import com.proyect.tiendaonline.entity.Pedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    //Cargar pedido con items y productos asociados
    List<Pedido> findAllBy();

    //Buscar pedidos de un cliente especifico
    List<Pedido> findByCliente(Cliente cliente);

    //@Query permite escribir consultas JPQL personalizadas
    //Consulta personalizada para obtener total por cliente
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.cliente.id = :clienteId")
    BigDecimal calcularTotalPorCliente(@Param("clienteId") Long clienteId);

    // Cargar pedido con items y sus productos para evitar problemas de lazy loading al serializar
    @EntityGraph(attributePaths = {"items", "items.producto"})
    Optional<Pedido> findWithItemsById(Long id);
}
