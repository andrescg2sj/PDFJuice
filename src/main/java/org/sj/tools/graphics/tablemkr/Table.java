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

import java.util.Collections;
import java.util.Vector;
import java.util.logging.Logger;

import org.sj.tools.pdfjuice.CommonInfo;
import org.sj.tools.graphics.sectorizer.GraphicString;

public class Table implements CommonInfo {

	private static Logger log = Logger.getLogger("org.sj.tools.graphics.tablemkr.Table");
	//Vector<Area> areas;
	Cell cells[][];
	
	public Table(Cell cont[][]) {
		cells = cont;
	}
	
	public Table(int cols, int rows) {
		cells = new Cell[cols][rows];
		init();
	}
	
	public static Table emptyTable(int cols, int rows) {
		Cell c[][] = new Cell[cols][rows];
		return new Table(c);
	}
	
	
	
	public void init() {
		for(int r=0;r<getRows();r++) {
			for(int c=0;c<getCols();c++) {
				cells[c][r] = new Cell(1,1);
			}
		}
	}
	
	
	
	
	public Table(Table t) {
		//TODO: clone?
		cells = t.cells;
	}
	
	public void add(int col, int row, String str) {
		if(cells[col][row] == null) {
			Cell c = new Cell(1,1);
			//TODO: remove GraphicStrings ...
			c.add(str);
			cells[col][row] = c;
			
		} else if(cells[col][row] instanceof HiddenCell) {
			//TODO
		} else {
			cells[col][row].add(str);

		}
	}
	
	static int matrixNumRows(Cell data[][]) {
		return data[0].length;
	}
	
	static int matrixNumCols(Cell data[][]) {
		return data.length;
	}

	
	
	public Cell get(int col, int row)
	{
		return cells[col][row];
	}
	
	public CellLocation getVisibleCellLoc(int col, int row) {
		if(cells[col][row] instanceof HiddenCell) {
			HiddenCell hc = (HiddenCell) cells[col][row];
			return hc.hiddenBy;
		} 
		return new CellLocation(col, row, cells[col][row]);
	}
	
	public Cell getVisibleCell(int col, int row) {
		if(cells[col][row] instanceof HiddenCell) {
			HiddenCell hc = (HiddenCell) cells[col][row];
			return hc.hiddenBy.cell;
		} 
		return cells[col][row];
	}

	
	public Vector<String> getContents(int col, int row) {
		return getVisibleCell(col,row).contents;
	}
	
	//TODO: Test
	public Table subTable(int col, int row, int numCols, int numRows) {
		return new Table(submatrix(col, row, numCols, numRows));
	}
	
	@Deprecated
	public Table subTable_old(int col, int row, int numCols, int numRows) {
		Cell newCells[][] = new Cell[numCols][numRows];
		
		for(int r=0;r<numRows;r++) {
			for(int c=0;c<numCols;c++) {
				//FIXME
				Cell model = cells[c+col][r+row];
				
				
				if(model instanceof HiddenCell) {
					
					HiddenCell hc = (HiddenCell) model;
					if(hc.hiddenBy.col < col || hc.hiddenBy.col < row) {
						hc.hiddenBy.col = Math.max(0, hc.hiddenBy.col-col);
						hc.hiddenBy.row = Math.max(0, hc.hiddenBy.row-row);
						hc.hiddenBy.cell = newCells[hc.hiddenBy.col][hc.hiddenBy.row]; 
					}
				} 
				if((r + model.rowSpan >= getRows()) ||
						(c + model.colSpan >= getCols())) {
					int newCols = Math.min(model.colSpan, numCols - c);
					int newRows = Math.min(model.rowSpan, numRows - r);
					newCells[c][r] = new Cell(newCols, newRows, model.contents);
					
				} else {
					newCells[c][r] = model;
				}
			}
		}
		
		return new Table(newCells);
	}
	
