package com.trendora.tienda.usuario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trendora.tienda.usuario.dto.usuario.UsuarioListDTO;
import com.trendora.tienda.usuario.model.Usuario; // <-- Importar tu DTO

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // --- MÉTODOS DE LOGIN (Se quedan igual) ---
    // (Estos los usa Spring Security y NO deben ser paginados)
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);


    // --- MÉTODOS DE LISTADO (PAGINADOS) ---

    // Tu método anterior. Lo dejamos por si lo usas en otro lado.
    List<Usuario> findByRol_Nombre(String nombre);


    /**
     * NUEVO MÉTODO: Busca TODOS los usuarios de forma paginada y los proyecta a UsuarioListDTO.
     * Esta es la forma más eficiente de hacerlo.
     * "JOIN u.rol r" nos permite acceder a las propiedades del rol.
     */
    @Query("SELECT new com.trendora.tienda.usuario.dto.usuario.UsuarioListDTO(u.id, u.username, u.nombre, u.apellido, u.email, r.nombre) " +
           "FROM Usuario u JOIN u.rol r")
    Page<UsuarioListDTO> findAllProyectado(Pageable pageable);

    
    @Query("SELECT new com.trendora.tienda.usuario.dto.usuario.UsuarioListDTO(u.id, u.username, u.nombre, u.apellido, u.email, r.nombre) " +
           "FROM Usuario u JOIN u.rol r " +
           "WHERE r.nombre = :nombreRol")
    Page<UsuarioListDTO> findByRol_NombreProyectado(@Param("nombreRol") String nombreRol, Pageable pageable);
}