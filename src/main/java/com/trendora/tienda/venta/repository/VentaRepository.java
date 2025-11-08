package com.trendora.tienda.venta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.trendora.tienda.venta.model.Venta;

import java.util.List;
import com.trendora.tienda.usuario.model.Usuario;
import java.time.LocalDateTime;



@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByCliente(Usuario clienteID);

    List<Venta> findByVendedor(Usuario vendedorID);

    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin); 

    List<Venta> findByMetodoPago(String metodoPago);

    List<Venta> findByNumeroVenta(Long numeroVenta);

    List<Venta> findByTipoVenta(String tipoVenta);

    List<Venta> findByEstadoPedido(String estadoPedido);

    List<Venta> findByClienteAndEstadoPedido(Usuario clienteID, String estadoPedido);

    long countByEstadoPedido(String estadoPedido);

}
