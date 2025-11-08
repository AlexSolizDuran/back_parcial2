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
    private String numeroCasa;

    @Column(name = "referencial", nullable = true, length = 255) //
    private String referencia;

    //relacion 1:1 con usuario
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_usuario"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;

    @PrePersist
    private void PrePersist(){
        if(this.numeroCasa == null){
            this.numeroCasa = "N/A";
        }
        if(this.referencia == null){
            this.referencia = "N/A";
        }
    }
}
