package com.trendora.tienda.service.seeding;

import com.opencsv.CSVReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.trendora.tienda.inventario.model.Color;
import com.trendora.tienda.inventario.model.ProdVariante;
import com.trendora.tienda.inventario.model.Talla;
import com.trendora.tienda.inventario.repository.ColorRepository;
import com.trendora.tienda.inventario.repository.ProdVarianteRepository;
import com.trendora.tienda.inventario.repository.TallaRepository;
import com.trendora.tienda.producto.model.Producto;
import com.trendora.tienda.producto.repository.ProductoRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdVarianteSeedingService {

    private final ProdVarianteRepository prodVarianteRepository;
    private final ProductoRepository productoRepository;
    private final ColorRepository colorRepository;
    private final TallaRepository tallaRepository;
    private final ResourceLoader resourceLoader;

    public ProdVarianteSeedingService(ProdVarianteRepository prodVarianteRepository, ProductoRepository productoRepository, ColorRepository colorRepository, TallaRepository tallaRepository, ResourceLoader resourceLoader) {
        this.prodVarianteRepository = prodVarianteRepository;
        this.productoRepository = productoRepository;
        this.colorRepository = colorRepository;
        this.tallaRepository = tallaRepository;
        this.resourceLoader = resourceLoader;
    }

    @Transactional
    public void seedProdVariantes() {
        if (prodVarianteRepository.count() > 0) {
            System.out.println("La base de datos de prod_variante ya contiene datos.");
            return;
        }

        System.out.println("Poblando base de datos de prod_variante desde CSV...");
        int contador = 0;

        try {
            Resource resource = resourceLoader.getResource("classpath:data/prod_variantes_data.csv");
            try (Reader reader = new InputStreamReader(resource.getInputStream())) {

                CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build();

                List<String[]> lineas = csvReader.readAll();

                for (String[] linea : lineas) {
                    String productoDescripcion = linea[0];
                    String colorNombre = linea[1];
                    String tallaTalla = linea[2];
                    BigDecimal costo = new BigDecimal(linea[3]);
                    BigDecimal ppp = new BigDecimal(linea[4]);
                    BigDecimal precio = new BigDecimal(linea[5]);
                    String sku = linea[6];
                    Integer stock = Integer.parseInt(linea[7]);

                    Producto producto = productoRepository.findByDescripcion(productoDescripcion).orElse(null);
                    Color color = colorRepository.findByNombre(colorNombre).orElse(null);
                    Talla talla = tallaRepository.findByTalla(tallaTalla).orElse(null);

                    if (producto == null || color == null || talla == null) {
                        System.err.println("Error: No se pudo encontrar alguna de las entidades relacionadas para el SKU: " + sku);
                        continue;
                    }

                    ProdVariante nuevaVariante = new ProdVariante();
                    nuevaVariante.setProducto(producto);
                    nuevaVariante.setColor(color);
                    nuevaVariante.setTalla(talla);
                    nuevaVariante.setCosto(costo);
                    nuevaVariante.setPpp(ppp);
                    nuevaVariante.setPrecio(precio);
                    nuevaVariante.setSku(sku);
                    nuevaVariante.setStock(stock);

                    prodVarianteRepository.save(nuevaVariante);
                    contador++;
                }
            }
            System.out.println("Se cargaron " + contador + " variantes de producto desde el CSV.");
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar variantes de producto desde CSV: " + e.getMessage(), e);
        }
    }
}
