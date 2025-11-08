package com.trendora.tienda.usuario.model;
//librerias core
import jakarta.persistence.*;
import lombok.*;
//librerias core



@Entity
@Table(name = "rol") 
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
}
