package com.trendora.tienda.service.seeding;

import com.opencsv.CSVReader;
import com.trendora.tienda.inventario.model.Color;
import com.trendora.tienda.inventario.repository.ColorRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class ColorSeedingService {

    private final ColorRepository colorRepository;
    private final ResourceLoader resourceLoader;

    public ColorSeedingService(ColorRepository colorRepository, ResourceLoader resourceLoader) {
        this.colorRepository = colorRepository;
        this.resourceLoader = resourceLoader;
    }

    public void seedColores() {
        if (colorRepository.count() > 0) {
            System.out.println("La base de datos de colores ya contiene datos.");
            return;
        }

        System.out.println("Poblando base de datos de colores desde CSV...");
        int contador = 0;

        try {
            Resource resource = resourceLoader.getResource("classpath:data/colores_data.csv");
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {

                List<String[]> lineas = csvReader.readAll();

                for (String[] linea : lineas) {
                    String nombre = linea[0];
                    String codHexa = linea[1];
                    Color nuevoColor = new Color();
                    nuevoColor.setNombre(nombre);
                    nuevoColor.setCodHexa(codHexa);
                    colorRepository.save(nuevoColor);
                    contador++;
                }
            }
            System.out.println("Se cargaron " + contador + " colores desde el CSV.");
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar colores desde CSV: " + e.getMessage(), e);
        }
    }
}
