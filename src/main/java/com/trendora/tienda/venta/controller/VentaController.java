package com.trendora.tienda.venta.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.security.Principal;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.usuario.repository.UsuarioRepository;
import com.trendora.tienda.venta.dto.CheckoutRequestDTO;
import com.trendora.tienda.venta.dto.CheckoutResponseDTO;
import com.trendora.tienda.venta.dto.VentaRequestDTO;
import com.trendora.tienda.venta.dto.VentaResponseDTO;
import com.trendora.tienda.venta.service.interfaces.IVentaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/venta/venta")
public class VentaController {

    @Autowired
    private final IVentaService ventaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public VentaController(IVentaService ventaService) {
        this.ventaService = ventaService;
    }
    

    @PostMapping("/generar-pago")
    public ResponseEntity<CheckoutResponseDTO> generarPago(
            @RequestBody CheckoutRequestDTO checkoutRequest,
            Principal principal // Obtiene el usuario del token JWT
    ) {
        try {
            // Buscamos al usuario por su username (que está en el token)
            Usuario cliente = usuarioRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

            CheckoutResponseDTO response = ventaService.generarPagoLibelula(checkoutRequest, cliente.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error en /generar-pago: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new CheckoutResponseDTO(null)); // Devuelve null en error
        }
    }

    @GetMapping("/libelula-callback")
    public ResponseEntity<String> handleLibelulaCallback(
            @RequestParam("myVentaId") Long ventaId,
            @RequestParam(name = "transaction_id", required = false) String libelulaTransactionId
    ) {
        try {
            ventaService.confirmarPago(ventaId, libelulaTransactionId);
            
            // (Opcional) Aquí podrías redirigir al usuario a una página de "éxito" en tu app
            // Por ahora, solo devolvemos un OK a Libélula
            return ResponseEntity.ok("Pago procesado. Gracias.");
        } catch (Exception e) {
            System.err.println("Error en Callback de Libélula: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error al procesar el pago.");
        }
    }
    
    @GetMapping// por defecto /venta/venta
    public ResponseEntity<List<VentaResponseDTO>> getAllVentas() { //get todas las ventas 
        List<VentaResponseDTO> ventas = ventaService.listarTodo().stream().map(ventaService::convertToResponseDTO).toList();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> getVentaById(@PathVariable Long id) { // get por id de venta
        Optional<VentaResponseDTO> venta = ventaService.buscarById(id)
                .map(ventaService::convertToResponseDTO);
        return venta.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping // por defecto /venta/venta
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
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        if (ventaService.buscarById(id).isPresent()) {
            ventaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/porcliente/{clienteId}")
    public ResponseEntity<List<VentaResponseDTO>> getVentaByClienteId(@PathVariable Long clienteId) {
        List<VentaResponseDTO> listaVentasByCliente = ventaService.buscarByCliente(new Usuario(clienteId))
                .stream()
                .map(ventaService::convertToResponseDTO)
                .toList();
        return ResponseEntity.ok(listaVentasByCliente);
    }

    @GetMapping("/porvendedor/{vendedorId}")
    public ResponseEntity<List<VentaResponseDTO>> getVentaByVendedorId(@PathVariable Long vendedorId) {
        List<VentaResponseDTO> listaVentaByVendedor = ventaService.buscarByVendedor(new Usuario(vendedorId))
                .stream().map(ventaService::convertToResponseDTO).toList();
        return ResponseEntity.ok(listaVentaByVendedor);
    }

    @GetMapping("/porestado/{estado}")
    public ResponseEntity<List<VentaResponseDTO>> getVentaByEstado(@PathVariable String estado) {
        List<VentaResponseDTO> listaVentaByEstado = ventaService.buscarByEstadoPedido(estado).stream().
                map(ventaService::convertToResponseDTO).toList();
        return ResponseEntity.ok(listaVentaByEstado);
    }

    @GetMapping("/porclienteyestado")
    public ResponseEntity<List<VentaResponseDTO>> getVenetaByClienteAndEstado(@RequestParam Long clienteId, @RequestParam String estado) {
        Usuario cliente = usuarioRepository.findById(clienteId).orElseThrow(
            () -> new RuntimeException("no hay usuario")
        );
        List<VentaResponseDTO> listaRespuesta = ventaService.buscarByClienteYEstadoPedido(cliente, estado).stream()
        .map(ventaService::convertToResponseDTO).toList();
        return ResponseEntity.ok(listaRespuesta);
    }
    
    @GetMapping("/entrefecha") // /ventas/entrefecha?inicio=2025-11-01T00:00:00&fin=2025-11-06T23:59:59
    public ResponseEntity<List<VentaResponseDTO>> getVentraBetweenfecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechainicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechafin
    ) {
        List<VentaResponseDTO> listaVentaBetweenFecha = ventaService.buscarByFechaVenta(fechainicio, fechafin).stream().
                map(ventaService::convertToResponseDTO).toList();
        return ResponseEntity.ok(listaVentaBetweenFecha);
    }

}
