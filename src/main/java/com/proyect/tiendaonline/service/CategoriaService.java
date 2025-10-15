package com.proyect.tiendaonline.service;

import com.proyect.tiendaonline.entity.Categoria;
import com.proyect.tiendaonline.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    //Listar todas
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    //Obtener por ID
    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    //Crear
    @Transactional
    public Categoria crear(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    //Actualizar
    @Transactional
    public Categoria actualizar(Long id, Categoria datos) {
        Categoria categoria = obtenerPorId(id);
        categoria.setNombre(datos.getNombre());
        return categoriaRepository.save(categoria);
    }

    //Eliminar
    @Transactional
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }
}
