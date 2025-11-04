package com.trendora.tienda.venta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.venta.model.Venta;
import com.trendora.tienda.venta.repository.VentaRepository;
import com.trendora.tienda.venta.service.interfaces.IVentaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



@Service
public class VentaService implements IVentaService {
    
    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public List<Venta> listarTodo(){
        return ventaRepository.findAll();
    } 
    
    @Override
    public Optional<Venta> buscarById(Long id){
        return ventaRepository.findById(id);
    }

    @Override
    public Venta guardar(Venta venta){
        return ventaRepository.save(venta);
    }

    @Override
    public void eliminar(Long id){
        ventaRepository.deleteById(id);
    }

    @Override
    public List<Venta> buscarByCliente(Usuario clienteID){
        return ventaRepository.findByCliente(clienteID);
    }
    
    @Override
    public List<Venta> buscarByVendedor(Usuario vendedorID){
        return ventaRepository.findByVendedor(vendedorID);
    }

    @Override
    public List<Venta> buscarByFechaVenta(LocalDateTime inicio, LocalDateTime fin){
        return ventaRepository.findByFechaVentaBetween(inicio, fin);
    }

    @Override
    public List<Venta> buscarByMetodoPago(String metodoPago){
        return ventaRepository.findByMetodoPago(metodoPago);
    }

    @Override
    public List<Venta> buscarByNumeroVenta(Long numeroVenta){
        return ventaRepository.findByNumeroVenta(numeroVenta);
    }

    @Override
    public List<Venta> buscarByTipoVenta(String tipoVenta){
        return ventaRepository.findByTipoVenta(tipoVenta);
    }

    @Override
    public List<Venta> buscarByEstadoPedido(String estadoPedido){
        return ventaRepository.findByEstadoPedido(estadoPedido);
    }

    @Override
    public List<Venta> buscarByClienteYEstadoPedido(Usuario clienteID, String estadoPedido){
        return ventaRepository.findByClienteAndEstadoPedido(clienteID, estadoPedido);
    }

    @Override
    public long contarByEstadoPedido(String estadoPedido){
        return ventaRepository.countByEstadoPedido(estadoPedido);
    }
}
