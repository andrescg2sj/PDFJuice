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
package org.sj.tools.pdfjuice.slide.build;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.sj.tools.graphics.sectorizer.ContentRegion;
import org.sj.tools.graphics.sectorizer.GraphicString;
import org.sj.tools.graphics.sectorizer.PosRegionCluster;
import org.sj.tools.graphics.sectorizer.Positionable;
import org.sj.tools.graphics.sectorizer.StrRegionCluster;
import org.sj.tools.graphics.sectorizer.StringRegion;
import org.sj.tools.graphics.sectorizer.geom.MultiplyTransform;
import org.sj.tools.graphics.sectorizer.geom.NumberVector;
import org.sj.tools.graphics.sectorizer.geom.RectAccumulator;
import org.sj.tools.graphics.tablemkr.CellLocation;
import org.sj.tools.graphics.tablemkr.Frame;
import org.sj.tools.graphics.tablemkr.GridBorders;
import org.sj.tools.graphics.tablemkr.Table;
import org.sj.tools.pdfjuice.slide.SldGroup;
import org.sj.tools.pdfjuice.slide.SldTable;
import org.sj.tools.pdfjuice.slide.SldText;
import org.sj.tools.pdfjuice.slide.Slide;

public class PosterSlideBuilder extends BasicSlideBuilder {
	
	private static Logger log = Logger.getLogger("org.sj.tools.pdfjuice.slide.build.PosterSlideBuilder");

	public PosterSlideBuilder() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Slide build(StrRegionCluster cluster) {
		Rectangle2D area = cluster.getBounds();
		titleMinY = area.getMinY() + (area.getHeight()*titleSizePercent)/100;
		
		//StrRegionCluster title = new StrRegionCluster();
		log.fine("cluster - regions: "+cluster.getNumberOfRegions());
		log.fine("cluster - remaining: "+cluster.countRemaining());
		
		cluster.remainingToRegions(new MultiplyTransform(3));
		cluster.sortRegions();
		cluster.meltRegions();

		log.fine("content - regions: "+cluster.getNumberOfRegions());
		log.fine("content - Remaining: "+cluster.countRemaining());

		/*
		Vector<StrRegionCluster> vect = cluster.strRCDivideY(titleMinY);
		StrRegionCluster titleCl = vect.get(1);
		StrRegionCluster contentCl = vect.get(0);
		if(titleCl.getNumberOfRegions() == 0)
			titleCl.pushRegionForRemaining();
		
		log.fine("title - regions: "+titleCl.getNumberOfRegions());
		log.fine("title - remaining: "+titleCl.countRemaining());
		*/

		cluster.sortReverseYRegions();
		
		//Slide s = buildElements(cluster);
		Slide s = buildAsTable(cluster);
		
		return s;
	}
	
	
	public void regionsToTextElements(Slide s, StrRegionCluster cluster) {
		for(int i=0; i<cluster.getNumberOfRegions();i++) {
			ContentRegion<GraphicString> cr = cluster.getRegion(i);
			cr.sortReverseY();
			StringRegion sreg = new StringRegion(cr);
			Iterator<GraphicString> it = sreg.contentIterator();
			
			SldText text = null;
			
			while(it.hasNext()) {
				GraphicString gs = it.next();
				if(text == null) {
					text = new SldText(buildPDFString(gs));
				} else {
					text.add(buildPDFString(gs));
				}
			}
			if(text != null) {
				s.add(text);
				log.finest("add text: " + text.getText());
			}
		}

	}

	public static List<Rectangle2D> getRects(StrRegionCluster cluster) {
		List<Rectangle2D> rects = new LinkedList<Rectangle2D>();
		for(int i=0; i<cluster.getNumberOfRegions(); i++) {
			rects.add(cluster.getRegion(i).getBounds());
		}
		return rects;
	}
	
	public static double[] getHeights(List<Rectangle2D> rects) {
		double numbers[] = new double[rects.size()];
		int i=0;
		for(Rectangle2D r: rects) {
			numbers[i++] = r.getHeight();
		}
		return numbers;
	}

