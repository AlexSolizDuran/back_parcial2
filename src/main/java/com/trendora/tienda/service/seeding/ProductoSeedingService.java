package com.trendora.tienda.service.seeding;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.trendora.tienda.producto.model.Categoria;
import com.trendora.tienda.producto.model.Etiqueta;
import com.trendora.tienda.producto.model.Material;
import com.trendora.tienda.producto.model.Modelo;
import com.trendora.tienda.producto.model.Producto;
import com.trendora.tienda.producto.repository.CategoriaRepository;
import com.trendora.tienda.producto.repository.EtiquetaRepository;
import com.trendora.tienda.producto.repository.MaterialRepository;
import com.trendora.tienda.producto.repository.ModeloRepository;
import com.trendora.tienda.producto.repository.ProductoRepository;

@Service
public class ProductoSeedingService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ModeloRepository modeloRepository;
    private final MaterialRepository materialRepository;
    private final EtiquetaRepository etiquetaRepository;
    private final ResourceLoader resourceLoader;

    public ProductoSeedingService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository, ModeloRepository modeloRepository, MaterialRepository materialRepository, EtiquetaRepository etiquetaRepository, ResourceLoader resourceLoader) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.modeloRepository = modeloRepository;
        this.materialRepository = materialRepository;
        this.etiquetaRepository = etiquetaRepository;
        this.resourceLoader = resourceLoader;
    }

    @Transactional
    public void seedProductos() {
        if (productoRepository.count() > 0) {
            System.out.println("La base de datos de productos ya contiene datos.");
            return;
        }

        System.out.println("Poblando base de datos de productos desde CSV...");
        int contador = 0;

        try {
            Resource resource = resourceLoader.getResource("classpath:data/productos_data.csv");
            try (Reader reader = new InputStreamReader(resource.getInputStream())) {
                
                CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build();

                List<String[]> lineas = csvReader.readAll();

                for (String[] linea : lineas) {
                    String descripcion = linea[0];
                    String categoriaNombre = linea[1];
                    String modeloNombre = linea[2];
                    String materialNombre = linea[3];
                    String[] etiquetaNombres = linea[4].split(",");

                    Categoria categoria = categoriaRepository.findByNombre(categoriaNombre).orElse(null);
                    Modelo modelo = modeloRepository.findByNombre(modeloNombre).orElse(null);
                    Material material = materialRepository.findByNombre(materialNombre).orElse(null);

                    if (categoria == null || modelo == null || material == null) {
                        System.err.println("Error: No se pudo encontrar alguna de las entidades relacionadas para el producto: " + descripcion);
                        continue;
                    }

                    Set<Etiqueta> etiquetas = new HashSet<>();
                    for (String etiquetaNombre : etiquetaNombres) {
                        etiquetaRepository.findByNombre(etiquetaNombre.trim()).ifPresent(etiquetas::add);
                    }

                    Producto nuevoProducto = new Producto();
                    nuevoProducto.setDescripcion(descripcion);
                    nuevoProducto.setCategoria(categoria);
                    nuevoProducto.setModelo(modelo);
                    nuevoProducto.setMaterial(material);
                    nuevoProducto.setEtiquetas(etiquetas);

                    productoRepository.save(nuevoProducto);
                    contador++;
                }
            }
            System.out.println("Se cargaron " + contador + " productos desde el CSV.");
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar productos desde CSV: " + e.getMessage(), e);
        }
    }
}
