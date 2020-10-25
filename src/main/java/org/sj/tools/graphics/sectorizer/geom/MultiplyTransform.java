package org.sj.tools.graphics.sectorizer.geom;

import java.awt.geom.Rectangle2D;

public class MultiplyTransform implements RectTransformation {
	
	double x_factor, y_factor;
	
	public MultiplyTransform(double f) {
		x_factor = y_factor = f;
	}
	
	public MultiplyTransform(double fx, double fy) {
		x_factor = fx;
		y_factor = fy;
	}


	@Override
	public Rectangle2D transform(Rectangle2D r) {
		return multiplyRect(r, x_factor, y_factor);
	}
	
	public static Rectangle2D multiplyRect(Rectangle2D rect, double factor) {
		return multiplyRect(rect, factor,factor);
	}


	public static Rectangle2D multiplyRect(Rectangle2D rect, double fx, double  fy) {
		double width = rect.getWidth();
		double height = rect.getHeight();
		return new Rectangle2D.Double(rect.getX() + width*(1-fx)/2,
				rect.getY() + height*(1-fy)/2,
				width*fx,
				height*fy);
		
	}

}
