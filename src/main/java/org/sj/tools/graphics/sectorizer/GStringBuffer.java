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
 

package org.sj.tools.graphics.sectorizer;

import java.awt.Point;
import java.awt.Rectangle;

public class GStringBuffer {

	Rectangle region;
	StringBuilder text;
	
	public GStringBuffer() {
		reset();
	}
	
	public void add(String s) {
		text.append(s);
	}
	

	public void add(Point p) {
		if(p == null) return;
		if(region == null) {
			region = new Rectangle((int) p.getX(), (int) p.getY(), 0, 0);
		} else if(region.contains(p)) {
			return;
		} else {
			region.add(p);
			/*
			int x1 = (int) region.getX();
			int y1 = (int) region.getY();
			int x2 = (int) (region.getX()+region.getWidth());
			int y2 = (int) (region.getY()+region.getHeight());
			if(p.getX() < x1)
				x1 = (int) p.getX();
			if(p.getY() < y1)
				y1 = (int) p.getY();
			if(p.getX() > x2)
				x2 = (int) p.getX();
			if(p.getX() > x2)
				y2 = (int) p.getY();
			region = new Rectangle(x1, y1, x2-x1, y2-y1);
			*/	
 		}
	}
	
	public void add(Rectangle r) {
		if(r==null) return;
		if(region==null) {
			region = r;
			return;
		}
		region  = (Rectangle) region.createUnion(r);
	}
	
	public void reset() {
		region = null;
		text = new StringBuilder();
	}
	
	
	public String getText() {
		return text.toString();
		
	}
	
	public Rectangle getRegion() {
		return region;
	}
	
	public GraphicString makeString() {
		return new GraphicString(getText(), getRegion());
	}
	
	
}
