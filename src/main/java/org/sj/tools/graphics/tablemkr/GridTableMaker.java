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

import java.util.Vector;
import java.util.logging.Logger;

import org.sj.tools.graphics.sectorizer.GraphicString;
import org.sj.tools.graphics.sectorizer.ReverseYComparator;

//TODO: decide a consistent order for column and row parameters
public class GridTableMaker extends TableMaker {
	
	private static Logger log = Logger.getLogger("org.sj.tools.graphics.tablemkr.GridTableMaker");
	
	GridBorders grid;

	public GridTableMaker()
	{
		
	}
	
	public GridTableMaker(GridBorders g)
	{
		grid = g;
	}
	
	

	//Thi method is now in Table
	@Deprecated
	static void fillCells(Cell matrix[][], Cell cell, int col, int row)
	{
		HiddenCell hidden = new HiddenCell(col, row, cell);
		for(int r=0;r<cell.rowSpan;r++) {
			for(int c=0;c<cell.colSpan;c++) {
				if(r == 0 && c == 0) {
					matrix[c+col][r+row] = cell;
				} else {
					matrix[c+col][r+row] = hidden;  
				}
			}
		}
	}
	
	public static Table fromGrid(GridBorders g) {
		//System.out.println(" --- ");
		//g.log();
		
		GridTableMaker maker = new GridTableMaker(g);
		//return maker.makeFromGrid();
		//return new Table(maker.cellMatrix());
		//Table table = new Table(g.numCols(), g.numRows());
		Table table = Table.emptyTable(g.numCols(), g.numRows());
		maker.fillTable(table);
		return table;
	}
	
	public void fillTable(Table t) {
    	for(int r=0; r<grid.numRows();r++) {
    		for(int c=0; c<grid.numCols();c++) {
    			if(t.get(c, r) == null) {
    				Cell cell = grid.getMaxCell(c,r);
    				//System.out.println(String.format("  fill:%d,%d - %s", c,r, cell.toString()));
    				t.fillCells(cell, c,r);
    			}
    		}
    	}
	}
	
	@Deprecated
	public Cell[][] cellMatrix() {
		System.out.println("cellMatrix");
    	Cell table[][] = new Cell[grid.numCols()][grid.numRows()];
    	for(int r=0; r<grid.numRows();r++) {
    		for(int c=0; c<grid.numCols();c++) {
    			if(table[c][r] == null) {
    				Cell cell = grid.getMaxCell(c,r);
    				fillCells(table, cell, c,r);
    				System.out.println("  ");
    			}
    		}
    	}
    	return table;
	}
	
	/** 
	 * Make the table, once the grid is built.
	 * @return
	 */
	Table makeFromGrid() {
    	//Cell table[][] = cellMatrix();
		//Table table = new Table(grid.numCols(), grid.numRows());
		Table table = Table.emptyTable(grid.numCols(), grid.numRows());
		fillTable(table);
    	
    	
    	Vector<Area> areas = buildAreas(table.cells);
    	//System.out.println()
   		log.fine("areas:" + areas.size());
    	addStringsToAreas(areas);

    	// debug
    	toSVG(areas);

    	//FIXME: encapsulate? superclass method?
       	for(Area a: areas) {
			//System.out.println("Build location: " + a.toString());
       		log.finer("area:");
       		for(GraphicString gs: a.content) {
       			log.finer("  gs:" + gs.getText());
       		}
       			
			a.sort(ReverseYComparator.getInstance());
    		CellLocation clo = frame.areaToCellLoc(a, this.collisionThreshold);
    		if(clo == null)
    			throw new NullPointerException("Frame created a null CellLocation");
    		try {
				//System.out.println("Testing: c:" + clo.col+", r:"+clo.row);
    			CellLocation mainLoc = table.getVisibleCellLoc(clo.col, clo.row);
    			
    			if(mainLoc.cell != null) {
    				//table.cells[clo.col][clo.row].contents = clo.cell.contents;
    				//cell.contents.addAll(cell.contents);
    				mainLoc.cell.contents.addAll(clo.cell.contents);
    				log.finer(String.format("put: col=%d,row%d, '%s'",clo.col, clo.row, table.get(clo.col, clo.row).fullText()));
    				log.finer(String.format("  loc: col=%d,row%d",mainLoc.col, mainLoc.row));
    			} else {
    				// TODO
    				log.warning(String.format("Trying to fill empty cell: col=%d,row%d",clo.col, clo.row));
    			}
    		} catch(Exception ie) {
    			ie.printStackTrace();
    		} //catch(ArrayIndexOutOfBounds)
    	}
    	

       	
    	
		return new Table(table);
	}
	
	/*public Table makeFromGrid() {
		return makeFromGrid(grid);
	}*/
	
	public Table makeTable() {
		log.fine("lines:" + lines.size());
		//make frame from lines
		frame = buildFrame();
		//frame.log();
		
		if(frame.numCols() == 0 || frame.numRows() == 0) {
			return null;
		}
		
		grid = new GridBorders(frame.numCols(), frame.numRows());
		log.info(String.format("Frame cols=%d,rows=%d",frame.numCols(), frame.numRows()));
		
		// volver a recorrer. Rellenar CellBorders
		for(TLine l: lines) {
			CellLocation cloc = frame.lineToLoc(l, collisionThreshold);
			
			if(cloc.getColSpan() == 0 && cloc.getRowSpan() == 0) {
				log.fine("dot line: "+ l.toString());
			}
			//System.out.println("*line: "+l.toString());
			//System.out.println("*location: "+cloc.toString());

			if(l.isHoriz()) {
				for(int i=0; i<=cloc.cell.colSpan-1;i++) {
					log.finest("set top: c:"+(cloc.col+i)+", r:"+cloc.row);
					grid.setTop(cloc.col+i,cloc.row);
				}
			} else {
				for(int i=0; i<=cloc.cell.rowSpan-1;i++) {
					log.finest("set left: c:" +cloc.col+", r:"+(cloc.row+i));
					grid.setLeft(cloc.col,cloc.row+i);
				}
			}
		}
		
		log.info("grid:\n" + grid.logStr());

		return makeFromGrid();
		
	}
	
	public Vector<Area> buildAreas(Cell cells[][]) {
	    //Line lineArray[] = 	lines.toArray(new Line[lines.size()]);
		// return buildAreas(lineArray);
		Vector<Area> areas = new Vector<Area>();
		for(int col=0;col<grid.numCols();col++) {
			for(int row=0;row<grid.numRows();row++) {
				if(cells[col][row] instanceof HiddenCell) {
					//System.out.println(String.format("hidden: col:%d,row:%d",col, row));
				} else {
					Cell cell = cells[col][row];
					//System.out.println(String.format("Cell: col:%d,row:%d",col, row));
					Area a = frame.cellToArea(col, row, cell.colSpan, cell.rowSpan);
					//System.out.println("  built area: "+a);
					areas.add(a);
				}
			}
		}
		return areas;
	}

}
