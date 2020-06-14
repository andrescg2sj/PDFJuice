package org.sj.tools.graphics.sectorizer;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public class CompoundTransform implements RectTransformation {
	
	LinkedList<RectTransformation> transforms = new LinkedList<RectTransformation>();

	
	public CompoundTransform() {
		
	}

	public CompoundTransform(RectTransformation t) {
		add(t);
	}

	
	public void add(RectTransformation t) {
		transforms.add(t);
	}
	
	@Override
	public Rectangle2D transform(Rectangle2D r) {
		for(RectTransformation t: transforms) {
			r = t.transform(r);
		}
		return r;
	}

}
