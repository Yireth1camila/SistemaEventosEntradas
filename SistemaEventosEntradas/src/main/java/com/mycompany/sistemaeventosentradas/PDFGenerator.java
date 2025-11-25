/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaeventosentradas;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PDFGenerator {
    
    // MÉTODO AUXILIAR para convertir HEX a BASECOLOR
    private BaseColor hexToBaseColor(String hex) {
        try {
            if (hex.startsWith("#")) {
                hex = hex.substring(1);
            }
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            return new BaseColor(r, g, b);
        } catch (Exception e) {
            return BaseColor.BLACK;
        }
    }

    /**
     * Genera el PDF de la boleta de acceso individual con el diseño lineal y QR centrado.
     * @param entrada Objeto Entrada con todos los datos de la venta.
     * @param nombreAdmin Nombre del administrador a incluir en la boleta.
     */
    public void generarPDFEntrada(Entrada entrada, String nombreAdmin) {
        String nombreEvento = entrada.getEvento().getNombre().replaceAll("\\s+", "_");
        String nombreCliente = entrada.getCliente().getNombre().replaceAll("\\s+", "_");
        String archivoPDF = "Boleta_" + nombreEvento + "_" + nombreCliente + ".pdf";
        
        Font fontEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.DARK_GRAY);
        Font fontNivel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.WHITE);
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        
        Document document = new Document();
        
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(archivoPDF));
            document.open();
            
            // --- 1. ENCABEZADO CON FRASE ALUSIVA ---
            Paragraph pTitulo = new Paragraph("BOLETA DE ACCESO", fontEncabezado);
            pTitulo.setAlignment(Element.ALIGN_CENTER);
            document.add(pTitulo);
            
            Paragraph pFrase = new Paragraph(entrada.getFraseCancion(), FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 14, BaseColor.RED));
            pFrase.setAlignment(Element.ALIGN_CENTER);
            document.add(pFrase);
            document.add(Chunk.NEWLINE);
            
            // Línea separadora
            Paragraph pSeparador = new Paragraph("— — — — — — — — — — — — — — — — — — — — — — — — — — — — — —", fontSubtitulo);
            pSeparador.setAlignment(Element.ALIGN_CENTER);
            document.add(pSeparador);
            document.add(Chunk.NEWLINE);

            // --- 2. DATOS DEL EVENTO Y PERSONAL (alineados a la izquierda, en fila) ---
            document.add(new Paragraph("EVENTO: " + entrada.getEvento().getNombre() + " - " + entrada.getEvento().getArtista(), fontSubtitulo));
            document.add(new Paragraph("VENDEDOR: " + entrada.getVendedor().getNombre(), fontNormal));
            document.add(new Paragraph("FECHA: " + entrada.getEvento().getFecha() + " | LUGAR: " + entrada.getEvento().getLugar(), fontNormal));
            document.add(new Paragraph("ADMINISTRADOR: " + nombreAdmin, fontNormal));
            document.add(Chunk.NEWLINE);
            
            // --- 3. NIVEL ASIGNADO (Centrado) ---
            String colorHex = entrada.getNivel().getColorHex();
            BaseColor nivelColor = hexToBaseColor(colorHex); 
            
            PdfContentByte canvas = writer.getDirectContentUnder();
            float rectWidth = 200; 
            float rectHeight = 35;
            
            float pageWidth = document.getPageSize().getWidth();
            float rectX = (pageWidth - rectWidth) / 2;
            
            // Posición Y basada en la posición actual del cursor
            float rectY = writer.getVerticalPosition(true) - rectHeight; 
            
            canvas.setColorFill(nivelColor);
            canvas.rectangle(rectX, rectY, rectWidth, rectHeight);
            canvas.fill();
            
            // Saltar las líneas para reservar el espacio del rectángulo
            document.add(new Paragraph("\n\n\n\n\n")); 
            
            // Dibuja el texto del nivel centrado sobre el rectángulo
            String textoNivel = "NIVEL: " + entrada.getNivel().getNombre();
            float textWidth = fontNivel.getBaseFont().getWidthPoint(textoNivel, fontNivel.getSize());
            float textY = rectY + (rectHeight / 2) - (fontNivel.getSize() / 2); // Ajuste vertical
            float textX = rectX + (rectWidth - textWidth) / 2; 
            
            canvas.beginText();
            canvas.setFontAndSize(fontNivel.getBaseFont(), fontNivel.getSize());
            canvas.setColorFill(fontNivel.getColor());
            canvas.setTextMatrix(textX, textY);
            canvas.showText(textoNivel);
            canvas.endText();
            
            document.add(Chunk.NEWLINE);
            
            // --- 4. CÓDIGO QR Y CÓDIGO DE VERIFICACIÓN (Centrados, después del Nivel) ---
            String qrData = entrada.getCodigoVerificacion();
            int qrSize = 120;
            
            // Código QR (Imagen)
            try {
                BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    qrData, BarcodeFormat.QR_CODE, qrSize, qrSize);

                try (ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream()) {
                    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
                    byte[] pngData = pngOutputStream.toByteArray();
                    Image qrImage = Image.getInstance(pngData);
                
                    qrImage.scaleAbsolute(qrSize, qrSize); 
                    qrImage.setAlignment(Element.ALIGN_CENTER); 
                    document.add(qrImage);
                }
            } catch (Exception qrEx) {
                System.err.println("❌ ERROR al generar o añadir la imagen del QR: " + qrEx.getMessage());
                document.add(new Paragraph("Error al mostrar QR: " + qrEx.getMessage(), fontNormal)); 
            }
            
            // Código de Verificación (Texto)
            Paragraph pCodigo = new Paragraph("CÓDIGO DE VERIFICACIÓN: " + qrData, fontSubtitulo);
            pCodigo.setAlignment(Element.ALIGN_CENTER);
            document.add(pCodigo);
            
            document.add(Chunk.NEWLINE);

            // --- 5. DETALLE DE LA COMPRA (alineados a la izquierda, en fila) ---
            document.add(new Paragraph("--- DETALLE DE LA COMPRA ---", fontSubtitulo));
            document.add(new Paragraph("PRECIO: $" + String.format("%.2f", entrada.getPrecio()), fontNormal));
            document.add(new Paragraph("MÉTODO DE PAGO: " + entrada.getMetodoPago(), fontNormal));
            document.add(new Paragraph("CUENTA ORIGEN: " + entrada.getCuentaOrigen(), fontNormal));
            document.add(Chunk.NEWLINE);
            
            // Pie de página
            Paragraph pGracias = new Paragraph("¡GRACIAS POR SU COMPRA!", fontEncabezado);
            pGracias.setAlignment(Element.ALIGN_CENTER);
            document.add(pGracias);


            System.out.println("\n✅ PDF generado exitosamente: " + archivoPDF);

        } catch (Exception e) {
            System.err.println("\n❌ ERROR al generar el PDF de Boleta. Verifique librerías.");
            System.err.println("Detalle del Error: " + e.getMessage());
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }
    
    // Método generarPDFRegistro se mantiene igual
    public void generarPDFRegistro(RegistroVentas registro) {
        String archivoPDF = "Reporte_Ventas_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".pdf";
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, new FileOutputStream(archivoPDF));
            document.open();
            
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.RED);
            Font fontCabeceraTabla = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            Font fontCelda = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);
            
            document.add(new Paragraph("REPORTE DE VENTAS DEL SISTEMA", fontTitulo));
            document.add(new Paragraph("Generado el: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            document.add(Chunk.NEWLINE);

            List<Entrada> ventas = registro.getEntradas(); 

            if (ventas == null || ventas.isEmpty()) {
                document.add(new Paragraph("No hay ventas registradas para generar el reporte."));
            } else {
                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                
                float[] columnWidths = {1.5f, 1.5f, 2f, 1.2f, 1.2f, 2.6f};
                table.setWidths(columnWidths);

                String[] headers = {"Cliente", "Vendedor", "Evento", "Nivel", "Precio ($)", "Cód. Verificación"};
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header, fontCabeceraTabla));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(BaseColor.DARK_GRAY);
                    cell.setPadding(5);
                    table.addCell(cell);
                }

                double totalRecaudado = 0;
                for (Entrada entrada : ventas) {
                    table.addCell(new Phrase(entrada.getCliente().getNombre(), fontCelda));
                    table.addCell(new Phrase(entrada.getVendedor().getNombre(), fontCelda));
                    table.addCell(new Phrase(entrada.getEvento().getNombre(), fontCelda));
                    
                    PdfPCell nivelCell = new PdfPCell(new Phrase(entrada.getNivel().getNombre(), fontCelda));
                    nivelCell.setBackgroundColor(hexToBaseColor(entrada.getNivel().getColorHex()));
                    table.addCell(nivelCell);
                    
                    PdfPCell precioCell = new PdfPCell(new Phrase(String.format("%.2f", entrada.getPrecio()), fontCelda));
                    precioCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(precioCell);
                    
                    table.addCell(new Phrase(entrada.getCodigoVerificacion(), fontCelda));
                    
                    totalRecaudado += entrada.getPrecio();
                }
                
                document.add(table);

                document.add(Chunk.NEWLINE);
                document.add(new Paragraph("--- RESUMEN DE VENTAS ---"));
                document.add(new Paragraph("Total de Entradas Vendidas: " + ventas.size()));
                document.add(new Paragraph("Total Recaudado: $" + String.format("%.2f", totalRecaudado), fontTitulo));
            }
            
            System.out.println("\n✅ REPORTE DE VENTAS generado exitosamente: " + archivoPDF);

        } catch (Exception e) {
            System.err.println("\n❌ ERROR al generar el PDF de Registro. Verifique librerías.");
            System.err.println("Detalle del Error: " + e.getMessage());
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }
}
