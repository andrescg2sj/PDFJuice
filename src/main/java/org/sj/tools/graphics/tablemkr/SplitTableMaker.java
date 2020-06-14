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

import java.util.Arrays;
import java.util.Vector;

import org.sj.tools.graphics.sectorizer.GraphicString;
import org.sj.tools.graphics.tablemkr.graphtrace.SvgTrace;

public class SplitTableMaker extends TableMaker {


    public SplitTableMaker() {
    	super();
    }


    private void logLines(TLine lines[]) {
    	System.out.println("lines:" + lines.length);
    	for(TLine l: lines) {
    		System.out.println("   "+l.toString());
    	}
    }
    
    
    public Table makeTable()
    {
    	Vector<Area> areas = buildAreas();
    	return areasToTable(areas);
    }
    
    public Table MakeTable_impl2()
    {
    	
    	return null;
    }
    
    public static void fillSpan(CellLocation ltable[][], CellLocation value,
    		int col, int row, int hspan, int vspan)
    {
    	
    	for(int i=row;i<=(row+hspan); i++) {
        	for(int j=col;j<=(col+vspan); j++) {
        		try {
        			ltable[i][j] = value;
        		} catch(ArrayIndexOutOfBoundsException ie) {
        			System.err.println("Index exception: i="+i+", j="+j);
        			System.err.println(" hspan="+hspan+", vspan="+vspan);
        		}
        	}
    	}
    	
    }
    
    @Deprecated
    static Cell[][] locToCells(CellLocation loct[][])
    {
    	Cell table[][] = new Cell[loct.length][loct[0].length];
    	for(int i=0; i<loct.length;i++) {
    		for(int j=0; j<loct[0].length;j++) {
    			table[i][j] = loct[i][j].cell;
    		}
    	}
    	return table;
    }
    
    
    public Table areasToTable(Vector<Area> areas)
    {
    	frame = buildFrame();
    	
    	int cols = frame.x.length - 1;
    	int rows = frame.y.length - 1;
    	addStringsToAreas(areas);
    	
    	Cell table[][] = new Cell[cols][rows];

    	addAreasToMatrix(areas, table);
    	
    	return new Table(table);
    }
    
    
    
   public Vector<Area> buildAreas() {
    	TLine lineArray[] = 	lines.toArray(new TLine[lines.size()]);
    	return buildAreas(lineArray);
    }

    public void reset() {
    	if(lines.size() > 0)
    		lines = new Vector<TLine>();
    }
    
    
    public Vector<Area> buildAreas(TLine lineArray[]) {
    	//TODO: manage bad tables
    	Area fullArea = new RectArea(Area.getMaximumRect(lineArray));
    	Vector<Area> areas = new Vector<Area>();
    	

    	areas.add(fullArea);

    	Vector<TLine> lines = new Vector<TLine>(Arrays.asList(lineArray));
    	
    	//TODO: Is this algorithm optimal? Is there any redundancy?
    	while(lines.size() > 0) {
    		
    		for(int i=0; i<lines.size(); i++) {
    			int hits = 0;
				TLine l = lines.get(i);
	    	    for(int j=0;j<areas.size(); j++) {
	    	    	Area a = areas.get(j);

    				if(a.collision(l, collisionThreshold)) {
    					Area parts[] = a.split(l);
    					areas.remove(j);
    					areas.addAll(Arrays.asList(parts));
    					hits++;
    				}
    				    				
    			}
    			if(hits == 0)
    				lines.remove(l);
    		}
    	}

    	
    	return areas;
    }


    public Vector<Area> buildAreas_old(TLine lineArray[]) {
    	//TODO: manage bad tables
    	Area fullArea = new RectArea(Area.getMaximumRect(lineArray));

    	Vector<Area> areas = new Vector<Area>();

    	areas.add(fullArea);

    	
    	//TODO: Refactor algorithm. Remove lines that don't collide with any Rectangle. 
    	for(TLine l: lineArray) {
    	    //Vector<Area> newAreas = new Vector<Area>();
    	    
    	    for(int i=0;i<areas.size(); i++) {
	    		Area a = areas.get(i);
	    		if(a.collision(l, collisionThreshold)) {
	    		    Area parts[] = a.split(l);
	
	    		    // this should not happen, if collision does its job.
	    		    if(parts == null)
	    			throw new NullPointerException("Failed to split area");
	
	    		    /* remove area */
	    		    areas.remove(i);
	    		    
	    		    /* add parts */
	    		    for(Area p: parts) {
	    		    	areas.add(p);
	    		    }
	    		}
    	    }
    	    //areas.addAll(newAreas);
    	}
    	return areas;
    }
    
    /**
     * 
     * For debug purposes.
     * 
     * @param areas
     */
    public void logAreas(Vector<Area> areas) {
    	System.out.println("Areas: "+ areas.size());
    	for(org.sj.tools.graphics.tablemkr.Area a: areas) {
    		System.out.println(a.toString());
    	}
    }




    /**
     * Remove bounding lines of 'a' in array
     */
    public TLine[] removeBounds(Area a, TLine lines[]) {
    	Vector<TLine> out = new Vector<TLine>(lines.length - 4);
    	for(TLine l: lines) {
    		if(!a.outOrBound(l))
    			out.add(l);
    	}
    	return out.toArray(new TLine[out.size()]);
    }

    
    
    public static void show(Vector<Area> parts) {
    	for(Area a: parts) {
    		System.out.println(a.toString());
    	}
    }


}
