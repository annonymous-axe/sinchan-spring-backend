package com.sinchan.pdf_generator_service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.sinchan.entities.Invoice;
import com.sinchan.entities.InvoiceItems;
import com.sinchan.entities.User;

import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class PDFGeneratorService {

    private static final PDRectangle A4 = PDRectangle.A4;
    private static final float MARGIN = 20;
    private static final float BORDER_OFFSET = 1;
    private static PDFont hindiFont;

    public byte[] generateInvoice(Invoice invoice, User user) throws IOException {
    	
    	PDDocument document = null;
    	byte[] byteArray = null;
    	List<InvoiceItems> itemList = invoice.getInvoiceItemList();
    	
        try {
        	
        	document = new PDDocument();
        			
            hindiFont = PDType0Font.load(document, getClass().getResourceAsStream("/fonts/Mangal-Regular.ttf"));

                
            	int totalItems = invoice.getInvoiceItemList().size();
                int itemsPerPage = 10;
                int totalPages = (int) Math.ceil((double) totalItems/itemsPerPage);
                
                log.info("totalPages : "+totalPages);
                
                for(int i = 0; i < totalPages; i++) {
                
                    PDPage page = new PDPage(A4);
                    log.info("Creating new page.");
                    
                    document.addPage(page);
                    log.info("Adding created page in document.");
                    
                    try(PDPageContentStream contentStream = new PDPageContentStream(document, page)){
                    
                    	drawImage(document, page, contentStream);
	                    // Draw static elements
                    	log.info("Draw static eleemnts.");
	                    drawStaticElements(contentStream, page, invoice, user);
	                    log.info("Finished static content drawing.");
	                    
	                    int fromIndex = itemsPerPage * i;
	                    int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);
	                    
	                    List<InvoiceItems> subItemList = itemList.subList(fromIndex, toIndex);
	                    
	                    invoice.setInvoiceItemList(subItemList);
	                    
	                    log.info("Adding contnet into page.");
	                	addContent(document, contentStream, page, invoice);
	                	log.info("Finished adding content.");
	                	
                    }
                	
                }
            
            try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
	            document.save(outputStream);
	            byteArray = outputStream.toByteArray();
            }
        }catch(Exception e) {
        	log.error("Exception during generating the PDF : "+e);
        }finally {
        	if(document != null) {
        		document.close();
        	}
        }
        
        return byteArray;
    }
    
    private void drawImage(PDDocument document, PDPage page, PDPageContentStream contentStream) throws IOException {
    	// Load logo image from resources
    	PDImageXObject pdImage = PDImageXObject.createFromFile(
    	        new ClassPathResource("static/images/logo.png").getFile().getAbsolutePath(), document);

    	// Set the desired dimensions and position (top-right)
    	float imageWidth = 60;
    	float imageHeight = 60;
    	float imageX = page.getMediaBox().getWidth() - imageWidth - 30; // 30px margin from right
    	float imageY = page.getMediaBox().getHeight() - imageHeight - 10; // 20px margin from top

    	contentStream.drawImage(pdImage, imageX, imageY, imageWidth, imageHeight);    	
    }

    private void drawStaticElements(PDPageContentStream contentStream, PDPage page, Invoice invoice, User user) throws IOException {
    	
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        float startY = pageHeight;
        float startX = 30;

        // Border
        contentStream.setStrokingColor(Color.BLACK);
        contentStream.addRect(BORDER_OFFSET, BORDER_OFFSET, 
                            pageWidth - 2 * BORDER_OFFSET, startY - 2 * BORDER_OFFSET);
        contentStream.stroke();
        
        startY -= 40;

        // Title
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
        String firmName = user.getFirmNameEn();
        drawCenteredString(contentStream, firmName!=null?firmName.toUpperCase():"FIRM NAME", pageWidth / 2, startY, pageWidth, 18);
        
        startY -= 15;

        // Address
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        drawCenteredString(contentStream, "Add-At Post-Arthe kh, Tal-Shirpur, Dist-Dhule-425427", 
                         pageWidth / 2, startY, pageWidth, 10);

        startY -= 15;
        
        // Sub-heading in red
        contentStream.setNonStrokingColor(Color.RED);
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
        drawCenteredString(contentStream, "Authorized Dealer - Skipper Metzer India LLP.", 
                         pageWidth / 2, startY, pageWidth, 11);
        contentStream.setNonStrokingColor(Color.BLACK);

        startY -= 15;
        
        // Line break under subheading
        contentStream.setStrokingColor(Color.BLACK);
        contentStream.moveTo(BORDER_OFFSET, startY);
        contentStream.lineTo(pageWidth - BORDER_OFFSET, startY);
        contentStream.stroke();
        

        startY -= 15;
        
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(startX, startY);
        contentStream.showText("Proprieter Name : "+user.getFirstNameEn()+" "+user.getLastNameEn());
        contentStream.endText();
        
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(pageWidth - 200, startY);
        contentStream.showText("Mobile No. : "+user.getContactNumber());
        contentStream.endText();        

        startY -= 10;
        
        // Line break under subheading
        contentStream.setStrokingColor(Color.BLACK);
        contentStream.moveTo(BORDER_OFFSET, startY);
        contentStream.lineTo(pageWidth - BORDER_OFFSET, startY);
        contentStream.stroke();        

        startY -= 20;
        
        // Customer Information (left)
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(40, startY);
        contentStream.showText("Farmer Name : "+invoice.getFarmer());
        contentStream.endText();
        
        startY -= 15;
        
        String[] addressLines = invoice.getAddress().split("\n");
        
    	contentStream.beginText();
		contentStream.newLineAtOffset(40, startY);
		contentStream.showText("Address : ");
		contentStream.endText();        
        
        for(String address : addressLines) {
        	log.info("address line : "+address);
        	contentStream.beginText();
			contentStream.newLineAtOffset(90, startY);
			contentStream.showText(address.trim());
			contentStream.endText();
				
			startY -= 15;
        }
        
        contentStream.beginText();
        contentStream.newLineAtOffset(40, startY);
        contentStream.showText("Addhar ID : "+invoice.getAadharId());
        contentStream.endText();
        
        startY -= 15;

        contentStream.beginText();
        contentStream.newLineAtOffset(40, startY);
        contentStream.showText("Farmer ID : "+invoice.getFarmerId());
        contentStream.endText();        

        // Date Information (right)
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        float dateX = pageWidth - 200;
        
        startY = pageHeight - 130;        

        contentStream.beginText();
        contentStream.newLineAtOffset(dateX, startY);
        contentStream.showText("District Name : "+invoice.getDistrictName());
        contentStream.endText(); 
        
        startY -= 15;

        contentStream.beginText();
        contentStream.newLineAtOffset(dateX, startY);
        contentStream.showText("Tehsil Name : "+invoice.getTehsilName());
        contentStream.endText();
        
        startY -= 15;
        
        contentStream.beginText();
        contentStream.newLineAtOffset(dateX, startY);
        contentStream.showText("Invoice Date: " + dateFormatter.format(invoice.getCreatedAt()));
        contentStream.endText();
        
        startY -= 15;
        
        contentStream.beginText();
        contentStream.newLineAtOffset(dateX, startY);
        contentStream.showText("Generated Date: " + dateFormatter.format(new Date()));
        contentStream.endText();       

        // Footer with signatures
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
        drawCenteredString(contentStream, "FARMER SIGN", pageWidth / 6, startY - 600, pageWidth, 10);
        drawCenteredString(contentStream, "COMPANY SIGN", pageWidth / 2, startY - 600, pageWidth, 10);
        drawCenteredString(contentStream, "CHAMUNDAI IRRIGATION", pageWidth * 5 / 6, startY - 600, pageWidth, 10);
    }

    private void addContent(PDDocument document, PDPageContentStream contentStream, PDPage page, Invoice invoice) throws IOException {
        float pageWidth = page.getMediaBox().getWidth();
        float startY = page.getMediaBox().getHeight() - 220; // Start below header

        startY -= addLineBreak(contentStream, startY, pageWidth, null);

        // Quotation title
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        drawCenteredString(contentStream, "Quotation", pageWidth / 2, startY, pageWidth, 12);

        startY -= addLineBreak(contentStream, startY, pageWidth, null);

        startY -= 20;

        // Create item table
        String[][] items = new String[invoice.getInvoiceItemList().size()+1][8]; 
        items[0] = new String[]{"Sr No.", "DESCRIPTION", "CML NO", "BATCH NO", "QTY", "UNIT", "SELLING PRICE", "AMOUNT"};
        int i = 1;
        float grandTotalAmount = 0;
        for(InvoiceItems invoiceItem : invoice.getInvoiceItemList()) {
        	String[] newItem = {
        		String.valueOf(i), 	
        		(invoiceItem.getCategoryId()>0?invoiceItem.getCategoryName():"")+" "+(invoiceItem.getItemId()>0?invoiceItem.getItemName():""),
                invoiceItem.getCmlNumber(), "", String.valueOf(invoiceItem.getQuantity()),
        		invoiceItem.getUnit(), String.valueOf(invoiceItem.getRate()), String.valueOf(invoiceItem.getTotal())
        	};
        	
        	grandTotalAmount += invoiceItem.getTotal();
        	
        	items[i] = newItem;
        	i++;
        }

        float[] columnWidths = {35, 200, 55, 60, 40, 40, 80, 60};
        float tableWidth = columnWidths[0] + columnWidths[1] + columnWidths[2] + columnWidths[3] + columnWidths[4] + columnWidths[5] + columnWidths[6] + columnWidths[7];
        float tableX = (pageWidth - tableWidth) / 2;

        // Draw table
        log.info("Adding table");
        startY = drawTable(contentStream, items, tableX, startY, columnWidths);
        startY -= 50;

        // Draw totals table
        String[][] summaries = {
            {"Total", String.valueOf(grandTotalAmount)},
            {"SUBSIDY 50%", "200...X"},
            {"", "200...X"},
            {"file", "1000...X"},
            {"", "68400...X"}
        };

        float totalsX = pageWidth - 40 - 170; // Right-aligned
        log.info("Adding summary table.");
        drawSimpleTable(contentStream, summaries, totalsX, startY, new float[]{100, 100});
    }

    private float drawTable(PDPageContentStream contentStream, String[][] data, 
                                float startX, float startY, float[] columnWidths) throws IOException {
        float rowHeight = 25;
        float tableWidth = 0;
        for (float width : columnWidths) {
            tableWidth += width;
        }
        float pageY = startY;

        // Draw header background
        contentStream.setNonStrokingColor(Color.GRAY);
        contentStream.addRect(startX, startY, tableWidth, rowHeight);
        contentStream.fill();
        contentStream.setNonStrokingColor(Color.BLACK);

        // Draw grid and content
        contentStream.setStrokingColor(Color.BLACK);

        for (int row = 0; row < data.length; row++) {
            float y = startY - (row * rowHeight);

            // Draw horizontal lines
            contentStream.moveTo(startX, y);
            contentStream.lineTo(startX + tableWidth, y);
            contentStream.stroke();

            // Draw vertical lines and content
            float x = startX;
            for (int col = 0; col < data[row].length; col++) {
                // Vertical line
                contentStream.moveTo(x, y);
                contentStream.lineTo(x, y + rowHeight);
                contentStream.stroke();

                // Cell content
                contentStream.beginText();
                
                // Header row styling
                if (row == 0) {
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
                    contentStream.setNonStrokingColor(Color.WHITE);
                } else {
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
                    contentStream.setNonStrokingColor(Color.BLACK);
                }

                log.info("Start adding content");

                log.info("data : "+data[row][col]);
                // Center align for quantity, price, total columns
                float textX = x + 5;
                if (col > 0) {
                	try {
	                    float textWidth = hindiFont.getStringWidth(data[row][col]!=null?data[row][col]:"") / 1000 * 10;
	                    textX = x + (columnWidths[col] - textWidth) / 2;
                	}catch(IllegalArgumentException exception) {
                        float textWidth = new PDType1Font(Standard14Fonts.FontName.HELVETICA).getStringWidth(data[row][col]!=null?data[row][col]:"") / 1000 * 10;
                        textX = x + (columnWidths[col] - textWidth) / 2;
                	}                    
                }
                log.info("done 1....");
                if(col == 1) {
                	textX = x + 5;
                }

                log.info("data : "+data[row][col]);

                contentStream.newLineAtOffset(textX, y + 5);
                try {
                	contentStream.showText(data[row][col]!=null?data[row][col]:"");
                }catch(IllegalArgumentException exception) {
                	contentStream.setFont(hindiFont, 10);
                	contentStream.showText(data[row][col]!=null?data[row][col]:"");
                }
                contentStream.endText();
                log.info("done 2....");
                x += columnWidths[col];
                pageY = y;
            }
            // Last vertical line
            contentStream.moveTo(x, y);
            contentStream.lineTo(x, y + rowHeight);
            contentStream.stroke();
        }
        
        return pageY;

    }

    private void drawSimpleTable(PDPageContentStream contentStream, String[][] data, 
                                      float startX, float startY, float[] columnWidths) throws IOException {
        float rowHeight = 20;

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
        contentStream.setNonStrokingColor(Color.BLACK);

        for (int row = 0; row < data.length; row++) {
            float y = startY - (row * rowHeight);
            
            float x = startX;
            for (int col = 0; col < data[row].length; col++) {
                contentStream.beginText();
                
                // Right align for amounts
                float textX = x;
                if (col == 1) {
                    float textWidth = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD).getStringWidth(data[row][col]) / 1000 * 10;
                    textX = x + columnWidths[col] - textWidth - 5;
                } else {
                    textX = x + 5;
                }

                contentStream.newLineAtOffset(textX + 5, y + 5);
                contentStream.showText(data[row][col]);
                contentStream.endText();

                x += columnWidths[col];
            }
        }
    }

    private void drawCenteredString(PDPageContentStream contentStream, String text,
    										float centerX, float y, float pageWidth, float fontSize) throws IOException {
        float stringWidth = new PDType1Font(Standard14Fonts.FontName.HELVETICA).getStringWidth(text) / 1000 * fontSize;
        float startX = centerX - (stringWidth / 2);

        contentStream.beginText();
        contentStream.newLineAtOffset(startX, y);
        contentStream.showText(text);
        contentStream.endText();

    }

    private float addLineBreak(PDPageContentStream contentStream, float y, float pageWidth, Float lineWidth) throws IOException {
        if (lineWidth == null) {
            lineWidth = 1f; // default line width
        }
        
        y = y-5;
        
        contentStream.setStrokingColor(Color.BLACK);
        contentStream.setLineWidth(lineWidth);
        contentStream.moveTo(BORDER_OFFSET, y);
        contentStream.lineTo(pageWidth - BORDER_OFFSET, y);
        contentStream.stroke();
        
        return 16;
    }

}