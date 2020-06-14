package org.sj.tools.graphics.tablemkr;

public class HiddenCell extends Cell {

	CellLocation hiddenBy; 
	
	public HiddenCell(int col, int row, Cell above) {
		super(0,0);
		hiddenBy = new CellLocation(col, row, above);
	}

}