	public Table trim() {
		int col = 0;
		int row = 0;
		while(col < getCols() && isEmptyCol(col)) col++;
		if(col >= getCols()) return null;
		while(row < getRows() && isEmptyRow(row)) row++;
		
		int lastCol = getCols() - 1;
		int lastRow = getRows() - 1;
		while(isEmptyCol(lastCol) && lastCol > col) lastCol--;
		while(isEmptyRow(lastRow) && lastRow > row) lastRow--;
		int numCols = lastCol - col + 1;
		int numRows = lastRow - row + 1;
		return subTable(col, row, numCols, numRows);
	}
	
	public int countEmptyRows() {
		int count = 0;
		for(int r=0; r<getRows();r++) {
			if(isEmptyRow(r)) count++;
		}
		return count;
	}
	
	
	public Vector<Table> divideOnEmptyRow() {
		Vector<Table> tables = new Vector<Table>(); 
		int firstRow = -1;
		for(int r=0;r<getRows();r++) {
			if(isEmptyRow(r)) {
				if(firstRow >= 0) {
					Table t = this.subTable(0, firstRow, getCols(), r-firstRow);
					tables.add(t);
					firstRow = -1;
				}
			} else {
				if(firstRow < 0) {
					firstRow = r;
				}
			}
		}
		if(firstRow >= 0) {
			Table t = this.subTable(0, firstRow, getCols(), getRows()-firstRow);
			tables.add(t);
		}
		Collections.reverse(tables);
		return tables;
	}
	
	int getMinColSpan(int col) {
		int min = getCols();
		for(int row =0; row < getRows(); row++) {
			Cell c = cells[col][row];
			if(c == null) {
				return 1;
			} else {
				if(c instanceof HiddenCell) {
					HiddenCell hc = (HiddenCell) c;
					min =  Math.min(hc.hiddenBy.cell.colSpan + hc.hiddenBy.col - col, min);
					if(min == 1) {
						return 1;
					}
				} else if(c.colSpan < min) {
					min = c.colSpan;
				}
			}

		}
		return min;
	}
	
	int getMinRowSpan(int row) {
		int min = getRows();
		for(int col =0; col < getCols(); col++) {
			Cell c = cells[col][row];
			if(c == null) {
				return 1;
			} else {
				if(c instanceof HiddenCell) {
					HiddenCell hc = (HiddenCell) c;
					min =  Math.min(hc.hiddenBy.cell.rowSpan + hc.hiddenBy.row - row, min);
					if(min == 1) {
						return 1;
					}
				} else if(c.rowSpan < min) {
					min = c.rowSpan;
				}
			}

		}
		return min;
	}

	public void spanCell(int col, int row, int colSpan, int rowSpan) {
		Cell c = cells[col][row];
		c.colSpan = colSpan;
		c.rowSpan = rowSpan;
		fillCells(c, col, row);
	}
	
	/**
	 * 
	 * @param col index of the column which is going to be simplified.
	 * @return number of deleted columns.
	 */
	private int simplifyCol(int col) {
		int minColSpan = getMinColSpan(col);

		if(minColSpan > 1) {
			int decrement = minColSpan - 1;
			for(int row =0; row < getRows(); row++) {
				Cell cell = cells[col][row];
				if(cell == null) {
					//throw new NullPointerException("at row: "+ row);
					log.warning("Null row:"+row);
				} else if(!(cell instanceof HiddenCell)) {
					/* get hidden contents in this cell */
					for(int c=col+1; c<(col+minColSpan);c++) {
						if(cells[c][row] != null) {
							cells[col][row].contents.addAll(cells[c][row].contents);
						}
					}
					cell.colSpan -= decrement;
				} 
			}
			return decrement;
		} else {
			return 0;
		}
	}
	
	
	/**
	 * 
	 * @param col
	 * @return number of deleted columns.
	 */
	private int simplifyRow(int row) {
		int minRowSpan = getMinRowSpan(row);

		if(minRowSpan > 1) {
			int decrement = minRowSpan - 1;
			for(int col =0; col < getCols(); col++) {
				Cell c = cells[col][row];
				if(c == null) {
					//throw new NullPointerException("at col:" +c);
					log.warning("Null col:"+col);
					
				} else if(!(c instanceof HiddenCell)) {
					c.rowSpan -= decrement;
				}
			}
			return decrement;
		} else {
			return 0;
		}
	}


	
	void moveDataBack(int col, int row, int backCols, int backRows) {
		if(backCols == 0 && backRows == 0)
			return;
		int numCols = this.getCols() - col;
		int numRows = this.getRows() - row;
		Cell data[][] = null;
		try {
			data = submatrix(col, row, numCols, numRows);
		//Table debug = new Table(data);
		//System.out.println(debug.toHTML());
			copy2d(data,0,0,cells, col-backCols, row-backRows, numCols, numRows);
		} catch(ArrayIndexOutOfBoundsException e) {
			System.err.println(String.format("col=%d row=%d backCols=%d backRows=%d",
					col,row, backCols,backRows));
			if(data != null) {
				System.err.println(String.format("data.cols=%d data.rows=%d",
					matrixNumCols(data),matrixNumRows(data)));
			}
			e.printStackTrace();
		}
	}
	
