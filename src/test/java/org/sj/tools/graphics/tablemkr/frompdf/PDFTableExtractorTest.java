package org.sj.tools.graphics.tablemkr.frompdf;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;


import org.sj.tools.graphics.tablemkr.Table;

public class PDFTableExtractorTest {
	
	public List<Table> testPDFTableNumber(String path, int numTables, boolean clean) throws IOException {
		PDFTableExtractor extractor = new PDFTableExtractor(new File(path));
		List<Table> tables = extractor.getAllTables(clean);
		Assert.assertEquals("t4 - Table count", numTables, tables.size());
		return tables;
	}
	
	public List<Table> testPDF(String path, int numTables, int cols, int rows) throws IOException {
		return testPDF(path, numTables, cols, rows, false);
	}
	
	public List<Table> testPDF(String path, int numTables, int cols, int rows, boolean clean) throws IOException {
		List<Table> tables = testPDFTableNumber(path, numTables, clean);
		Table t = tables.get(0);
		Assert.assertEquals("Table cols", cols, t.getCols());
		Assert.assertEquals("Table rows", rows, t.getRows());
		return tables;
	}
	
	void testFirstCell(String path, String heads[]) throws IOException {
		List<Table> tables = testPDFTableNumber(path,heads.length, true);
		Iterator<Table> it = tables.iterator();
		for(int i=0; i<heads.length; i++) {
			Table t = it.next();
			String test = t.get(0, 0).fullText().trim().substring(0, heads[i].length());
			String msg = String.format("tables %d from '%s'",
						   i, path);
			//Assert.assertEquals(msg, heads[i], test);
		}
	}
	
	@Test
	public void testingOrder() throws IOException {
		String heads_arg6[] = {"ÁREA", "Formación", "Talleres", "Asesorías", "Denominación", "NACIONALIDAD", "Denominación"};
		testFirstCell("res/test/202006-arg-1.pdf", heads_arg6);

		String heads_arg11[] = {"ÁREA", "Formación", "Cursos", "Talleres"};
		testFirstCell("res/test/202011-arg-1.pdf", heads_arg11);

		String heads_cham3[] = {"Idiomas", "Asesorías"};
		testFirstCell("res/test/202006-cham-3.pdf", heads_cham3);

		String heads_cham5[] = {"Taller", "Actividades", "ACTIVIDADES", "Atenciones"};
		testFirstCell("res/test/202006-cham-5.pdf", heads_cham5);

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
