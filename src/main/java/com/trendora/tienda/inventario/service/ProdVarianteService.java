package com.trendora.tienda.inventario.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trendora.tienda.inventario.dto.prodVariante.ProdVarianteListDTO;
import com.trendora.tienda.inventario.dto.prodVariante.ProdVarianteRequestDTO;
import com.trendora.tienda.inventario.dto.prodVariante.ProdVarianteResponseDTO;
import com.trendora.tienda.inventario.model.Color;
import com.trendora.tienda.inventario.model.ProdVariante;
import com.trendora.tienda.inventario.model.Talla;
import com.trendora.tienda.inventario.repository.ColorRepository;
import com.trendora.tienda.inventario.repository.ProdVarianteRepository;
import com.trendora.tienda.inventario.repository.TallaRepository;
import com.trendora.tienda.inventario.service.interfaces.IColorService;
import com.trendora.tienda.inventario.service.interfaces.IProdVarianteService;
import com.trendora.tienda.inventario.service.interfaces.ITallaService;
import com.trendora.tienda.producto.model.Producto;
import com.trendora.tienda.producto.repository.ProductoRepository;
import com.trendora.tienda.producto.service.interfaces.IProductoService;

@Service
public class ProdVarianteService implements IProdVarianteService {

    @Autowired
    private ProdVarianteRepository prodVarianteRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private TallaRepository tallaRepository;

    @Autowired
    private IProductoService productoService;
    @Autowired
    private IColorService colorService;
    @Autowired
    private ITallaService tallaService;

    @Override
    @Transactional(readOnly = true)
    public List<ProdVarianteListDTO> listAll() {
        return prodVarianteRepository.findAll().stream().map(this::convertToProdVarianteListDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdVarianteResponseDTO> findByProductoId(Long productoId) {
        return prodVarianteRepository.findByProductoId(productoId).stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProdVarianteResponseDTO> findById(Long id) {
        return prodVarianteRepository.findById(id).map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public ProdVarianteResponseDTO create(ProdVarianteRequestDTO requestDTO) {
        ProdVariante prodVariante = new ProdVariante();
        updateFromDTO(prodVariante, requestDTO);
        return convertToResponseDTO(prodVarianteRepository.save(prodVariante));
    }

    @Override
    @Transactional
    public Optional<ProdVarianteResponseDTO> update(Long id, ProdVarianteRequestDTO requestDTO) {
        return prodVarianteRepository.findById(id).map(prodVariante -> {
            updateFromDTO(prodVariante, requestDTO);
            return convertToResponseDTO(prodVarianteRepository.save(prodVariante));
        });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        prodVarianteRepository.deleteById(id);
    }

    private void updateFromDTO(ProdVariante prodVariante, ProdVarianteRequestDTO dto) {
        Producto producto = productoRepository.findById(dto.productoId()).orElseThrow(() -> new RuntimeException("Producto not found"));
        Color color = colorRepository.findById(dto.colorId()).orElseThrow(() -> new RuntimeException("Color not found"));
        Talla talla = tallaRepository.findById(dto.tallaId()).orElseThrow(() -> new RuntimeException("Talla not found"));

        prodVariante.setProducto(producto);
        prodVariante.setColor(color);
        prodVariante.setTalla(talla);
        prodVariante.setCosto(dto.costo());
        prodVariante.setPpp(dto.ppp());
        prodVariante.setPrecio(dto.precio());
        prodVariante.setSku(dto.sku());
        prodVariante.setStock(dto.stock());
    }

    @Override
    public ProdVarianteResponseDTO convertToResponseDTO(ProdVariante pv) {
        return new ProdVarianteResponseDTO(
                pv.getId(),
                productoService.convertToResponseDTO(pv.getProducto()),
                colorService.convertToResponseDTO(pv.getColor()),
                tallaService.convertToResponseDTO(pv.getTalla()),
                pv.getCosto(),
                pv.getPpp(),
                pv.getPrecio(),
                pv.getSku(),
                pv.getStock()
        );
    }

    @Override
    public ProdVarianteListDTO convertToProdVarianteListDTO(ProdVariante pv) {
        return new ProdVarianteListDTO(
                pv.getId(),
                pv.getProducto().getId(),
                pv.getColor().getId(),
                pv.getTalla().getId(),
                pv.getCosto(),
                pv.getPpp(),
                pv.getPrecio(),
                pv.getSku(),
                pv.getStock()
        );
    }
}