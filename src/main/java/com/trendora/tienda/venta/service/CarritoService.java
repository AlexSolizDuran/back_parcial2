package com.trendora.tienda.venta.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.usuario.repository.UsuarioRepository;
import com.trendora.tienda.venta.dto.carrito.CarritoRequestDTO;
import com.trendora.tienda.venta.dto.carrito.CarritoResponseDTO;
import com.trendora.tienda.venta.model.Carrito;
import com.trendora.tienda.venta.repository.CarritoRepository;
import com.trendora.tienda.venta.service.interfaces.ICarritoService;

@Service
public class CarritoService implements ICarritoService{
    
    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Carrito> listarTodo(){
        return carritoRepository.findAll();
    }

    @Override
    public Optional<Carrito> buscarById(Long id){
        return carritoRepository.findById(id);
    }

    @Override
    public Carrito guardar(Carrito carrito){
        return carritoRepository.save(carrito);
    }

    @Override
    public void eliminar(Long id){
        carritoRepository.deleteById(id);
    }

    @Override
    public List<Carrito> buscarByCliente(Usuario clienteID){
        return carritoRepository.findByCliente(clienteID);
    }

    @Override
    public List<Carrito> buscarByEstado(String estado){
        return carritoRepository.findByEstado(estado);
    }

    @Override
    public List<Carrito> buscarByClienteYEstado(Usuario clienteID, String estado){
        return carritoRepository.findByClienteAndEstado(clienteID, estado);
    }

    @Override
    public List<Carrito> buscarByClienteYFecha(Usuario clienteID, LocalDateTime fecha){
        return carritoRepository.findByClienteAndFecha(clienteID, fecha);
    }

    //aux
    private Carrito convertToEntity(CarritoRequestDTO dto){
        Carrito carrito = new Carrito();
        updateEntityFromDTO(carrito, dto);
        return carrito;
    }

    private void updateEntityFromDTO(Carrito carrito, CarritoRequestDTO dto){
        Usuario usuario = usuarioRepository.findById(dto.clienteId()).orElseThrow(
            ()-> new RuntimeException("no existe cliente con el id" + dto.clienteId())
        );
        carrito.setCliente(usuario);
    }

    @Override
    @Transactional
    public CarritoResponseDTO create(CarritoRequestDTO dto){
        Carrito carrito = convertToEntity(dto);
        carrito=carritoRepository.save(carrito);
        return convertToResponseDTO(carrito);
    }

    @Override
    @Transactional
    public Optional<CarritoResponseDTO> update(Long id, CarritoRequestDTO dto){
        return carritoRepository.findById(id).map(carrito -> {
            updateEntityFromDTO(carrito, dto);
            carrito=carritoRepository.save(carrito);
            return convertToResponseDTO(carrito);
        });
    }

    @Override
    @Transactional
    public CarritoResponseDTO convertToResponseDTO(Carrito carrito){
        return new CarritoResponseDTO(
            carrito.getId(),
            carrito.getCliente().getId(),
            carrito.getFecha(),
            carrito.getEstado()
        );
    }
}
