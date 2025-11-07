package com.trendora.tienda.venta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trendora.tienda.venta.model.Carrito;
import com.trendora.tienda.venta.model.ItemCarrito;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    
    List<ItemCarrito> findByCarrito(Carrito carrito);
    
}
