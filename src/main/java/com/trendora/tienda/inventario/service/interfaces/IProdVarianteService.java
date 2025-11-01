package com.trendora.tienda.inventario.service.interfaces;

import com.trendora.tienda.inventario.dto.prodVariante.ProdVarianteRequestDTO;
import com.trendora.tienda.inventario.dto.prodVariante.ProdVarianteResponseDTO;
import com.trendora.tienda.inventario.model.ProdVariante;

import java.util.List;
import java.util.Optional;

public interface IProdVarianteService {
    List<ProdVarianteResponseDTO> listAll();
    List<ProdVarianteResponseDTO> findByProductoId(Long productoId);
    Optional<ProdVarianteResponseDTO> findById(Long id);
    ProdVarianteResponseDTO create(ProdVarianteRequestDTO requestDTO);
    Optional<ProdVarianteResponseDTO> update(Long id, ProdVarianteRequestDTO requestDTO);
    void delete(Long id);
    ProdVarianteResponseDTO convertToResponseDTO(ProdVariante prodVariante);
}
