package com.trendora.tienda.producto.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trendora.tienda.producto.dto.modelo.ModeloRequestDTO;
import com.trendora.tienda.producto.dto.modelo.ModeloResponseDTO;
import com.trendora.tienda.producto.model.Marca;
import com.trendora.tienda.producto.model.Modelo;
import com.trendora.tienda.producto.repository.MarcaRepository;
import com.trendora.tienda.producto.repository.ModeloRepository;
import com.trendora.tienda.producto.service.interfaces.IModeloService;

@Service
public class ModeloService implements IModeloService {

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ModeloResponseDTO> listAll() {
        return modeloRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModeloResponseDTO> findByMarcaId(Long id) {
        return modeloRepository.findByMarcaId(id).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ModeloResponseDTO> findById(Long id) {
        return modeloRepository.findById(id).map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public ModeloResponseDTO create(ModeloRequestDTO modeloRequestDTO) {
        Marca marca = marcaRepository.findById(modeloRequestDTO.marcaId())
                .orElseThrow(() -> new RuntimeException("Marca not found"));
        Modelo modelo = new Modelo();
        modelo.setNombre(modeloRequestDTO.nombre());
        modelo.setMarca(marca);
        Modelo savedModelo = modeloRepository.save(modelo);
        return convertToResponseDTO(savedModelo);
    }

    @Override
    @Transactional
    public Optional<ModeloResponseDTO> update(Long id, ModeloRequestDTO modeloRequestDTO) {
        return modeloRepository.findById(id)
                .map(modelo -> {
                    Marca marca = marcaRepository.findById(modeloRequestDTO.marcaId())
                            .orElseThrow(() -> new RuntimeException("Marca not found"));
                    modelo.setNombre(modeloRequestDTO.nombre());
                    modelo.setMarca(marca);
                    Modelo updatedModelo = modeloRepository.save(modelo);
                    return convertToResponseDTO(updatedModelo);
                });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        modeloRepository.deleteById(id);
    }

    @Override
    public ModeloResponseDTO convertToResponseDTO(Modelo modelo) {
        return new ModeloResponseDTO(modelo.getId(), modelo.getNombre(), modelo.getMarca().getId());
    }
}
