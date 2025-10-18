package com.proyect.tiendaonline.dto;

import com.proyect.tiendaonline.entity.Cliente;
import com.proyect.tiendaonline.entity.Direccion;

public class ClienteMapper {

    public static ClienteDTO toDTO(Cliente cliente) {
        if (cliente == null) return null;
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        if (cliente.getDireccion() != null) {
            DireccionDTO d = new DireccionDTO();
            d.setId(cliente.getDireccion().getId());
            d.setCalle(cliente.getDireccion().getCalle());
            d.setCiudad(cliente.getDireccion().getCiudad());
            d.setPais(cliente.getDireccion().getPais());
            d.setZip(cliente.getDireccion().getZip());
            dto.setDireccion(d);
        }
        return dto;
    }

    public static Cliente toEntity(ClienteDTO dto) {
        if (dto == null) return null;
        Cliente c = new Cliente();
        c.setNombre(dto.getNombre());
        c.setEmail(dto.getEmail());
        if (dto.getDireccion() != null) {
            Direccion d = new Direccion();
            d.setCalle(dto.getDireccion().getCalle());
            d.setCiudad(dto.getDireccion().getCiudad());
            d.setPais(dto.getDireccion().getPais());
            d.setZip(dto.getDireccion().getZip());
            c.setDireccion(d);
        }
        return c;
    }
}

