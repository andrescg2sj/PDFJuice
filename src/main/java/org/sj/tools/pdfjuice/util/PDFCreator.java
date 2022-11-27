package org.sj.tools.pdfjuice.util;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFCreator {
	
	String path;
	
	public PDFCreator(String _path) {
		path = _path;
	}
	
	/*
	 * See: 
	 * https://www.tutorialspoint.com/pdfbox/pdfbox_adding_text.htm
	 * https://www.tutorialspoint.com/pdfbox/pdfbox_inserting_image.htm
	 */
	public void create() {
    	PDDocument document = new PDDocument();

        for (int i=0; i<10; i++) {
           //Creating a blank page 
           PDPage blankPage = new PDPage();

           //Adding the blank page to the document
           document.addPage( blankPage );
        } 
       
        //Saving the document
        try {
			document.save(path);
	        document.close();
	        System.out.println("PDF created");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //Closing the document
		
	}
	
	public void createWithText() {
		try {
			File file = new File("C:/PdfBox_Examples/my_doc.pdf");
		      PDDocument document = PDDocument.load(file);
		       
		      //Retrieving the pages of the document 
		      PDPage page = document.getPage(1);
		      PDPageContentStream contentStream = new PDPageContentStream(document, page);
		      
		      //Begin the Content stream 
		      contentStream.beginText(); 
		       
		      //Setting the font to the Content stream  
				contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
		
		      //Setting the position for the line 
		      contentStream.newLineAtOffset(25, 500);
		
		      String text = "This is the sample document and we are adding content to it.";
		
		      //Adding text in the form of string 
		      contentStream.showText(text);      
		
		      //Ending the content stream
		      contentStream.endText();
		
		      System.out.println("Content added");
		
		      //Closing the content stream
		      contentStream.close();
		
		      //Saving the document
		      document.save(new File("C:/PdfBox_Examples/new.pdf"));
		
		      //Closing the document
		      document.close();
	      } catch (IOException e) {
	    	  // TODO Auto-generated catch block
	    	  e.printStackTrace();
	      }

	}
}
