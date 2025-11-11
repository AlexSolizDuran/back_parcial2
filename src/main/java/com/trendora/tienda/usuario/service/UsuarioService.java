package com.trendora.tienda.usuario.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trendora.tienda.usuario.dto.usuario.UsuarioListDTO;
import com.trendora.tienda.usuario.dto.usuario.UsuarioRequestDTO;
import com.trendora.tienda.usuario.dto.usuario.UsuarioResponseDTO;
import com.trendora.tienda.usuario.model.Rol;
import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.usuario.repository.RolRepository;
import com.trendora.tienda.usuario.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- CREATE ---
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO requestDTO) {

        if (usuarioRepository.findByUsername(requestDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Error: El nombre de usuario ya existe.");
        }
        if (usuarioRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Error: El email ya está en uso.");
        }

        Usuario nuevoUsuario = new Usuario();
        convertirRequestDTOAEntidad(requestDTO, nuevoUsuario);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return convertirAResponseDTO(usuarioGuardado);
    }

    // --- READ (Todos) ---
    public List<UsuarioListDTO> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarios.stream()
                .map(this::convertirAListDTO)
                .collect(Collectors.toList());
    }

    // --- READ (Uno por ID) ---
    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        return convertirAResponseDTO(usuario);
    }

    // --- UPDATE ---
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO requestDTO) {

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        if (!usuarioExistente.getUsername().equals(requestDTO.getUsername())
                && usuarioRepository.findByUsername(requestDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Error: El nuevo nombre de usuario ya existe.");
        }
        if (!usuarioExistente.getEmail().equals(requestDTO.getEmail())
                && usuarioRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Error: El nuevo email ya está en uso.");
        }

        convertirRequestDTOAEntidad(requestDTO, usuarioExistente);

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        return convertirAResponseDTO(usuarioActualizado);
    }

    // --- DELETE ---
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public List<UsuarioListDTO> obtenerUsuariosPorRol(String rolNombre) {

        // 1. Llama al método de repositorio que discutimos en la pregunta anterior
        List<Usuario> usuarios = usuarioRepository.findByRol_Nombre(rolNombre);

        // 2. Mapea los resultados a DTO (misma lógica que antes)
        return usuarios.stream()
                .map(this::convertirAListDTO)
                .collect(Collectors.toList());
    }

    // ==================================================================
    // MÉTODOS PRIVADOS DE TRADUCCIÓN (MAPPERS)
    // ==================================================================
    private UsuarioResponseDTO convertirAResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setUsername(usuario.getUsername());
        dto.setTelefono(usuario.getTelefono());
        dto.setRolNombre(usuario.getRol().getNombre());
        return dto;
    }

    private UsuarioListDTO convertirAListDTO(Usuario usuario) {
        UsuarioListDTO dto = new UsuarioListDTO(usuario.getId(), usuario.getUsername(), usuario.getNombre(),
                usuario.getApellido(), usuario.getEmail(), usuario.getRol().getNombre());
        
        return dto;
    }

    private void convertirRequestDTOAEntidad(UsuarioRequestDTO dto, Usuario usuario) {
        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setUsername(dto.getUsername());
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(rol);

        if (dto.getPassword() != null && !dto.getPassword().isEmpty() && !dto.getPassword().equals("defaultpassword")) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

            // Temporalmente (BORRA ESTO DESPUÉS):
            // usuario.setPassword(dto.getPassword());
        }
    }
    public Page<UsuarioListDTO> obtenerUsuariosPaginados(Pageable pageable) {
        return usuarioRepository.findAllProyectado(pageable);
    }

    /**
     * Método para el endpoint GET /api/usuarios?rol=CLIENTE (paginado)
     */
    public Page<UsuarioListDTO> obtenerUsuariosPorRolPaginados(String rolNombre, Pageable pageable) {
        return usuarioRepository.findByRol_NombreProyectado(rolNombre, pageable);
    }
}
