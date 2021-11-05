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
package org.sj.tools.graphics.sectorizer.geom;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

public class ExpandTransform implements RectTransformation {
	
	double border_x, border_y;
	
	public ExpandTransform(double b) {
		border_x = border_y = b;
	}

	public ExpandTransform(double bx, double by) {
		border_x = bx;
		border_y = by;
	}

	

	@Override
	public Rectangle2D transform(Rectangle2D r) {
		return expandRect(r, border_x, border_y);
	}


	public static Rectangle2D expandRectX(Rectangle2D rect, double border) {
		return new Rectangle2D.Double(rect.getX() - border, rect.getY(),
				rect.getWidth() + border*2,
				rect.getHeight() );
	}



	public static Rectangle2D expandRect(Rectangle2D rect, double border) {
		return expandRect(rect, border, border);
	}
		
		public static Rectangle2D expandRect(Rectangle2D rect, double bx, double by) {
			return new Rectangle2D.Double(rect.getX() - bx, rect.getY() - by,
				rect.getWidth() + bx*2,
				rect.getHeight() + by*2);
	}
	
	

}
