package com.proyect.tiendaonline.repository;

import com.proyect.tiendaonline.entity.Cliente;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Esto sirve para crear consultas personalizadsas solo declarando el metodo
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    //El Optional evita el error: NullPointerException
    //Buscar cliente por email
    Optional<Cliente> findByEmail(String email);

    //@EntityGraph fuerza a cargar la direccion sin romper el fetch = LAZY
    //Cargar cliente con direccion usando EntityGraph
    @EntityGraph(attributePaths = "direccion")
    Optional<Cliente> findWithDireccionById(Long id);

}
