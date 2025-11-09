package com.trendora.tienda.venta.repository;

import com.trendora.tienda.inventario.model.ProdVariante;
import com.trendora.tienda.venta.model.DetalleVenta;
import com.trendora.tienda.venta.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByVenta(Venta venta);
    List<DetalleVenta> findByProdVariante(ProdVariante prodVariante);
}