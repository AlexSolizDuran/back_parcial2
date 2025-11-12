package com.trendora.tienda.reporte.generator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.trendora.tienda.reporte.dto.ReporteGeneradoDTO;

@Service("excelGenerator")
public class ExcelGeneratorService implements IReportGenerator {

    @Override
    public ReporteGeneradoDTO generar(List<Map<String, Object>> datos, String nombreReporteBase) {
        
        // Si no hay datos, devolvemos un archivo Excel vacío o lanzamos un error.
        if (datos == null || datos.isEmpty()) {
            throw new RuntimeException("No hay datos para generar el reporte Excel.");
        }
        
        // 1. Crear el libro de trabajo (.xlsx)
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte");

            // Obtener los encabezados (asumimos que la primera fila tiene todas las claves)
            Set<String> headers = datos.get(0).keySet();
            
            // 2. Crear la fila de encabezados
            Row headerRow = sheet.createRow(0);
            int colNum = 0;
            for (String header : headers) {
                Cell cell = headerRow.createCell(colNum++);
                cell.setCellValue(header);
            }

            // 3. Llenar las filas de datos
            int rowNum = 1;
            for (Map<String, Object> rowData : datos) {
                Row row = sheet.createRow(rowNum++);
                colNum = 0;
                
                for (String header : headers) {
                    Cell cell = row.createCell(colNum++);
                    Object value = rowData.get(header);
                    
                    if (value != null) {
                        // Lógica simple para manejar diferentes tipos de datos
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    } else {
                        cell.setCellValue("");
                    }
                }
            }
            
            // 4. Escribir el contenido en el ByteArrayOutputStream
            workbook.write(out);

            // 5. Devolver el DTO con los bytes y metadatos
            return new ReporteGeneradoDTO(
                out.toByteArray(),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                nombreReporteBase + ".xlsx"
            );

        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo Excel con Apache POI.", e);
        }
    }
}