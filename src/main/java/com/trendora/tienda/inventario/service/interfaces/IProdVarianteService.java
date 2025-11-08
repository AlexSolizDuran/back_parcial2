package com.trendora.tienda.inventario.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.trendora.tienda.inventario.dto.prodVariante.ProdVarianteListDTO;
import com.trendora.tienda.inventario.dto.prodVariante.ProdVarianteRequestDTO;
import com.trendora.tienda.inventario.dto.prodVariante.ProdVarianteResponseDTO;
import com.trendora.tienda.inventario.model.ProdVariante;

public interface IProdVarianteService {
    List<ProdVarianteListDTO> listAll();
    List<ProdVarianteResponseDTO> findByProductoId(Long productoId);
    Optional<ProdVarianteResponseDTO> findById(Long id);
    ProdVarianteResponseDTO create(ProdVarianteRequestDTO requestDTO);
    Optional<ProdVarianteResponseDTO> update(Long id, ProdVarianteRequestDTO requestDTO);
    void delete(Long id);
    ProdVarianteResponseDTO convertToResponseDTO(ProdVariante prodVariante);
    ProdVarianteListDTO convertToProdVarianteListDTO(ProdVariante prodVariante);
}

