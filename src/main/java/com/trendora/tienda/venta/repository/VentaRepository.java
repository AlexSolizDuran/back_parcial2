package com.trendora.tienda.venta.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.venta.model.Venta;



@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT COALESCE(MAX(v.numeroVenta), 0) FROM Venta v")
    Long findMaxNumeroVenta();

    List<Venta> findByCliente(Usuario clienteID);

    List<Venta> findByVendedor(Usuario vendedorID);

    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin); 

    List<Venta> findByMetodoPago(String metodoPago);

    List<Venta> findByNumeroVenta(Long numeroVenta);

    List<Venta> findByTipoVenta(String tipoVenta);

    List<Venta> findByEstadoPedido(String estadoPedido);

    List<Venta> findByClienteAndEstadoPedido(Usuario clienteID, String estadoPedido);

    long countByEstadoPedido(String estadoPedido);

    List<Venta> findByFechaVentaAfter(LocalDateTime fechaVenta);

    // CORREGIDO: findTop5ByOrderByFechaDesc -> findTop5ByOrderByFechaVentaDesc
    List<Venta> findTop5ByOrderByFechaVentaDesc();

}
