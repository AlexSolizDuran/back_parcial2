package com.trendora.tienda.venta.model;
import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cantidad", nullable = false) //defecto 0
    private Integer cantidad;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false, referencedColumnName = "id" ,foreignKey = @ForeignKey(name = "fk_carrito"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Carrito carrito;

    //private PruductoVariante productoVariante;

    @PrePersist
    private void prePersist() {
        this.fecha = LocalDateTime.now();
        this.cantidad = 0;
    }
}
