/*
 * Apache License
 *
 * Copyright (c) 2021 Andres Gonzalez SJ
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

package org.sj.tools.graphics.pdf;


import java.awt.Point;
import java.awt.Rectangle;

import org.apache.pdfbox.pdmodel.font.PDFont;
//import org.sj.punidos.crminer.sectorizer.Positionable;
import org.sj.tools.graphics.sectorizer.GraphicString;

public class PDFString extends GraphicString {
	
	PDFont font;
	
	public PDFString(GraphicString gs, PDFont f) {
		super(gs);
		font = f;
	}
	
	public PDFString(String t, Rectangle b, PDFont f) {
		super(t, b);
		font = f;
	}

	public PDFString(PDFString str) {
		super(str);
		font = str.font;
	}
}
