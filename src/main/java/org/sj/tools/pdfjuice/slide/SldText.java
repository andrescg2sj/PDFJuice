package org.sj.tools.pdfjuice.slide;

//import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.sj.tools.graphics.pdf.PDFString;
import org.sj.tools.graphics.sectorizer.GraphicString;

public class SldText extends PDFString implements SldObject {
	
	public SldText(PDFString str) {
		super(str);
	}
	
	public SldText(GraphicString gs, PDFont font) {
		super(gs, font);
	}

	
	public SldText(String text, Rectangle bounds, PDFont f) {
		super(text, bounds, f);
	}
	
	public String toString() {
		return getText();
	}
	
	public String toHTML() {
		return getText();
	}



}
