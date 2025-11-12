package com.trendora.tienda.reporte.generator;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.trendora.tienda.reporte.dto.ReporteGeneradoDTO;

@Service("pdfGenerator")
public class PdfGeneratorService implements IReportGenerator {

    @Override
    public ReporteGeneradoDTO generar(List<Map<String, Object>> datos, String nombreReporteBase) {
        
        if (datos == null || datos.isEmpty()) {
            throw new RuntimeException("No hay datos para generar el reporte PDF.");
        }
        
        // 1. Configurar streams y documentos
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            // Obtener encabezados
            Set<String> headers = datos.get(0).keySet();
            int numColumns = headers.size();

            // 2. Crear título y tabla
            document.add(new Paragraph("Reporte Generado por IA"));
            
            // Crear una tabla que ocupe todo el ancho
            Table table = new Table(UnitValue.createPercentArray(numColumns)).useAllAvailableWidth();
            
            // 3. Añadir encabezados
            for (String header : headers) {
                table.addHeaderCell(header);
            }
            
            // 4. Añadir filas de datos
            for (Map<String, Object> rowData : datos) {
                for (String header : headers) {
                    Object value = rowData.get(header);
                    String cellContent = (value != null) ? value.toString() : "";
                    table.addCell(cellContent);
                }
            }

            // 5. Añadir la tabla al documento y cerrarlo
            document.add(table);
            document.close();
            
            // 6. Devolver el DTO con los bytes y metadatos
            return new ReporteGeneradoDTO(
                out.toByteArray(),
                "application/pdf",
                nombreReporteBase + ".pdf"
            );

        } catch (IOException e) {
            throw new RuntimeException("Error al configurar streams de PDF.", e);
        } catch (Exception e) {
             throw new RuntimeException("Error al generar el archivo PDF con iText 7.", e);
        }
    }
}
