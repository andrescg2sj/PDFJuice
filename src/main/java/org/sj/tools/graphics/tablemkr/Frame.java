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
 

package org.sj.tools.graphics.tablemkr;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import org.sj.tools.graphics.sectorizer.ContentRegion;
import org.sj.tools.graphics.sectorizer.GraphicString;
import org.sj.tools.graphics.sectorizer.HorizBandTransform;
import org.sj.tools.graphics.sectorizer.StrRegionCluster;
import org.sj.tools.graphics.sectorizer.StringRegion;

public class Frame {

	int x[];
	int y[];

	public Frame(int _x[], int _y[]) {
		x = _x;
		y = _y;
	}
	
	public static void logArray(String title, int values[]) 
	{
		System.out.println(title + ": ");
		for(int v: values) {
			System.out.print(" "+v);
		}
		System.out.println();

	}
	
	public void log() {
		System.out.println("Frame:");
		logArray("x", x);
		logArray("y", y);
	}
	
	static int getClosestIndex(double value, int marks[]) {
		double min_dist = Math.abs(marks[0] - value);
		int closest = 0;
		for(int i=1;i<marks.length;i++) {
			double dist = Math.abs(marks[i] - value);
			if(dist < min_dist) {
				closest = i;
				min_dist = dist;
			}
			if(dist > min_dist) {
				return closest;
			}
		}
		return closest;
	}
	
    static int getLastIndexBelow(double value, int marks[]) 
    {
    	int i=0;
    	if(value < marks[0]) {
    		//throw new IllegalArgumentException("Value is too low:" +value);
    		//TODO: log
    		System.err.println("Value is too low: "+value
    				+ ". Min is: "+marks[0]);
    		return 0;
    	}
    	
    	if(value > marks[marks.length-1]) {
    		/*throw new IllegalArgumentException*/
    		//TODO: log
    		System.err.println("Value is too high: "+value
    				+ ". Max is: "+marks[marks.length-1]);
    		//FIXME
    		return marks.length-1;
    	}
    	while((i <= marks.length) && (marks[i] < value)) {
    		i++;
    	}
    	return i-1;
    }
    
    int reverseIndexY(int i) {
    	return y.length-2-i;
    }
    
    static int getFirstIndexAbove(double value, int marks[])
    {
    	return 1 + getLastIndexBelow(value, marks);
    }
    
    public CellLocation rectToLoc(Rectangle2D rect, double threshold) {
    	int x_index = getLastIndexBelow(rect.getX()+threshold, this.x);
		int y_index = getLastIndexBelow(rect.getY()+threshold, this.y);
		int max_x_index = getFirstIndexAbove(rect.getMaxX()-threshold, this.x);
		int max_y_index = getFirstIndexAbove(rect.getMaxY()-threshold, this.y);
		int horizSpan = max_x_index- x_index;
		int vertSpan = max_y_index - y_index;
		
		/*System.out.println(String.format("y_index:%d max_y_index:%d\n",
				y_index, max_y_index));
		System.out.println(String.format("rev(max_y_index):%d\n",
				reverseIndexY(max_y_index-1)));*/

		

		//return new CellLocation(x_index, y_index, horizSpan, vertSpan);
		return new CellLocation(x_index, reverseIndexY(max_y_index-1), horizSpan, vertSpan);
    }
    
    public CellLocation horizLineToLoc(TLine line, double threshold) {
    	/* FIXME: Sometimes pushes values out of table limits. */
    	int x_index = getClosestIndex(line.getA().getX(), this.x);
		int y_index = getClosestIndex(line.getA().getY(), this.y);
		int max_x_index = getClosestIndex(line.getB().getX(), this.x);
		//int max_y_index = getFirstIndexAbove(line.getB().getY()-threshold, this.y);
		int horizSpan = max_x_index - x_index;
		int vertSpan = 0;
		//System.out.println(String.format("y_index:%d\n", y_index));

		return new CellLocation(x_index, reverseIndexY(y_index-1), horizSpan, vertSpan);
    }
    
