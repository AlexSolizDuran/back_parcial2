package com.trendora.tienda.inventario.model;

import com.trendora.tienda.producto.model.Producto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    private String imagen;

    @Column(name = "ppp")
    private BigDecimal ppp;

    @Column(nullable = false)
    private BigDecimal precio;

    @Column(nullable = false, unique = true, length = 100)
    private String sku;

    @Column(nullable = false)
    private Integer stock;
}