package org.sj.tools.pdfjuice.slide;

import java.awt.Rectangle;

import org.sj.tools.graphics.pdf.PDFString;

public class SldTitle extends SldText {
	
	public SldTitle(PDFString str) {
		super(str);
	}

	public SldTitle(String text, Rectangle bounds) {
		super(text, bounds, null);
	}
	
	public String toHTML() {
		return "<h2>" + super.toHTML() + "</h2>";
	}
	
}
