package com.trendora.tienda.usuario.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trendora.tienda.usuario.dto.direccion.DireccionRequestDTO;
import com.trendora.tienda.usuario.dto.direccion.DireccionResponseDTO;
import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.usuario.service.intefaces.IDireccionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/usuario/direccion")
public class DireccionController {
    @Autowired
    private final IDireccionService direccionService;

    public DireccionController(IDireccionService direccionService){
        this.direccionService = direccionService;
    }

    @GetMapping
    public ResponseEntity<List<DireccionResponseDTO>> getAllDireccion(){
        List<DireccionResponseDTO> listaDirecion = direccionService.listarTodo()
        .stream().map(direccionService::convertToResponseDTO).toList();
        return ResponseEntity.ok(listaDirecion);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DireccionResponseDTO> getDetalleById(@PathVariable Long id){
        Optional<DireccionResponseDTO> direccion = direccionService.buscarById(id)
        .map(direccionService::convertToResponseDTO);
        return direccion.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DireccionResponseDTO> crearDireccion(@RequestBody DireccionRequestDTO direccionRequestDTO){
        DireccionResponseDTO direccionCreada = direccionService.create(direccionRequestDTO);
        return new ResponseEntity<>(direccionCreada, HttpStatus.CREATED);
    }

    @GetMapping("/porcliente/{id}")
    public ResponseEntity<List<DireccionResponseDTO>> getDireccionByClienteId(
        @PathVariable Long id
    ){
        List<DireccionResponseDTO> listaDireccion = direccionService.buscarByUsuarioId(id).stream()
        .map(direccionService::convertToResponseDTO).toList();
        return ResponseEntity.ok(listaDireccion);
    }

    @GetMapping("/porclienteversion2")
    public ResponseEntity<List<DireccionResponseDTO>> getDireccionByCLiente(
        @RequestParam Long id
    ){
        List<DireccionResponseDTO> listaDireccion = direccionService.buscarByUsuario(new Usuario(id))
        .stream()
        .map(direccionService::convertToResponseDTO).toList();
        return ResponseEntity.ok(listaDireccion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DireccionResponseDTO> actualizarDireccion(
            @PathVariable Long id, 
            @RequestBody DireccionRequestDTO direccionRequestDTO
    ) {
        return direccionService.update(id, direccionRequestDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
