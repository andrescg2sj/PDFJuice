/*
 * Apache License
 *
 * Copyright (c) 2019 andrescg2sj
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
 

package org.sj.tools.graphics.sectorizer;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

//import org.apache.pdfbox.pdmodel.font.PDFont;

public class GraphicString implements Positionable {

	String text;
	Rectangle2D bounds;
	
	public GraphicString(GraphicString gs) {
		text = gs.text;
		bounds = gs.bounds;
	}
	
	public GraphicString(String t, Rectangle2D b) {
		text = t;
		bounds = b;
	}
	
	public Rectangle2D getBounds() {
		return bounds;
	}
	
	public String getText() {
		return text;
	}

	public Point2D getPosition() {
		if(bounds == null) {
			return null;
		}
		return new Point2D.Double(bounds.getX(),bounds.getY());	
	}
	
	public String toString() {
		return String.format("(%s,%2f,%2f)", text, bounds.getX(), bounds.getY());
	}


    	public void add(GraphicString gs, String union) {
		text = text + union + gs.text;
		bounds.add(gs.bounds);
	}

	
	public void add(GraphicString gs) {
		add(gs, "");
	}

}
