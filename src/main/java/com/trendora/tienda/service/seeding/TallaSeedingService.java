package com.trendora.tienda.service.seeding;

import com.opencsv.CSVReader;
import com.trendora.tienda.inventario.model.Talla;
import com.trendora.tienda.inventario.repository.TallaRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class TallaSeedingService {

    private final TallaRepository tallaRepository;
    private final ResourceLoader resourceLoader;

    public TallaSeedingService(TallaRepository tallaRepository, ResourceLoader resourceLoader) {
        this.tallaRepository = tallaRepository;
        this.resourceLoader = resourceLoader;
    }

    public void seedTallas() {
        if (tallaRepository.count() > 0) {
            System.out.println("La base de datos de tallas ya contiene datos.");
            return;
        }

        System.out.println("Poblando base de datos de tallas desde CSV...");
        int contador = 0;

        try {
            Resource resource = resourceLoader.getResource("classpath:data/tallas_data.csv");
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {

                List<String[]> lineas = csvReader.readAll();

                for (String[] linea : lineas) {
                    String tallaStr = linea[0];
                    Talla nuevaTalla = new Talla();
                    nuevaTalla.setTalla(tallaStr);
                    tallaRepository.save(nuevaTalla);
                    contador++;
                }
            }
            System.out.println("Se cargaron " + contador + " tallas desde el CSV.");
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar tallas desde CSV: " + e.getMessage(), e);
        }
    }
}
