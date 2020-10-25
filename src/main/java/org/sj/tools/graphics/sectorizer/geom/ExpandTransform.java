package org.sj.tools.graphics.sectorizer.geom;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

public class ExpandTransform implements RectTransformation {
	
	double border_x, border_y;
	
	public ExpandTransform(double b) {
		border_x = border_y = b;
	}

	public ExpandTransform(double bx, double by) {
		border_x = bx;
		border_y = by;
	}

	

	@Override
	public Rectangle2D transform(Rectangle2D r) {
		return expandRect(r, border_x, border_y);
	}


	public static Rectangle2D expandRectX(Rectangle2D rect, double border) {
		return new Rectangle2D.Double(rect.getX() - border, rect.getY(),
				rect.getWidth() + border*2,
				rect.getHeight() );
	}



	public static Rectangle2D expandRect(Rectangle2D rect, double border) {
		return expandRect(rect, border, border);
	}
		
		public static Rectangle2D expandRect(Rectangle2D rect, double bx, double by) {
			return new Rectangle2D.Double(rect.getX() - bx, rect.getY() - by,
				rect.getWidth() + bx*2,
				rect.getHeight() + by*2);
	}
	
	

}
