package org.sj.tools.graphics.sectorizer;

import java.awt.geom.Rectangle2D;

import org.junit.Assert;
import org.junit.Test;


public class TestContentRegion {

	@Test
	public void testingSharedArea() {
		Rectangle2D a = new Rectangle2D.Double(10,10, 20,10);
		Rectangle2D b = new Rectangle2D.Double(20,10, 20,10);
		Rectangle2D c = new Rectangle2D.Double(35,10, 20,10);
		
		Assert.assertEquals("shared a-b", 0.5, ContentRegion.sharedArea(a, b),0.01);
		Assert.assertEquals("shared a-c", 0, ContentRegion.sharedArea(a, c),0.01);
	}
}
