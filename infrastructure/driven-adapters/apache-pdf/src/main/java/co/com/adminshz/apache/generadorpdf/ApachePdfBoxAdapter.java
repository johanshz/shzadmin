package co.com.adminshz.apache.generadorpdf;

import co.com.adminshz.model.factura.Factura;
import co.com.adminshz.model.generadorpdf.PdfGenerador;
import co.com.adminshz.model.product.Product;
import lombok.extern.java.Log;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;


@Repository
@Log
public class ApachePdfBoxAdapter  implements PdfGenerador{
    @Override
    public Mono<Integer> generarPdf(Factura factura, String filePath) {
        try {
            String separador = "-----------------------------------------";
            String titulo = factura.getIsCompraRealizada() ? "FACTURA" : "COTIZACION";
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            float y = 700;
            contentStream.beginText();
            contentStream.newLineAtOffset(100, y);
            contentStream.showText(separador);
            contentStream.newLineAtOffset(0, -15);
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.showText("                   "+ titulo +"          ");
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText(separador);
            contentStream.newLineAtOffset(0, -15);
            crearInforCliente(factura,contentStream);
            contentStream.showText(separador);
            contentStream.newLineAtOffset(0, -15);
            contentStream.newLine();
            contentStream.setFont(PDType1Font.HELVETICA, 8);
            contentStream.showText("CANT          DESCRIPCION          TOTAL");
            contentStream.newLineAtOffset(0, -15);
            contentStream.newLine();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.showText(separador);
            contentStream.newLine();
            contentStream.newLineAtOffset(0, -15);
            crearProductos(factura,contentStream);
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.showText(separador);
            contentStream.newLine();
            contentStream.newLineAtOffset(0, -15);
            crearInfoPago(factura,contentStream);
            contentStream.endText();
            contentStream.close();
            document.save(filePath);
            document.close();

            return Mono.just(factura.getIdCompra());
        } catch (Exception e) {
           return   Mono.error(new RuntimeException("Error al generar el PDF de la factura", e));
        }

    }
    private void crearInforCliente(Factura  factura,  PDPageContentStream contentStream ) throws IOException{
        String nombre = factura.getNombreCliente().isEmpty() ? "         N/A" : factura.getNombreCliente();
        if(nombre.length() > 13){
            nombre = nombre.substring(0,13);
        }
        String identificacion = factura.getIdentificacion().isEmpty() ? "          N/A" : factura.getIdentificacion();
        contentStream.setFont(PDType1Font.HELVETICA, 8);
        contentStream.showText("CLIENTE:                               " + nombre);
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLine();
        contentStream.showText("NIT/CC:                                  "+identificacion);
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLine();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
    }
    private void crearInfoPago(Factura factura,  PDPageContentStream contentStream ) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, 8);
        contentStream.showText("SUBTOTAL                                   " + formatoNumero(factura.getSubTotal()));
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLine();
        contentStream.showText("DESCUENTO TOTAL                     " + formatoNumero(factura.getDescuento()));
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLine();
        contentStream.showText("TOTAL A PAGAR                          " + formatoNumero(factura.getTotalFactura()));
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLine();
        if(Boolean.TRUE.equals(factura.getIsCompraRealizada())){
            if(factura.getMedioPago().equals("1")){
                contentStream.showText("Efectivo                                         " + formatoNumero(factura.getEfectivo()));
                contentStream.newLineAtOffset(0, -15);
                contentStream.newLine();
            }
            if(factura.getMedioPago().equals("2")){
                contentStream.showText("Pago Electronico                          " + formatoNumero(factura.getOtroMedioPago()));
                contentStream.newLineAtOffset(0, -15);
                contentStream.newLine();
            }
            if(factura.getMedioPago().equals("3")){
                contentStream.showText("Efectivo                                         " + formatoNumero(factura.getEfectivo()));
                contentStream.newLineAtOffset(0, -15);
                contentStream.newLine();
                contentStream.showText("Pago Electronico                          " + formatoNumero(factura.getOtroMedioPago()));
                contentStream.newLineAtOffset(0, -15);
                contentStream.newLine();
            }
            contentStream.showText("Cambio                                             " + formatoNumero(factura.getCambio()));
            contentStream.newLineAtOffset(0, -15);
            contentStream.newLine();
        }
        contentStream.setFont(PDType1Font.HELVETICA, 12);
    }
    private void crearInfoPagoCotizacion(Factura factura,  PDPageContentStream contentStream ) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, 8);
        contentStream.showText("DESCUENTO TOTAL                     " + formatoNumero(factura.getDescuento()));
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLine();
    }
    private void crearProductos(Factura factura,  PDPageContentStream contentStream ) throws IOException {
        List<Product> productos = factura.getProductos();

        for(int i = 0;i < productos.size();i++){
            String textoFactura = getTextoProducto(productos.get(i));
            contentStream.setFont(PDType1Font.HELVETICA, 8);
            contentStream.showText(textoFactura);
            contentStream.newLine();
            contentStream.newLineAtOffset(0, -15);
            if(productos.get(i).getValorDescuento() > 0){
                contentStream.setFont(PDType1Font.HELVETICA, 6);
                contentStream.showText(getTextoDescuento(productos.get(i).getValorDescuento()));
                contentStream.newLine();
                contentStream.newLineAtOffset(0, -15);
            }
        }
    }
    private String getTextoDescuento(Double descuento){
        return "                 Tu ahorro en este producto: "  + formatoNumero(descuento);
    }
    private String getTextoProducto(Product producto){
        return agregarEspacios(producto.getCantidad().toString(),7) + " " + recortarNombre(producto.getNombreProducto()) + " "
                + agregarEspacios(formatoNumero(producto.getPrecioTotal()),12);
    }
    private String formatoNumero(Double numero){
        DecimalFormat formato = new DecimalFormat("#,###");
        return formato.format(numero);
    }
    private String recortarNombre(String nombre){
      return nombre.length() >= 15 ? nombre.substring(0,15) +  "     " : agregarEspacios(nombre,18);
    }
    private String agregarEspacios(String nombre,int cantidadCaracteres){
        int cantidadEspacios = (cantidadCaracteres - nombre.length());
        if(cantidadEspacios == 1){
            cantidadEspacios++;
        }else{
            double cantidadEspaciosExtras = 0;
            for (int i=0;i<cantidadEspacios;i++){
                if(cantidadEspacios > 4) {
                    cantidadEspaciosExtras = cantidadEspaciosExtras + 1;
                }else{
                    cantidadEspaciosExtras = cantidadEspaciosExtras + 0.5;
                }
            }
            cantidadEspacios = cantidadEspacios +  ((int) cantidadEspaciosExtras);
        }
        for(int i = 0;i < cantidadEspacios;i++){
            nombre = nombre.concat(" ");
        }
        return nombre;
    }
}
