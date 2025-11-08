package com.trendora.tienda.venta.model;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

//librerias core
import jakarta.persistence.*;
import lombok.*;
//librerias core
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.trendora.tienda.usuario.model.Usuario;


@Entity
@Table(name = "venta")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "numero_venta", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numeroVenta;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;

    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago; //tajeta, efectivo, QR

    @Column(name = "tipo_venta", nullable = false) 
    private String tipoVenta; //online, presencial

    @Column(name = "estado_pedido", nullable = false)
    private String estadoPedido;  //pendiente, enviado, entregado, cancelado

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuario_cli"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendedor_id", nullable = true, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuario_ven"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario vendedor;
    
    @PrePersist
    private void prePersist() {
        this.fechaVenta = LocalDateTime.now();
        this.montoTotal = 0.0;
        this.estadoPedido = "pendiente";
    }

}
