package com.trendora.tienda.venta.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
