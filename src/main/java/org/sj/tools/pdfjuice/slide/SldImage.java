package org.sj.tools.pdfjuice.slide;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.sj.tools.graphics.elements.Image;

public class SldImage extends Image implements SldObject {
	
	public SldImage(Point2D _position, BufferedImage _image) {
		super(_position, _image);
	}


	public SldImage(Image img) {
		super(img);
	}


	@Override
	public String getText() {
		return "<image>";
	}


	@Override
	public String toHTML() {
		// TODO encode with Base64
		// See: https://www.w3docs.com/snippets/html/how-to-display-base64-images-in-html.html
		return "<img src=\"data:image/png;base64, ...\" />";
	}

}
