package org.sj.tools.pdfjuice.slide;

import java.awt.geom.Rectangle2D;

import org.sj.tools.graphics.sectorizer.Positionable;

public interface SldObject extends Positionable {

	public String getText();
	
	public Rectangle2D getBounds();
	
	//TODO: move to package .html
	public String toHTML();

	
}
