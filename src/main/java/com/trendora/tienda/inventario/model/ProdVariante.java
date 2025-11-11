package com.trendora.tienda.inventario.model;

import java.math.BigDecimal;

import com.trendora.tienda.producto.model.Producto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prod_variante")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdVariante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_color", nullable = false)
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_talla", nullable = false)
    private Talla talla;

    @Column(nullable = false)
    private BigDecimal costo;

    @Column(name = "ppp")
    private BigDecimal ppp;

    @Column(name = "ppv")
    private BigDecimal ppv;

    @Column(nullable = false)
    private BigDecimal precio;

    @Column(nullable = false, unique = true, length = 100)
    private String sku;

    @Column(nullable = false)
    private Integer stock;

    public ProdVariante(Long id) {
        this.id = id;
    }

    @PrePersist
    public void setPppAndPpv() {
        if (this.ppp == null) {
            this.ppp = this.costo;
        }
        if (this.ppv == null) {
            this.ppv = this.precio;
        }
    }
}
