package org.sj.tools.graphics.tablemkr;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import org.sj.tools.graphics.sectorizer.GraphicString;
import org.sj.tools.graphics.tablemkr.Area;
import org.sj.tools.graphics.tablemkr.RectArea;
import org.sj.tools.graphics.tablemkr.SplitTableMaker;
import org.sj.tools.graphics.tablemkr.TLine;
import org.sj.tools.graphics.tablemkr.Table;
import org.junit.Test;

public class SplitTableMakerTest {
	
	public static TLine[] strokesExample1() {
		TLine strokes[] = new TLine[4];
		strokes[0] = new TLine(10,10,200,10);
		strokes[1] = new TLine(10,10,10,100);
		strokes[2] = new TLine(200,10,200,100);
		strokes[3] = new TLine(10,100,200,100);
		return strokes;

	}
	
	public static TLine[] strokesExample2() {
		TLine strokes[] = new TLine[5];
		strokes[0] = new TLine(10,10,200,10);
		strokes[1] = new TLine(10,10,10,100);
		strokes[2] = new TLine(200,10,200,100);
		strokes[3] = new TLine(10,100,200,100);
		strokes[4] = new TLine(50,10,50,100);
		return strokes;
	}
	
	public static TLine[] badTable1() {
		TLine strokes[] = new TLine[4];
		strokes[0] = new TLine(10,10,200,10);
		strokes[1] = new TLine(10,10,200,10);
		strokes[2] = new TLine(200,10,200,100);
		strokes[3] = new TLine(10,100,200,100);
		return strokes;

	}


	
	@Test
	public void testingBuildAreas2() {
		TLine strokes[] = strokesExample2();

		//Vector<Area> areas = new Vector<Area>();
		//areas.add(new RectArea(new Rectangle2D.Double(10,10,40,90)));
		//areas.add(new RectArea(new Rectangle2D.Double(50,10,150,90)));
		//TODO
	}

	
	@Test
	public void testingAreasToTable() {
		TLine strokes[] = strokesExample2();

		Vector<Area> areas = new Vector<Area>();
		areas.add(new RectArea(new Rectangle2D.Double(10,10,40,90)));
		areas.add(new RectArea(new Rectangle2D.Double(50,10,150,90)));
	}
	

	@Test
	public void testingBuildAreas1() {
		
		TLine strokes[] = strokesExample1();

		SplitTableMaker maker = new SplitTableMaker();
		
		for(TLine s: strokes) {
			maker.add(s);
		}
		Vector<Area> areas = maker.buildAreas(strokes);
		//Table table = maker.makeTable();
		assertEquals("areas", 1, areas.size());
		//assertEquals("rows", 1,table.getRows());
		
	}
	
	@Test
	public void testingContent() {
		
		TLine strokes[] = strokesExample2();
		SplitTableMaker maker = new SplitTableMaker();
		
		GraphicString s1 = new GraphicString("A", new Rectangle(15,20,15,20));
		GraphicString s2 = new GraphicString("B", new Rectangle(60,20,40,40));
		
		for(TLine s: strokes) {
			maker.add(s);
		}
		maker.add(s1);
		maker.add(s2);
		//Vector<Area> areas = maker.buildAreas(strokes);
		Table table = maker.makeTable();
		
		//assertEquals("areas", 2, areas.size());
		assertEquals("rows", 1,table.getRows());
		assertEquals("cols", 2,table.getCols());
		assertEquals("Content(0,0)", "A", table.getCell(0, 0).getString(0));
		assertEquals("Content(1,0)", "B", table.getCell(1, 0).getString(0));
	}

	
	
	public void testTable(TLine strokes[], int expectedCols, int expectedRows) {
		
		//Line strokes[] = strokesExample1();

		SplitTableMaker maker = new SplitTableMaker();
		
		for(TLine s: strokes) {
			maker.add(s);
		}
		Vector<Area> areas = maker.buildAreas(strokes);
		Table table = maker.areasToTable(areas);
		
		assertEquals("cols", expectedCols, table.getCols());
		assertEquals("rows", expectedRows,table.getRows());
		
	}
	
	@Test
	public void testingMakeTable1() {
		
		TLine strokes[] = strokesExample1();
		testTable(strokes, 1,1);
	}

	
	

	
	@Test
	public void testingMakeTable2() {
		
		TLine strokes[] = strokesExample2();
		testTable(strokes, 2,1);
		
	}

    //TODO: make test for complex situations...
    /*
	@Test
	public void testingMakeBadTable1() {
		
		Line strokes[] = badTable1();
		testTable(strokes, 1,1);
	}
    */
}
