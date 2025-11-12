package com.trendora.tienda.venta.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendora.tienda.inventario.model.ProdVariante;
import com.trendora.tienda.inventario.repository.ProdVarianteRepository;
import com.trendora.tienda.venta.dto.CheckoutItemDTO;
import com.trendora.tienda.venta.dto.CheckoutRequestDTO;
import com.trendora.tienda.venta.dto.CheckoutResponseDTO;
import com.trendora.tienda.venta.model.DetalleVenta;
import com.trendora.tienda.venta.model.Carrito;
import com.trendora.tienda.venta.repository.CarritoRepository;
import com.trendora.tienda.venta.repository.DetalleVentaRepository;
import com.trendora.tienda.venta.repository.ItemCarritoRepository;
import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.usuario.repository.UsuarioRepository;
import com.trendora.tienda.venta.dto.VentaRequestDTO;
import com.trendora.tienda.venta.dto.VentaResponseDTO;
import com.trendora.tienda.venta.model.Venta;
import com.trendora.tienda.venta.repository.VentaRepository;
import com.trendora.tienda.venta.service.interfaces.IVentaService;

@Service
public class VentaService implements IVentaService {
    
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired 
    private DetalleVentaRepository detalleVentaRepository;
    @Autowired 
    private ProdVarianteRepository prodVarianteRepository;
    @Autowired 
    private CarritoRepository carritoRepository;
    @Autowired 
    private ItemCarritoRepository itemCarritoRepository;
    @Autowired 
    private RestTemplate restTemplate;
    @Autowired 
    private ObjectMapper objectMapper;

    @Override
    public List<Venta> listarTodo() {
        return ventaRepository.findAll();
    } 
    
