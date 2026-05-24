package com.ahorroperu.util;

import com.ahorroperu.modelo.SolicitudPrestamo;
import com.ahorroperu.modelo.Cuota;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Generador de reportes Excel con Apache POI
 */
public class ExcelUtil {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Reporte de Solicitudes ─────────────────────────────────────────────

    public static byte[] reporteSolicitudes(List<SolicitudPrestamo> lista) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Solicitudes");

            // Estilos
            CellStyle estiloTitulo = crearEstiloTitulo(wb);
            CellStyle estiloHeader = crearEstiloHeader(wb);
            CellStyle estiloMonto  = crearEstiloMonto(wb);
            CellStyle estiloCelda  = wb.createCellStyle();
            estiloCelda.setBorderBottom(BorderStyle.THIN);
            estiloCelda.setBorderLeft(BorderStyle.THIN);
            estiloCelda.setBorderRight(BorderStyle.THIN);

            // Título
            Row rowTitulo = sheet.createRow(0);
            Cell cellTitulo = rowTitulo.createCell(0);
            cellTitulo.setCellValue("REPORTE DE SOLICITUDES DE PRÉSTAMO - AHORROPERU");
            cellTitulo.setCellStyle(estiloTitulo);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            // Encabezados
            String[] headers = {"ID", "Asociado", "DNI", "Tipo", "Monto (S/.)", "Plazo", "Estado", "Fecha"};
            Row rowHeader = sheet.createRow(2);
            for (int i = 0; i < headers.length; i++) {
                Cell c = rowHeader.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(estiloHeader);
            }

            // Datos
            int fila = 3;
            for (SolicitudPrestamo s : lista) {
                Row row = sheet.createRow(fila++);
                crearCelda(row, 0, String.valueOf(s.getIdSolicitud()), estiloCelda);
                crearCelda(row, 1, s.getNombreAsociado(), estiloCelda);
                crearCelda(row, 2, s.getDniAsociado(), estiloCelda);
                crearCelda(row, 3, s.getNombreTipo(), estiloCelda);
                Cell cMonto = row.createCell(4);
                cMonto.setCellValue(s.getMontoSolicitado().doubleValue());
                cMonto.setCellStyle(estiloMonto);
                crearCelda(row, 5, s.getPlazoMeses() + " meses", estiloCelda);
                crearCelda(row, 6, s.getEstado(), estiloCelda);
                crearCelda(row, 7,
                    s.getFechaSolicitud() != null ? s.getFechaSolicitud().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "",
                    estiloCelda);
            }

            // Ancho automático
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        }
    }

    // ── Reporte de Cuotas ──────────────────────────────────────────────────

    public static byte[] reporteCuotas(List<Cuota> lista, int idPrestamo) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Cuotas Préstamo " + idPrestamo);

            CellStyle estiloTitulo = crearEstiloTitulo(wb);
            CellStyle estiloHeader = crearEstiloHeader(wb);
            CellStyle estiloMonto  = crearEstiloMonto(wb);
            CellStyle estiloCelda  = wb.createCellStyle();
            estiloCelda.setBorderBottom(BorderStyle.THIN);
            estiloCelda.setBorderLeft(BorderStyle.THIN);
            estiloCelda.setBorderRight(BorderStyle.THIN);

            Row rowTitulo = sheet.createRow(0);
            Cell cellTitulo = rowTitulo.createCell(0);
            cellTitulo.setCellValue("CRONOGRAMA DE CUOTAS - PRÉSTAMO #" + idPrestamo + " - AHORROPERU");
            cellTitulo.setCellStyle(estiloTitulo);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

            String[] headers = {"N°", "Vencimiento", "Capital (S/.)", "Interés (S/.)", "Total (S/.)", "Estado", "Fecha Pago"};
            Row rowHeader = sheet.createRow(2);
            for (int i = 0; i < headers.length; i++) {
                Cell c = rowHeader.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(estiloHeader);
            }

            int fila = 3;
            double totalCapital = 0, totalInteres = 0, totalGeneral = 0;
            for (Cuota c : lista) {
                Row row = sheet.createRow(fila++);
                crearCelda(row, 0, String.valueOf(c.getNumeroCuota()), estiloCelda);
                crearCelda(row, 1, c.getFechaVencimiento().format(FMT), estiloCelda);
                Cell cCap = row.createCell(2); cCap.setCellValue(c.getMontoCapital().doubleValue()); cCap.setCellStyle(estiloMonto);
                Cell cInt = row.createCell(3); cInt.setCellValue(c.getMontoInteres().doubleValue()); cInt.setCellStyle(estiloMonto);
                Cell cTot = row.createCell(4); cTot.setCellValue(c.getMontoTotal().doubleValue());   cTot.setCellStyle(estiloMonto);
                crearCelda(row, 5, c.getEstado(), estiloCelda);
                crearCelda(row, 6, c.getFechaPago() != null ? c.getFechaPago().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-", estiloCelda);
                totalCapital += c.getMontoCapital().doubleValue();
                totalInteres += c.getMontoInteres().doubleValue();
                totalGeneral += c.getMontoTotal().doubleValue();
            }

            // Fila totales
            CellStyle estiloTotal = crearEstiloHeader(wb);
            Row rowTotal = sheet.createRow(fila);
            crearCelda(rowTotal, 0, "TOTAL", estiloTotal);
            crearCelda(rowTotal, 1, "", estiloTotal);
            Cell t2 = rowTotal.createCell(2); t2.setCellValue(totalCapital); t2.setCellStyle(estiloMonto);
            Cell t3 = rowTotal.createCell(3); t3.setCellValue(totalInteres); t3.setCellStyle(estiloMonto);
            Cell t4 = rowTotal.createCell(4); t4.setCellValue(totalGeneral); t4.setCellStyle(estiloMonto);

            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return out.toByteArray();
        }
    }

    // ── Estilos ────────────────────────────────────────────────────────────

    private static CellStyle crearEstiloTitulo(XSSFWorkbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        f.setFontHeightInPoints((short) 13);
        f.setColor(IndexedColors.WHITE.getIndex());
        s.setFont(f);
        s.setFillForegroundColor(new XSSFColor(new byte[]{(byte)0x1A,(byte)0x53,(byte)0x9E}, null));
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setAlignment(HorizontalAlignment.CENTER);
        return s;
    }

    private static CellStyle crearEstiloHeader(XSSFWorkbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        f.setColor(IndexedColors.WHITE.getIndex());
        s.setFont(f);
        s.setFillForegroundColor(new XSSFColor(new byte[]{(byte)0x2E,(byte)0x75,(byte)0xB6}, null));
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);
        return s;
    }

    private static CellStyle crearEstiloMonto(XSSFWorkbook wb) {
        CellStyle s = wb.createCellStyle();
        DataFormat df = wb.createDataFormat();
        s.setDataFormat(df.getFormat("#,##0.00"));
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);
        return s;
    }

    private static void crearCelda(Row row, int col, String valor, CellStyle estilo) {
        Cell c = row.createCell(col);
        c.setCellValue(valor != null ? valor : "");
        c.setCellStyle(estilo);
    }
}
