package com.trendora.tienda.usuario.model;
//librerias core
import jakarta.persistence.*;
import lombok.*;
//librerias core
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notificaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false) 
    private Long id;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "descripcion", nullable = true, length = 500)
    private String descripcion;

    @Column(name = "leido", nullable = false)
    private Boolean leido;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    //la realacion con usuario N:1
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuario"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;

    //valores por defecto antes de insertar
    @PrePersist
    public void PrePersist() {

        this.fecha = LocalDateTime.now();
        this.leido = false;

        if (this.descripcion == null){
            this.descripcion = "";
        }
    }
}
