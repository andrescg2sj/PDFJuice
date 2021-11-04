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
 

package org.sj.tools.graphics.sectorizer;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import static java.util.stream.Collectors.toList;

public class StringRegion extends ContentRegion<GraphicString> {
	
	
	public StringRegion(Rectangle2D r)
	{
		super(r);
	}
	
	public StringRegion(ContentRegion<GraphicString> c) {
		super(c);
		
	}
	
	/*public void add(GraphicString gs) {
		strings.add(gs);
	}*/
	
	//public static String getText(GraphicsString )
	
	public void addAll(Collection<GraphicString> gs) {
		this.contents.addAll(gs);
	}
	
	
	public List<String> getStrings() 
	{
		//This would require Java 1.8
		//return getGraphicStrings().stream().map(GraphicString::getText).collect(toList());

		LinkedList<String> strings = new LinkedList<String>();
		for(GraphicString gs: getGraphicStrings()) {
			strings.add(gs.getText());
		}
		return strings;
	}


	
	List<GraphicString> getGraphicStrings() {
		LinkedList<GraphicString> gstrings= new LinkedList<GraphicString>(); 
		for(Positionable p : contents) {
			if (p instanceof GraphicString) {
				gstrings.add((GraphicString) p);
			}
		}
		return gstrings;
	}
	
	public String fullString() {
		return fullString(" ");
	}
	
	public String fullString(String join) {
		return String.join(join, getStrings());
	}

	
	/**
	 * Fills this cluster only with objects that match the template class. 
	 * @param cluster
	 */
	public void filterCopy(ContentRegion<Positionable> region) {
		for(Positionable p: region.contents) {
			if(p instanceof GraphicString) {
				this.add((GraphicString) p);
			}
		}
	}

	
}
