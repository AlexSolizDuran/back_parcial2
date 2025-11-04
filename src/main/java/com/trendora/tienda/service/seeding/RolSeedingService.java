package com.trendora.tienda.service.seeding; // Puedes crear un nuevo paquete 'seeding'

import java.util.List;

import org.springframework.stereotype.Service;

import com.trendora.tienda.usuario.model.Rol;
import com.trendora.tienda.usuario.repository.RolRepository;

@Service
public class RolSeedingService {

    private final RolRepository rolRepository;

    public RolSeedingService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    /**
     * Método público que inicia la carga de roles.
     */
    public void seedRoles() {
        System.out.println("Iniciando carga de roles...");
        List<String> rolesNombres = List.of("ADMIN", "CLIENTE", "VENDEDOR");
        
        rolesNombres.forEach(this::crearRolSiNoExiste);
        
        System.out.println("Carga de roles finalizada.");
    }

    /**
     * Método privado que comprueba si un rol existe y lo crea si no.
     */
    private void crearRolSiNoExiste(String nombreRol) {
        if (rolRepository.findByNombre(nombreRol).isEmpty()) {
            Rol nuevoRol = new Rol();
            nuevoRol.setNombre(nombreRol);
            rolRepository.save(nuevoRol);
            System.out.println("Rol " + nombreRol + " creado.");
        }
    }
}