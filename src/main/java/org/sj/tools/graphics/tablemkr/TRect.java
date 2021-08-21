package org.sj.tools.graphics.tablemkr;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.sj.tools.graphics.sectorizer.Positionable;

public class TRect extends Rectangle2D.Double implements Positionable {
	
	Color color;
	
	public TRect(Rectangle2D r, Color c) {
		this.setFrame(r);
		color = c;
	}

	@Override
	public Point2D getPosition() {
		return new Point2D.Double(getX(), getY());
	}

	
}
