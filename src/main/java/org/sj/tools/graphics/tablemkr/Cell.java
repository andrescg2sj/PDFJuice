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

import java.util.Vector;

import org.sj.tools.graphics.sectorizer.GraphicString;

public class Cell {
	

	int colSpan = 1;
	int rowSpan = 1;
	
	//TODO: What class?
	Vector<GraphicString> contents;
	//String content;
	
	public Cell(int hspan, int vspan) {
		colSpan = hspan;
		rowSpan = vspan;
		contents = new Vector<GraphicString>();
	}

	Cell(int hspan, int vspan, Vector<GraphicString> data) {
		colSpan = hspan;
		rowSpan = vspan;
		contents = data;
	}

	public void add(GraphicString gstr) {
		contents.add(gstr);
	}
	
    void addAll(Vector<GraphicString> cont)
    {
    	/*
    	if(this.contains(gstr.getBounds()))
    		content.add(gstr);
    		*/
    	if(cont == null) {
    		System.err.println("Warning! Trying to add null content");
    	} else {
    		contents = cont;
    	}
    }
    
    public boolean isEmpty() {
    	return contents.size() == 0 || fullText().trim().equals("");
    }
    
    public int numStrings() {
    	return contents.size();
    }
    
    public String fullText() {
    	String t = "";
    	for(GraphicString gs : contents) {
    		t += gs.getText();
    	}
    	return t;
    }

	
	public String getString(int i) {
		if(i>=contents.size()) throw new ArrayIndexOutOfBoundsException("getString: i="+i);
		GraphicString gstr = contents.get(i);
		if(gstr.getText() == null)
			throw new NullPointerException("GStr has null text. i="+i);
		return contents.get(i).getText();
	}
	
	public String toString() {
		return String.format("Cell{size:%d,%d}", colSpan, rowSpan);
	}
	
}
