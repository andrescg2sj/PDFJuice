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

import org.sj.tools.graphics.sectorizer.GraphicString;

public class CellLocation {
	
	int row = -1;
	int col = -1;
	
	Cell cell;

	public CellLocation(int c, int r, int hspan, int vspan) {
		row = r;
		col = c;
		cell = new Cell(hspan, vspan);
	}

	public CellLocation(int c, int r, Cell referenced) {
		row = r;
		col = c;
		cell = referenced;
	}

	
	public String toString() {
		return String.format("cell(%d,%d,{%s})", col, row, cell.toString());
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void add(String str)
	{
		cell.add(str);
	}
	
	public boolean contains(CellLocation cloc) 
	{
		return ((cloc.getRow() >= this.getRow()) && (cloc.getCol() >= this.getCol()) &&
			((cloc.getRow()+cloc.cell.rowSpan) <= (this.getRow()+this.cell.rowSpan))  && 
			((cloc.getCol()+cloc.cell.colSpan) <= (this.getCol()+this.cell.colSpan)) );
	}
	
	
}
