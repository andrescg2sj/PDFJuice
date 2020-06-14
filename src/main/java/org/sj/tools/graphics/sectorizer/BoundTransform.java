package org.sj.tools.graphics.sectorizer;

import java.awt.geom.Rectangle2D;

public abstract class BoundTransform implements RectTransformation {
	
	Rectangle2D bounds;
	
	public BoundTransform(Rectangle2D r) {
		bounds = r;
	}

	public static Rectangle2D mixXY(Rectangle2D a, Rectangle2D b) {
		return new Rectangle2D.Double(a.getMinX(), b.getMinY(),a.getWidth(),b.getHeight());
		
	}


}
