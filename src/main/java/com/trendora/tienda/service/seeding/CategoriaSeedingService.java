package com.trendora.tienda.service.seeding;

import java.io.InputStreamReader;
import java.io.Reader;// Importa tu entidad Categoria
import java.util.HashMap; // Importa tu Repositorio de Categoria
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.trendora.tienda.producto.model.Categoria;
import com.trendora.tienda.producto.repository.CategoriaRepository;

@Service
public class CategoriaSeedingService {

    private final CategoriaRepository categoriaRepository;
    private final ResourceLoader resourceLoader;

    public CategoriaSeedingService(CategoriaRepository categoriaRepository,
                                   ResourceLoader resourceLoader) {
        this.categoriaRepository = categoriaRepository;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Método público que inicia la carga de categorías desde el CSV.
     */
    public void seedCategorias() {
        if (categoriaRepository.count() > 0) {
            System.out.println("La base de datos de categorías ya contiene datos.");
            return; // Salimos si ya hay categorías
        }

        System.out.println("Poblando base de datos de categorías desde CSV...");
        
        // Usamos un Map para rastrear las categorías que vamos guardando
        // Clave: Nombre (String), Valor: Entidad Categoria (con ID)
        Map<String, Categoria> categoriasGuardadasMap = new HashMap<>();
        int contador = 0;

        try {
            // Ubicamos y leemos el archivo CSV
            Resource resource = resourceLoader.getResource("classpath:data/categorias_data.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {

                List<String[]> lineas = csvReader.readAll();
                
                // Iteramos sobre cada línea del CSV
                for (String[] linea : lineas) {
                    String nombre = linea[0];
                    // El padreNombre puede estar vacío (para categorías raíz)
                    String padreNombre = (linea.length > 1 && !linea[1].isEmpty()) ? linea[1] : null;

                    Categoria nuevaCategoria = new Categoria();
                    nuevaCategoria.setNombre(nombre);

                    // --- Lógica de Jerarquía ---
                    if (padreNombre != null) {
                        // Si esta categoría tiene un padre, lo buscamos en nuestro Map
                        Categoria padre = categoriasGuardadasMap.get(padreNombre);
                        
                        if (padre == null) {
                            // ¡Error! El CSV no está ordenado (padre no encontrado)
                            System.err.println(
                                "ERROR: El padre '" + padreNombre + "' para la categoría '" + nombre + "' " +
                                "no se ha encontrado. Asegúrese que el CSV esté ordenado (padres primero)."
                            );
                            continue; // Saltar esta línea
                        }
                        
                        // Asignamos el padre (que ya tiene un ID)
                        nuevaCategoria.setPadre(padre);
                    }
                    // --------------------------

                    // Guardamos la nueva categoría UNA POR UNA
                    // para obtener su ID generado
                    Categoria categoriaGuardada = categoriaRepository.save(nuevaCategoria);
                    
                    // Añadimos la categoría recién guardada (con su ID) al Map,
                    // para que los futuros hijos puedan encontrarla.
                    categoriasGuardadasMap.put(categoriaGuardada.getNombre(), categoriaGuardada);
                    contador++;
                }
            } // El try-with-resources cierra los readers aquí

            System.out.println("Se cargaron " + contador + " categorías desde el CSV.");

        } catch (Exception e) {
            // Capturamos cualquier excepción (ej. archivo no encontrado, error de CSV)
            throw new RuntimeException("Error al cargar categorías desde CSV: " + e.getMessage(), e);
        }
    }
}
