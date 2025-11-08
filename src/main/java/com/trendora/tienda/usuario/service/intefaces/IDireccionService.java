package com.trendora.tienda.usuario.service.intefaces;

import java.util.List;
import java.util.Optional;

import com.trendora.tienda.usuario.dto.direccion.DireccionRequestDTO;
import com.trendora.tienda.usuario.dto.direccion.DireccionResponseDTO;
import com.trendora.tienda.usuario.model.Direccion;
import com.trendora.tienda.usuario.model.Usuario;

public interface IDireccionService {
    List<Direccion> listarTodo();
    Optional<Direccion> buscarById(Long id);
    void eliminar(Long id);

    Optional<Direccion> buscarByUsuario(Usuario usuario);
    Optional<Direccion> buscarByUsuarioId(Long id);

    DireccionResponseDTO create(DireccionRequestDTO dto);
    Optional<DireccionResponseDTO> update(Long id, DireccionRequestDTO dto);
    DireccionResponseDTO convertToResponseDTO(Direccion direccion);
}
