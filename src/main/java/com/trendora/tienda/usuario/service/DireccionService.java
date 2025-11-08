package com.trendora.tienda.usuario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.trendora.tienda.usuario.dto.direccion.DireccionRequestDTO;
import com.trendora.tienda.usuario.dto.direccion.DireccionResponseDTO;
import com.trendora.tienda.usuario.model.Direccion;
import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.usuario.repository.DireccionRepository;
import com.trendora.tienda.usuario.repository.UsuarioRepository;
import com.trendora.tienda.usuario.service.intefaces.IDireccionService;

public class DireccionService implements IDireccionService{

    @Autowired
    DireccionRepository direccionRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public List<Direccion> listarTodo() {
        // TODO Auto-generated method stub
        return direccionRepository.findAll();
    }

    @Override
    public Optional<Direccion> buscarById(Long id) {
        // TODO Auto-generated method stub
        return direccionRepository.findById(id);
    }

    @Override
    public void eliminar(Long id) {
        // TODO Auto-generated method stub
        if(!direccionRepository.existsById(id)){
            throw new RuntimeException("el id a eliminar " + id + "no existe/n Error DireccionSerice,java funcion eliminar");
        }
        direccionRepository.deleteById(id);
    }

    @Override
    public Optional<Direccion> buscarByUsuario(Usuario usuario) {
        // TODO Auto-generated method stub
        return direccionRepository.findByUsuario(usuario);
    }

    @Override
    public Optional<Direccion> buscarByUsuarioId(Long id) {
        // TODO Auto-generated method stub
        return direccionRepository.findByUsuario(new Usuario(id));
    }

    @Override
    @Transactional
    public DireccionResponseDTO create(DireccionRequestDTO dto) {
        // TODO Auto-generated method stub
        Direccion direccion = convertToEntity(dto);
        direccion = direccionRepository.save(direccion);
        return convertToResponseDTO(direccion);
    }

    @Override
    @Transactional
    public Optional<DireccionResponseDTO> update(Long id, DireccionRequestDTO dto) {
        // TODO Auto-generated method stub
        return direccionRepository.findById(id).map(direccion->{
            updateEntityFromDTO(direccion, dto);
            direccion=direccionRepository.save(direccion);
            return convertToResponseDTO(direccion);
        });
    }

    @Override
    @Transactional
    public DireccionResponseDTO convertToResponseDTO(Direccion direccion) {
        // TODO Auto-generated method stub
        return new DireccionResponseDTO(
            direccion.getDepartamento(),
            direccion.getZona(),
            direccion.getCalle(),
            direccion.getNumeroCasa(),
            direccion.getReferencia(),
            direccion.getUsuario().getId()
        );
    }
    
    private Direccion convertToEntity(DireccionRequestDTO dto){
        Direccion direccion = new Direccion();
        updateEntityFromDTO(direccion, dto);
        return direccion;
    }
    private void updateEntityFromDTO(Direccion direccion, DireccionRequestDTO dto){
        Usuario usuario = usuarioRepository.findById(dto.usuarioId()).orElseThrow(
            ()-> new RuntimeException("error no existe id con ese usuario/n Error DireccionService funcion updateEntityFromDTO")
        );
        
        direccion.setDepartamento(dto.departamento());
        direccion.setZona(dto.zona());
        direccion.setCalle(dto.calle());
        direccion.setNumeroCasa(dto.numeroCasa());
        direccion.setReferencia(dto.referencia());
        direccion.setUsuario(usuario);
    }
}
