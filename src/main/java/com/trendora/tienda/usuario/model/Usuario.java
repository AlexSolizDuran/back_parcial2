package com.trendora.tienda.usuario.model;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

//librerias core
import jakarta.persistence.*;
import lombok.*;
//librerias core

@Entity
@Table(name = "usuario")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "email", nullable =  false, length = 150, unique = true)
    private String email;

    @Column(name = "telefono", nullable = false, length = 30)
    private String telefono;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    //la realacion con rol N:1
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "rol_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_rol"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Rol rol;

}
