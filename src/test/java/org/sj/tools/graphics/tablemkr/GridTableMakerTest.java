/*
 * Apache License
 *
 * Copyright (c) 2021 andrescg2sj
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
import org.sj.tools.graphics.tablemkr.GridBorders;
import org.sj.tools.graphics.tablemkr.GridTableMaker;
import org.sj.tools.graphics.tablemkr.Table;

public class GridTableMakerTest {

	@Test
	public void testingMakeFromGrid() {
		GridBorders g = new GridBorders(2,2);
		g.setAll();
		
		Table t = GridTableMaker.fromGrid(g);
		assertEquals("full table. colSpan", 1,t.getCell(0, 0).colSpan);
		assertEquals("full table. rowSpan", 1,t.getCell(0, 0).rowSpan);
		
		g.setRight(0,0,false);
		assertEquals("right", false,g.getRight(0, 0));

		t = GridTableMaker.fromGrid(g);
		assertEquals("table. colSpan", 2,t.getCell(0, 0).colSpan);
		assertEquals("table. rowSpan", 1,t.getCell(0, 0).rowSpan);
		
		g.setAll();
		g.setBottom(1, 0, false);
		t = GridTableMaker.fromGrid(g);
		assertEquals("table 3. colSpan", 1,t.getCell(1, 0).colSpan);
		assertEquals("table 3. rowSpan", 2,t.getCell(1, 0).rowSpan);
		
		g = new GridBorders(3,3);
		g.setAll();
		//g.setBottom(0, 2, false);
		g.setBottom(2, 1, false);
		//g.log();
		t = GridTableMaker.fromGrid(g);
		assertEquals("table 4. colSpan", 1,t.getCell(2, 1).colSpan);
		assertEquals("table 4. rowSpan", 2,t.getCell(2, 1).rowSpan);
		
	}
}
