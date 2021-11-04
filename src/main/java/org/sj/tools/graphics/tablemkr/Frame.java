/*
 * Apache License
 *
 * Copyright (c) 2021 andrescg2sj
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
import org.sj.tools.graphics.sectorizer.StrRegionCluster;
import org.sj.tools.graphics.sectorizer.StringRegion;
import org.sj.tools.graphics.sectorizer.geom.Grid;
import org.sj.tools.graphics.sectorizer.geom.HorizBandTransform;
import org.sj.tools.graphics.sectorizer.geom.NumberVector;

public class Frame extends Grid {

	@Deprecated
	public Frame(int _x[], int _y[]) {
		super(_x,_y);
	}
	
	public Frame(double _x[], double _y[]) {
		super(_x,_y);
	}

	
	public Frame(NumberVector x, NumberVector y) {
		super(x,y);
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
		//logArray("x", x);
		//logArray("y", y);
	}
	
    
    
    
    public CellLocation rectToLoc(Rectangle2D rect, double threshold) {
    	int x_index = xLimits.getLastIndexBelow(rect.getX()+threshold);
		int y_index = yLimits.getLastIndexBelow(rect.getY()+threshold);
		int max_x_index = xLimits.getFirstIndexAbove(rect.getMaxX()-threshold);
		int max_y_index = yLimits.getFirstIndexAbove(rect.getMaxY()-threshold);
		int horizSpan = max_x_index- x_index;
		int vertSpan = max_y_index - y_index;
		
		/*System.out.println(String.format("y_index:%d max_y_index:%d\n",
				y_index, max_y_index));
		System.out.println(String.format("rev(max_y_index):%d\n",
				reverseIndexY(max_y_index-1)));*/

		

		//return new CellLocation(x_index, y_index, horizSpan, vertSpan);
		return new CellLocation(x_index, yLimits.reverseIndex(max_y_index-1), horizSpan, vertSpan);
    }
    
    public CellLocation horizLineToLoc(TLine line, double threshold) {
    	/* FIXME: Sometimes pushes values out of table limits. */
    	int x_index = xLimits.getClosestIndex(line.getA().getX());
		int y_index = yLimits.getClosestIndex(line.getA().getY());
		int max_x_index = xLimits.getClosestIndex(line.getB().getX());
		//int max_y_index = getFirstIndexAbove(line.getB().getY()-threshold);
		int horizSpan = max_x_index - x_index;
		int vertSpan = 0;
		//System.out.println(String.format("y_index:%d\n", y_index));

		return new CellLocation(x_index, yLimits.reverseIndex(y_index-1), horizSpan, vertSpan);
    }
    
    public CellLocation vertLineToLoc(TLine line, double threshold) {
    	/* FIXME: Sometimes pushes values out of table limits. */

    	int x_index = xLimits.getClosestIndex(line.getA().getX());
		int y_index = yLimits.getClosestIndex(line.getA().getY());
		//int max_x_index = xLimits.getFirstIndexAbove(line.getB().getX()-threshold);
		int max_y_index = yLimits.getClosestIndex(line.getB().getY());
		int horizSpan = 0;
		int vertSpan = max_y_index - y_index;
		/*
		System.out.println(String.format("y_index:%d max_y_index:%d\n",
				y_index, max_y_index));*/


		return new CellLocation(x_index, yLimits.reverseIndex(max_y_index-1), horizSpan, vertSpan);
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
		return Math.max(yLimits.countEdges() - 1,0);
	}
	
	public int numCols()
	{
		return Math.max(xLimits.countEdges() - 1,0);
	}
	
	public Area cellToArea(int col, int row, int colSpan, int rowSpan) {
		if(colSpan < 1) throw new IllegalArgumentException("colSpan="+colSpan);
		if(rowSpan < 1) throw new IllegalArgumentException("rowSpan="+rowSpan);
		double min_x = xLimits.getEdge(col); 
		int high_index_y = yLimits.reverseIndex(row)+1;
		//System.out.println(String.format("  row:%d, index:%d", row, high_index_y));
		double max_y = yLimits.getEdge(high_index_y); 
		double max_x = xLimits.getEdge(col+colSpan); 
		int low_index_y = high_index_y-rowSpan;
		//System.out.println(String.format("  last_row(reversed):%d", low_index_y));
		double min_y = yLimits.getEdge(low_index_y);
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
