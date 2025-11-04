package com.trendora.tienda.config; // (El paquete original donde estaba)

import org.springframework.boot.CommandLineRunner; // Importar el nuevo servicio
import org.springframework.stereotype.Component; // Importar el nuevo servicio

import com.trendora.tienda.service.seeding.RolSeedingService;
import com.trendora.tienda.service.seeding.UsuarioSeedingService;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolSeedingService roleSeedingService;
    private final UsuarioSeedingService userSeedingService;
    // ... aquí inyectarás tus futuros servicios (ProductSeedingService, etc.)

    public DataInitializer(RolSeedingService roleSeedingService,
                           UsuarioSeedingService userSeedingService) {
        this.roleSeedingService = roleSeedingService;
        this.userSeedingService = userSeedingService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- INICIO DE CARGA DE DATOS INICIALES ---");
        
        // 1. Cargar Roles PRIMERO (porque los usuarios los necesitan)
        roleSeedingService.seedRoles();

        // 2. Cargar Usuarios DESPUÉS
        userSeedingService.seedUsers();

        // 3. (FUTURO) Cargar Productos
        // productSeedingService.seedProducts();

        // 4. (FUTURO) Cargar Ventas
        // saleSeedingService.seedSales();

        System.out.println("--- FIN DE CARGA DE DATOS INICIALES ---");
    }
}