    public CellLocation vertLineToLoc(TLine line, double threshold) {
    	/* FIXME: Sometimes pushes values out of table limits. */

    	int x_index = getClosestIndex(line.getA().getX(), this.x);
		int y_index = getClosestIndex(line.getA().getY(), this.y);
		//int max_x_index = getFirstIndexAbove(line.getB().getX()-threshold, this.x);
		int max_y_index = getClosestIndex(line.getB().getY(), this.y);
		int horizSpan = 0;
		int vertSpan = max_y_index - y_index;
		/*
		System.out.println(String.format("y_index:%d max_y_index:%d\n",
				y_index, max_y_index));*/


		return new CellLocation(x_index, reverseIndexY(max_y_index-1), horizSpan, vertSpan);
    }

    
    public CellLocation lineToLoc(TLine line, double threshold) {
    	if(line.isHoriz()) {
    		return horizLineToLoc(line,threshold);
    	}
    	return vertLineToLoc(line, threshold);
    }
    
    
    public CellLocation textLocation(GraphicString gstr, double threshold) {
    	Rectangle2D r = gstr.getBounds();
    	CellLocation cloc = rectToLoc(r, threshold);
    	cloc.add(gstr.getText());
    	return cloc;
    }
    
    
    public List<String> getLines(StringRegion sr) {
    	StrRegionCluster c = new StrRegionCluster();
    	
    	c.pushRegion(sr);
    	c.clearRegions();
    	c.remainingToRegions(null);
    	c.sortReverseYRegions();
    	Rectangle2D bounds = sr.getBounds();
    	HorizBandTransform hb = new HorizBandTransform(bounds);
    	c.transformRegions(hb);
    	c.meltRegions();
    	List<String> lines = new LinkedList<String>();
    	for(int i=0; i< c.getNumberOfRegions(); i++) {
    		ContentRegion<GraphicString> cr = c.getRegion(i);
    		StringRegion scr = new StringRegion(cr);
    		lines.add(String.join("", scr.getStrings()));
    	}
    	return lines;
    }
	
	public CellLocation areaToCellLoc(Area a, double threshold) {
		Rectangle2D r = a.getBounds();
    	CellLocation cloc = rectToLoc(r, threshold);
    	//TODO: change all "Areas" to StringRegions
    	StringRegion sr =  new StringRegion(a.getBounds());
    	sr.addAll(a.content);
    	cloc.cell.addAll(getLines(sr));
    	//cloc.cell.addAll(sr.getStrings());
    	return cloc;
	}
	
	public int numRows()
	{
		return Math.max(y.length - 1,0);
	}
	
	public int numCols()
	{
		return Math.max(x.length - 1,0);
	}
	
	public Area cellToArea(int col, int row, int colSpan, int rowSpan) {
		if(colSpan < 1) throw new IllegalArgumentException("colSpan="+colSpan);
		if(rowSpan < 1) throw new IllegalArgumentException("rowSpan="+rowSpan);
		double min_x = x[col]; 
		int high_index_y = reverseIndexY(row)+1;
		//System.out.println(String.format("  row:%d, index:%d", row, high_index_y));
		double max_y = y[high_index_y]; 
		double max_x = x[col+colSpan]; 
		int low_index_y = high_index_y-rowSpan;
		//System.out.println(String.format("  last_row(reversed):%d", low_index_y));
		double min_y = y[low_index_y];
		double height = max_y - min_y;
		if(height < 0) {
			System.out.println("Warning!. height="+height);
		}
		return new RectArea(min_x, min_y, max_x-min_x, height);
				
	}

	
	public Cell areaToCell(Area a, double threshold) {
		Rectangle2D r = a.getBounds();
    	CellLocation cloc = rectToLoc(r, threshold);
    	return cloc.cell;
	}
	

}
