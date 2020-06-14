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
 

package org.sj.tools.graphics.tablemkr.util;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.sj.tools.common.Timestamp;
import org.sj.tools.graphics.tablemkr.frompdf.PDFTableExtractor;
import org.sj.tools.pdfjuice.CommonInfo;



public class PDFTableExporter implements CommonInfo
{
    
	public static final String HTML_HEAD = 
			"<html><head>"
			+ "<style>table {\r\n" + 
			"  border-collapse: collapse;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"table, th, td {\r\n" + 
			"  border: 1px solid black;\r\n" + 
			"}</style>"
			+ "</head>";

    //File inFile; 
    PDDocument doc;
    
    String dstFilename;
    
    double maxLineThickness = PDFTableExtractor.DEFAULT_THICKNESS;
	double minProximity = PDFTableExtractor.DEFAULT_PROXIMITY;
	
	Rectangle clipRect =  null;	

	
	public void setMaxLineThickness(double v) {
		maxLineThickness = v;
	}

	public void setMinProximity(double v) {
		minProximity = v;
	}

	/**
     * Parse
     */
    public static Rectangle parseDimensions(String spec) {
    	String parts[] = spec.split(",");
    	if(parts.length != 4) {
    		throw new IllegalArgumentException("Expected: 4 numbers separated by comma. Given: "+parts.length);
    	}
    	int x = Integer.parseInt(parts[0]);
    	int y = Integer.parseInt(parts[1]);
    	int width = Integer.parseInt(parts[2]);
    	int height = Integer.parseInt(parts[3]);
    	return new Rectangle(x,y,width,height);
    }


	
	public static PDFTableExporter parseOptions(String args[]) throws ParseException {

		Options options = new Options();

		/*
        Option output = new Option("o", "output", true, "output file");
        output.setRequired(false);
        options.addOption(output);
        */

        Option clip = new Option("c", "clip", true, "format: x,y,width,height");
        clip.setRequired(false);
        options.addOption(clip);

        Option optThickness = new Option("t", "thickness", true, "máximum line thickness");
        optThickness.setRequired(false);
        options.addOption(optThickness);

        Option optProximity = new Option("p", "proximity", true, "minimum distance between tables");
        optProximity.setRequired(false);
        options.addOption(optProximity);

        CommandLineParser parser = new DefaultParser();
        //HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

    	cmd = parser.parse(options, args);
        //String remaining[] = cmd.getArgs();

		
		PDFTableExporter proc = new PDFTableExporter(); 
		
		if(cmd.hasOption("t")) {
			proc.setMaxLineThickness(Double.parseDouble(cmd.getOptionValue("t")));
		}
		
		if(cmd.hasOption("p")) {
			proc.setMinProximity(Double.parseDouble(cmd.getOptionValue("p")));
		}
		
		
        if(cmd.hasOption("c")) {
	    	proc.setClip(parseDimensions(cmd.getOptionValue("c")));
	    } else {
	    	System.out.println("NO CLIP");
	    }

		
		return proc;

	}
	
	public void setClip(Rectangle r) {
		clipRect = r;
    	//System.out.println("Clipping rectangle:"+ clipRect.toString());
	}
    		
    
    public PDFTableExporter() {
    	//inFile = new File(path);
    	
    	dstFilename = DST_PATH + "doc"+ Timestamp.getTimestamp()+".htm";
        
    }
    
    public static void createDestDirectory() {
    	 File dir = new File(DST_PATH);
    	 if(!dir.isDirectory()) {
    		 dir.mkdirs();
    	 }
    }
    
    public static void test1() {
    	//Rectangle clipArea = new Rectangle(0,108,583, 720-108);

    }
    
    
    public void writeHTMLHead(OutputStreamWriter out) throws IOException
    {
    	out.write(HTML_HEAD + "<body>");
    }

    public void writeHTMLTail(OutputStreamWriter out) throws IOException
    {
    	out.write("</body></html>");
    }

    
    public void run(String srcPath)
    {
    	File inFile = new File(srcPath);
    	try {
    		createDestDirectory();

    		File dstFile = new File(dstFilename);
        	FileOutputStream fos = new FileOutputStream(dstFile);
        	OutputStreamWriter out = new OutputStreamWriter(fos);

        	writeHTMLHead(out);
        	

    	    doc = PDDocument.load(inFile);
    	    for(int i=0; i< doc.getNumberOfPages(); i++) {
    	    	PDPage page = doc.getPage(i);
    	    	PDFTableExtractor engine =
    	    			new PDFTableExtractor(page, clipRect, 
    	    					maxLineThickness, minProximity);
    	    	engine.run();
        	    engine.writeHTMLTables(out);
    	    }
    	    doc.close();
    	    
    	    writeHTMLTail(out);
        	out.close();

    	    	
    	    //System.out.println("------------------");
    	    //System.out.println(engine.cluster.toHTML());
            
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}

    }
    
    public void setDestination(String path) {
    	dstFilename = path;
    }


}
