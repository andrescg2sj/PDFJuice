package org.sj.tools.pdfjuice.slide.util;

import java.awt.Rectangle;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.sj.tools.graphics.pdf.PDFString;
import org.sj.tools.graphics.sectorizer.GStringBuffer;

public class PDFStringBuffer extends GStringBuffer {
	
	PDFont font;
	
	public void setFont(PDFont f) {
		//FIXME: font is overriden 
		font = f;
	}
	
	public PDFString makeString() {
		return new PDFString(super.makeString(),font);
	}

}
