package org.sj.tools.graphics.elements;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.sj.tools.graphics.sectorizer.Positionable;

public class Image implements Positionable {
	
	Point2D position;
	BufferedImage image;
	
	public Image(Point2D _position, BufferedImage _image) {
		position = _position;
		image = _image;
	}
	
	public Image(Image img) {
		this.position = img.position;
		this.image = img.image;
	}


	@Override
	public Point2D getPosition() {
		return position;
	}
	
	@Override
	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(position.getX(),position.getY(),
				image.getWidth(), image.getHeight());
	}

}
