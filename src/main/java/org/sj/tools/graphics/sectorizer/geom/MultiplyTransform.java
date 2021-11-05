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

public class MultiplyTransform implements RectTransformation {
	
	double x_factor, y_factor;
	
	public MultiplyTransform(double f) {
		x_factor = y_factor = f;
	}
	
	public MultiplyTransform(double fx, double fy) {
		x_factor = fx;
		y_factor = fy;
	}


	@Override
	public Rectangle2D transform(Rectangle2D r) {
		return multiplyRect(r, x_factor, y_factor);
	}
	
	public static Rectangle2D multiplyRect(Rectangle2D rect, double factor) {
		return multiplyRect(rect, factor,factor);
	}


	public static Rectangle2D multiplyRect(Rectangle2D rect, double fx, double  fy) {
		double width = rect.getWidth();
		double height = rect.getHeight();
		return new Rectangle2D.Double(rect.getX() + width*(1-fx)/2,
				rect.getY() + height*(1-fy)/2,
				width*fx,
				height*fy);
		
	}

}
