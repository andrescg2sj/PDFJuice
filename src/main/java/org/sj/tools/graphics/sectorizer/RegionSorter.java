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

import java.util.Vector;

import java.awt.geom.Point2D;

public class RegionSorter {

	static void swap(Vector<Positionable> regs, int a, int b) {
		Positionable temp;
		temp = regs.get(a);
		regs.set(a, regs.get(b));
		regs.set(b,temp);
	}
	
	static boolean greater(Positionable a, Positionable b) {
		return greater(a.getPosition(), b.getPosition());
	}
	
	static boolean greater(Point2D a, Point2D b) {
		if(a.getY() > b.getY()) return true;
		else if(a.getY() == b.getY()) {
			if(a.getX() > b.getX()) return true;
		}
		return false;
	}

	static boolean lowerOrEqual(Positionable a, Positionable b) {
		return greater(b,a);
	}
	
	static void qsort(Vector<Positionable> regs, int left, int right) {
		/*
		 * Ref:
		 * http://puntocomnoesunlenguaje.blogspot.com/2012/12/java-quicksort.html
		 */
		Positionable pivot = regs.get(left);
		int i = left;
		int j = right;
		
		while(i<j) {
			while(lowerOrEqual(regs.get(i),pivot) && i<j) i++;
			while(greater(regs.get(j),pivot)) j--;
			
			if(i<j) {
				swap(regs, i,j);
			}
		}
		regs.set(left, regs.get(j));
		regs.set(j,pivot);
		
		if(left<(j-1))
			qsort(regs, left,j-1);
		if((j+1)>right)
			qsort(regs,j+1,right);
	}
	
	public static void sortRegions(Vector<Positionable> regs) {
		//throw new UnsupportedOperationException("sortRegions");
		qsort(regs, 0, regs.size());
	}
}
