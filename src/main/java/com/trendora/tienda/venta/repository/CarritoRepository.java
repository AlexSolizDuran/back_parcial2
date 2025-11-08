package com.trendora.tienda.venta.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.venta.model.Carrito;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long>{
    List<Carrito> findByCliente (Usuario clienteID);

    List<Carrito> findByEstado (String estado);

    List<Carrito> findByClienteAndEstado (Usuario clienteID, String estado);

    List<Carrito> findByClienteAndFecha (Usuario clienteID, LocalDateTime fecha);
}
