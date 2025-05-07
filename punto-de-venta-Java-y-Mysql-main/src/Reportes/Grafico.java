package Reportes;

import Modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Clase para generar gráficos de reportes de ventas.
 */
public class Grafico {
    
    /**
     * Genera y muestra un gráfico de ventas por fecha
     * @param fecha la fecha para la cual generar el reporte
     */
    public static void Graficar(String fecha) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT total FROM ventas WHERE fecha = ?";
            Conexion cn = new Conexion();
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, fecha);
            rs = ps.executeQuery();
            
            DefaultPieDataset dataset = new DefaultPieDataset();
            int index = 1;
            
            while(rs.next()) {
                dataset.setValue("Venta " + index++, rs.getDouble("total"));
            }
            
            final JFreeChart jf = ChartFactory.createPieChart(
                "Reporte de Venta - " + fecha, 
                dataset, 
                true, // mostrar leyenda
                true, // mostrar tooltips
                false // sin URLs
            );
            
            // Mostrar gráfico en el hilo de UI
            SwingUtilities.invokeLater(() -> {
                ChartFrame f = new ChartFrame("Total de Ventas por día: " + fecha, jf);
                f.setSize(1000, 500);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            });
            
        } catch (SQLException e) {
            System.err.println("Error al generar gráfico: " + e.getMessage());
        } finally {
            // Cerrar recursos
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (ps != null) ps.close(); } catch (SQLException e) { }
            try { if (con != null) con.close(); } catch (SQLException e) { }
        }
    }
}
