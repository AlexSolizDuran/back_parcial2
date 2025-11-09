package com.trendora.tienda.venta.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.trendora.tienda.inventario.model.ProdVariante;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_carrito")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_carrito"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_variante_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_prod_variante"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProdVariante prodVariante;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @PrePersist
    private void prePersist() {
        if (this.cantidad == null || this.cantidad == 0) {
            this.cantidad = 1;
        }
    }
}

