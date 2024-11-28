package com.zpi.amoz.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PDFService {
    public byte[] generatePDFFromHTML(String html) {
        try (ByteArrayOutputStream pdfStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            var baseUrl = getClass()
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toString();
            builder.withHtmlContent(html, baseUrl);
            builder.toStream(pdfStream);
            builder.run();

            return pdfStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}