package com.trendora.tienda.service.seeding;

import com.opencsv.CSVReader;
import com.trendora.tienda.producto.model.Etiqueta;
import com.trendora.tienda.producto.repository.EtiquetaRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class EtiquetaSeedingService {

    private final EtiquetaRepository etiquetaRepository;
    private final ResourceLoader resourceLoader;

    public EtiquetaSeedingService(EtiquetaRepository etiquetaRepository, ResourceLoader resourceLoader) {
        this.etiquetaRepository = etiquetaRepository;
        this.resourceLoader = resourceLoader;
    }

    public void seedEtiquetas() {
        if (etiquetaRepository.count() > 0) {
            System.out.println("La base de datos de etiquetas ya contiene datos.");
            return;
        }

        System.out.println("Poblando base de datos de etiquetas desde CSV...");
        int contador = 0;

        try {
            Resource resource = resourceLoader.getResource("classpath:data/etiquetas_data.csv");
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {

                List<String[]> lineas = csvReader.readAll();

                for (String[] linea : lineas) {
                    String nombre = linea[0];
                    Etiqueta nuevaEtiqueta = new Etiqueta();
                    nuevaEtiqueta.setNombre(nombre);
                    etiquetaRepository.save(nuevaEtiqueta);
                    contador++;
                }
            }
            System.out.println("Se cargaron " + contador + " etiquetas desde el CSV.");
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar etiquetas desde CSV: " + e.getMessage(), e);
        }
    }
}