	@Deprecated
	void moveDataBack_old(int col, int row, int backCols, int backRows) {
		if(backCols == 0 && backRows == 0)
			return;
		//System.out.println("  move:"+col+","+row);

		int numRows = getRows();
		int numCols = getCols();
		for(int c = col; c < numCols; c++) {
			for(int r = row; r < numRows; r++) {
				cells[c-backCols][r-backRows]  = cells[c][r];
				if(cells[c][r] instanceof HiddenCell) {
					HiddenCell hc = (HiddenCell) cells[c][r];
					if(hc.hiddenBy.col >= col && hc.hiddenBy.row >= row) {
						hc.hiddenBy.col -= backCols;
						hc.hiddenBy.row -= backRows;
						hc.hiddenBy.cell = cells[hc.hiddenBy.col][hc.hiddenBy.row];
					} else if (hc.hiddenBy.col> (col-backCols) && hc.hiddenBy.row > (row-backRows)) {
						hc.hiddenBy = null;
					}
 				}
			}
		}
		for(int c = numCols-backCols; c < numCols; c++) {
			for(int r=row; r < numRows; r++) {
				cells[c][r] = null;
			}
		}
		for(int r = numRows-backRows; r < numRows; r++) {
			for(int c=col; c < numCols; c++) {
				cells[c][r] = null;
			}
		}
		
	}
	
	static void copy2d(Cell src[][], int srcCol, int srcRow, 
			Cell dst[][], int dstCol, int dstRow, int numCols, int numRows) {

		for(int r=0;r<numRows;r++) {
			for(int c=0;c<numCols;c++) {
				//FIXME
				Cell model = src[c+srcCol][r+srcRow];
				
				
				if(model instanceof HiddenCell) {
					
					HiddenCell hc = (HiddenCell) model;
					if(hc.hiddenBy.col < srcCol || hc.hiddenBy.col < srcRow) {
						hc.hiddenBy.col = Math.max(0, hc.hiddenBy.col-srcCol+dstCol);
						hc.hiddenBy.row = Math.max(0, hc.hiddenBy.row-srcRow+dstRow);
						hc.hiddenBy.cell = dst[hc.hiddenBy.col][hc.hiddenBy.row]; 
					}
				} 
				int dstNumRows = matrixNumRows(dst);
				int dstNumCols = matrixNumCols(dst);
				if((r + model.rowSpan >= dstNumRows) ||
						(c + model.colSpan >= dstNumCols)) {
					int newCols = Math.min(model.colSpan, numCols - c);
					int newRows = Math.min(model.rowSpan, numRows - r);
					dst[c+dstCol][r+dstRow] = new Cell(newCols, newRows, model.contents);
					
				} else {
					dst[c+dstCol][r+dstRow] = model;
				}
			}
		}
		
	}

	
	Cell[][] submatrix(int col, int row, int numCols, int numRows) {
		Cell newCells[][] = new Cell[numCols][numRows];
		copy2d(cells, col, row, newCells, 0,0, numCols, numRows);
		return newCells;
	}
	

