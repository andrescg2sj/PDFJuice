package org.sj.tools.pdfjuice.slide.build;

import java.util.Vector;
import org.sj.tools.graphics.sectorizer.geom.NumberVector;

import org.junit.Assert;
import org.junit.Test;



public class PosterSlideBuilderTest {

    @Test
    public void testingGetMinIndexes() {
	
	int a[] = {3,1,2};

	int mins[] = PosterSlideBuilder.getMinIndexes(new NumberVector(a));
	Assert.assertEquals("size", 1, mins.length);
    }

}
