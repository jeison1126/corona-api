package corona.financiero.nmda.admision.service;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Slf4j
public class DocumentoService {

    public String hojaResumenBase64() throws IOException {
        byte[] bytes = generaResumenPDF();
        String documentoBase64 = Base64.getEncoder().encodeToString(bytes);
        log.debug("Hoja resumen Base 64: {}", documentoBase64);

        return documentoBase64;
    }

    public String contratoBase64() throws IOException {
        byte[] bytes = generaContratoPDF();
        String documentoBase64 = Base64.getEncoder().encodeToString(bytes);
        log.debug("Contrato Base 64: {}", documentoBase64);

        return documentoBase64;
    }

    public String anexoBase64(String plantilla) throws IOException {
        byte[] bytes = generaAnexoPDF(plantilla);
        String documentoBase64 = Base64.getEncoder().encodeToString(bytes);
        log.debug("Anexo Base 64: {}", documentoBase64);

        return documentoBase64;
    }

    public ByteArrayInputStream exportarHojaResumen() throws IOException {

        byte[] bytes = generaResumenPDF();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        return byteArrayInputStream;
    }

    private byte[] generaResumenPDF() throws IOException {
        StringBuilder html = new StringBuilder();

        ClassPathResource classPathResource = new ClassPathResource("plantilla_resumen.html");
        File file = classPathResource.getFile();

        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                if (linea.contains("HR_TA_NOMBRE_CLIENTE")) {
                    linea = linea.replace("HR_TA_NOMBRE_CLIENTE", "Nicolas Aranda");
                }
                html.append(linea);
            }


            String result = html.toString();
            InputStream inputStream = IOUtils.toInputStream(result, StandardCharsets.UTF_8);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfWriter pdfWriter = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            Header headerHandler = new Header("");
            Footer footerHandler = new Footer();

            pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler);
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);

            HtmlConverter.convertToDocument(inputStream, pdfDocument, null);

            // Write the total number of pages to the placeholder
            footerHandler.writeTotal(pdfDocument);
            pdfDocument.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return new byte[0];
    }

    private byte[] generaContratoPDF() throws IOException {
        StringBuilder html = new StringBuilder();

        ClassPathResource classPathResource = new ClassPathResource("plantilla_contrato.html");
        File file = classPathResource.getFile();

        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                html.append(linea);
            }


            String result = html.toString();
            InputStream inputStream = IOUtils.toInputStream(result, StandardCharsets.UTF_8);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfWriter pdfWriter = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            Header headerHandler = new Header("");
            Footer footerHandler = new Footer();

            pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler);
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);

            HtmlConverter.convertToDocument(inputStream, pdfDocument, null);

            // Write the total number of pages to the placeholder
            footerHandler.writeTotal(pdfDocument);
            pdfDocument.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return new byte[0];
    }

    public ByteArrayInputStream exportarContrato() throws IOException {
        byte[] bytes = generaContratoPDF();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        return byteArrayInputStream;
    }

    public ByteArrayInputStream exportarAnexo(String plantilla) throws IOException {
        byte[] bytes = generaAnexoPDF(plantilla);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        return byteArrayInputStream;
    }

    private byte[] generaAnexoPDF(String plantilla) throws IOException {
        StringBuilder html = new StringBuilder();

        ClassPathResource classPathResource = new ClassPathResource(plantilla);
        File file = classPathResource.getFile();

        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                html.append(linea);
            }


            String result = html.toString();
            InputStream inputStream = IOUtils.toInputStream(result, StandardCharsets.UTF_8);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfWriter pdfWriter = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            Header headerHandler = new Header("");
            Footer footerHandler = new Footer();

            pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler);
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);

            HtmlConverter.convertToDocument(inputStream, pdfDocument, null);

            // Write the total number of pages to the placeholder
            footerHandler.writeTotal(pdfDocument);
            pdfDocument.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return new byte[0];
    }

    protected class Header implements IEventHandler {
        private String header;

        public Header(String header) {
            this.header = header;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();

            PdfPage page = docEvent.getPage();
            Rectangle pageSize = page.getPageSize();

            Canvas canvas = new Canvas(new PdfCanvas(page), pageSize);
            canvas.setFontSize(10);

            // Write text at position
            canvas.showTextAligned(header,
                    pageSize.getWidth() / 2,
                    pageSize.getTop() - 30, TextAlignment.CENTER);
            canvas.close();
        }
    }

    protected class Footer implements IEventHandler {
        protected PdfFormXObject placeholder;
        protected float side = 20;
        protected float x = 300;
        protected float y = 25;
        protected float space = 4.5f;
        protected float descent = 3;

        public Footer() {
            placeholder = new PdfFormXObject(new Rectangle(0, 0, side, side));
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            int pageNumber = pdf.getPageNumber(page);
            Rectangle pageSize = page.getPageSize();

            // Creates drawing canvas
            PdfCanvas pdfCanvas = new PdfCanvas(page);
            Canvas canvas = new Canvas(pdfCanvas, pageSize);

            Paragraph p = new Paragraph()
                    .add("Pagina ")
                    .add(String.valueOf(pageNumber))
                    .add(" de");

            canvas.showTextAligned(p, x, y, TextAlignment.RIGHT);
            canvas.close();

            pdfCanvas.addXObjectAt(placeholder, x + space, y - descent);
            pdfCanvas.release();
        }

        public void writeTotal(PdfDocument pdf) {
            Canvas canvas = new Canvas(placeholder, pdf);
            canvas.showTextAligned(String.valueOf(pdf.getNumberOfPages()),
                    0, descent, TextAlignment.LEFT);
            canvas.close();
        }
    }
}
