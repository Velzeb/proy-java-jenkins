package Modelo;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.filechooser.FileSystemView;

public class VentaDao {
    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;
    int r;
    
    public int IdVenta(){
        int id = 0;
        String sql = "SELECT MAX(id) FROM ventas";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return id;
    }
    
    public int RegistrarVenta(Venta v){
        String sql = "INSERT INTO ventas (cliente, vendedor, total, fecha) VALUES (?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, v.getCliente());
            ps.setString(2, v.getVendedor());
            ps.setDouble(3, v.getTotal());
            ps.setString(4, v.getFecha());
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return r;
    }
    
    public int RegistrarDetalle(Detalle Dv){
       String sql = "INSERT INTO detalle (id_pro, cantidad, precio, id_venta) VALUES (?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, Dv.getId_pro());
            ps.setInt(2, Dv.getCantidad());
            ps.setDouble(3, Dv.getPrecio());
            ps.setInt(4, Dv.getId());
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return r;
    }
    
    public boolean ActualizarStock(int cant, int id){
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1,cant);
            ps.setInt(2, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    public List Listarventas(){
       List<Venta> ListaVenta = new ArrayList();
       String sql = "SELECT c.id AS id_cli, c.nombre, v.* FROM clientes c INNER JOIN ventas v ON c.id = v.cliente";
       try {
           con = cn.getConnection();
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery();
           while (rs.next()) {               
               Venta vent = new Venta();
               vent.setId(rs.getInt("id"));
               vent.setNombre_cli(rs.getString("nombre"));
               vent.setVendedor(rs.getString("vendedor"));
               vent.setTotal(rs.getDouble("total"));
               ListaVenta.add(vent);
           }
       } catch (SQLException e) {
           System.out.println(e.toString());
       }
       return ListaVenta;
   }
    public Venta BuscarVenta(int id){
        Venta cl = new Venta();
        String sql = "SELECT * FROM ventas WHERE id = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                cl.setId(rs.getInt("id"));
                cl.setCliente(rs.getInt("cliente"));
                cl.setTotal(rs.getDouble("total"));
                cl.setVendedor(rs.getString("vendedor"));
                cl.setFecha(rs.getString("fecha"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return cl;
    }
    public void pdfV(int idventa, int Cliente, double total, String usuario) {
        try {
            Date date = new Date();
            FileOutputStream archivo;
            String url = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
            File salida = new File(url + File.separator + "venta_" + idventa + ".pdf");
            archivo = new FileOutputStream(salida);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            
            // Colores personalizados
            BaseColor colorPrimario = new BaseColor(0, 75, 155); // Azul corporativo
            BaseColor colorSecundario = new BaseColor(240, 240, 240); // Gris claro para fondos
            BaseColor colorAcento = new BaseColor(255, 140, 0); // Naranja para acentos
            
            // Fuentes personalizadas
            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, colorPrimario);
            Font fuenteSubtitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, colorPrimario);
            Font fuenteNegrita = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font fuenteNormal = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
            Font fuenteResaltado = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, colorAcento);
            
            // Logo
            Image img = Image.getInstance(getClass().getResource("/Img/logo_pdf.png"));
            img.scalePercent(75); // Escalar el logo al 75%
            
            // Encabezado con logo y datos de la empresa
            PdfPTable encabezado = new PdfPTable(3);
            encabezado.setWidthPercentage(100);
            encabezado.setWidths(new float[]{30f, 40f, 30f});
            encabezado.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            
            // Celda para el logo
            PdfPCell celdaLogo = new PdfPCell();
            celdaLogo.addElement(img);
            celdaLogo.setBorder(Rectangle.NO_BORDER);
            celdaLogo.setPaddingBottom(10);
            encabezado.addCell(celdaLogo);
            
            // Celda para el título
            PdfPCell celdaTitulo = new PdfPCell();
            Paragraph titulo = new Paragraph("COMPROBANTE DE VENTA", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            Paragraph subTitulo = new Paragraph("Folio: " + idventa, fuenteSubtitulo);
            subTitulo.setAlignment(Element.ALIGN_CENTER);
            celdaTitulo.addElement(titulo);
            celdaTitulo.addElement(subTitulo);
            celdaTitulo.setBorder(Rectangle.NO_BORDER);
            celdaTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            encabezado.addCell(celdaTitulo);
            
            // Celda para la fecha
            PdfPCell celdaFecha = new PdfPCell();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Paragraph fecha = new Paragraph("Fecha: " + sdf.format(date) + "\nVendedor: " + usuario, fuenteNormal);
            fecha.setAlignment(Element.ALIGN_RIGHT);
            celdaFecha.addElement(fecha);
            celdaFecha.setBorder(Rectangle.NO_BORDER);
            celdaFecha.setVerticalAlignment(Element.ALIGN_MIDDLE);
            encabezado.addCell(celdaFecha);
            
            doc.add(encabezado);
            
            // Separador
            Paragraph separador = new Paragraph(" ");
            separador.add(Chunk.NEWLINE);
            doc.add(separador);
            
            // Mensaje de la empresa (recuperado de la BD)
            String mensaje = "";
            String nombreEmpresa = "Empresa";
            try {
                con = cn.getConnection();
                ps = con.prepareStatement("SELECT * FROM config");
                rs = ps.executeQuery();
                if (rs.next()) {
                    mensaje = rs.getString("mensaje");
                    try {
                        nombreEmpresa = rs.getString("nombre");
                    } catch (SQLException ex) {
                        nombreEmpresa = "Empresa";
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
            
            // Banner con datos de la empresa
            PdfPTable banner = new PdfPTable(1);
            banner.setWidthPercentage(100);
            
            PdfPCell celdaBanner = new PdfPCell();
            celdaBanner.setBackgroundColor(colorPrimario);
            Paragraph nombreEmpresaParagraph = new Paragraph(nombreEmpresa, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE));
            nombreEmpresaParagraph.setAlignment(Element.ALIGN_CENTER);
            celdaBanner.addElement(nombreEmpresaParagraph);
            celdaBanner.setPadding(8f);
            banner.addCell(celdaBanner);
            
            doc.add(banner);
            
            // Datos del cliente
            Paragraph tituloCliente = new Paragraph("DATOS DEL CLIENTE", fuenteSubtitulo);
            tituloCliente.setSpacingBefore(10);
            tituloCliente.setSpacingAfter(5);
            doc.add(tituloCliente);
            
            PdfPTable tablaCliente = new PdfPTable(3);
            tablaCliente.setWidthPercentage(100);
            tablaCliente.setWidths(new float[]{40f, 30f, 30f});
            
            // Encabezados de tabla cliente
            PdfPCell headerNombre = new PdfPCell(new Phrase("Nombre", fuenteNegrita));
            PdfPCell headerTelefono = new PdfPCell(new Phrase("Teléfono", fuenteNegrita));
            PdfPCell headerDireccion = new PdfPCell(new Phrase("Dirección", fuenteNegrita));
            
            // Estilo para los encabezados
            headerNombre.setBackgroundColor(colorSecundario);
            headerTelefono.setBackgroundColor(colorSecundario);
            headerDireccion.setBackgroundColor(colorSecundario);
            
            headerNombre.setPadding(5);
            headerTelefono.setPadding(5);
            headerDireccion.setPadding(5);
            
            tablaCliente.addCell(headerNombre);
            tablaCliente.addCell(headerTelefono);
            tablaCliente.addCell(headerDireccion);
            
            // Consulta de datos del cliente
            try {
                con = cn.getConnection();
                ps = con.prepareStatement("SELECT * FROM clientes WHERE id = ?");
                ps.setInt(1, Cliente);
                rs = ps.executeQuery();
                if (rs.next()) {
                    PdfPCell celdaNombre = new PdfPCell();
                    PdfPCell celdaTelefono = new PdfPCell();
                    PdfPCell celdaDireccion = new PdfPCell();
                    
                    try {
                        celdaNombre.addElement(new Paragraph(rs.getString("nombre"), fuenteNormal));
                        celdaTelefono.addElement(new Paragraph(rs.getString("telefono"), fuenteNormal));
                    } catch (SQLException ex) {
                        celdaNombre.addElement(new Paragraph("Cliente #" + rs.getInt("id"), fuenteNormal));
                        celdaTelefono.addElement(new Paragraph("No disponible", fuenteNormal));
                    }
                    celdaDireccion.addElement(new Paragraph(rs.getString("direccion"), fuenteNormal));
                    
                    celdaNombre.setPadding(5);
                    celdaTelefono.setPadding(5);
                    celdaDireccion.setPadding(5);
                    
                    tablaCliente.addCell(celdaNombre);
                    tablaCliente.addCell(celdaTelefono);
                    tablaCliente.addCell(celdaDireccion);
                } else {
                    PdfPCell celdaNoCliente = new PdfPCell(new Phrase("Cliente no encontrado", fuenteNormal));
                    celdaNoCliente.setColspan(3);
                    celdaNoCliente.setPadding(5);
                    tablaCliente.addCell(celdaNoCliente);
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
            
            doc.add(tablaCliente);
            
            // Título productos
            Paragraph tituloProductos = new Paragraph("DETALLE DE PRODUCTOS", fuenteSubtitulo);
            tituloProductos.setSpacingBefore(15);
            tituloProductos.setSpacingAfter(5);
            doc.add(tituloProductos);
            
            // Tabla de productos
            PdfPTable tablaProductos = new PdfPTable(4);
            tablaProductos.setWidthPercentage(100);
            tablaProductos.setWidths(new float[]{10f, 50f, 20f, 20f});
            
            // Encabezados de tabla productos
            PdfPCell headerCant = new PdfPCell(new Phrase("Cant.", fuenteNegrita));
            PdfPCell headerDesc = new PdfPCell(new Phrase("Descripción", fuenteNegrita));
            PdfPCell headerPUnit = new PdfPCell(new Phrase("P. Unitario", fuenteNegrita));
            PdfPCell headerPTotal = new PdfPCell(new Phrase("P. Total", fuenteNegrita));
            
            headerCant.setBackgroundColor(colorSecundario);
            headerDesc.setBackgroundColor(colorSecundario);
            headerPUnit.setBackgroundColor(colorSecundario);
            headerPTotal.setBackgroundColor(colorSecundario);
            
            headerCant.setPadding(5);
            headerDesc.setPadding(5);
            headerPUnit.setPadding(5);
            headerPTotal.setPadding(5);
            
            tablaProductos.addCell(headerCant);
            tablaProductos.addCell(headerDesc);
            tablaProductos.addCell(headerPUnit);
            tablaProductos.addCell(headerPTotal);
            
            // Consulta productos
            String consultaProductos = "SELECT d.id, d.id_pro, d.id_venta, d.precio, d.cantidad, p.id, p.nombre FROM detalle d " +
                                      "INNER JOIN productos p ON d.id_pro = p.id WHERE d.id_venta = ?";
            try {
                ps = con.prepareStatement(consultaProductos);
                ps.setInt(1, idventa);
                rs = ps.executeQuery();
                boolean tieneProductos = false;
                
                while (rs.next()) {
                    tieneProductos = true;
                    double subTotal = rs.getInt("cantidad") * rs.getDouble("precio");
                    
                    PdfPCell celdaCantidad = new PdfPCell(new Phrase(rs.getString("cantidad"), fuenteNormal));
                    PdfPCell celdaDescripcion = new PdfPCell();
                    
                    try {
                        celdaDescripcion.addElement(new Paragraph(rs.getString("nombre"), fuenteNormal));
                    } catch (SQLException ex) {
                        celdaDescripcion.addElement(new Paragraph("Producto #" + rs.getString("id_pro"), fuenteNormal));
                    }
                    
                    PdfPCell celdaPrecioUnit = new PdfPCell(new Phrase("$" + String.format("%.2f", rs.getDouble("precio")), fuenteNormal));
                    PdfPCell celdaPrecioTotal = new PdfPCell(new Phrase("$" + String.format("%.2f", subTotal), fuenteNormal));
                    
                    celdaCantidad.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celdaPrecioUnit.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    celdaPrecioTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    
                    celdaCantidad.setPadding(5);
                    celdaDescripcion.setPadding(5);
                    celdaPrecioUnit.setPadding(5);
                    celdaPrecioTotal.setPadding(5);
                    
                    tablaProductos.addCell(celdaCantidad);
                    tablaProductos.addCell(celdaDescripcion);
                    tablaProductos.addCell(celdaPrecioUnit);
                    tablaProductos.addCell(celdaPrecioTotal);
                }
                
                if (!tieneProductos) {
                    PdfPCell celdaNoProductos = new PdfPCell(new Phrase("No se encontraron productos", fuenteNormal));
                    celdaNoProductos.setColspan(4);
                    celdaNoProductos.setPadding(5);
                    celdaNoProductos.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tablaProductos.addCell(celdaNoProductos);
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
            
            doc.add(tablaProductos);
            
            // Tabla de totales
            PdfPTable tablaTotales = new PdfPTable(2);
            tablaTotales.setWidthPercentage(40);
            tablaTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaTotales.setWidths(new float[]{50f, 50f});
            tablaTotales.setSpacingBefore(10);
            
            PdfPCell celdaTextoTotal = new PdfPCell(new Phrase("TOTAL:", fuenteResaltado));
            PdfPCell celdaValorTotal = new PdfPCell(new Phrase("$" + String.format("%.2f", total), fuenteResaltado));
            
            celdaTextoTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaValorTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            
            celdaTextoTotal.setPadding(5);
            celdaValorTotal.setPadding(5);
            
            celdaTextoTotal.setBackgroundColor(colorSecundario);
            celdaValorTotal.setBackgroundColor(colorSecundario);
            
            tablaTotales.addCell(celdaTextoTotal);
            tablaTotales.addCell(celdaValorTotal);
            
            doc.add(tablaTotales);
            
            // Sección de firma
            Paragraph seccionFirma = new Paragraph();
            seccionFirma.add(Chunk.NEWLINE);
            seccionFirma.add(Chunk.NEWLINE);
            seccionFirma.add(new Phrase("CANCELACIÓN", fuenteNegrita));
            seccionFirma.add(Chunk.NEWLINE);
            seccionFirma.add(Chunk.NEWLINE);
            seccionFirma.add("____________________________");
            seccionFirma.add(Chunk.NEWLINE);
            seccionFirma.add(new Phrase("Firma", fuenteNormal));
            seccionFirma.setAlignment(Element.ALIGN_CENTER);
            doc.add(seccionFirma);
            
            // Pie de página con mensaje
            if (!mensaje.isEmpty()) {
                Paragraph piePagina = new Paragraph();
                piePagina.add(Chunk.NEWLINE);
                piePagina.add(Chunk.NEWLINE);
                piePagina.add(new Phrase(mensaje, fuenteResaltado));
                piePagina.setAlignment(Element.ALIGN_CENTER);
                doc.add(piePagina);
            }
            
            doc.close();
            archivo.close();
            Desktop.getDesktop().open(salida);
        } catch (DocumentException | IOException e) {
            System.out.println(e.toString());
        }
    }
}
