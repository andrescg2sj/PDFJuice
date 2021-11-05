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
import java.util.LinkedList;
import java.util.List;

public class RectAccumulator {

	public static final int X_AXIS = 0;
	public static final int Y_AXIS = -1;
	
	private int direction = X_AXIS;
	
	public RectAccumulator(int axis) {
		direction = axis;
	}
	
	
	public static Rectangle2D alignX(Rectangle2D a, Rectangle2D b) {
		if(a == b) return a;
		return new Rectangle2D.Double(b.getX(), a.getY(), b.getWidth(),b.getHeight());
	}
	public static Rectangle2D alignY(Rectangle2D a, Rectangle2D b) {
		if(a == b) return a;
		return new Rectangle2D.Double(b.getX(), a.getY(), b.getWidth(),b.getHeight());
	}

	
	/**
	 *
	 * Build a new rectangle, the same size of b, but aligned with a. 
	 *
	 */
	public Rectangle2D align(Rectangle2D a, Rectangle2D b) {
		if(direction == X_AXIS) {
			return alignX(a,b);
		} else {
			return alignY(a,b);
		}
	}
	
	public static Rectangle2D transpose(Rectangle2D r) {
		return new Rectangle2D.Double(r.getY(), r.getX(), r.getHeight(), r.getWidth());
	}
	
	public static List<Rectangle2D> transpose(List<Rectangle2D> list) {
		List<Rectangle2D> result = new LinkedList<Rectangle2D>();
		for(Rectangle2D r: list) {
			result.add(transpose(r));
		}
		return result;
	}
	
	public static List<Rectangle2D> transposeAndAlignX(List<Rectangle2D> list) {
		List<Rectangle2D> result = new LinkedList<Rectangle2D>();
		Rectangle2D first = list.get(0); 
		for(Rectangle2D r: list) {
			Rectangle2D s = alignX(first, r);
			s = transpose(s);
			result.add(s);
		}
		return result;
	}

	
	/**
	 * Builds a list of rectangles, with the accumulated height or width of the given. 
	 * @param a
	 * @param b
	 * @return
	 */
	public List<Rectangle2D> accumulate(Rectangle2D a, Rectangle2D b) {
		List<Rectangle2D> result = new LinkedList<Rectangle2D>();
		if(direction == Y_AXIS) {
			a = transpose(a);
			b = transpose(b);
		}
		Rectangle2D c = alignX(a,b);
		if(c.intersects(a)) {
			NumberVector vect = new NumberVector();
			vect.insert(a.getMinX());
			vect.insert(a.getMaxX());
			vect.insert(b.getMinX());
			vect.insert(b.getMaxX());
			for(int i=0; i  < vect.size()-1; i++) {
				double current_x = vect.get(i);
				double next_x = vect.get(i+1); 
				
				double mid_x = (current_x+next_x)/2;
				double height = 0; 
				if(a.getMinX() <= mid_x && a.getMaxX() <= mid_x) {
					height += a.getHeight();
				}
				if(b.getMinX() <= mid_x && b.getMaxX() <= mid_x) {
					height += b.getHeight();
				}
				result.add(new Rectangle2D.Double(current_x, a.getMinY(), next_x - current_x, height));
			}
		} else {
			result.add(a);
			result.add(b);
		}
		if(direction == Y_AXIS) {
			return transpose(result);
		}
		return result;
	}
	

    /**
     * Divide by 'x' coordinate, accumulate heights.
     */
	
	public static List<Rectangle2D> accumulateX(List<Rectangle2D> list) {
		List<Rectangle2D> result = new LinkedList<Rectangle2D>();
		if(list.size() == 0)
			return result;

		NumberVector vect = new NumberVector();
		for(Rectangle2D r : list) {
			vect.insert(r.getMinX());
			vect.insert(r.getMaxX());
		}
		double y = list.get(0).getMinY();
		for(int i=0; i  < vect.size()-1; i++) {
			double current_x = vect.get(i);
			double next_x = vect.get(i+1); 
			
			double mid_x = (current_x+next_x)/2;
			int rectsOverSegment = 0;
			double heightSum = 0; 
			for(Rectangle2D r: list) {
				if(r.getMinX() <= mid_x && r.getMaxX() >= mid_x) {
					heightSum += r.getHeight();
					rectsOverSegment ++;
					if(r.getMinY() < y) {
						y = r.getMinY();
					}
					
				}
				
			}
			if(rectsOverSegment > 0)
				result.add(new Rectangle2D.Double(current_x, y, next_x - current_x, heightSum));
		}
		return result;
	}
	
	
	public static List<Rectangle2D> accumulateY(List<Rectangle2D> list) {
		list = transposeAndAlignX(list);
		List<Rectangle2D> result = accumulateX(list);
		return transpose(result);
	}
	
	public static List<Rectangle2D> accumulate(List<Rectangle2D> list, int dir) {
		if(dir == Y_AXIS) {
			return accumulateY(list);
		} 
		return accumulateX(list);
	}
	

	public List<Rectangle2D> accumulate(List<Rectangle2D> list) {
		return accumulate(list, direction);
	}

}
