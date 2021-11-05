/*
 * Apache License
 *
 * Copyright (c) 2021 Andrés González SJ
 *
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
