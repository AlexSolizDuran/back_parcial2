package com.trendora.tienda.service.seeding;

import com.opencsv.CSVReader;
import com.trendora.tienda.producto.model.Marca;
import com.trendora.tienda.producto.model.Modelo;
import com.trendora.tienda.producto.repository.MarcaRepository;
import com.trendora.tienda.producto.repository.ModeloRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;

@Service
public class ModeloSeedingService {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;
    private final ResourceLoader resourceLoader;

    public ModeloSeedingService(ModeloRepository modeloRepository, MarcaRepository marcaRepository, ResourceLoader resourceLoader) {
        this.modeloRepository = modeloRepository;
        this.marcaRepository = marcaRepository;
        this.resourceLoader = resourceLoader;
    }

    public void seedModelos() {
        if (modeloRepository.count() > 0) {
            System.out.println("La base de datos de modelos ya contiene datos.");
            return;
        }

        System.out.println("Poblando base de datos de modelos desde CSV...");
        int contador = 0;

        try {
            Resource resource = resourceLoader.getResource("classpath:data/modelos_data.csv");
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {

                List<String[]> lineas = csvReader.readAll();

                for (String[] linea : lineas) {
                    String nombre = linea[0];
                    String marcaNombre = linea[1];

                    Optional<Marca> marcaOpt = marcaRepository.findByNombre(marcaNombre);
                    if (marcaOpt.isPresent()) {
                        Modelo nuevoModelo = new Modelo();
                        nuevoModelo.setNombre(nombre);
                        nuevoModelo.setMarca(marcaOpt.get());
                        modeloRepository.save(nuevoModelo);
                        contador++;
                    } else {
                        System.err.println("ERROR: La marca '" + marcaNombre + "' para el modelo '" + nombre + "' no se ha encontrado.");
                    }
                }
            }
            System.out.println("Se cargaron " + contador + " modelos desde el CSV.");
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar modelos desde CSV: " + e.getMessage(), e);
        }
    }
}
