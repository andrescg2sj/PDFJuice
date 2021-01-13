package org.sj.tools.graphics.tablemkr.frompdf;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;


import org.sj.tools.graphics.tablemkr.Table;

public class PDFTableExtractorTest {
	
	public List<Table> testPDF(String path, int numTables, int cols, int rows) throws IOException {
		PDFTableExtractor extractor = new PDFTableExtractor(new File(path));
		List<Table> tables = extractor.getAllTables(false);
		Assert.assertEquals("t4 - Table count", numTables, tables.size());
		Table t = tables.get(0);
		Assert.assertEquals("Table cols", cols, t.getCols());
		Assert.assertEquals("Table rows", rows, t.getRows());
		return tables;
	}

	@Test
	public void testingTable4() throws IOException {
		testPDF("res/test04.pdf",1,2,7);
	}
	
	@Test
	public void testingMergedCells() throws IOException {
		List<Table> tables =testPDF("res/test/merged-cells1.pdf",1,3,3);
		Table t = tables.get(0);
		Assert.assertTrue(t.get(0, 1).fullText().contains("Merinas"));
	}

	
	@Test
	public void testingTable5() throws IOException {
		testPDF("res/test05.pdf",1,4,7);
	}

	@Test
	public void testingTable6() throws IOException {
		testPDF("res/test06.pdf",1,4,7);
	}

	@Test
	public void testingTable1() throws IOException {
		testPDF("res/test01-1cell.pdf",1,1,1);
	}

	@Test
	public void testingTable2() throws IOException {
		testPDF("res/test02-2cell.pdf",1,2,1);
	}


	@Test
	public void testingTable3() throws IOException {
		testPDF("res/test03-4cell.pdf",1,2,2);
	}

	
	@Test
	public void testingContents() throws IOException {
		String example = "res/CEPI-1-1.pdf";
		File file = new File(example);
		PDFTableExtractor extractor = new PDFTableExtractor(file);
		List<Table> tables = extractor.getAllTables();
		Assert.assertEquals("Table size", 3, tables.size());
		
		
		extractor = new PDFTableExtractor(new File("res/test01-1cell.pdf"));
		tables = extractor.getAllTables();
		Assert.assertEquals("Table count", 1, tables.size());
		Table t = tables.get(0);
		Assert.assertEquals("Table cols", 1, t.getCols());
		Assert.assertEquals("Table rows", 1, t.getRows());
		Assert.assertEquals("Table contents:", "Cuadro 1", t.get(0, 0).fullText().trim());

		extractor = new PDFTableExtractor(new File("res/test02-2cell.pdf"));
		tables = extractor.getAllTables();
		Assert.assertEquals("Table count", 1, tables.size());
		t = tables.get(0);
		Assert.assertEquals("Table cols", 2, t.getCols());
		Assert.assertEquals("Table rows", 1, t.getRows());
		Assert.assertEquals("Table contents:", "X", t.get(0, 0).fullText().trim());
		Assert.assertEquals("Table contents:", "Y", t.get(1, 0).fullText().trim());

		extractor = new PDFTableExtractor(new File("res/test03-4cell.pdf"));
		tables = extractor.getAllTables();
		Assert.assertEquals("Table count", 1, tables.size());
		t = tables.get(0);
		Assert.assertEquals("Table cols", 2, t.getCols());
		Assert.assertEquals("Table rows", 2, t.getRows());
		Assert.assertEquals("Table contents:", "A", t.get(0, 0).fullText().trim());
		Assert.assertEquals("Table contents:", "B", t.get(1, 0).fullText().trim());
		Assert.assertEquals("Table contents:", "C", t.get(0, 1).fullText().trim());
		Assert.assertEquals("Table contents:", "D", t.get(1, 1).fullText().trim());
		

	}
	
}
