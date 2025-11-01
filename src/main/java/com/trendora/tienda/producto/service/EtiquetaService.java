package com.trendora.tienda.producto.service;

import com.trendora.tienda.producto.dto.etiqueta.EtiquetaRequestDTO;
import com.trendora.tienda.producto.dto.etiqueta.EtiquetaResponseDTO;
import com.trendora.tienda.producto.model.Etiqueta;
import com.trendora.tienda.producto.repository.EtiquetaRepository;
import com.trendora.tienda.producto.service.interfaces.IEtiquetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EtiquetaService implements IEtiquetaService {

    @Autowired
    private EtiquetaRepository etiquetaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EtiquetaResponseDTO> listAll() {
        return etiquetaRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EtiquetaResponseDTO> findById(Long id) {
        return etiquetaRepository.findById(id).map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public EtiquetaResponseDTO create(EtiquetaRequestDTO etiquetaRequestDTO) {
        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setNombre(etiquetaRequestDTO.nombre());
        Etiqueta savedEtiqueta = etiquetaRepository.save(etiqueta);
        return convertToResponseDTO(savedEtiqueta);
    }

    @Override
    @Transactional
    public Optional<EtiquetaResponseDTO> update(Long id, EtiquetaRequestDTO etiquetaRequestDTO) {
        return etiquetaRepository.findById(id)
                .map(etiqueta -> {
                    etiqueta.setNombre(etiquetaRequestDTO.nombre());
                    Etiqueta updatedEtiqueta = etiquetaRepository.save(etiqueta);
                    return convertToResponseDTO(updatedEtiqueta);
                });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        etiquetaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EtiquetaResponseDTO> findByProductoId(Long id) {
        return etiquetaRepository.findByProductosId(id).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EtiquetaResponseDTO convertToResponseDTO(Etiqueta etiqueta) {
        return new EtiquetaResponseDTO(etiqueta.getId(), etiqueta.getNombre());
    }
}
