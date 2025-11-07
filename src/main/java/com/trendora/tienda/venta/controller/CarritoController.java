package com.trendora.tienda.venta.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trendora.tienda.venta.dto.carrito.CarritoRequestDTO;
import com.trendora.tienda.venta.dto.carrito.CarritoResponseDTO;
import com.trendora.tienda.venta.service.interfaces.ICarritoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/venta/carrito")
public class CarritoController {
    @Autowired
    private final ICarritoService carritoService;

    public CarritoController(ICarritoService carritoService){
        this.carritoService = carritoService;
    }

    @GetMapping
    public ResponseEntity<List<CarritoResponseDTO>> getAllCarrito() {
        List<CarritoResponseDTO> carrito = carritoService.listarTodo()
        .stream()
        .map(carritoService::convertToResponseDTO).toList();
        return ResponseEntity.ok(carrito);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> getCarritoById(@PathVariable Long id){
        Optional<CarritoResponseDTO> carrito = carritoService.buscarById(id)
        .map(carritoService::convertToResponseDTO);
        return carrito.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CarritoResponseDTO> crearCarrito(@RequestBody CarritoRequestDTO carritoRequestDTO){
        CarritoResponseDTO carritoCreado = carritoService.create(carritoRequestDTO);
        return new ResponseEntity<>(carritoCreado, HttpStatus.CREATED);
    }

    

}
