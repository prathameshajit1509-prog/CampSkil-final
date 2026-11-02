package com.campusconnect.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.campusconnect.model.OnCampusDrive;
import org.springframework.stereotype.Service;

import java.io.OutputStream;

@Service
public class PdfGeneratorService {

    public void generateDriveReport(OnCampusDrive drive, OutputStream outputStream) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Campus Connect - Placement Drive Report", titleFont));
            document.add(new Paragraph("----------------------------------------------------------------", bodyFont));
            document.add(new Paragraph("Company Name: " + drive.getCompanyName(), bodyFont));
            document.add(new Paragraph("Job Profile: " + drive.getJobProfile(), bodyFont));
            document.add(new Paragraph("Eligibility Criteria: " + drive.getEligibilityCriteria(), bodyFont));
            document.add(new Paragraph("Interview Date: " + (drive.getInterviewDate() != null ? drive.getInterviewDate().toString() : "TBA"), bodyFont));

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}