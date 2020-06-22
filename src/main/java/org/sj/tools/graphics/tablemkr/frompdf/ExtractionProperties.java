package org.sj.tools.graphics.tablemkr.frompdf;

import java.awt.geom.Rectangle2D;

import org.sj.tools.graphics.elements.ClippingArea;

public class ExtractionProperties {
	
	ClippingArea clip;

    public static final double DEFAULT_THICKNESS = 2;
    public static final double DEFAULT_PROXIMITY = 0.3;
    
    /**
     * maximum thickness of a rectangle in order to be considered as a line.
     */
    double maxLineThickness = DEFAULT_THICKNESS;

    /**
     * maximum distance of two objects to be considered in the same region (table).
     */
    double tableThreshold = DEFAULT_PROXIMITY;

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

	public void setMaxLineThickness(double v) {
		maxLineThickness = v;
	}

	public void setMinProximity(double v) {
		tableThreshold = v;
	}


	public void setClipRect(Rectangle2D rect) {
		clip.set(rect);
	}






}
