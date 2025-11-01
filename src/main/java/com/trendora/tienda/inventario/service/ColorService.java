package com.trendora.tienda.inventario.service;

import com.trendora.tienda.inventario.dto.color.ColorRequestDTO;
import com.trendora.tienda.inventario.dto.color.ColorResponseDTO;
import com.trendora.tienda.inventario.model.Color;
import com.trendora.tienda.inventario.repository.ColorRepository;
import com.trendora.tienda.inventario.service.interfaces.IColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColorService implements IColorService {

    @Autowired
    private ColorRepository colorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ColorResponseDTO> listAll() {
        return colorRepository.findAll().stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ColorResponseDTO> findById(Long id) {
        return colorRepository.findById(id).map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public ColorResponseDTO create(ColorRequestDTO requestDTO) {
        Color color = new Color();
        color.setNombre(requestDTO.nombre());
        color.setCodHexa(requestDTO.codHexa());
        return convertToResponseDTO(colorRepository.save(color));
    }

    @Override
    @Transactional
    public Optional<ColorResponseDTO> update(Long id, ColorRequestDTO requestDTO) {
        return colorRepository.findById(id).map(color -> {
            color.setNombre(requestDTO.nombre());
            color.setCodHexa(requestDTO.codHexa());
            return convertToResponseDTO(colorRepository.save(color));
        });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        colorRepository.deleteById(id);
    }

    @Override
    public ColorResponseDTO convertToResponseDTO(Color color) {
        return new ColorResponseDTO(color.getId(), color.getNombre(), color.getCodHexa());
    }
}
