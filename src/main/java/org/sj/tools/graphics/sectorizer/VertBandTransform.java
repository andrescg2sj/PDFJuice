package org.sj.tools.graphics.sectorizer;

import java.awt.geom.Rectangle2D;

public class VertBandTransform extends BoundTransform {

	public VertBandTransform(Rectangle2D r) {
		super(r);
	}
	
	@Override
	public Rectangle2D transform(Rectangle2D r) {
		return mixXY(r, bounds);
	}

}
