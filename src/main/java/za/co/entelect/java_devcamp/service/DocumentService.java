package za.co.entelect.java_devcamp.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.entity.ContractDocument;
import za.co.entelect.java_devcamp.entity.Profile;
import za.co.entelect.java_devcamp.model.Notification;
import za.co.entelect.java_devcamp.repository.DocumentRepository;
import za.co.entelect.java_devcamp.serviceinterface.IDocumentService;
import za.co.entelect.java_devcamp.serviceinterface.INotificationService;
import za.co.entelect.java_devcamp.serviceinterface.IProfileService;
import za.co.entelect.java_devcamp.util.NotificationContent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@Service
public class DocumentService implements IDocumentService {

    private final IProfileService iProfileService;
    private final INotificationService iNotificationService;
    private final DocumentRepository documentRepository;

    @Override
    public String generateCustomerContract(List<ProductDto> orderItemDtoList, String customerEmail) {
        Profile customerProfile = iProfileService.getProfileByUserName(customerEmail);
        String documentUrl = " ";
        for (ProductDto order : orderItemDtoList) {
            //generate document
            try {
                String fileName = customerProfile.getFirstName() + "_" + customerProfile.getLastName() + "_" + order.Name() + UUID.randomUUID() + "_Contract.pdf";
                documentUrl = fileName;
                Font font = FontFactory.getFont(FontFactory.TIMES, 16, BaseColor.BLACK);
                Font termsFont = FontFactory.getFont(FontFactory.TIMES_ITALIC, 7, BaseColor.BLACK);
                Document document = new Document();

                PdfWriter writer = PdfWriter.getInstance(
                        document,
                        new FileOutputStream(fileName));

                document.open();

// Date
                String today = LocalDate.now().toString();
                Paragraph date = new Paragraph("Date: " + today);
                date.setSpacingAfter(10);
                document.add(date);

// Greeting
                Paragraph greeting = new Paragraph("Dear " +
                        customerProfile.getFirstName() + " " +
                        customerProfile.getLastName() + ",");
                greeting.setSpacingAfter(10);
                document.add(greeting);

// Body
                Paragraph body = new Paragraph(
                        "We are pleased to inform you that your order for the product \""
                                + order.Name() + "\" has been successfully processed. This document serves as a formal agreement outlining the terms and conditions associated with your purchase. Please review the contents carefully and retain this contract for your records."
                );
                body.setAlignment(Element.ALIGN_LEFT);
                body.setSpacingAfter(20);
                document.add(body);

                PdfPTable table = new PdfPTable(3);
                addTableHeader(table);
                addRows(table, order.Name(), order.Description(), order.Price().toString(), order.imageUrl());

                document.add(table);


// Signature
                Paragraph signatureLine = new Paragraph();
                signatureLine.add("Signature: ");
                signatureLine.add(new Chunk(new DottedLineSeparator()));
                signatureLine.setSpacingBefore(20);
                document.add(signatureLine);

// Footer (BOTTOM)
                String terms = "Terms and Conditions: This document is confidential and intended solely for the recipient...";

                PdfContentByte canvas = writer.getDirectContent();
                ColumnText.showTextAligned(
                        canvas,
                        Element.ALIGN_CENTER,
                        new Phrase(terms, termsFont),
                        297.5f,
                        20,
                        0
                );

                document.close();

               ContractDocument generatedDocument = new ContractDocument();
                generatedDocument.setFileName(fileName);
                generatedDocument.setFilePath(documentUrl);
                generatedDocument.setCreatedAt(LocalDateTime.now());

                documentRepository.save(generatedDocument);

            } catch (DocumentException e) {
                throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            String subject = "Contract for product " + order.Name();
            String emailContent = NotificationContent.Contract_document_available + "\n" + "Here is the link to your contract: " + documentUrl;
            Notification notification = new Notification(customerEmail, subject,emailContent );
            iNotificationService.sendNotification(notification);
        }

        return documentUrl;
    }

    private void addTableHeader(PdfPTable table) {
        PdfPCell header = new PdfPCell(new Phrase("Product Details"));
        header.setColspan(2); // spans across both columns
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setBorderWidth(2);

        table.addCell(header);
    }

    private void addRows(PdfPTable table, String name, String description, String price, String url) {
        table.addCell("Product name");
        table.addCell(name);

        table.addCell("Product description");
        table.addCell(description);

        table.addCell("Price");
        table.addCell(price);

        table.addCell("Image");
        table.addCell(url);
    }


}
