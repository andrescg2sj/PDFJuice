package org.sj.tools.graphics.tablemkr;

import static org.junit.Assert.assertEquals;


import java.awt.geom.Rectangle2D;

import org.junit.Test;
import org.sj.tools.graphics.tablemkr.Area;
import org.sj.tools.graphics.tablemkr.CellLocation;
import org.sj.tools.graphics.tablemkr.Frame;
import org.sj.tools.graphics.tablemkr.RectArea;


public class FrameTest {

	@Test
	public void testingAreaToCell() {
		int x[] = {10, 50, 200};
		int y[] = {10, 100};
		
		Area a = new RectArea(new Rectangle2D.Float(10,10,40,90));
		Area b = new RectArea(new Rectangle2D.Float(50,10,140,90));
		
		Frame frame = new Frame(x,y);
		CellLocation cla = frame.areaToCellLoc(a, 5);
		CellLocation clb = frame.areaToCellLoc(b, 5);
		assertEquals("col a", 0, cla.getCol());
		assertEquals("row a", 0, cla.getRow());
		assertEquals("col b", 1, clb.getCol());
		assertEquals("row b", 0, clb.getRow());
		
	}
}
