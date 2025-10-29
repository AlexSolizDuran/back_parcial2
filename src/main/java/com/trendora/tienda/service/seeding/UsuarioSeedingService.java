package com.trendora.tienda.service.seeding;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.trendora.tienda.usuario.model.Rol;
import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.usuario.repository.RolRepository;
import com.trendora.tienda.usuario.repository.UsuarioRepository;

@Service
public class UsuarioSeedingService {

    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResourceLoader resourceLoader;
    private final UsuarioRepository usuarioRepository; 

    public UsuarioSeedingService(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              PasswordEncoder passwordEncoder,
                              ResourceLoader resourceLoader) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Método público que inicia la carga de usuarios desde el CSV.
     */
    public void seedUsers() {
        if (usuarioRepository.count() > 0) {
            System.out.println("La base de datos de usuarios ya contiene datos.");
            return; // Salimos del método si ya hay usuarios
        }

        System.out.println("Poblando base de datos de usuarios desde CSV...");

        try {
            // A. Obtenemos los roles y los ponemos en un Mapa
            Map<String, Rol> rolesMap = rolRepository.findAll().stream()
                    .collect(Collectors.toMap(Rol::getNombre, rol -> rol));

            List<Usuario> usuariosACargar = new ArrayList<>();

            // B. Ubicamos y leemos el archivo CSV
            Resource resource = resourceLoader.getResource("classpath:data/usuarios_data.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {

                List<String[]> lineas = csvReader.readAll();
                
                for (String[] linea : lineas) {
                    // Mapeamos cada columna
                    String username = linea[0];
                    String email = linea[1];
                    String nombre = linea[2];
                    String apellido = linea[3];
                    String telefono = linea[4];
                    String passwordPlano = linea[5];
                    String rolNombre = linea[6];

                    // Buscamos el Rol en el Mapa
                    Rol rol = rolesMap.get(rolNombre);
                    if (rol == null) {
                        System.err.println("ADVERTENCIA: Rol no encontrado '" + rolNombre + "'. Saltando línea.");
                        continue;
                    }

                    Usuario usuario = new Usuario();
                    usuario.setUsername(username);
                    usuario.setEmail(email);
                    usuario.setNombre(nombre);
                    usuario.setApellido(apellido);
                    usuario.setTelefono(telefono);
                    usuario.setPassword(passwordEncoder.encode(passwordPlano));
                    usuario.setRol(rol);

                    usuariosACargar.add(usuario);
                }

            } // El try-with-resources cierra los readers aquí

            // C. Guardamos todo en la base de datos
            if (!usuariosACargar.isEmpty()) {
                usuarioRepository.saveAll(usuariosACargar);
                System.out.println("Se cargaron " + usuariosACargar.size() + " usuarios desde el CSV.");
            }

        } catch (Exception e) {
            // Capturamos cualquier excepción (ej. archivo no encontrado, error de CSV)
            throw new RuntimeException("Error al cargar usuarios desde CSV: " + e.getMessage(), e);
        }
    }
}