package org.sj.tools.pdfjuice.slide.build;

import java.util.Vector;
import org.sj.tools.graphics.sectorizer.geom.NumberVector;

import java.awt.geom.Rectangle2D;
import java.util.Vector;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;



public class PosterSlideBuilderTest {

    @Test
    public void testingGetMinIndexes() {
	
	int a[] = {3,1,2};

	int mins[] = PosterSlideBuilder.getMinIndexes(new NumberVector(a));
	Assert.assertEquals("size", 1, mins.length);
    }


        @Test
    public void testingGetVertVoids() {

	List<Rectangle2D> rects = new Vector<Rectangle2D>();
	Rectangle2D a = new Rectangle2D.Double(0,0,10, 10);
	Rectangle2D b = new Rectangle2D.Double(30,0,10, 10);

	rects.add(a);
	rects.add(b);

	NumberVector numbers = PosterSlideBuilder.getVertVoids(rects);
	Assert.assertEquals("number of edges", 1, numbers.size());
    }

}