	public static Table parseTable(String str) {
		String lines[] = str.split(";");
		String parts[] = lines[0].split(",");
		int cols = parts.length;
		int rows = lines.length;
		Table t = new Table(cols,rows);
		
		for(int j=0; j< rows;j++) {
			String cells[] = lines[j].split(",");
			for(int i=0;i<cells.length; i++) {
				t.add(i, j, cells[i]);
			}
			
		}
		return t;
	}

	
	/**
	 * 
	 */
	public void simplifyTable() {
		int col = 0;
		log.finest("simplify Table");

		int deletedCols = 0;
		int numCols = getCols();
		//System.out.println("numCols:" + numCols);
		while(col < (numCols-deletedCols)) {
			try {
				System.out.println("simplify col:" + col);
				int cols = simplifyCol(col);
				deletedCols+=cols;
				System.out.println("move data:" + cols);
				moveDataBack(col+cols+1,0, cols,0);
			} catch(NullPointerException ne) {
				System.out.println(String.format("Malformed table(col=%d): %s",col,ne));
			}
			col++;
		}
		//System.out.println("  deleted cols="+deletedCols);
		int row = 0;
		int numRows = getRows();
		int deletedRows = 0;
		//System.out.println("Simplify rows");
		while(row < (numRows-deletedRows)) {
			try { 
				int rows = simplifyRow(row);
				//System.out.println("  delete rows:"+rows);
				deletedRows += rows;
				//System.out.println("  sum="+deletedRows);

				moveDataBack(0,row+rows+1, 0, rows);
			} catch(NullPointerException ne) {
				System.out.println(String.format("Malformed table(row =%d): %s",row,ne));
			}
			row++;
		}

		if(deletedCols > 0 || deletedRows > 0) {
			//cells = subTable(0,0,numCols-deletedCols, numRows-deletedRows).cells;
			cells = submatrix(0,0,numCols-deletedCols, numRows-deletedRows);
		}
	}
	
	public int countEmptyCols() {
		int count = 0;
		for(int c=0; c<getCols();c++) {
			if(isEmptyCol(c)) count++;
		}
		return count;
	}
	
	public boolean isEmpty() {
		for(int c=0;c<getCols();c++) {
			if(!isEmptyCol(c)) return false;
		}
		return true;
	}

	
	public boolean isEmptyRow(int row) {
		for(int c=0;c<getCols();c++) {
			if(cells[c][row] != null && !cells[c][row].isEmpty()) return false;
		}
		return true;
	}
	
	public String row_log(int row) {
		StringBuilder sb = new StringBuilder();
		for(int c=0;c<getCols();c++) {
		
			//if(!cells[c][row].isEmpty()) return false;
			sb.append(String.format(" %d", cells[c][row].contents.size()));
		}
		return sb.toString();
	}

	public boolean isEmptyCol(int col) {
		for(int r=0;r<getRows();r++) {
			if(cells[col][r] != null && !cells[col][r].isEmpty()) return false;
		}
		return true;
	}

	
	public void fillCells(Cell cell, int col, int row)
	{
		HiddenCell hidden = new HiddenCell(col, row, cell);
		for(int r=0;r<cell.rowSpan;r++) {
			for(int c=0;c<cell.colSpan;c++) {
				if(r == 0 && c == 0) {
					cells[c+col][r+row] = cell;
				} else {
					cells[c+col][r+row] = hidden;  
				}
			}
		}
	}
	
	
	
	public int getRows() {
		return matrixNumRows(cells);
	}
	
	public int getCols() {
		return matrixNumCols(cells);
	}
	
	public Cell getCell(int col, int row) {
		return cells[col][row];
	}
	

	public String toHTML() {
		String data = "<table style=\"\">";
		for(int row=0; row<getRows();row++) {
			data += "<tr>" ;
			for(int col=0; col<getCols();col++) {
				Cell cell = cells[col][row];
				if(cell != null && !(cell instanceof HiddenCell)) {
					String opentag = "<td";
					if(cell.colSpan > 1) opentag+=String.format(" colspan=\"%d\"", cell.colSpan);
					if(cell.rowSpan > 1) opentag+=String.format(" rowspan=\"%d\"", cell.rowSpan);
					opentag +=">";
					data += opentag + cell.fullText() +"</td>";
				}
			}
			data += "</tr>" + NEW_LINE;
		}
		return data + "</table>";
		//throw new UnsupportedOperationException("Table.toHTML");
	}
}
