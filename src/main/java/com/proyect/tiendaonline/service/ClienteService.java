package com.proyect.tiendaonline.service;

import com.proyect.tiendaonline.entity.Cliente;
import com.proyect.tiendaonline.entity.Direccion;
import com.proyect.tiendaonline.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    //Listar todos los clientes
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    //Buscar cliente por id
    public Cliente obtenerClienteId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    //Crear cliente con direccion
    @Transactional
    public Cliente crearClienteConDireccion(Cliente cliente, Direccion direccion) {
        cliente.setDireccion(direccion);
        return clienteRepository.save(cliente);
    }

    //Actualizar cliente
    @Transactional
    public Cliente actualizarCliente(Long id, Cliente datos) {
        Cliente cliente = obtenerClienteId(id);
        cliente.setNombre(datos.getNombre());
        cliente.setEmail(datos.getEmail());
        return clienteRepository.save(cliente);
    }

    //Eliminar cliente
    public void eliminarCliente(Long id) {
        Cliente cliente = obtenerClienteId(id);
        clienteRepository.delete(cliente);
    }

    //Buscar por email
    public Cliente buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    //Obtener cliente por su direccion (usando EntityGraph)
    public Cliente clienteConDireccion(Long id) {
        return clienteRepository.findWithDireccionById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }
}
