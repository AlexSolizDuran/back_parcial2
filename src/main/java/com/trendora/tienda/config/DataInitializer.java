package com.trendora.tienda.config;

import com.trendora.tienda.service.seeding.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolSeedingService roleSeedingService;
    private final UsuarioSeedingService userSeedingService;
    private final CategoriaSeedingService categoriaSeedingService;
    private final MarcaSeedingService marcaSeedingService;
    private final MaterialSeedingService materialSeedingService;
    private final EtiquetaSeedingService etiquetaSeedingService;
    private final ModeloSeedingService modeloSeedingService;
    private final TallaSeedingService tallaSeedingService;
    private final ColorSeedingService colorSeedingService;
    private final ProductoSeedingService productoSeedingService;
    private final ProdVarianteSeedingService prodVarianteSeedingService;
    private final VentaSeedingService ventaSeedingService;
    private final DetalleVentaSeedingService detalleVentaSeedingService;

    public DataInitializer(RolSeedingService roleSeedingService,
                           UsuarioSeedingService userSeedingService,
                           CategoriaSeedingService categoriaSeedingService,
                           MarcaSeedingService marcaSeedingService,
                           MaterialSeedingService materialSeedingService,
                           EtiquetaSeedingService etiquetaSeedingService,
                           ModeloSeedingService modeloSeedingService,
                           TallaSeedingService tallaSeedingService,
                           ColorSeedingService colorSeedingService,
                           ProductoSeedingService productoSeedingService,
                           ProdVarianteSeedingService prodVarianteSeedingService,
                           VentaSeedingService ventaSeedingService,
                           DetalleVentaSeedingService detalleVentaSeedingService) {
        this.roleSeedingService = roleSeedingService;
        this.userSeedingService = userSeedingService;
        this.categoriaSeedingService = categoriaSeedingService;
        this.marcaSeedingService = marcaSeedingService;
        this.materialSeedingService = materialSeedingService;
        this.etiquetaSeedingService = etiquetaSeedingService;
        this.modeloSeedingService = modeloSeedingService;
        this.tallaSeedingService = tallaSeedingService;
        this.colorSeedingService = colorSeedingService;
        this.productoSeedingService = productoSeedingService;
        this.prodVarianteSeedingService = prodVarianteSeedingService;
        this.ventaSeedingService = ventaSeedingService;
        this.detalleVentaSeedingService = detalleVentaSeedingService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- INICIO DE CARGA DE DATOS INICIALES ---");

        roleSeedingService.seedRoles();
        userSeedingService.seedUsers();
        categoriaSeedingService.seedCategorias();
        marcaSeedingService.seedMarcas();
        materialSeedingService.seedMateriales();
        etiquetaSeedingService.seedEtiquetas();
        modeloSeedingService.seedModelos();
        tallaSeedingService.seedTallas();
        colorSeedingService.seedColores();
        productoSeedingService.seedProductos();
        prodVarianteSeedingService.seedProdVariantes();
        ventaSeedingService.seedVentas();
        detalleVentaSeedingService.seedDetalleVentas();

        System.out.println("--- FIN DE CARGA DE DATOS INICIALES ---");
    }
}