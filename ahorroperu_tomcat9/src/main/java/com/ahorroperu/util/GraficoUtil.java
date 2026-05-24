package com.ahorroperu.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Generador de gráficos con JFreeChart
 */
public class GraficoUtil {

    /**
     * Gráfico de torta: solicitudes por estado
     * datos: [[estado, cantidad], ...]
     */
    public static byte[] graficoPorEstado(List<Object[]> datos) throws IOException {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Object[] fila : datos) {
            dataset.setValue((String) fila[0], (Number) fila[1]);
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Solicitudes por Estado",
            dataset,
            true,   // leyenda
            true,
            false
        );

        PiePlot<?> plot = (PiePlot<?>) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setSectionPaint("PENDIENTE",  new Color(0xFF, 0xC1, 0x07));
        plot.setSectionPaint("APROBADO",   new Color(0x28, 0xA7, 0x45));
        plot.setSectionPaint("RECHAZADO",  new Color(0xDC, 0x35, 0x45));
        chart.setBackgroundPaint(Color.WHITE);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, 500, 350);
        return out.toByteArray();
    }

    /**
     * Gráfico de barras: montos aprobados por tipo
     * datos: [[tipo, monto], ...]
     */
    public static byte[] graficoMontosPorTipo(List<Object[]> datos) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Object[] fila : datos) {
            String tipo  = (String) fila[0];
            Number monto = (fila[1] instanceof BigDecimal) ? (BigDecimal) fila[1] : (Number) fila[1];
            dataset.addValue(monto, "Monto (S/.)", tipo);
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "Montos Aprobados por Tipo de Préstamo",
            "Tipo",
            "Monto (S/.)",
            dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0x1A, 0x53, 0x9E));
        renderer.setDrawBarOutline(false);

        chart.setBackgroundPaint(Color.WHITE);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, 600, 350);
        return out.toByteArray();
    }
}
