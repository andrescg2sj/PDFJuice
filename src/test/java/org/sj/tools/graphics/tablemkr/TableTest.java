/*
 * Apache License
 *
 * Copyright (c) 2019 andrescg2sj
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
import static org.junit.Assert.assertTrue;

import java.util.Vector;
import org.junit.Test;
import org.sj.tools.graphics.tablemkr.Cell;
import org.sj.tools.graphics.tablemkr.HiddenCell;
import org.sj.tools.graphics.tablemkr.Table;
import org.junit.Assert;

public class TableTest {
	
	@Test
	public void testingFillCells()
	{
		Table table = new Table(3,3);
		
		Cell cell = new Cell(2,2);
		
		table.fillCells(cell, 0, 0);
		
		assertEquals("Corner ", cell, table.get(0, 0));
		assertTrue("0,1 ", table.get(0, 1) instanceof HiddenCell);
		assertTrue("1,1 ", table.get(1, 1) instanceof HiddenCell);
		assertTrue("1,0 ", table.get(1, 0) instanceof HiddenCell);

	}
	
	@Test
	public void testingAdd() 
	{
		Table t = new Table(4,4);
		t.add(1, 1, "a");
		Assert.assertEquals("a", t.get(1, 1).fullText());
	}
	
	@Test
	public void testingSubTable() 
	{
		Table t = new Table(4,4);
		t.add(1, 1, "a");
		
		Table u = t.subTable(1, 1, 2, 2);
		Assert.assertEquals("cols", 2, u.getCols());
		Assert.assertEquals("rowss", 2, u.getRows());
		Assert.assertEquals("content 0,0", "a", u.getCell(0, 0).fullText());
	}
	
	@Test
	public void testingDivide() {
		Table t = new Table(2,3);
		t.add(0, 0, "a");
		t.add(1, 0, "b");
		t.add(0, 2, "c");
		t.add(1, 2, "d");
		
		Vector<Table> tables = t.divideOnEmptyRow();
		Assert.assertEquals("number of tables", 2,tables.size());
		Assert.assertEquals("A. getCols", 2,tables.get(0).getCols());
		Assert.assertEquals("A. getRows", 1,tables.get(0).getRows());
		Assert.assertEquals("B. getCols", 2,tables.get(1).getCols());
		Assert.assertEquals("B. getRows", 1,tables.get(1).getRows());
		
	}
	
	@Test
	public void testingTableTrim() 
	{
		Table t = new Table(4,4);
		t.add(1, 1, "a");
		t.add(1, 2, "b");
		t.add(2, 1, "c");
		t.add(2, 2, "d");
		
		Table u = t.trim();
		Assert.assertEquals("cols", 2, u.getCols());
		Assert.assertEquals("rowss", 2, u.getRows());
		Assert.assertEquals("content 0,0", "a", u.getCell(0, 0).fullText());
	}
	
	@Test
	public void testingMinColSpan() 
	{
		Table t = new Table(3,2);
		t.add(0,0,"a");
		t.add(0,1,"b");
		t.add(2,0,"c");
		t.add(2,1,"d");
		
		t.spanCell(0, 0, 2, 1);
		t.spanCell(0, 1, 2, 1);
		
		Assert.assertEquals("min col span:",2, t.getMinColSpan(0));
	}

	@Test
	public void testingSimplifyTable() 
	{
		Table t = new Table(3,2);
		t.add(0,0,"a");
		t.add(0,1,"b");
		t.add(2,0,"c");
		t.add(2,1,"d");
		
		t.spanCell(0, 0, 2, 1);
		t.spanCell(0, 1, 2, 1);
		
		t.simplifyTable();
		
		Assert.assertEquals("table columns:",2, t.getCols());
		Assert.assertEquals("table rows:",2, t.getRows());
		Assert.assertEquals("table content 0,0:","a", t.get(0, 0).fullText());
		Assert.assertEquals("table 0,0 colSpan:",1, t.get(0, 0).colSpan);
		Assert.assertEquals("table content 1,1:","c", t.get(1, 0).fullText());
		Assert.assertEquals("table content 1,1:","d", t.get(1, 1).fullText());
	}

}
