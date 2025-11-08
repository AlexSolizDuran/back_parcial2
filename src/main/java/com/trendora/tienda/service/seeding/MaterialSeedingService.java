package com.trendora.tienda.service.seeding;

import com.opencsv.CSVReader;
import com.trendora.tienda.producto.model.Material;
import com.trendora.tienda.producto.repository.MaterialRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class MaterialSeedingService {

    private final MaterialRepository materialRepository;
    private final ResourceLoader resourceLoader;

    public MaterialSeedingService(MaterialRepository materialRepository, ResourceLoader resourceLoader) {
        this.materialRepository = materialRepository;
        this.resourceLoader = resourceLoader;
    }

    public void seedMateriales() {
        if (materialRepository.count() > 0) {
            System.out.println("La base de datos de materiales ya contiene datos.");
            return;
        }

        System.out.println("Poblando base de datos de materiales desde CSV...");
        int contador = 0;

        try {
            Resource resource = resourceLoader.getResource("classpath:data/materiales_data.csv");
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {

                List<String[]> lineas = csvReader.readAll();

                for (String[] linea : lineas) {
                    String nombre = linea[0];
                    Material nuevoMaterial = new Material();
                    nuevoMaterial.setNombre(nombre);
                    materialRepository.save(nuevoMaterial);
                    contador++;
                }
            }
            System.out.println("Se cargaron " + contador + " materiales desde el CSV.");
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar materiales desde CSV: " + e.getMessage(), e);
        }
    }
}
