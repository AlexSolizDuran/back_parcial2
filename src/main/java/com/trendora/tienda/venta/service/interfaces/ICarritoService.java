package com.trendora.tienda.venta.service.interfaces;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.trendora.tienda.venta.dto.carrito.CarritoRequestDTO;
import com.trendora.tienda.venta.dto.carrito.CarritoResponseDTO;
import com.trendora.tienda.venta.model.Carrito;
import com.trendora.tienda.usuario.model.Usuario;
public interface ICarritoService {
    List<Carrito> listarTodo();

    Optional<Carrito> buscarById(Long id);

    Carrito guardar(Carrito carrito);

    void eliminar(Long id);

    ///
    List<Carrito> buscarByCliente(Usuario clienteID);

    List<Carrito> buscarByEstado(String estado);

    List<Carrito> buscarByClienteYEstado(Usuario clienteID, String estado);

    List<Carrito> buscarByClienteYFecha(Usuario clienteID, LocalDateTime fecha);
    ///
    
    CarritoResponseDTO create(CarritoRequestDTO dto);
    Optional<CarritoResponseDTO> update(Long id, CarritoRequestDTO dto);
    CarritoResponseDTO convertToResponseDTO(Carrito carrito);
}
