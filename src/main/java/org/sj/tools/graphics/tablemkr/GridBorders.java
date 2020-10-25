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

public class Grid {
	
	CellLimits grid[][];
	
	CellLimits[][] createMatrix(int cols, int rows) {
		CellLimits lim[][] = new CellLimits[cols][rows];
    	for(int r=0; r<rows;r++) {
    		for(int c=0; c<cols;c++) {
    			lim[c][r] = new CellLimits();
    		}
    	}
		return lim;
	}
	
	public void setAll() {
    	for(int r=0; r<numRows();r++) {
    		for(int c=0; c<numCols();c++) {
    			grid[c][r].setBottom();
    			grid[c][r].setRight();
    		}
    	}
		
	}
	
	public void log()
	{
		for(int r=0;r<numRows();r++) {
			for(int c=0; c<numCols();c++) {
				System.out.print(grid[c][r]);
			}
			System.out.println();
			
		}
	}
	
	public int numRows() {
		return grid[0].length;
	}
	
	public int numCols() {
		return grid.length;
	}
	
	//FIXME: use consistent cols/rows order.
	public Grid(int cols, int rows) {
		grid = createMatrix(cols, rows);
	}

	public void setLeft(int col, int row) {
		setLeft(col, row, true);
	}

	public void setLeft(int col, int row, boolean value) {
		if(col > 0) setRight(col-1, row, value);
	}

	public void setTop(int col, int row) {
		setTop(col, row, true);
	}

	
	public void setTop(int col, int row, boolean value) {
		if(row > 0) setBottom(col, row-1, value);
	}
	
	public void setBottom(int col, int row) {
		grid[col][row].setBottom();
	}

	public void setBottom(int col, int row, boolean value) {
		grid[col][row].bottom = value;
	}
	

	public void setRight(int col, int row) {
		grid[col][row].setRight();
	}
	
	public void setRight(int col, int row, boolean value) {
		grid[col][row].right = value;
	}
	
	public boolean getRight(int col, int row) {
		return grid[col][row].right;
	}

	public boolean getBottom(int col, int row) {
		return grid[col][row].bottom;
	}

	/*
	Cell getMaxCell(int col, int row) {
		return getMaxCell(grid, col, row);
	}*/
	
	/*TODO: This static method doesn't look like a good design. 
	 * Maybe another class that encapsulates CellLimits matrix operations is desirable.
	 */
	public Cell getMaxCell(int col, int row) {
		int c=col;
		int r=row;
		int colSpan,rowSpan;
		int lastCol;
		boolean lineFound = false;
		boolean vertLine = false;
		//System.out.println("getMaxCell");
		
		while(c < numCols() && !vertLine) {
			lineFound |= grid[c][row].bottom;
			vertLine |= grid[c][row].right;
			c++;
		}
		lastCol = c;
		colSpan = c-col;
		r++;
		//System.out.println(" lastCol="+lastCol);
		//System.out.println(" lineFound="+lineFound);
		//System.out.println(" vertF="+lastCol);
		//System.out.println(" numRows()="+numRows());

		while(r < numRows() && !lineFound) {
			lineFound = false;
			vertLine = false;
			c = col;			
			//System.out.println(" r="+r);
			while(c < lastCol  && !vertLine) {
				lineFound |= grid[c][r].bottom;
				vertLine |= grid[c][r].right;
				//System.out.println("  " + lineFound + ", "+vertLine);
				c++;
			}
			//System.out.println(" c="+c);
			if(c < lastCol) lineFound = true;
			r++;
		}

		rowSpan = r-row;
		return new Cell(colSpan,rowSpan);

	}

}
