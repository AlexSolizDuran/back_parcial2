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

        jdbcTemplate.execute("""
            CREATE OR REPLACE FUNCTION fn_actualizar_monto_total_venta()
            RETURNS TRIGGER AS $$
            DECLARE
                v_venta_id BIGINT;
            BEGIN
                -- Determinar el 'venta_id' afectado
                IF (TG_OP = 'INSERT') OR (TG_OP = 'UPDATE') THEN
                    v_venta_id := NEW.venta_id;
                ELSEIF (TG_OP = 'DELETE') THEN
                    v_venta_id := OLD.venta_id;
                END IF;

                -- Recalcular el SUM() completo para esa venta
                UPDATE venta
                SET monto_total = (
                    SELECT COALESCE(SUM(subtotal), 0)
                    FROM detalle_venta
                    WHERE venta_id = v_venta_id
                )
                WHERE id = v_venta_id;

                -- Caso especial: si un detalle se MUEVE de una venta a otra
                IF (TG_OP = 'UPDATE' AND NEW.venta_id != OLD.venta_id) THEN
                    UPDATE venta
                    SET monto_total = (
                        SELECT COALESCE(SUM(subtotal), 0)
                        FROM detalle_venta
                        WHERE venta_id = OLD.venta_id
                    )
                    WHERE id = OLD.venta_id;
                END IF;

                IF (TG_OP = 'DELETE') THEN
                    RETURN OLD;
                ELSE
                    RETURN NEW;
                END IF;
            END;
            $$ LANGUAGE plpgsql;
        """);

        jdbcTemplate.execute("""
            DROP TRIGGER IF EXISTS trg_actualizar_monto_total ON detalle_venta;
            
            CREATE TRIGGER trg_actualizar_monto_total
            AFTER INSERT OR UPDATE OR DELETE ON detalle_venta -- Se ejecuta en los 3 casos
            FOR EACH ROW
            EXECUTE FUNCTION fn_actualizar_monto_total_venta();
        """);

        // --- TRIGGER SET 3: Calcular 'ppp' y 'ppv' en 'prod_variante' ---
        // Este es el trigger que calcula el costo y precio promedio ponderado
        // cuando se actualiza el inventario.
        jdbcTemplate.execute("""
            CREATE OR REPLACE FUNCTION fn_calcular_ppp_ppv_on_update()
            RETURNS TRIGGER AS $$
            DECLARE
                added_stock INTEGER;
            BEGIN
                -- Escenario 1: Se AÑADIÓ stock (compra de inventario)
                IF (NEW.stock > OLD.stock) THEN
                    added_stock := NEW.stock - OLD.stock;
                    
                    -- Cálculo de PPP (Costo Promedio Ponderado)
                    NEW.ppp = ((OLD.stock * OLD.ppp) + (added_stock * NEW.costo)) / NEW.stock;
                    
                    -- Cálculo de PPV (Precio Promedio Ponderado)
                    NEW.ppv = ((OLD.stock * OLD.ppv) + (added_stock * NEW.precio)) / NEW.stock;

                -- Escenario 2: Se quitó stock (venta) o el stock no cambió
                -- El costo (ppp) y precio (ppv) del inventario restante no cambian.
                ELSE
                    NEW.ppp = OLD.ppp;
                    NEW.ppv = OLD.ppv;
                END IF;
            
                RETURN NEW;
            END;
            $$ LANGUAGE plpgsql;
        """);

        jdbcTemplate.execute("""
            DROP TRIGGER IF EXISTS trg_calcular_ppp_ppv ON prod_variante;
            
            CREATE TRIGGER trg_calcular_ppp_ppv
            BEFORE UPDATE ON prod_variante
            FOR EACH ROW
            -- Solo se ejecuta si cambia el costo, precio o stock
            WHEN (OLD.costo IS DISTINCT FROM NEW.costo OR
                  OLD.precio IS DISTINCT FROM NEW.precio OR
                  OLD.stock IS DISTINCT FROM NEW.stock)
            EXECUTE FUNCTION fn_calcular_ppp_ppv_on_update();
        """);

        System.out.println("--- Todos los Triggers de BD han sido (re)creados ---");
    }

}
