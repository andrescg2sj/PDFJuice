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


import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.sj.tools.graphics.sectorizer.ContentRegion;
import org.sj.tools.graphics.sectorizer.GraphicString;
import org.sj.tools.graphics.sectorizer.geom.NumberVector;
import org.sj.tools.graphics.tablemkr.graphtrace.SvgTrace;

import java.util.logging.Logger;
import java.util.logging.Level;

public abstract class TableMaker
{
	private static Logger log = Logger.getLogger("org.sj.tools.graphics.tablemkr.TableMaker");
	
    float collisionThreshold = 2;
    
    Frame frame;
    
    Vector<TLine> lines;
    Vector<GraphicString> gstrings;

    /* debug */
    SvgTrace tracer = new SvgTrace();

    public TableMaker() {
    	lines = new Vector<TLine>();
    	gstrings = new Vector<GraphicString>();
       
    }

    public abstract Table makeTable();
    
    Frame buildFrame() {
    	NumberVector x = new NumberVector();
    	NumberVector y = new NumberVector();
    	for(TLine l: lines) {
    		if(l.isHoriz()) {
    			log.fine(l.toString() + " is horziontal.");
    			y.insert(l.getA().getY());
    			
    		} else {
    			log.fine(l.toString() + " is vertical.");
    			x.insert(l.getA().getX());
    		}
    	}
    	return new Frame(x, y);
    }
    
    @Deprecated
    Frame _old_buildFrame() {
    	/* convenience array that stores all marks */
    	int i=0;
    	int j = lines.size()-1;
    	int allMarks[] = new int[lines.size()];
    	for(TLine l: lines) {
    		if(l.isHoriz()) {
    			log.fine(l.toString() + " is horziontal.");
    			allMarks[i++] = (int) l.getA().getY();
    			
    		} else {
    			log.fine(l.toString() + " is vertical.");
    			allMarks[j--] = (int) l.getA().getX();
    		}
    	}
    	//int x[] = new int[i];
    	//int y[] = new int[allMarks.length - i];
    	log.fine(String.format("lines.size=%d, i=%d, j=%d", lines.size(), i,j));
    	log.fine(String.format("marks.length=%d", allMarks.length));
    	int x[] = Arrays.copyOfRange(allMarks, i, allMarks.length);
    	if(x.length != j) {
    		log.warning("x.length="+x.length+" expected:"+j);
    	}
    	int y[] = Arrays.copyOf(allMarks, i);
    	Arrays.sort(x);
    	Arrays.sort(y);
    	x = reduce(x);
    	y = reduce(y);
    	
    	return new Frame(x,y);
    }
    
    public static int[] reduce(int values[]) {
    	if(values.length == 0) {
    		return values;
    	}
    	int elements = 1;
    	int last = values[0];
    	for(int i=1; i< values.length;i++) {
    		if(values[i] != last) {
    			last = values[i];
    			elements ++;
    		}
    	}
    	
    	int newValues[] = new int[elements];
    	int j = 0;
    	//last = values[0];
    	newValues[0] = values[0];
    	for(int i=1; i< values.length;i++) {
    		if(values[i] != newValues[j]) {
    			j++;
    			newValues[j]= values[i];
    		}
    	}

    	return newValues;
    }
    
    public void add(TLine line) {
    	lines.add(line);
    }
    
    public void add(GraphicString gstr) {
    	gstrings.add(gstr);
    }
    
    boolean addStringToBestMatch(Vector<Area> areas, GraphicString gstr) {
    	Area selected = null;
    	double max = 0;
		for(Area a: areas) {
			double shared = ContentRegion.sharedArea(a.getBounds(), gstr.getBounds());  
			if(shared > max) {
				max = shared;
				selected = a;
			}
		}
		if(selected == null) {
			return false;
		}
		selected.pushContent(gstr);
		return true;
    }

    boolean addStringToFirstMatch(Vector<Area> areas, GraphicString gstr) {
		for(Area a: areas) {
			if(a.containsMost(gstr.getBounds())) {
				a.pushContent(gstr);
				return true;
			}
		}
    	return false;
    }

    
    boolean addStringToOneArea(Vector<Area> areas, GraphicString gstr) {
		for(Area a: areas) {
			if(a.contains(gstr.getBounds())) {
				a.addContent(gstr);
				return true;
			}
		}
    	//return addStringToBestMatch(areas,gstr);
    	return false;
    }
    
    void addStringsToAreas(Vector<Area> areas) {
    	int count = 0;
    	for(GraphicString gstr: this.gstrings)
    	{
    		if(addStringToOneArea(areas, gstr)) {
    			count++;
    		} else {
    			log.info("  failed 1st round: "+ gstr.getText());
    			
    			if(addStringToBestMatch(areas,gstr)) {
    			//if(addStringToFirstMatch(areas,gstr)) {
    				log.info("placed in 2nd round");
    			} else {
    				log.info("Discarded");
    			}
    		}
    	}
    	log.info("added "+count+ " strings from "+gstrings.size());
    }
    
    /**
     * 
     * For debug purposes.
     * 
     * @param areas
     */
    public void toSVG(Vector<Area> areas) {
    	log.finest("Exporting areas.");
    	if(log.getLevel() == Level.FINEST) { 
    		tracer.exportAreasAndGStrings(areas, gstrings);
    	}
    	log.finest("   GStrings: "+gstrings.size());
	//	tracer.exportAreasAndText(areas,);
    }


    public void addAreasToMatrix(Vector<Area> areas, Cell table[][])
    {
    	for(Area a: areas) {
			log.fine("Build location: " + a.toString());
    		CellLocation clo = frame.areaToCellLoc(a, this.collisionThreshold);
    		if(clo == null)
    			throw new NullPointerException("Frame created a null CellLocation");
    		try {
				log.fine("Testing: c:" +clo.col+", r:"+clo.row);
    			if(table[clo.col][clo.row] != null) {
    				System.err.println("Warning! repeated.");
    			} else {
    				log.fine("cell: "+clo.toString());
    				log.fine("   indices: c:"+clo.col+ ", r:"+clo.row);
    				table[clo.col][clo.row] = clo.cell;

    				/*fillSpan(loctable, clo, clo.col, clo.row,
    						clo.cell.horizSpan, clo.cell.vertSpan);*/
    				
    			}
    		} catch(Exception ie) {
    			ie.printStackTrace();
    		}
    	}

    }

    
    



}
