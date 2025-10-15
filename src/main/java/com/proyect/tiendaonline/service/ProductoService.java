package com.proyect.tiendaonline.service;

import com.proyect.tiendaonline.entity.Categoria;
import com.proyect.tiendaonline.entity.Producto;
import com.proyect.tiendaonline.repository.CategoriaRepository;
import com.proyect.tiendaonline.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    //Listar productos con paginado (lo nuevo que ví)
    public Page<Producto> listarPorCategoria(String categoria, Pageable pageable) {
        return productoRepository.findByCategoriaNombre(categoria, pageable);
    }

    //Listar todo
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    //Buscar producto por id
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    //Crear producto
    @Transactional
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    //Actualizar producto
    @Transactional
    public Producto actualizarProducto(Long id, Producto datos) {
        Producto producto = obtenerPorId(id);
        producto.setNombre(datos.getNombre());
        producto.setPrecio(datos.getPrecio());
        producto.setStock(datos.getStock());
        return productoRepository.save(producto);
    }

    //Eliminar producto
    @Transactional
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    //Asignar o crear categorias
    @Transactional
    public Producto asignarCateforias(Long productoId, Set<String> nombresCategorias) {
        Producto producto = obtenerPorId(productoId);
        Set<Categoria> categorias = new HashSet<>();

        for (String nombre : nombresCategorias) {
            Categoria categoria = categoriaRepository.findByNombre(nombre).orElseGet(() -> categoriaRepository.save(new Categoria(nombre)));
            categorias.add(categoria);
        }
        producto.getCategorias().clear();
        categorias.forEach(producto::addCategoria);
        return productoRepository.save(producto);
    }
}
