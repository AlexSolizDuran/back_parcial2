package com.trendora.tienda.venta.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trendora.tienda.inventario.model.ProdVariante;
import com.trendora.tienda.inventario.repository.ProdVarianteRepository;
import com.trendora.tienda.inventario.service.interfaces.IProdVarianteService;
import com.trendora.tienda.venta.dto.itemcarrito.ItemCarritoRequestDTO;
import com.trendora.tienda.venta.dto.itemcarrito.ItemCarritoResponseDTO;
import com.trendora.tienda.venta.model.Carrito;
import com.trendora.tienda.venta.model.ItemCarrito;
import com.trendora.tienda.venta.repository.CarritoRepository;
import com.trendora.tienda.venta.repository.ItemCarritoRepository;
import com.trendora.tienda.venta.service.interfaces.IItemCarritoService;

@Service
public class ItemCarritoService implements IItemCarritoService {

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProdVarianteRepository prodVarianteRepository;

    @Autowired
    private IProdVarianteService prodVarianteService;

    @Override
    @Transactional(readOnly = true)
    public List<ItemCarritoResponseDTO> listarTodo() {
        return itemCarritoRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemCarritoResponseDTO> buscarById(Long id) {
        return itemCarritoRepository.findById(id).map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        itemCarritoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemCarritoResponseDTO> buscarByCarrito(Carrito carrito) {
        return itemCarritoRepository.findByCarrito(carrito).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private ItemCarrito convertToEntity(ItemCarritoRequestDTO dto) {
        ItemCarrito itemCarrito = new ItemCarrito();
        updateEntityFromDTO(itemCarrito, dto);
        return itemCarrito;
    }

    private void updateEntityFromDTO(ItemCarrito itemCarrito, ItemCarritoRequestDTO dto) {
        Carrito carrito = carritoRepository.findById(dto.carritoId()).orElseThrow(
                () -> new RuntimeException("No existe carrito con ese id")
        );
        System.out.println("ID VARIANTE"+dto.prodVarianteId());
        ProdVariante prodVariante = prodVarianteRepository.findById(dto.prodVarianteId()).orElseThrow(
                () -> new RuntimeException("No existe producto variante con esa id")
        );

        itemCarrito.setCarrito(carrito);
        itemCarrito.setProdVariante(prodVariante);
        itemCarrito.setCantidad(dto.cantidad());
    }

    @Override
    @Transactional
    public ItemCarritoResponseDTO create(ItemCarritoRequestDTO dto) {
        ItemCarrito itemCarrito = convertToEntity(dto);
        itemCarrito = itemCarritoRepository.save(itemCarrito);
        return convertToResponseDTO(itemCarrito);
    }

    @Override
    @Transactional
    public Optional<ItemCarritoResponseDTO> update(Long id, ItemCarritoRequestDTO dto) {
        return itemCarritoRepository.findById(id).map(itemCarrito -> {
            updateEntityFromDTO(itemCarrito, dto);
            itemCarrito = itemCarritoRepository.save(itemCarrito);
            return convertToResponseDTO(itemCarrito);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public ItemCarritoResponseDTO convertToResponseDTO(ItemCarrito itemCarrito) {
        return new ItemCarritoResponseDTO(
                itemCarrito.getId(),
                itemCarrito.getCarrito().getId(),
                prodVarianteService.convertToResponseDTO(itemCarrito.getProdVariante()),
                itemCarrito.getCantidad()
        );
    }
}

