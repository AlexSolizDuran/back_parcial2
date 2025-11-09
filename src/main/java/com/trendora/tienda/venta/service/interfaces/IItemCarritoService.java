package com.trendora.tienda.venta.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.trendora.tienda.venta.dto.itemcarrito.ItemCarritoRequestDTO;
import com.trendora.tienda.venta.dto.itemcarrito.ItemCarritoResponseDTO;
import com.trendora.tienda.venta.model.Carrito;
import com.trendora.tienda.venta.model.ItemCarrito;

public interface IItemCarritoService {
    List<ItemCarritoResponseDTO> listarTodo();
    Optional<ItemCarritoResponseDTO> buscarById(Long id);
    void eliminar(Long id);
    List<ItemCarritoResponseDTO> buscarByCarrito(Carrito carrito);

    ItemCarritoResponseDTO create(ItemCarritoRequestDTO dto);
    Optional<ItemCarritoResponseDTO> update(Long id, ItemCarritoRequestDTO dto);
    ItemCarritoResponseDTO convertToResponseDTO(ItemCarrito itemCarrito);
}

