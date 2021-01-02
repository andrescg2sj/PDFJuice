package org.sj.tools.graphics.sectorizer.geom;

import java.awt.geom.Rectangle2D;

import java.util.List;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

public class RectAccumulatorTest {

    @Test
    public void testingAccumulateX() {
	List<Rectangle2D> rects = new LinkedList<Rectangle2D>();
	Rectangle2D a = new Rectangle2D.Double(0,0,20,10);
	Rectangle2D b = new Rectangle2D.Double(10,0,20,20);

	rects.add(a);
	rects.add(b);
	List<Rectangle2D> result = RectAccumulator.accumulateX(rects);

	Assert.assertEquals("number of rects", 3, result.size());

    }

}
