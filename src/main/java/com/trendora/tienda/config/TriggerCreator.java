package com.trendora.tienda.config;

import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate; // <-- 1. Importa esto
import org.springframework.stereotype.Component; // <-- 2. Importa esto

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;

@Component
// 3. AÑADIR ESTA LÍNEA:
// Esto le dice a Spring: "No crees este Bean hasta que el
// bean 'entityManagerFactory' (que maneja ddl-auto) esté listo".
// Esto soluciona tu error de "no existe la relación".
@DependsOn("entityManagerFactory") 
public class TriggerCreator {

    private final JdbcTemplate jdbcTemplate;

    // 4. Modificar el constructor para inyectar EntityManagerFactory
    // (Aunque no lo usemos directamente, la inyección fuerza el orden)
    public TriggerCreator(JdbcTemplate jdbcTemplate, EntityManagerFactory emf) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void createTriggers() {
        
        // --- 1. FUNCIÓN DEL TRIGGER ---
        // Esta función se dispara DESPUÉS de que se inserta un nuevo detalle
        // y suma el subtotal de ESE detalle al monto_total de la venta.
        
        // Basado en tu modelo Venta (tabla 'venta', columna 'monto_total')
        // y tu modelo DetalleVenta (tabla 'detalle_venta', columnas 'venta_id' y 'subtotal')
        
        jdbcTemplate.execute("""
            CREATE OR REPLACE FUNCTION fn_actualizar_monto_total_on_insert()
            RETURNS TRIGGER AS $$
            BEGIN
                -- Actualizamos la tabla 'venta'
                UPDATE venta
                -- Sumamos el subtotal del NUEVO detalle al monto_total existente
                SET monto_total = monto_total + NEW.subtotal
                -- Asegurándonos de que coincida el ID de la venta
                WHERE id = NEW.venta_id;
                
                RETURN NEW;
            END;
            $$ LANGUAGE plpgsql;
        """);

        // --- 2. EL TRIGGER ---
        // Borramos cualquier trigger anterior con este nombre
        jdbcTemplate.execute("""
            DROP TRIGGER IF EXISTS trg_actualizar_monto_on_insert ON detalle_venta;
            
            -- Creamos el trigger para que se ejecute DESPUÉS DE CADA INSERT
            -- ('cada vez que un detalle venta es creado')
            CREATE TRIGGER trg_actualizar_monto_on_insert
            AFTER INSERT ON detalle_venta
            FOR EACH ROW
            EXECUTE FUNCTION fn_actualizar_monto_total_on_insert();
        """);
        
        System.out.println("--- Trigger 'trg_actualizar_monto_on_insert' (re)creado ---");
    }
}