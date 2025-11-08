package com.trendora.tienda.producto.dto.categoria; // O tu paquete de DTOs

import java.util.ArrayList;
import java.util.List;

// Puedes usar un 'record' o una 'class'
public class CategoriaTreeDTO {
    
    private Long id;
    private String nombre;
    private List<CategoriaTreeDTO> hijos; // <-- La clave está aquí

    // Constructor principal
    public CategoriaTreeDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.hijos = new ArrayList<>(); // Inicializar siempre la lista
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public List<CategoriaTreeDTO> getHijos() { return hijos; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setHijos(List<CategoriaTreeDTO> hijos) { this.hijos = hijos; }

    // Método de ayuda para añadir hijos
    public void addHijo(CategoriaTreeDTO hijo) {
        this.hijos.add(hijo);
    }
}
