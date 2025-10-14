package com.proyect.tiendaonline.repository;

import com.proyect.tiendaonline.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    //Una cosa nueva que ví es que Pageable permite paginacion automatica incluye el page, size, sort.
    //Buscar productos por nombre de categoria ya que tiene cardinacion ManyToMany
    @Query("SELECT p FROM Producto p JOIN p.categorias c WHERE c.nombre = :nombre")
    Page<Producto> findByCategoriaNombre(@Param("nombre") String nombre, Pageable pageable);
}
