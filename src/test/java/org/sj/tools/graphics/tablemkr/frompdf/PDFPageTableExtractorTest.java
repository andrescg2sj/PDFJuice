package org.sj.tools.graphics.tablemkr.frompdf;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;



public class PDFPageTableExtractorTest {

	@Test
	public void testingReverse() {
		List<String> list = new LinkedList<String>();
		
		list.add("A");
		list.add("B");
		list.add("C");
		
		List<String> rev = PDFPageTableExtractor.reverse(list);
		Assert.assertEquals("first element", "C", rev.get(0));
		

	}
}
