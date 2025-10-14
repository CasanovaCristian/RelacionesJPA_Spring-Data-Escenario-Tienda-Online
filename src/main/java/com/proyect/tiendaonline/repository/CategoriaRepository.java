package com.proyect.tiendaonline.repository;

import com.proyect.tiendaonline.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    //"Verfica" si la categoria ya existe antes de crear una nueva
    Optional<Categoria> findByNombre(String nombre);
}
