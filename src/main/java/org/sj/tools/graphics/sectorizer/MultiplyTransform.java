package org.sj.tools.graphics.sectorizer;

import java.awt.geom.Rectangle2D;

public class MultiplyTransform implements RectTransformation {
	
	double factor;
	
	public MultiplyTransform(double f) {
		factor = f;
	}

	@Override
	public Rectangle2D transform(Rectangle2D r) {
		return multiplyRect(r, factor);
	}

	public static Rectangle2D multiplyRect(Rectangle2D rect, double factor) {
		double width = rect.getWidth() * factor;
		double height = rect.getHeight() * factor;
		return new Rectangle2D.Double(rect.getX() - width*(2*(factor-1))/2,
				rect.getY() - height*(2*(factor-1))/2,
				width,
				height);
		
	}

}