    @Override
    public Optional<Venta> buscarById(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    public Venta guardar(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public void eliminar(Long id) {
        ventaRepository.deleteById(id);
    }

    @Override
    public List<Venta> buscarByCliente(Usuario clienteID) {
        return ventaRepository.findByCliente(clienteID);
    }
    
    @Override
    public List<Venta> buscarByVendedor(Usuario vendedorID) {
        return ventaRepository.findByVendedor(vendedorID);
    }

    @Override
    public List<Venta> buscarByFechaVenta(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaVentaBetween(inicio, fin);
    }

    @Override
    public List<Venta> buscarByMetodoPago(String metodoPago) {
        return ventaRepository.findByMetodoPago(metodoPago);
    }

    @Override
    public List<Venta> buscarByNumeroVenta(Long numeroVenta) {
        return ventaRepository.findByNumeroVenta(numeroVenta);
    }

    @Override
    public List<Venta> buscarByTipoVenta(String tipoVenta) {
        return ventaRepository.findByTipoVenta(tipoVenta);
    }

    @Override
    public List<Venta> buscarByEstadoPedido(String estadoPedido) {
        return ventaRepository.findByEstadoPedido(estadoPedido);
    }

    @Override
    public List<Venta> buscarByClienteYEstadoPedido(Usuario clienteID, String estadoPedido) {
        return ventaRepository.findByClienteAndEstadoPedido(clienteID, estadoPedido);
    }

    @Override
    public long contarByEstadoPedido(String estadoPedido) {
        return ventaRepository.countByEstadoPedido(estadoPedido);
    }

    //metodos auxilares
    private Venta convertToEntity(VentaRequestDTO dto) {
        Venta venta = new Venta();
        updateEntityFromDTO(venta, dto);
        return venta;
    }

    private void updateEntityFromDTO(Venta venta, VentaRequestDTO dto) {
        Usuario cliente = usuarioRepository.findById(dto.clienteID()).orElseThrow(
                () -> new RuntimeException("no hay cliente con ese id")
        );

        Usuario vendedor = usuarioRepository.findById(dto.vendedorID()).orElseThrow(
                () -> new RuntimeException("no hay vendedor con ese id")
        );

        venta.setCliente(cliente);
        venta.setVendedor(vendedor);
        venta.setMetodoPago(dto.metodoPago());
        venta.setTipoVenta(dto.tipoVenta());
    }

    @Override
    @Transactional
    public VentaResponseDTO create(VentaRequestDTO dto) {
        Venta venta = convertToEntity(dto);
        Long maxNumeroVenta = ventaRepository.findMaxNumeroVenta();
        venta.setNumeroVenta(maxNumeroVenta + 1);
        venta = ventaRepository.save(venta);

        return convertToResponseDTO(venta);
    }

    @Override
    @Transactional
    public Optional<VentaResponseDTO> update(Long id, VentaRequestDTO dto) {
        return ventaRepository.findById(id).map(venta -> {
            updateEntityFromDTO(venta, dto);
            venta = ventaRepository.save(venta);
            return convertToResponseDTO(venta);
        });
    }

    @Override
    @Transactional
    public VentaResponseDTO convertToResponseDTO(Venta venta) {
        return new VentaResponseDTO(
                venta.getId(),
                venta.getNumeroVenta(),
                venta.getCliente().getId(),
                venta.getVendedor().getId(),
                venta.getMetodoPago(),
                venta.getTipoVenta(),
                venta.getMontoTotal(),
                venta.getEstadoPedido(),
                venta.getFechaVenta()
        );
    }

    @Override
    @Transactional
    public CheckoutResponseDTO generarPagoLibelula(CheckoutRequestDTO dto, Long clienteId) {
        try {
            Usuario cliente = usuarioRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            
            // 1. Crear la Venta con estado "pendiente"
            Venta venta = new Venta();
            venta.setCliente(cliente);
            // Asignar un vendedor por defecto (ej. el admin con ID 1)
            venta.setVendedor(usuarioRepository.findById(1L).orElse(cliente));
            venta.setMetodoPago(dto.metodoPago());
            venta.setTipoVenta("Online");
            venta.setEstadoPedido("pendiente"); // Estado inicial
            
            Long maxNumeroVenta = ventaRepository.findMaxNumeroVenta();
            venta.setNumeroVenta(maxNumeroVenta + 1);

            Venta savedVenta = ventaRepository.save(venta);
            
            double montoTotalReal = 0.0;

            // 2. Crear los DetalleVenta y calcular total real
            List<Map<String, Object>> lineasDetalleReales = new ArrayList<>();
            for (CheckoutItemDTO item : dto.items()) {
                ProdVariante pv = prodVarianteRepository.findById(item.prodVarianteId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                
                if (pv.getStock() < item.cantidad()) {
                    throw new RuntimeException("Stock insuficiente para: " + pv.getSku());
                }
                
                // Actualizar stock
                pv.setStock(pv.getStock() - item.cantidad());
                prodVarianteRepository.save(pv);

                DetalleVenta dv = new DetalleVenta();
                dv.setVenta(savedVenta);
                dv.setProdVariante(pv);
                dv.setCantidad(item.cantidad());
                double precioUnit = pv.getPrecio().doubleValue();
                dv.setPrecioUnitario(precioUnit);
                dv.setDescuento(0.0); // Sin descuento desde el carrito
                double subtotal = precioUnit * item.cantidad();
                dv.setSubtotal(subtotal);
                
                detalleVentaRepository.save(dv);
                montoTotalReal += subtotal;
            }

            // 3. Guardar el monto total real en la venta
            savedVenta.setMontoTotal(montoTotalReal);
            ventaRepository.save(savedVenta);

            // 4. --- HACK 0.1 Bs ---
            // Preparamos el payload como DATOS DE FORMULARIO (igual que en Python)
            
            String libelulaApiUrl = "https://api.libelula.bo/rest/deuda/registrar";             
            String appBackendUrl = "https://backparcial2-393159630636.northamerica-south1.run.app"; // (Quitado el / al final) //cambiar por url backend
            String callbackUrl = appBackendUrl + "/venta/venta/libelula-callback?myVentaId=" + savedVenta.getId();

            // 1. Crear el payload como MultiValueMap (para form-urlencoded)
            MultiValueMap<String, String> payloadLibelula = new LinkedMultiValueMap<>();
            
            // 2. Añadir todos los parámetros planos
            payloadLibelula.add("appkey", "11bb10ce-68ba-4af1-8eb7-4e6624fed729");
            payloadLibelula.add("email_cliente", cliente.getEmail());
            String identificadorUnico = savedVenta.getNumeroVenta().toString() + "_" + System.currentTimeMillis();
            payloadLibelula.add("identificador", identificadorUnico);
            payloadLibelula.add("nombre_cliente", cliente.getNombre());
            payloadLibelula.add("apellido_cliente", cliente.getApellido());
            payloadLibelula.add("nit", "0");
            payloadLibelula.add("razon_social", cliente.getNombre() + " " + cliente.getApellido());
            payloadLibelula.add("moneda", "BOB");
            payloadLibelula.add("callback_url", callbackUrl);
            payloadLibelula.add("descripcion", "Pago Online por Venta Nro: " + savedVenta.getNumeroVenta().toString());
            // 3. Aplanar las líneas de detalle (como en tu código Python)
            // Usamos el "hack" de 0.1 Bs
            payloadLibelula.add("lineas_detalle_deuda[0].concepto", "Pago de prueba Trendora");
            payloadLibelula.add("lineas_detalle_deuda[0].cantidad", "1");
            payloadLibelula.add("lineas_detalle_deuda[0].costo_unitario", "0.1");
            payloadLibelula.add("lineas_detalle_deuda[0].descuento_unitario", "0");
            
            System.out.println("¡¡¡ENVIANDO COMO FORMULARIO!!! Payload: " + payloadLibelula);

            // 5. Llamar a la API de Libélula (enviando como FORMULARIO)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // <-- ¡LA CLAVE!
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(payloadLibelula, headers);
            
            String responseJson = restTemplate.postForObject(libelulaApiUrl, request, String.class);
            
            // El resto del código (procesar la respuesta JSON) es igual
            Map<String, Object> responseMap = objectMapper.readValue(responseJson, Map.class);

            if ((Integer)responseMap.get("error") != 0) {
            // ... (el resto de tu bloque try-catch sigue igual) ...
                throw new RuntimeException("Error Libélula: " + responseMap.get("mensaje"));
            }
            
            // 6. Guardar el ID de Libélula y vaciar carrito
            String urlPasarela = (String) responseMap.get("url_pasarela_pagos");
            String idTransaccion = (String) responseMap.get("id_transaccion");

            savedVenta.setLibelulaTransactionId(idTransaccion);
            ventaRepository.save(savedVenta);

            // Vaciar el carrito del 
            Carrito carrito = carritoRepository.findByCliente(cliente)
                                .stream().findFirst()
                                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
            itemCarritoRepository.deleteAll(carrito.getItems());

            // 7. Devolver la URL de pago a Flutter
            return new CheckoutResponseDTO(urlPasarela);

        } catch (Exception e) {
            throw new RuntimeException("Error al generar pago: " + e.getMessage(), e);
        }
    }

    // CALLBACK
    @Override
    @Transactional
    public void confirmarPago(Long ventaId, String libelulaTransactionId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + ventaId));
        
        // Verificamos si ya está pagada para evitar doble procesamiento
        if (venta.getEstadoPedido().equals("pendiente")) {
            venta.setEstadoPedido("pagado");
            
            // (Opcional) Guardamos el ID de Libélula si no lo guardamos antes
            if (venta.getLibelulaTransactionId() == null && libelulaTransactionId != null) {
                 venta.setLibelulaTransactionId(libelulaTransactionId);
            }
            ventaRepository.save(venta);
            System.out.println("Venta ID: " + ventaId + " marcada como PAGADA.");
        }
    }

}