	public static int[] getMinIndexes(double[] heights) {
		int indexes[] = new int[heights.length/2];
		int count = 0;
 		if(heights.length < 3) {
			return null;
		}
		for(int i=1; i< heights.length-1; i++) {
			double value = heights[i];
			if(value < heights[i-1] && value < heights[i+1]) {
				indexes[count] = i;
				count++;
			}
		}
		return java.util.Arrays.copyOf(indexes, count);
	}

    /**
     * Get vertical axes that divide the cluster.
     *
     * @returns NumberVector with the x coordinates of the vertical axes.
     */
	public static NumberVector getVertVoids(List<Rectangle2D> rects) {
	    log.finest("accumulate");
		List<Rectangle2D> xAccumRects = RectAccumulator.accumulateX(rects);
		log.finest("setup");
		double heights[] = getHeights(xAccumRects);
		NumberVector x_voids = new NumberVector();
		int min_i[] = getMinIndexes(heights);
		if(min_i != null) {
			log.finest("for");
			for(int k=0; k<min_i.length; k++) {
				log.finest("k="+k);
				x_voids.insert(xAccumRects.get(min_i[k]).getCenterY());
			}
		}
		return x_voids;

	}

	public static NumberVector getClusterVertVoids(StrRegionCluster cluster) {
		Rectangle2D bounds = cluster.getBounds();
		Rectangle2D xAxis = MultiplyTransform.multiplyRect(bounds, 1, 0);

		List<Rectangle2D> rects = getRects(cluster);
		rects.add(xAxis);
		NumberVector voids  = getVertVoids(rects);
		voids.insert(xAxis.getMinY());
		voids.insert(xAxis.getMaxY());
		return voids;
	}
	
	public static NumberVector getClusterHorizVoids(StrRegionCluster cluster) {
		Rectangle2D bounds = cluster.getBounds();
		Rectangle2D yAxis= MultiplyTransform.multiplyRect(bounds, 0, 1);

		List<Rectangle2D> rects = getRects(cluster);
		rects.add(yAxis);
		rects = RectAccumulator.transpose(rects);
		NumberVector voids  = getVertVoids(rects);
		voids.insert(yAxis.getMinX());
		voids.insert(yAxis.getMaxX());
		return voids;
	}

	
	
	public Slide buildAsTable(StrRegionCluster contentCl) {
		Slide s= new Slide();
		
		Rectangle2D bounds = contentCl.getBounds();
		
		NumberVector xGuides = getClusterVertVoids(contentCl); 
		NumberVector yGuides = getClusterHorizVoids(contentCl); 
		log.fine("xGuides: "+xGuides);
		log.fine("yGuides: "+yGuides);
		
		Frame frame = new Frame(xGuides, yGuides);
		int numCols = xGuides.size()-1;
		int numRows = yGuides.size()-1;
		Table t = new Table(numCols, numRows);
		//GridBorders borders = new GridBorders(numCols, numRows);
		//borders.setAll();
		for(int i=0; i< contentCl.getNumberOfRegions(); i++) {
			StringRegion sreg =  contentCl.getStrRegion(i);
			Rectangle2D rect = sreg.getBounds();
			CellLocation loc = frame.rectToLoc(rect, 0);
			/*borders.setBetween(loc.getCol(), loc.getRow(),
					loc.getColSpan(), loc.getRowSpan(), false);*/
			t.spanCell(loc.getCol(), loc.getRow(), loc.getColSpan(), loc.getRowSpan());
			for(int j=0; j<sreg.countElements(); j++) {
				GraphicString gs = sreg.get(j);
				t.add(loc.getCol(), loc.getRow(), gs.getText());
			}
		}
		
		//TODO: insert Table into Slide
		s.add(new SldTable(t, bounds));
		
		return s;
	}
	
	public Slide buildElements(StrRegionCluster contentCl) {
		
		Slide s= new Slide();
		log.fine("creating content");
		
		
		regionsToTextElements(s,contentCl);
		
		

		for(int i=0; i<contentCl.countRemaining();i++) {
			GraphicString gs = contentCl.getRemaining(i);
			SldText text = new SldText(buildPDFString(gs));
			log.fine("creating(2):"+gs.getText());
			s.add(text);
		}
		
		return s;
	}


}
