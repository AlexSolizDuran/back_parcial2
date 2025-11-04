package com.trendora.tienda.usuario.model; // (Tu paquete)

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table; // <-- Importante
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @Column(nullable = false, length = 255) // Debe ser largo para la contraseña encriptada
    private String password;

    @Column(length = 20) // Teléfono puede ser nulo
    private String telefono;

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER es importante para que el rol se cargue siempre
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Devuelve una lista con el nombre del rol
        // Spring Security necesita que los roles tengan el prefijo "ROLE_"
        // Si tus roles se llaman "ADMIN" (sin prefijo), debes añadirlo aquí.
        // Si ya se llaman "ROLE_ADMIN", solo usa rol.getNombre()
        String roleName = rol.getNombre().startsWith("ROLE_") ? 
                          rol.getNombre() : "ROLE_" + rol.getNombre();
        
        return List.of(new SimpleGrantedAuthority(roleName));
    }
    @Override
    public String getUsername() {
        return this.username;
    }
    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true; // La cuenta no ha expirado
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // La cuenta no está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Las credenciales no han expirado
    }

    @Override
    public boolean isEnabled() {
        return true; // La cuenta está habilitada
    }
}
