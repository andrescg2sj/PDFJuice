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
package org.sj.tools.graphics.tablemkr.frompdf;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.sj.tools.graphics.elements.ClippingArea;

public class ExtractionProperties {
	
	ClippingArea clip;

    public static final double DEFAULT_THICKNESS = 2;
    public static final double DEFAULT_PROXIMITY = 0.3;
    
    public static final int RGB_MASK = 0xffffff;
    
    /**
     * maximum thickness of a rectangle in order to be considered as a line.
     */
    double maxLineThickness = DEFAULT_THICKNESS;

    /**
     * maximum distance of two objects to be considered in the same region (table).
     */
    double tableThreshold = DEFAULT_PROXIMITY;
    
    boolean enableDetectShapes = false;
    
    boolean filterColoredLines = true;
    
    Color lineFilterColor = Color.BLACK;

    public ExtractionProperties() {
    	clip = new ClippingArea();
    }
    
    public ExtractionProperties(Rectangle2D clipRect) {
    	clip = new ClippingArea(clipRect);
    	
    }

    public ExtractionProperties(Rectangle2D clipRect, double proximity, double thickness) {
    	clip = new ClippingArea(clipRect);
    	maxLineThickness = thickness;
    	tableThreshold = proximity;
    }
    
    public void setShapeDetection(boolean detect) {
    	this.enableDetectShapes = detect;
    }

	public void setMaxLineThickness(double v) {
		maxLineThickness = v;
	}
	
	public void setFilterColoredLines(boolean filter) {
		this.filterColoredLines = filter;
	}

	public void setMinProximity(double v) {
		tableThreshold = v;
	}


	public void setClipRect(Rectangle2D rect) {
		clip.set(rect);
	}
	
	/**
	 * Check if a color passes the filter (or the filter is not activated).
	 * 
	 * @param rgb (red, green, blue) color specification.
	 * @return true if the given rgb color passes the filter (or everything is accepted).
	 */
	public boolean filterColor(int rgb) {
		if(this.filterColoredLines) {
			return ((lineFilterColor.getRGB() & RGB_MASK) == (rgb & RGB_MASK));
		}
		return true;
	}
	

}
