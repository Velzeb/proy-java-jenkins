package Reportes;

import Modelo.Conexion;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

public class Grafico {
    /**
     * Genera un gráfico circular de ventas para una fecha específica
     * @param fecha La fecha para la cual generar el reporte de ventas
     */
    public static void Graficar(String fecha){
        Connection con;
        Conexion cn = new Conexion();
        PreparedStatement ps;
        ResultSet rs;
        try {
            String sql = "SELECT total FROM ventas WHERE fecha = ?";
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fecha);
            rs = ps.executeQuery();
            DefaultPieDataset dateset = new DefaultPieDataset();
            while(rs.next()){
                dateset.setValue(rs.getString("total"), rs.getDouble("total"));
            }
            
            // Crear gráfico mejorado con efecto 3D
            JFreeChart jf = ChartFactory.createPieChart3D(
                "Reporte de Venta", 
                dateset, 
                true,    // Incluir leyenda
                true,    // Mostrar tooltips
                false    // No incluir URLs
            );
            
            // Personalizar el gráfico
            PiePlot3D plot = (PiePlot3D) jf.getPlot();
            plot.setStartAngle(290);
            plot.setDirection(Rotation.CLOCKWISE);
            plot.setForegroundAlpha(0.8f);
            plot.setNoDataMessage("No hay datos disponibles para la fecha seleccionada");
            plot.setBackgroundPaint(new Color(242, 242, 242));
            
            // Añadir subtítulo con la fecha
            jf.addSubtitle(new TextTitle("Fecha: " + fecha, new Font("Arial", Font.ITALIC, 14)));
            
            // Configurar el marco
            ChartFrame f = new ChartFrame("Total de Ventas por día", jf);
            f.setSize(1000, 600);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        } catch (SQLException e) {
            System.out.println("Error al generar gráfico: " + e.toString());
        }
    }
    
    /**
     * Genera un gráfico de barras para ventas por producto
     * @param fecha La fecha para la cual generar el reporte de ventas por producto
     */
    public static void GraficarBarras(String fecha) {
        Connection con;
        Conexion cn = new Conexion();
        PreparedStatement ps;
        ResultSet rs;
        try {
            String sql = "SELECT p.nombre, d.cantidad FROM detalle d " +
                         "INNER JOIN productos p ON d.cod_pro = p.codigo " +
                         "INNER JOIN ventas v ON d.id_venta = v.id " +
                         "WHERE v.fecha = ? LIMIT 10";
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fecha);
            rs = ps.executeQuery();
            
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            
            while(rs.next()) {
                dataset.setValue(
                    rs.getInt("cantidad"),
                    "Ventas",
                    rs.getString("nombre")
                );
            }
            
            // Crear gráfico de barras
            JFreeChart chart = ChartFactory.createBarChart3D(
                "Productos Más Vendidos", // Título del gráfico
                "Productos",              // Etiqueta para el eje X
                "Cantidad",               // Etiqueta para el eje Y
                dataset,                  // Dataset
                PlotOrientation.VERTICAL, // Orientación
                true,                     // Incluir leyenda
                true,                     // Tooltips
                false                     // URLs
            );
            
            // Personalizar el gráfico
            chart.setBackgroundPaint(Color.WHITE);
            chart.getTitle().setFont(new Font("Arial", Font.BOLD, 18));
            chart.addSubtitle(new TextTitle("Fecha: " + fecha, new Font("Arial", Font.ITALIC, 14)));
            
            // Personalizar el plot
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(new Color(242, 242, 242));
            plot.setDomainGridlinePaint(Color.GRAY);
            plot.setRangeGridlinePaint(Color.GRAY);
            
            // Personalizar las barras
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new Color(0, 129, 198));
            
            // Mostrar gráfico
            ChartFrame frame = new ChartFrame("Productos más vendidos por día", chart);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
        } catch (SQLException e) {
            System.out.println("Error al generar gráfico de barras: " + e.toString());
        }
    }
}
