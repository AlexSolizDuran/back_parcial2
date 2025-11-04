package com.trendora.tienda.venta.model;
import com.trendora.tienda.inventario.model.ProdVariante;

//librerias core
import jakarta.persistence.*;
import lombok.*;
//librerias core
@Entity
@Table(name = "detalle_venta")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unit", nullable = false)
    private Double precio_unit;

    @Column(name = "descuento", nullable = false)
    private Double descuento;
    
    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false, referencedColumnName = "id" ,foreignKey = @ForeignKey(name = "fk_venta"))
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_variante_id", nullable = false, referencedColumnName = "id" ,foreignKey = @ForeignKey(name = "fk_prod_variante"))
    private ProdVariante prodVariante;

}
