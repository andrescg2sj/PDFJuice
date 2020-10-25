package org.sj.tools.graphics.sectorizer.geom;

public class Grid {
	
	protected Segmenter xLimits;
	protected Segmenter yLimits;
	
	public Grid(double x[], double y[]) {
		xLimits = new Segmenter(x);
		yLimits = new Segmenter(y);
	}
	
	public Grid(int x[], int y[]) {
		xLimits = new Segmenter(x);
		yLimits = new Segmenter(y);
	}

	
	public Grid(NumberVector x, NumberVector y) {
		xLimits = new Segmenter(x);
		yLimits = new Segmenter(y);
	}
	
	

}
