package org.sj.tools.graphics.sectorizer;

import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

public class TestReverseYComparator {
	@Test
	public void testingCompare() 
	{
		GraphicString sa = TestNormalComparator.buildGString("a", 60,20);
		GraphicString sb = TestNormalComparator.buildGString("b", 40,40);
		GraphicString sc = TestNormalComparator.buildGString("c", 20,60);
		Vector<Positionable> vect = new Vector<Positionable>();
		vect.add(sa);
		vect.add(sb);
		vect.add(sc);
		
		ReverseYComparator nc = ReverseYComparator.getInstance(); 
		vect.sort(nc);
		Assert.assertEquals("string c", sc, vect.get(0));
		Assert.assertEquals("string b", sb, vect.get(1));
		Assert.assertEquals("string a", sa, vect.get(2));
	}
	

	@Test
	public void testingCompareHoriz() 
	{
		GraphicString sa = TestNormalComparator.buildGString("a", 20,10);
		GraphicString sb = TestNormalComparator.buildGString("b", 40,10);
		GraphicString sc = TestNormalComparator.buildGString("c", 60,10);
		Vector<Positionable> vect = new Vector<Positionable>();
		vect.add(sc);
		vect.add(sb);
		vect.add(sa);
		
		ReverseYComparator nc = ReverseYComparator.getInstance();
				
		vect.sort(nc);
		Assert.assertEquals("string a", sa, vect.get(0));
		Assert.assertEquals("string b", sb, vect.get(1));
		Assert.assertEquals("string c", sc, vect.get(2));
	}
	

}
