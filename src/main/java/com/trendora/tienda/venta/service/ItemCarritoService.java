package com.trendora.tienda.venta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trendora.tienda.venta.dto.itemcarrito.ItemCarritoRequestDTO;
import com.trendora.tienda.venta.dto.itemcarrito.ItemCarritoResponseDTO;
import com.trendora.tienda.venta.model.Carrito;
import com.trendora.tienda.venta.model.ItemCarrito;
import com.trendora.tienda.venta.repository.ItemCarritoRepository;
import com.trendora.tienda.venta.service.interfaces.IItemCarritoService;

import com.trendora.tienda.venta.repository.CarritoRepository;
import com.trendora.tienda.inventario.model.ProdVariante;
import com.trendora.tienda.inventario.repository.ProdVarianteRepository;;
@Service
public class ItemCarritoService implements IItemCarritoService{

    @Autowired
    private ItemCarritoRepository itemCarritoRepository; 

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProdVarianteRepository prodVarianteRepository;

    @Override
    public List<ItemCarrito> listarTodo() {
        // TODO Auto-generated method stub
        return itemCarritoRepository.findAll();
    }

    @Override
    public Optional<ItemCarrito> buscarById(Long id) {
        // TODO Auto-generated method stub
        return itemCarritoRepository.findById(id);
    }

    @Override
    public void eliminar(Long id) {
        // TODO Auto-generated method stub
        itemCarritoRepository.deleteById(id);
    }

    @Override
    public List<ItemCarrito> buscarByCarrito(Carrito carrito) {
        // TODO Auto-generated method stub
        return itemCarritoRepository.findByCarrito(carrito);
    }

    //au8x
    private ItemCarrito convertToEntity(ItemCarritoRequestDTO dto){
        ItemCarrito itemCarrito = new ItemCarrito();
        updateEntityFromDTO(itemCarrito, dto);
        return itemCarrito;
    }

    private void updateEntityFromDTO(ItemCarrito itemCarrito, ItemCarritoRequestDTO dto){
        Carrito carrito = carritoRepository.findById(dto.carritoId()).orElseThrow(
            ()-> new RuntimeException("no existe carrito con ese id")
        );

        ProdVariante prodVariante = prodVarianteRepository.findById(dto.prodVariableId()).orElseThrow(
            () -> new RuntimeException("no existe producto variante con esa id")
        );

        itemCarrito.setCarrito(carrito);
        itemCarrito.setProdVariante(prodVariante);
        itemCarrito.setCantidad(dto.cantidad());
    }

    @Override
    @Transactional
    public ItemCarritoResponseDTO create(ItemCarritoRequestDTO dto) {
        // TODO Auto-generated method stub
        ItemCarrito itemCarrito  = convertToEntity(dto);
        itemCarrito=itemCarritoRepository.save(itemCarrito);
        return convertToResponseDTO(itemCarrito);
    }

    @Override
    @Transactional
    public Optional<ItemCarritoResponseDTO> update(Long id, ItemCarritoRequestDTO dto) {
        // TODO Auto-generated method stub
        return itemCarritoRepository.findById(id).map(itemCarrito -> {
            updateEntityFromDTO(itemCarrito, dto);
            itemCarrito =itemCarritoRepository.save(itemCarrito);
            return convertToResponseDTO(itemCarrito);
        });
    }

    @Override
    @Transactional
    public ItemCarritoResponseDTO convertToResponseDTO(ItemCarrito itemCarrito) {
        // TODO Auto-generated method stub
        return new ItemCarritoResponseDTO(
            itemCarrito.getId(),
            itemCarrito.getCarrito().getId(),
            itemCarrito.getProdVariante().getId(),
            itemCarrito.getCantidad(),
            itemCarrito.getFecha()
        );
    }

}
