package com.trendora.tienda.venta.service.interfaces;

import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.venta.dto.VentaRequestDTO;
import com.trendora.tienda.venta.dto.VentaResponseDTO;
import com.trendora.tienda.venta.model.Venta;

import java.time.LocalDateTime;
import java.util.*;

public interface IVentaService {
    
    List<Venta> listarTodo();

    Optional<Venta> buscarById(Long id);

    Venta guardar(Venta venta);

    void eliminar(Long id);

    //////////////
    List<Venta> buscarByCliente(Usuario clienteID);

    List<Venta> buscarByVendedor(Usuario vendedorID);

    List<Venta> buscarByFechaVenta(LocalDateTime inicio, LocalDateTime fin);

    List<Venta> buscarByMetodoPago(String metodoPago);

    List<Venta> buscarByNumeroVenta(Long numeroVenta);

    List<Venta> buscarByTipoVenta(String tipoVenta);

    List<Venta> buscarByEstadoPedido(String estadoPedido);

    List<Venta> buscarByClienteYEstadoPedido(Usuario clienteID, String estadoPedido);

    long contarByEstadoPedido(String estadoPedido);
    ////////////

    VentaResponseDTO create(VentaRequestDTO dto);
    Optional<VentaResponseDTO> update(Long id, VentaRequestDTO dto);
    VentaResponseDTO convertToResponseDTO(Venta venta);
}
