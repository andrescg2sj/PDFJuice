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
 

package org.sj.tools.graphics.elements;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.Shape;
//import java.awt.geom.Point2D;

import java.util.Vector;
//import java.util.Iterator;

import org.sj.tools.graphics.sectorizer.StringRegion;

public class GrPath {


    Point2D position;
    Vector<Shape> elements;

    public GrPath() {
	elements = new Vector<Shape>();

    }

    public void reset() {
	elements.removeAllElements();
    }

    public void moveTo(Point2D p) {
	position = p;
    }


    public void lineTo(Point2D q) {
	if(position == null) {
	    //TODO: Warning
	} else {
	    Line2D line = new Line2D.Double(position, q);
	    elements.add(line);
	}
	moveTo(q);

    }
    
    //TODO: May the points describe a rotated rectangle?
    public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) {
    	//TODO: log/Warning/exception if the rectangle is rotated
    	/*
    	Rectangle2D rect = new Rectangle2D.Double(p0.getX(),p0.getY(),p1.getX()-p0.getX(),p1.getY()-p0.getY());
    	rect.add(p2);
    	rect.add(p3);
    	elements.add(rect);*/
    	Rectangle2D rect = StringRegion.rectangleFromPoints(p0, p1, p2, p3);
    	elements.add(rect);
    }

    public Iterable<Shape> getIterable() {
	return elements;
    }

    public void clear() {
	elements.clear();
    }

    public int numElements() 
    {
    	return elements.size();
    }
    
    public Point2D getPosition() {
    	return position;
    }
}

