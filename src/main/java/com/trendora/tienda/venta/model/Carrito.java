package com.trendora.tienda.venta.model;
import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.trendora.tienda.usuario.model.Usuario;

//librerias core
import jakarta.persistence.*;
import lombok.*;
//librerias core

@Entity
@Table(name = "carrito")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "estado", nullable = false)
    private String estado; //pendiente, completado, cancelado(abandonado)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_usuario_car"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario cliente;

    @PrePersist
    private void PrePersist(){
        this.fecha = LocalDateTime.now();
        this.estado = "pendiente";
    }
}
