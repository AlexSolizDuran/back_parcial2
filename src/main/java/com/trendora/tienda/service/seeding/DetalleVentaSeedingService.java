package com.trendora.tienda.service.seeding;

import com.opencsv.CSVReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.trendora.tienda.inventario.model.ProdVariante;
import com.trendora.tienda.inventario.repository.ProdVarianteRepository;
import com.trendora.tienda.venta.model.DetalleVenta;
import com.trendora.tienda.venta.model.Venta;
import com.trendora.tienda.venta.repository.DetalleVentaRepository;
import com.trendora.tienda.venta.repository.VentaRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class DetalleVentaSeedingService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final VentaRepository ventaRepository;
    private final ProdVarianteRepository prodVarianteRepository;
    private final ResourceLoader resourceLoader;

    public DetalleVentaSeedingService(DetalleVentaRepository detalleVentaRepository, VentaRepository ventaRepository, ProdVarianteRepository prodVarianteRepository, ResourceLoader resourceLoader) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaRepository = ventaRepository;
        this.prodVarianteRepository = prodVarianteRepository;
        this.resourceLoader = resourceLoader;
    }

    @Transactional
    public void seedDetalleVentas() {
        if (detalleVentaRepository.count() > 0) {
            System.out.println("La base de datos de detalle_venta ya contiene datos.");
            return;
        }

        System.out.println("Poblando base de datos de detalle_venta desde CSV...");
        int contador = 0;

        try {
            Resource resource = resourceLoader.getResource("classpath:data/detalle_ventas_data.csv");
            try (Reader reader = new InputStreamReader(resource.getInputStream())) {

                CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build();

                List<String[]> lineas = csvReader.readAll();

                for (String[] linea : lineas) {
                    Long numeroVenta = Long.parseLong(linea[0]);
                    String prodVarianteSku = linea[1];
                    Integer cantidad = Integer.parseInt(linea[2]);
                    Double precioUnitario = Double.parseDouble(linea[3]);
                    Double descuento = Double.parseDouble(linea[4]);
                    Double subtotal = Double.parseDouble(linea[5]);

                    Venta venta = ventaRepository.findByNumeroVenta(numeroVenta).get(0);
                    ProdVariante prodVariante = prodVarianteRepository.findBySku(prodVarianteSku).orElse(null);

                    if (venta == null || prodVariante == null) {
                        System.err.println("Error: No se pudo encontrar la venta o el producto para el detalle de venta con SKU: " + prodVarianteSku);
                        continue;
                    }
                    
                    if (prodVariante.getStock() < cantidad) {
                        System.err.println("Error: Stock insuficiente para el producto con SKU: " + prodVarianteSku);
                        continue;
                    }

                    prodVariante.setStock(prodVariante.getStock() - cantidad);
                    prodVarianteRepository.save(prodVariante);

                    DetalleVenta nuevoDetalle = new DetalleVenta();
                    nuevoDetalle.setVenta(venta);
                    nuevoDetalle.setProdVariante(prodVariante);
                    nuevoDetalle.setCantidad(cantidad);
                    nuevoDetalle.setPrecioUnitario(precioUnitario);
                    nuevoDetalle.setDescuento(descuento);
                    nuevoDetalle.setSubtotal(subtotal);
                    
                    detalleVentaRepository.save(nuevoDetalle);
                    contador++;
                }
            }
            System.out.println("Se cargaron " + contador + " detalles de venta desde el CSV.");
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar detalles de venta desde CSV: " + e.getMessage(), e);
        }
    }
}
