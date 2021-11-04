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
 

package org.sj.tools.graphics.tablemkr.graphtrace;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Point;
import java.util.Vector;
import java.util.Iterator;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import org.sj.tools.pdfjuice.CommonInfo;
import org.sj.tools.common.Timestamp;
import org.sj.tools.graphics.sectorizer.GraphicString;
import org.sj.tools.graphics.tablemkr.Area;

import java.util.Locale;



public class SvgTrace implements CommonInfo {

    //String path;
    //FileWriter fwriter;
    float drawAreaFactor = 1.1f;

    public SvgTrace() {
	//this.path = path;

    }
    

    public String generateLogFilename() {
    	String timestamp = Timestamp.getTimestamp();
    	int number = 0;
    	
    	String path = DST_PATH + "log" + timestamp +"-" + number+".svg";
    	File f = new File(path);
    	while(f.exists()) {
    		number++;
        	path = DST_PATH + "log" + timestamp +"-" + number+".svg";
        	f = new File(path);
    	}
    	
    	return path;
    }

    public void exportAreasAndGStrings(Vector<Area> areas,
				      Vector<GraphicString> gstrs)
    {
	exportAreasAndGStrings(generateLogFilename(), areas, gstrs);
	
    }

    public void exportAreas(Vector<Area> areas) {
	exportAreas(generateLogFilename(), areas);
    }

    public void exportAreasAndGStrings(String filename,
				       Vector<Area> areas,
				       Vector<GraphicString> gstrs) {
	//TODO: calculate content bounds
	String content = "";
	Rectangle2D bounds = areas.get(0).getBounds();
    	for(Area a: areas) {
	    content += areaToSvg(a, bounds);
    	}
	String s = String.format(Locale.ROOT, "<svg width=\"%.2f\" height=\"%.2f\" xmlns=\"http://www.w3.org/2000/svg\">"+NEW_LINE,
				 (float) bounds.getMaxX()*drawAreaFactor,
				 (float) bounds.getMaxY()*drawAreaFactor);

	if(gstrs != null) {
	    System.out.println("Trace: gstrings: " + gstrs.size());
	    for(GraphicString gs: gstrs) {
		content += gstrToSvg(gs);
		//System.out.println("  gstr: " + gs.getText());
	    }
	}

    	s = s+content+"</svg>";
	try {
	    FileWriter fwriter = new FileWriter(new File(filename));
	    fwriter.write(s);
	    fwriter.close();
	} catch(IOException ioe) {
	    //ioe.printStackTrace();
		System.err.println("Error trying to write graphic log: " + ioe.getMessage());
	}
    }

    public void exportAreas(String filename, Vector<Area> areas) {
	exportAreasAndGStrings(filename, areas, null);
    }

    
    public String gstrToSvg(GraphicString gstr) {
	Point2D p = gstr.getPosition();
	String svgBounds = svgRectangle(gstr.getBounds(),"red"); 
	String svgText = String.format(Locale.ROOT, "<text x=\"%.2f\" y=\"%.2f\" fill=\"red\">%s</text>",
		      p.getX(), p.getY(), gstr.getText()); 
	return  svgBounds+ NEW_LINE+ svgText + NEW_LINE;
 
    }

    public String svgRectangle(Rectangle2D rect) {
    	return svgRectangle(rect, "green");
    }
    
    public String svgRectangle(Rectangle2D rect, String color) {
    	return String.format(Locale.ROOT, "<rect x=\"%.2f\" y=\"%.2f\" width=\"%.2f\" height=\"%.2f\" stroke=\"%s\" stroke-width=\"3\" />"+NEW_LINE, 
    			rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), color);
    }

    public String areaToSvg(Area area, Rectangle2D bounds) {
	
	Rectangle2D rect = area.getBounds();
	bounds.add(rect);
	String xml = svgRectangle(rect);
	Iterator<GraphicString> cnt = area.getContents();
	while(cnt.hasNext()) {
	    GraphicString gstr = cnt.next();
	    xml += gstrToSvg(gstr)+NEW_LINE;
	    
	}
	 return xml;
    }

    
} 
