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

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import java.util.Comparator;
import java.util.Iterator;

import org.sj.tools.graphics.sectorizer.GraphicString;
import org.sj.tools.graphics.sectorizer.NormalComparator;
import org.sj.tools.graphics.sectorizer.Positionable;

/* 
 * TODO: Substitute by sectorizer.contentRegion?
 * - Only feasible for RecWtArea.
 */
public abstract class Area
{
    //Line top, left, right, bottom;
	Vector<GraphicString> content;
	
	Area() {
		content = new Vector<GraphicString>();
	}


    public abstract Area[] split(TLine l);

    public abstract boolean contains(Point2D p);

    
    public abstract boolean contains(Rectangle2D r);

    public abstract boolean outOrBound(TLine l);


    
    public abstract boolean strictlyContains(TLine l);

    public  abstract Rectangle2D getBounds();

    /**
     * Decide whether l collides with this area
     * collisionThreshold is used in order not to detect a collision
     * with a line very close to the border.
     */
    public abstract boolean collision(TLine l);

    /**
     * @param tolerance width of a border of this Area in which 
     * collsion is not detected.
     */
    public abstract boolean collision(TLine l, double tolerance);

    
    public static Rectangle2D getMaximumRect(TLine lines[]) {
	Point2D start = lines[0].getA();
	Rectangle2D r = new Rectangle2D.Double(start.getX(), start.getY(),
					       0,0);
	
	for(int i=0; i<lines.length; i++) {
	    r.add(lines[i].getA());	
	    r.add(lines[i].getB());
	    for(int j=i+1; j<lines.length; j++) {
		Point2D z = lines[i].outIntersection(lines[j]);
		if(z != null)
		    r.add(z);
	    }
	}
	return r;
    }
    
	public abstract boolean containsMost(Rectangle2D r);
	
	public void sort()
	{
		sort(NormalComparator.getInstance());
	}

	public void sort(Comparator<Positionable> c) {
		content.sort(c);
	}

    
    public void addContent(GraphicString gstr)
    {
    	if(this.contains(gstr.getBounds()))
    		content.add(gstr);
    }
    
    void pushContent(GraphicString gstr) {
		content.add(gstr);
    }
    

    public Iterator<GraphicString> getContents()
    {
	return content.iterator();
    }



    public String toString() {
	return getBounds().toString();
    }
    
}
