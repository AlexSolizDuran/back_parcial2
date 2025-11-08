package com.trendora.tienda.venta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trendora.tienda.venta.model.DetalleVenta;
import com.trendora.tienda.venta.model.Venta;
import com.trendora.tienda.inventario.model.ProdVariante;;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long>{
    List<DetalleVenta> findByVentas(Venta venta);
    List<DetalleVenta> finfByProdVariante(ProdVariante prodVarianmte);
}
