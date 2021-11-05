/*
 * Apache License
 *
 * Copyright (c) 2021 Andres Gonzalez SJ
 *
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 


package org.sj.tools.graphics.tablemkr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.sj.tools.graphics.tablemkr.Cell;
import org.sj.tools.graphics.tablemkr.GridBorders;


public class GridTest {
	
	@Test
	public void testingNumCols() {
		GridBorders g1 = new GridBorders(1,4);
		assertEquals("g1 ", 1, g1.numCols());
		
		GridBorders g2 = new GridBorders(2, 3);
		assertEquals("g2 ", 2, g2.numCols());

		GridBorders g3 = new GridBorders(5, 3);
		assertEquals("g3 ", 5, g3.numCols());
	}


	@Test
	public void testingNumRow() {
		GridBorders g1 = new GridBorders(1,4);
		assertEquals("g1 ", 4, g1.numRows());
		
		GridBorders g2 = new GridBorders(2, 3);
		assertEquals("g2 ", 3, g2.numRows());

		GridBorders g3 = new GridBorders(5, 2);
		assertEquals("g3 ", 2, g3.numRows());
	}

	@Test
	public void testingGetMaxCell()
	{
		//CellLimits lim[][] = createMatrix(2,2);
		GridBorders grid = new GridBorders(2,2);
		//GridTableMaker
		
		Cell a = grid.getMaxCell(0, 0);
		assertEquals("Empty grid. colSpan",2, a.colSpan);
		assertEquals("Empty grid. rowSpan",2, a.rowSpan);
		
		//lim[0][0].bottom = true;
		grid.setBottom(0, 0);
		
		Cell b = grid.getMaxCell(0, 0);
		assertEquals("H divided grid. colSpan",2, b.colSpan);
		assertEquals("H divided grid. rowSpan",1, b.rowSpan);
		
		grid.setBottom(0, 0,false);
		grid.setRight(0,0);
		//lim[0][0].right = true;
		
		Cell c = grid.getMaxCell(0, 0);
		assertEquals("V divided grid. colSpan",1, c.colSpan);
		assertEquals("V divided grid. rowSpan",2, c.rowSpan);
		
		
		GridBorders g = new GridBorders(2,2);
		g.setAll();
		
		Cell d = g.getMaxCell(0, 0);
		assertEquals("full table. colSpan", 1,d.colSpan);
		assertEquals("full table. rowSpan", 1,d.rowSpan);
		
		g.setRight(0,0,false);
		assertEquals("right", false,g.getRight(0, 0));

		Cell e = g.getMaxCell(0, 0);
		assertEquals("table. colSpan", 2, e.colSpan);
		assertEquals("table. rowSpan", 1, e.rowSpan);

		g.setAll();
		g.setBottom(1, 0, false);
		Cell f = g.getMaxCell(1,0);
		assertEquals("table 3. colSpan", 1,f.colSpan);
		assertEquals("table 3. rowSpan", 2,f.rowSpan);
		
		GridBorders h = new GridBorders(2,7);
		h.setAll();
		h.setBottom(1, 2,false);
		h.setBottom(1, 3,false);
		h.setBottom(1, 4,false);
		h.setBottom(1, 5,false);
		Cell hc = h.getMaxCell(1,2);
		assertEquals("table 3. colSpan", 1,hc.colSpan);
		assertEquals("table 3. rowSpan", 5,hc.rowSpan);
		

		
	}
}
