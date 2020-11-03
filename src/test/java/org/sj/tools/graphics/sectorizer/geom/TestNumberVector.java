package org.sj.tools.graphics.sectorizer.geom;

import org.junit.Assert;
import org.junit.Test;

public class TestNumberVector {
	
	private static final double delta = 0.001;
	
	@Test
	public void testingInsert() {
		
		NumberVector v = new NumberVector();
		v.insert(3);
		v.insert(1);
		v.insert(2);
		Assert.assertEquals("first",1, v.get(0),delta); 
		Assert.assertEquals("first",2, v.get(1),delta); 
		Assert.assertEquals("first",3, v.get(2),delta);
		
		NumberVector a = new NumberVector();
		double x[] = {5, 6,1,10,4,4,7,5};
		for(int i=0; i<x.length; i++) {
			a.insert(x[i]); 
		}
		Assert.assertEquals("count", 6, a.size());
		for(int i=1; i<a.size(); i++) {
			Assert.assertTrue("a order", a.get(i) > a.get(i-1));
		}
	}

}
