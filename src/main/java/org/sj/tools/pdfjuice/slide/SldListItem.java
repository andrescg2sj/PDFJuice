package org.sj.tools.pdfjuice.slide;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class SldListItem implements SldObject {
	
	SldText bullet;
	SldObject content;
	
	public SldListItem(SldText blt, SldObject cont) {
		this.bullet = blt;
		this.content = cont;
	}
	

	@Override
	public String getText() {
		return content.getText();
	}

	@Override
	public Rectangle2D getBounds() {
		Rectangle2D r = content.getBounds();
		return r.createUnion(content.getBounds());
	}


	@Override
	public Point2D getPosition() {
		Rectangle2D r = getBounds();
		return new Point2D.Double(r.getMinX(),r.getMinY());
	}
	
	public String toString() {
		return bullet.getText() + " "+content.getText() + "\r\n";
	}


	@Override
	public String toHTML() {
		return "<li>" + content.toHTML() + "</li>";
	}

}
