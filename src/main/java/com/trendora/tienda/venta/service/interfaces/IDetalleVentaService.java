package com.trendora.tienda.venta.service.interfaces;
import java.util.List;
import java.util.Optional;

import com.trendora.tienda.inventario.model.ProdVariante;
import com.trendora.tienda.venta.dto.detalleventa.DetalleVentaRequestDTO;
import com.trendora.tienda.venta.dto.detalleventa.DetalleVentaResponseDTO;
import com.trendora.tienda.venta.model.DetalleVenta;
import com.trendora.tienda.venta.model.Venta;
public interface IDetalleVentaService {
    List<DetalleVentaResponseDTO> listarTodo();
    DetalleVentaResponseDTO obtenerById(Long id);
    List<DetalleVentaResponseDTO> obtenerByVenta(Venta venta);
    List<DetalleVentaResponseDTO> obtenerByPrudVariante(ProdVariante prodVariante);
    void eliminar(Long id);

    DetalleVentaResponseDTO create(DetalleVentaRequestDTO dto);
    Optional<DetalleVentaResponseDTO> update(Long id, DetalleVentaRequestDTO dto);
    DetalleVentaResponseDTO convertToResponseDTO(DetalleVenta detalleVenta);
}
