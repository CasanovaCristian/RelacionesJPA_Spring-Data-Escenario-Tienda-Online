package com.proyect.tiendaonline.controller;

import com.proyect.tiendaonline.dto.ClienteDTO;
import com.proyect.tiendaonline.dto.ClienteMapper;
import com.proyect.tiendaonline.entity.Cliente;
import com.proyect.tiendaonline.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> obtenerTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        List<ClienteDTO> dtos = clientes.stream().map(ClienteMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(ClienteMapper.toDTO(cliente));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crear(@Validated @RequestBody ClienteDTO clienteDto) {
        Cliente entidad = ClienteMapper.toEntity(clienteDto);
        Cliente nuevoCliente = clienteService.crear(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toDTO(nuevoCliente));
    }

    // Crear cliente con dirección
    @PostMapping("/con-direccion")
    public ResponseEntity<ClienteDTO> crearClienteConDireccion(@Validated @RequestBody ClienteDTO clienteDto) {
        Cliente entidad = ClienteMapper.toEntity(clienteDto);
        Cliente nuevoCliente = clienteService.crearClienteConDireccion(entidad, entidad.getDireccion());
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toDTO(nuevoCliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizar(@PathVariable Long id, @Validated @RequestBody ClienteDTO clienteDto) {
        Cliente entidad = ClienteMapper.toEntity(clienteDto);
        Cliente clienteActualizado = clienteService.actualizar(id, entidad);
        return ResponseEntity.ok(ClienteMapper.toDTO(clienteActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Eliminar cliente (alias)
    @DeleteMapping("/cliente/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
