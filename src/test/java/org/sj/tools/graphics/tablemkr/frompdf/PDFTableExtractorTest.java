package org.sj.tools.graphics.tablemkr.frompdf;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;


import org.sj.tools.graphics.tablemkr.Table;

public class PDFTableExtractorTest {
	

	
	@Test
	public void testingGenerateAllTables() throws IOException {
		String example = "res/CEPI-1-1.pdf";
		File file = new File(example);
		PDFTableExtractor extractor = new PDFTableExtractor(file);
		List<Table> tables = extractor.getAllTables();
		assertEquals("Table size", 3, tables.size());
		
		extractor = new PDFTableExtractor(new File("res/test01-1cell.pdf"));
		tables = extractor.getAllTables();
		assertEquals("Table count", 1, tables.size());
		Table t = tables.get(0);
		assertEquals("Table cols", 1, t.getCols());
		assertEquals("Table rows", 1, t.getRows());
		assertEquals("Table contents:", "Cuadro 1", t.get(0, 0).fullText().trim());

		extractor = new PDFTableExtractor(new File("res/test02-2cell.pdf"));
		tables = extractor.getAllTables();
		assertEquals("Table count", 1, tables.size());
		t = tables.get(0);
		assertEquals("Table cols", 2, t.getCols());
		assertEquals("Table rows", 1, t.getRows());
		assertEquals("Table contents:", "X", t.get(0, 0).fullText().trim());
		assertEquals("Table contents:", "Y", t.get(1, 0).fullText().trim());

		extractor = new PDFTableExtractor(new File("res/test03-4cell.pdf"));
		tables = extractor.getAllTables();
		assertEquals("Table count", 1, tables.size());
		t = tables.get(0);
		assertEquals("Table cols", 2, t.getCols());
		assertEquals("Table rows", 2, t.getRows());
		assertEquals("Table contents:", "A", t.get(0, 0).fullText().trim());
		assertEquals("Table contents:", "B", t.get(1, 0).fullText().trim());
		assertEquals("Table contents:", "C", t.get(0, 1).fullText().trim());
		assertEquals("Table contents:", "D", t.get(1, 1).fullText().trim());

	}
	
}
