package com.trendora.tienda.venta.controller;

import org.springframework.stereotype.Controller;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.venta.dto.VentaRequestDTO;
import com.trendora.tienda.venta.dto.VentaResponseDTO;
import com.trendora.tienda.venta.model.Venta;
import com.trendora.tienda.venta.service.interfaces.IVentaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;





@Controller
@RequestMapping("/venta/venta")
public class VentaController {

    @Autowired
    private final IVentaService ventaService;

    public VentaController(IVentaService ventaService){
        this.ventaService = ventaService;
    }

    @GetMapping// por defecto /api/ventas
    public ResponseEntity<List<VentaResponseDTO>> getAllVentas() { //get todas las ventas 
        List<VentaResponseDTO> ventas = ventaService.listarTodo().stream().map(ventaService::convertToResponseDTO).toList();
        return ResponseEntity.ok(ventas);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> getVentaById(@PathVariable Long id){ // get por id de venta
        Optional<VentaResponseDTO> venta = ventaService.buscarById(id).map(ventaService::convertToResponseDTO);
        return venta.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping // por defecto /api/ventas
    public ResponseEntity<VentaResponseDTO> crearVenta(@RequestBody VentaRequestDTO ventaRequestDTO) {
        //TODO: process POST request
        VentaResponseDTO ventaCreada = ventaService.create(ventaRequestDTO);
        return new ResponseEntity<>(ventaCreada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> actualizarVenta(@PathVariable Long id, @RequestBody VentaRequestDTO ventaRequestDTO) {
        //TODO: process PUT request
        Optional<VentaResponseDTO> ventaActualizada = ventaService.update(id, ventaRequestDTO);
        return ventaActualizada.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id){
        if (ventaService.buscarById(id).isPresent()){
            ventaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/porcliente/{clienteid}")
    public ResponseEntity<List<VentaResponseDTO>> getVentaByClienteId(@PathVariable Long clienteId) {
        List<VentaResponseDTO> listaVentasByCliente = ventaService.buscarByCliente(new Usuario(clienteId))
        .stream()
        .map(ventaService::convertToResponseDTO)
        .toList();
        return ResponseEntity.ok(listaVentasByCliente);
    }
    
    @GetMapping("/porvendedor/{vendedorid}")
    public ResponseEntity<List<VentaResponseDTO>> getVentaByVendedorId(@PathVariable Long vendedorId){
        List<VentaResponseDTO> listaVentaByVendedor = ventaService.buscarByVendedor(new Usuario(vendedorId))
        .stream().map(ventaService::convertToResponseDTO).toList();
        return ResponseEntity.ok(listaVentaByVendedor);
    }

    @GetMapping("/porestado/{estado}")
    public ResponseEntity<List<VentaResponseDTO>> getVentaByEstado(@PathVariable String estado){
        List<VentaResponseDTO> listaVentaByEstado = ventaService.buscarByEstadoPedido(estado).stream().
        map(ventaService::convertToResponseDTO).toList();
        return ResponseEntity.ok(listaVentaByEstado);
    }


    @GetMapping("/entrefecha") // /ventas/entrefecha?inicio=2025-11-01T00:00:00&fin=2025-11-06T23:59:59
    public ResponseEntity<List<VentaResponseDTO>> getVentraBetweenfecha(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechainicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechafin
    ){
        List<VentaResponseDTO> listaVentaBetweenFecha = ventaService.buscarByFechaVenta(fechainicio, fechafin).stream().
        map(ventaService::convertToResponseDTO).toList();
        return ResponseEntity.ok(listaVentaBetweenFecha);
    }

}
