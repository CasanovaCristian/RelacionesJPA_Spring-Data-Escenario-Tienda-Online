package com.proyect.tiendaonline;

import com.proyect.tiendaonline.dto.ItemPedidoCreateDTO;
import com.proyect.tiendaonline.dto.PedidoCreateDTO;
import com.proyect.tiendaonline.dto.ClienteDTO;
import com.proyect.tiendaonline.dto.DireccionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    @Autowired
    private TestRestTemplate rest;

    private HttpHeaders jsonHeaders;

    @BeforeEach
    void setup() {
        jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void crearClienteConDireccionYLeerPorGet() {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setNombre("Juan Perez");
        cliente.setEmail("juan@example.com");
        DireccionDTO dir = new DireccionDTO();
        dir.setCalle("Av Siempre Viva 123");
        dir.setCiudad("Springfield");
        dir.setPais("Pais");
        dir.setZip("12345");
        cliente.setDireccion(dir);

        ResponseEntity<ClienteDTO> postResp = rest.postForEntity("/api/clientes/con-direccion", cliente, ClienteDTO.class);
        assertThat(postResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ClienteDTO creado = postResp.getBody();
        assertThat(creado).isNotNull();
        assertThat(creado.getId()).isNotNull();
        Long id = creado.getId();

        ResponseEntity<ClienteDTO> getResp = rest.getForEntity("/api/clientes/" + id, ClienteDTO.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        ClienteDTO leido = getResp.getBody();
        assertThat(leido).isNotNull();
        assertThat(leido.getNombre()).isEqualTo("Juan Perez");
        assertThat(leido.getDireccion()).isNotNull();
        assertThat(leido.getDireccion().getCalle()).isEqualTo("Av Siempre Viva 123");
    }

    @Test
    void crearProductosYCategoriasYListarPorCategoria() {
        // Crear categoría
        Map<String, String> cat = Collections.singletonMap("nombre", "Electronica");
        ResponseEntity<Map> catResp = rest.postForEntity("/api/categorias", cat, Map.class);
        assertThat(catResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Number catId = (Number) catResp.getBody().get("id");

        // Crear producto
        Map<String, Object> prod = Map.of(
                "nombre", "Auriculares",
                "precio", 59.99,
                "stock", 50
        );
        ResponseEntity<Map> prodResp = rest.postForEntity("/api/productos", prod, Map.class);
        assertThat(prodResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Number prodId = (Number) prodResp.getBody().get("id");

        // Asociar categoria al producto
        HttpEntity<List<Long>> categoriasEntity = new HttpEntity<>(List.of(catId.longValue()), jsonHeaders);
        ResponseEntity<Map> assocResp = rest.postForEntity("/api/productos/" + prodId.longValue() + "/categorias", categoriasEntity, Map.class);
        assertThat(assocResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Listar productos por categoría
        ResponseEntity<String> listaResp = rest.getForEntity("/api/productos?categoria=Electronica", String.class);
        assertThat(listaResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        String body = listaResp.getBody();
        assertThat(body).contains("Auriculares");
    }

    @Test
    void crearPedidoCon2ItemsVerificarStockYTotal() {
        // Crear cliente
        ClienteDTO cliente = new ClienteDTO();
        cliente.setNombre("Cliente Pedido");
        cliente.setEmail("pedido@example.com");
        ResponseEntity<ClienteDTO> cResp = rest.postForEntity("/api/clientes", cliente, ClienteDTO.class);
        Long clienteId = cResp.getBody().getId();

        // Crear productos
        Map<String, Object> p1 = Map.of("nombre", "Prod A", "precio", 10.0, "stock", 10);
        Map<String, Object> p2 = Map.of("nombre", "Prod B", "precio", 5.0, "stock", 5);
        Number id1 = ((Number) rest.postForEntity("/api/productos", p1, Map.class).getBody().get("id"));
        Number id2 = ((Number) rest.postForEntity("/api/productos", p2, Map.class).getBody().get("id"));

        // Crear pedido con 2 items
        ItemPedidoCreateDTO it1 = new ItemPedidoCreateDTO(); it1.setProductoId(id1.longValue()); it1.setCantidad(2);
        ItemPedidoCreateDTO it2 = new ItemPedidoCreateDTO(); it2.setProductoId(id2.longValue()); it2.setCantidad(1);
        PedidoCreateDTO pedido = new PedidoCreateDTO();
        pedido.setClienteId(clienteId);
        pedido.setItems(List.of(it1, it2));

        HttpEntity<PedidoCreateDTO> pedidoEntity = new HttpEntity<>(pedido, jsonHeaders);
        ResponseEntity<Map> resp = rest.postForEntity("/api/pedidos", pedidoEntity, Map.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Map body = resp.getBody();
        Number pedidoId = (Number) body.get("id");
        // Verificar total calculado = 2*10 + 1*5 = 25
        assertThat(new BigDecimal(body.get("total").toString())).isEqualByComparingTo(new BigDecimal("25"));

        // Verificar stock decrementado
        Map prod1 = rest.getForEntity("/api/productos/" + id1.longValue(), Map.class).getBody();
        Map prod2 = rest.getForEntity("/api/productos/" + id2.longValue(), Map.class).getBody();
        assertThat(((Number) prod1.get("stock")).intValue()).isEqualTo(8);
        assertThat(((Number) prod2.get("stock")).intValue()).isEqualTo(4);
    }

    @Test
    void agregarMismoProductoDosVecesEnPedidoDebeFallar() {
        // Crear cliente
        ClienteDTO cliente = new ClienteDTO(); cliente.setNombre("Cliente2"); cliente.setEmail("c2@example.com");
        Long clienteId = rest.postForEntity("/api/clientes", cliente, ClienteDTO.class).getBody().getId();
        // Crear producto
        Map<String, Object> p = Map.of("nombre", "ProdX", "precio", 3.0, "stock", 10);
        Long pid = ((Number) rest.postForEntity("/api/productos", p, Map.class).getBody().get("id")).longValue();

        // Crear pedido con el mismo producto dos veces
        ItemPedidoCreateDTO it1 = new ItemPedidoCreateDTO(); it1.setProductoId(pid); it1.setCantidad(1);
        ItemPedidoCreateDTO it2 = new ItemPedidoCreateDTO(); it2.setProductoId(pid); it2.setCantidad(2);
        PedidoCreateDTO pedido = new PedidoCreateDTO(); pedido.setClienteId(clienteId); pedido.setItems(List.of(it1, it2));

        HttpEntity<PedidoCreateDTO> pedidoEntity = new HttpEntity<>(pedido, jsonHeaders);
        ResponseEntity<String> resp = rest.postForEntity("/api/pedidos", pedidoEntity, String.class);
        // Debe fallar (RuntimeException -> 500)
        assertThat(resp.getStatusCode().is4xxClientError() || resp.getStatusCode().is5xxServerError()).isTrue();
    }

    @Test
    void cambiarEstadoPedidoYValidarReglas() {
        // Crear cliente
        ClienteDTO cliente = new ClienteDTO(); cliente.setNombre("Cliente3"); cliente.setEmail("c3@example.com");
        Long clienteId = rest.postForEntity("/api/clientes", cliente, ClienteDTO.class).getBody().getId();
        // Crear producto
        Map<String, Object> p = Map.of("nombre", "ProdY", "precio", 2.0, "stock", 5);
        Long pid = ((Number) rest.postForEntity("/api/productos", p, Map.class).getBody().get("id")).longValue();

        // Crear pedido con 1 item
        ItemPedidoCreateDTO it = new ItemPedidoCreateDTO(); it.setProductoId(pid); it.setCantidad(2);
        PedidoCreateDTO pedido = new PedidoCreateDTO(); pedido.setClienteId(clienteId); pedido.setItems(List.of(it));
        Long pedidoId = ((Number) rest.postForEntity("/api/pedidos", new HttpEntity<>(pedido, jsonHeaders), Map.class).getBody().get("id")).longValue();

        // Stock quedó en 3
        Map prodBefore = rest.getForEntity("/api/productos/" + pid, Map.class).getBody();
        assertThat(((Number) prodBefore.get("stock")).intValue()).isEqualTo(3);

        // Cambiar estado a CANCELADO
        HttpEntity<Void> empty = new HttpEntity<>(null, jsonHeaders);
        ResponseEntity<Map> cambio = rest.exchange("/api/pedidos/" + pedidoId + "/estado?estado=CANCELADO", HttpMethod.PUT, empty, Map.class);
        assertThat(cambio.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map pedidoResp = cambio.getBody();
        assertThat(pedidoResp.get("estado").toString()).isEqualTo("CANCELADO");

        // Verificar stock fue revertido a 5
        Map prodAfter = rest.getForEntity("/api/productos/" + pid, Map.class).getBody();
        assertThat(((Number) prodAfter.get("stock")).intValue()).isEqualTo(5);
    }
}

