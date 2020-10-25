package org.sj.tools.pdfjuice.slide;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.sj.tools.graphics.tablemkr.Table;

public class SldTable implements SldObject {
	
	Table table;
	Rectangle2D bounds;
	
	public SldTable(Table _table, Rectangle2D _bounds) {
		table = _table;
		bounds = _bounds;
	}


	@Override
	public Point2D getPosition() {
		return new Point2D.Double(bounds.getX(), bounds.getY());
	}

	@Override
	public String getText() {
		return table.toString();
	}

	@Override
	public Rectangle2D getBounds() {
		return bounds;
	}

	@Override
	public String toHTML() {
		//TODO
		return "<table>TODO</table>";
	}

}
