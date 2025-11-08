package com.trendora.tienda.service.seeding;

import com.opencsv.CSVReader;
import com.trendora.tienda.producto.model.Marca;
import com.trendora.tienda.producto.repository.MarcaRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class MarcaSeedingService {

    private final MarcaRepository marcaRepository;
    private final ResourceLoader resourceLoader;

    public MarcaSeedingService(MarcaRepository marcaRepository, ResourceLoader resourceLoader) {
        this.marcaRepository = marcaRepository;
        this.resourceLoader = resourceLoader;
    }

    public void seedMarcas() {
        if (marcaRepository.count() > 0) {
            System.out.println("La base de datos de marcas ya contiene datos.");
            return;
        }

        System.out.println("Poblando base de datos de marcas desde CSV...");
        int contador = 0;

        try {
            Resource resource = resourceLoader.getResource("classpath:data/marcas_data.csv");
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {

                List<String[]> lineas = csvReader.readAll();

                for (String[] linea : lineas) {
                    String nombre = linea[0];
                    Marca nuevaMarca = new Marca();
                    nuevaMarca.setNombre(nombre);
                    marcaRepository.save(nuevaMarca);
                    contador++;
                }
            }
            System.out.println("Se cargaron " + contador + " marcas desde el CSV.");
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar marcas desde CSV: " + e.getMessage(), e);
        }
    }
}
