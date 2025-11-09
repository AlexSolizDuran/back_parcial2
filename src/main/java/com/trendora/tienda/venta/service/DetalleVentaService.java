package com.trendora.tienda.venta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trendora.tienda.inventario.model.ProdVariante;
import com.trendora.tienda.inventario.repository.ProdVarianteRepository;
import com.trendora.tienda.venta.dto.detalleventa.DetalleVentaRequestDTO;
import com.trendora.tienda.venta.dto.detalleventa.DetalleVentaResponseDTO;
import com.trendora.tienda.venta.model.DetalleVenta;
import com.trendora.tienda.venta.model.Venta;
import com.trendora.tienda.venta.repository.DetalleVentaRepository;
import com.trendora.tienda.venta.repository.VentaRepository;
import com.trendora.tienda.venta.service.interfaces.IDetalleVentaService;

@Service
public class DetalleVentaService implements IDetalleVentaService{

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProdVarianteRepository prodVarianteRepository;

    @Override
    public List<DetalleVentaResponseDTO> listarTodo() {
        return detalleVentaRepository.findAll().stream().map(this::convertToResponseDTO).toList();
    }

    @Override
    public DetalleVentaResponseDTO obtenerById(Long id) {
        return detalleVentaRepository.findById(id).map(this::convertToResponseDTO).orElseThrow(
            () -> new RuntimeException("no hay detalleVenta con ese id")
        );
    }

    @Override
    public List<DetalleVentaResponseDTO> obtenerByVenta(Venta venta) {
        return detalleVentaRepository.findByVenta(venta).stream().map(this::convertToResponseDTO).toList();
    }

    @Override
    public List<DetalleVentaResponseDTO> obtenerByPrudVariante(ProdVariante prodVariante) {
        return detalleVentaRepository.findByProdVariante(prodVariante).stream().map(this::convertToResponseDTO).toList();
    }

    @Override
    public void eliminar(Long id) {
        if(!detalleVentaRepository.existsById(id)){
            throw new RuntimeException("no existe el id a eliminar: " + id);
        }
        detalleVentaRepository.deleteById(id);
    }

    private DetalleVenta converToEntity(DetalleVentaRequestDTO dto){
        DetalleVenta detalleVenta = new DetalleVenta();
        updateEntityFromDTO(detalleVenta, dto);
        return detalleVenta;
    }//
    private void updateEntityFromDTO(DetalleVenta detalleVenta, DetalleVentaRequestDTO dto){
        Venta venta = ventaRepository.findById(dto.ventaId()).orElseThrow(
            () -> new RuntimeException("no existe venta con ese id metodo UpdateEntitty DetalleVentaService")
        );

        ProdVariante prodVariante = prodVarianteRepository.findById(dto.prodVarianteId()).orElseThrow(
            () -> new RuntimeException("no existe prodVariante con ese id metodo UpdateEntitty DetalleVentaService")
        );

        if (prodVariante.getStock() < dto.cantidad()) {
            throw new RuntimeException("Stock insuficiente para el producto: " + prodVariante.getId());
        }

        prodVariante.setStock(prodVariante.getStock() - dto.cantidad());
        prodVarianteRepository.save(prodVariante);
        
        detalleVenta.setCantidad(dto.cantidad());
        detalleVenta.setPrecioUnitario(dto.precio_unit());
        detalleVenta.setDescuento(dto.descuento());
        detalleVenta.setSubtotal(dto.subtotal());
        detalleVenta.setVenta(venta);
        detalleVenta.setProdVariante(prodVariante);
    }

    @Override
    @Transactional
    public DetalleVentaResponseDTO create(DetalleVentaRequestDTO dto) {
        DetalleVenta detalleVenta = converToEntity(dto);
        detalleVenta = detalleVentaRepository.save(detalleVenta);
        return convertToResponseDTO(detalleVenta);
    }

    @Override
    @Transactional
    public Optional<DetalleVentaResponseDTO> update(Long id, DetalleVentaRequestDTO dto) {
        return detalleVentaRepository.findById(id).map(detalleVenta -> {
            updateEntityFromDTO(detalleVenta, dto);
            detalleVenta = detalleVentaRepository.save(detalleVenta);
            return convertToResponseDTO(detalleVenta);
        });
        
    }

    @Override
    @Transactional
    public DetalleVentaResponseDTO convertToResponseDTO(DetalleVenta detalleVenta) {
        return new DetalleVentaResponseDTO(
            detalleVenta.getId(),
            detalleVenta.getCantidad(),
            detalleVenta.getPrecioUnitario(),
            detalleVenta.getDescuento(),
            detalleVenta.getSubtotal(),
            detalleVenta.getVenta().getId(),
            detalleVenta.getProdVariante().getId()
        );
    }
    
}
