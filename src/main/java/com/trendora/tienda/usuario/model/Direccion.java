package com.trendora.tienda.usuario.model;
//librerias core
import jakarta.persistence.*;
import lombok.*;
//librerias core
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Entity
@Table(name = "direccion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "departamento", nullable = false, length = 100)
    private String departamento;

    @Column(name = "zona", nullable = false, length = 100)
    private String zona;

    @Column(name = "calle", nullable = false, length = 150)
    private String calle;

    @Column(name = "numero_casa", nullable = true) //
    private String numero_casa;

    @Column(name = "referencial", nullable = true, length = 255) //
    private String referencial;

    //relacion 1:1 con usuario
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_usuario"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;

    @PrePersist
    private void PrePersist(){
        if(this.numero_casa == null){
            this.numero_casa = "N/A";
        }
        if(this.referencial == null){
            this.referencial = "N/A";
        }
    }
}
