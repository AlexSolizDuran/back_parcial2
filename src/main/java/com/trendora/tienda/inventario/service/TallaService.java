package com.trendora.tienda.inventario.service;

import com.trendora.tienda.inventario.dto.talla.TallaRequestDTO;
import com.trendora.tienda.inventario.dto.talla.TallaResponseDTO;
import com.trendora.tienda.inventario.model.Talla;
import com.trendora.tienda.inventario.repository.TallaRepository;
import com.trendora.tienda.inventario.service.interfaces.ITallaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TallaService implements ITallaService {

    @Autowired
    private TallaRepository tallaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TallaResponseDTO> listAll() {
        return tallaRepository.findAll().stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TallaResponseDTO> findById(Long id) {
        return tallaRepository.findById(id).map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public TallaResponseDTO create(TallaRequestDTO requestDTO) {
        Talla talla = new Talla();
        talla.setTalla(requestDTO.talla());
        return convertToResponseDTO(tallaRepository.save(talla));
    }

    @Override
    @Transactional
    public Optional<TallaResponseDTO> update(Long id, TallaRequestDTO requestDTO) {
        return tallaRepository.findById(id).map(talla -> {
            talla.setTalla(requestDTO.talla());
            return convertToResponseDTO(tallaRepository.save(talla));
        });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        tallaRepository.deleteById(id);
    }

    @Override
    public TallaResponseDTO convertToResponseDTO(Talla talla) {
        return new TallaResponseDTO(talla.getId(), talla.getTalla());
    }
}
