package com.trendora.tienda.producto.service;

import com.trendora.tienda.producto.dto.material.MaterialReponseDTO;
import com.trendora.tienda.producto.dto.material.MaterialRequestDTO;
import com.trendora.tienda.producto.model.Material;
import com.trendora.tienda.producto.repository.MaterialRepository;
import com.trendora.tienda.producto.service.interfaces.IMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaterialService implements IMaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MaterialReponseDTO> listAll() {
        return materialRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialReponseDTO> findById(Long id) {
        return materialRepository.findById(id).map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public MaterialReponseDTO create(MaterialRequestDTO materialRequestDTO) {
        Material material = new Material();
        material.setNombre(materialRequestDTO.nombre());
        Material savedMaterial = materialRepository.save(material);
        return convertToResponseDTO(savedMaterial);
    }

    @Override
    @Transactional
    public Optional<MaterialReponseDTO> update(Long id, MaterialRequestDTO materialRequestDTO) {
        return materialRepository.findById(id)
                .map(material -> {
                    material.setNombre(materialRequestDTO.nombre());
                    Material updatedMaterial = materialRepository.save(material);
                    return convertToResponseDTO(updatedMaterial);
                });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        materialRepository.deleteById(id);
    }

    @Override
    public MaterialReponseDTO convertToResponseDTO(Material material) {
        return new MaterialReponseDTO(material.getId(), material.getNombre());
    }
}
