package com.trendora.tienda.venta.model;
import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.trendora.tienda.inventario.model.ProdVariante;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false, referencedColumnName = "id" ,foreignKey = @ForeignKey(name = "fk_carrito"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_variante_id", nullable = false, referencedColumnName = "id" ,foreignKey = @ForeignKey(name = "fk_prod_variante"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProdVariante prodVariante;

    @PrePersist
    private void prePersist() {
        this.fecha = LocalDateTime.now();
        this.cantidad = 0;
    }
}
