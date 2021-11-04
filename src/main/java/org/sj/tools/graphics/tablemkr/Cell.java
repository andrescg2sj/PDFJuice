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

import java.util.Collection;
import java.util.Vector;

import org.sj.tools.graphics.sectorizer.GraphicString;

public class Cell {
	

	int colSpan = 1;
	int rowSpan = 1;
	
	//TODO: What class?
	Vector<String> contents;
	//String content;
	
	public Cell(int hspan, int vspan) {
		colSpan = hspan;
		rowSpan = vspan;
		contents = new Vector<String>();
	}

	Cell(int hspan, int vspan, Vector<String> data) {
		colSpan = hspan;
		rowSpan = vspan;
		contents = data;
	}

	public void add(String gstr) {
		contents.add(gstr);
	}
	
    void addAll(Collection<String> cont)
    {
    	/*
    	if(this.contains(gstr.getBounds()))
    		content.add(gstr);
    		*/
    	if(cont == null) {
    		System.err.println("Warning! Trying to add null content");
    	} else {
    		contents.addAll(cont);
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
    	for(String s : contents) {
    		t += s;
    	}
    	return t;
    }

	
	public String getString(int i) {
		if(i>=contents.size()) throw new ArrayIndexOutOfBoundsException("getString: i="+i);
		String str = contents.get(i);
		if(str == null)
			throw new NullPointerException("null text. i="+i);
		return contents.get(i);
	}
	
	public String toString() {
		return String.format("Cell{size:%d,%d}", colSpan, rowSpan);
	}
	
	public int getColSpan() {
		return colSpan;
	}
	
	public int getRowSpan() {
		return rowSpan;
	}
	
}
