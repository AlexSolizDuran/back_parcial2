package com.trendora.tienda.service.seeding;

import com.opencsv.CSVReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.usuario.repository.UsuarioRepository;
import com.trendora.tienda.venta.model.Venta;
import com.trendora.tienda.venta.repository.VentaRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VentaSeedingService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ResourceLoader resourceLoader;

    public VentaSeedingService(VentaRepository ventaRepository, UsuarioRepository usuarioRepository, ResourceLoader resourceLoader) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.resourceLoader = resourceLoader;
    }

    @Transactional
    public void seedVentas() {
        if (ventaRepository.count() > 0) {
            System.out.println("La base de datos de ventas ya contiene datos.");
            return;
        }

        System.out.println("Poblando base de datos de ventas desde CSV...");
        int contador = 0;

        try {
            Resource resource = resourceLoader.getResource("classpath:data/ventas_data.csv");
            try (Reader reader = new InputStreamReader(resource.getInputStream())) {

                CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build();

                List<String[]> lineas = csvReader.readAll();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

                for (String[] linea : lineas) {
                    Long numeroVenta = Long.parseLong(linea[0]);
                    String clienteEmail = linea[1];
                    String vendedorEmail = linea[2];
                    String metodoPago = linea[3];
                    String tipoVenta = linea[4];
                    LocalDateTime fechaVenta = LocalDateTime.parse(linea[5], formatter);

                    Usuario cliente = usuarioRepository.findByEmail(clienteEmail).orElse(null);
                    Usuario vendedor = usuarioRepository.findByEmail(vendedorEmail).orElse(null);

                    if (cliente == null || vendedor == null) {
                        System.err.println("Error: No se pudo encontrar el cliente o vendedor para la venta: " + numeroVenta);
                        continue;
                    }

                    Venta nuevaVenta = new Venta();
                    nuevaVenta.setNumeroVenta(numeroVenta);
                    nuevaVenta.setCliente(cliente);
                    nuevaVenta.setVendedor(vendedor);
                    nuevaVenta.setMetodoPago(metodoPago);
                    nuevaVenta.setTipoVenta(tipoVenta);
                    nuevaVenta.setFechaVenta(fechaVenta);
                    
                    ventaRepository.save(nuevaVenta);
                    contador++;
                }
            }
            System.out.println("Se cargaron " + contador + " ventas desde el CSV.");
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar ventas desde CSV: " + e.getMessage(), e);
        }
    }
}
