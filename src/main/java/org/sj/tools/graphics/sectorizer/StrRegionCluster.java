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
 

package org.sj.tools.graphics.sectorizer;

import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.sj.tools.pdfjuice.CommonInfo;

//import org.sj.tools.graphics.pdf.PDFString;

public class StrRegionCluster extends PosRegionCluster<GraphicString> implements CommonInfo {
	
	
	public StrRegionCluster() {
		super();
	}
	
	public StrRegionCluster(PosRegionCluster<GraphicString> prc) {
		super(prc);
	}
	
	public Vector<StrRegionCluster> strRCDivideY(double y_coord) {
		Vector<PosRegionCluster<GraphicString>> vect =  super.divideY(y_coord);
		Vector<StrRegionCluster> strVect = new Vector<StrRegionCluster>();
		for(PosRegionCluster<GraphicString> prc : vect) {
			strVect.add(new StrRegionCluster(prc));
		}
		return strVect;
	}
	
	/**
	 * Fills this cluster only with objects that match the template class. 
	 * @param cluster
	 */
	public void filterCopy(PosRegionCluster<Positionable> cluster) {
		for(ContentRegion<Positionable> cr: cluster.regions) {
			StringRegion sr = new StringRegion(cr.getBounds());
			sr.filterCopy(cr);
			this.pushRegion(sr);
		}
	}

	
	public ContentRegion<GraphicString> find(String text) {
		for(ContentRegion<GraphicString> cr: regions) {
			GraphicString gs = findInRegion(cr, text);
			if(gs != null) {
				return cr;
			}
		}
		return null;
	}
	
	public static GraphicString findInRegion(ContentRegion<GraphicString> cr, String text) {
		Iterator<GraphicString> it = cr.contentIterator();
		while(it.hasNext()) {
			GraphicString gs = it.next();
			if(gs.getText().contains(text)) {
				return gs;
			}
		}
		return null;
	}

	
	public List<String> allStrings() {
		List<String> all = new LinkedList<String>();
		//FIXME
		for(ContentRegion<GraphicString> r: regions) {
			StringRegion sr = new StringRegion(r);
			all.addAll(sr.getStrings());
		}
		return all;
	}
	
	public String regionToHTML(StringRegion cr) {
		StringBuilder s = new StringBuilder();
		s.append("<div style=\"border-style: solid;\">\n");
		for(GraphicString g: cr.getGraphicStrings()) {
			s.append(g.getText()+"<br/>\n");
		}
		
		s.append("</div>\n");
		
		return s.toString();
	}
	
	public String toHTML() {
		filterOutEmptyRegions();
		sortRegions();
		StringBuilder s = new StringBuilder();
		System.out.println("Regions: "+ regions.size());
		for(ContentRegion<GraphicString> r: regions) {
			StringRegion sr = new StringRegion(r);
			s.append(regionToHTML(sr));
		}

		System.out.println("Remainin: "+ remaining.size());
		for(GraphicString obj: remaining) {
			if(obj instanceof GraphicString) {
				GraphicString gs = (GraphicString) obj;
				s.append("<p>"+gs.getText()+"</p>"+ NEW_LINE);
			}
		}
		return s.toString();
	}
	

	
	

}
