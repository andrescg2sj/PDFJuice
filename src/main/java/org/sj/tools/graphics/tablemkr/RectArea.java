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

import java.awt.geom.Point2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.sj.tools.graphics.sectorizer.ContentRegion;
import org.sj.tools.graphics.sectorizer.geom.ExpandTransform;


public  class RectArea extends Area
{
    //Line top, left, right, bottom;
    Rectangle2D bounds;


    public RectArea(Rectangle2D r) {
	bounds = r;
    }

    public RectArea(TLine top, TLine left, TLine right, TLine bottom) {
	/*
	top = t;
	left = l;
	right = r;
	b = b;
	*/
	if(!(top.isAxisParallel() && left.isAxisParallel() &&
	     right.isAxisParallel() && bottom.isAxisParallel()))
	    throw new IllegalArgumentException("Lines are not parallel to axis");
	if(!(top.isHoriz() && bottom.isHoriz()) ||
	   left.isHoriz() || right.isHoriz())
	    throw new IllegalArgumentException("Lines don't have proper direction.");

	bounds = new Rectangle2D.Double(left.getA().getX(),
				 top.getA().getY(),
				 right.getA().getX(),
				 bottom.getA().getY());
    }

    public RectArea(double x, double y, double width, double height) {
	/*
	if(x2 < x1) {
	    double swap = x2;
	    x2 = x1;
	    x1 = swap;
	}
	if(y2 < y1) {
	    double swap = y2;
	    y2 = y1;
	    y1 = swap;
	    }*/

	//bounds = new Rectangle2D.Double(x1, y1, x2, y2);
	bounds = new Rectangle2D.Double(x, y, width, height);
    }

    

    public boolean collision(TLine l) {
	return bounds.intersectsLine(l.getLine2D());
    }

    public boolean collision(TLine l, double tolerance) {
	Rectangle2D safeRect = reduceRect(bounds, tolerance);
	return safeRect.intersectsLine(l.getLine2D());
    }

    public static Rectangle2D reduceRect(Rectangle2D r, double border) {
    	return ExpandTransform.expandRect(r,-border);
    }


    public  Area[] split(TLine l) {
	//Rectangle2D bounds = getBounds();
	
	if(!l.isAxisParallel())
	    throw new IllegalArgumentException("Line is not axis paralell");

	if(!collision(l))
	    return null;

	Area parts[] = new Area[2];
	if(l.isHoriz()) {
	    //if(l.getA().getY() < bounds.get
	    double height1 = l.getA().getY() - bounds.getY();
	    double height2 = bounds.getHeight() - height1;
	       
	    parts[0] = new RectArea(bounds.getX(), bounds.getY(),
				    bounds.getWidth(), height1);
	    parts[1] = new RectArea(bounds.getX(), bounds.getY()+height1,
				    bounds.getWidth(), height2);
	    
	} else {
	    double width1 = l.getA().getX() - bounds.getX();
	    double width2 = bounds.getWidth() - width1;
	       
	    parts[0] = new RectArea(bounds.getX(), bounds.getY(),
				    width1, bounds.getHeight());	
	    parts[1] = new RectArea(bounds.getX()+width1, bounds.getY(),
				    width2, bounds.getHeight());
    
	}
	return parts;
    }

    public boolean contains(Point2D p) {
	return bounds.contains(p);
    }

    public boolean strictlyContains(Point2D p) {
	return bounds.contains(p) && (p.getX() > bounds.getX()) &&
	    (p.getY() > bounds.getY());
    }

    public boolean outOrBound(TLine l) {
	return ((l.getA().getY() <= bounds.getY()) &&
		(l.getB().getY() <= bounds.getY())) ||
	    ((l.getA().getY() >= bounds.getMaxY()) &&
		(l.getB().getY() >= bounds.getMaxY())) ||
	    ((l.getA().getX() <= bounds.getX()) &&
		(l.getB().getX() <= bounds.getX())) ||
	    ((l.getA().getX() >= bounds.getMaxX()) &&
	     (l.getB().getX() >= bounds.getMaxX())) ||
	    !bounds.intersectsLine(l.getLine2D());
	    
    }

    public  boolean strictlyContains(TLine l)
    {
	return strictlyContains(l.getA()) && strictlyContains(l.getB());
    }
    


    public Rectangle2D getBounds() {
	return bounds;
    }

    public Area getMaximumArea(TLine lines[])
    {
	return new RectArea(getMaximumRect(lines));
    }

    @Override
	public boolean containsMost(Rectangle2D r) {
		return ContentRegion.containsMost(bounds, r);
	}
    
	@Override
	public boolean contains(Rectangle2D r) {
		//System.out.println("RectArea.contains");
		//System.out.println("   this: "+ bounds);
		//System.out.println("      r: "+ r);
		boolean test = bounds.contains(r);
		//System.out.println("      result: "+ test);
		return test;
	}

    
}
