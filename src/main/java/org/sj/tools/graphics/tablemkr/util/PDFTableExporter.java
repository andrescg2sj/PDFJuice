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
import java.util.List;

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
import org.sj.tools.graphics.tablemkr.Table;
import org.sj.tools.graphics.tablemkr.frompdf.ExtractionProperties;
import org.sj.tools.graphics.tablemkr.frompdf.PDFPageTableExtractor;
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
    
    ExtractionProperties properties = new ExtractionProperties();
    
    public void setShapeDetection(boolean detect) {
    	properties.setShapeDetection(detect);
    }
	
	public void setMaxLineThickness(double v) {
		properties.setMaxLineThickness(v);
	}

	public void setMinProximity(double v) {
		properties.setMinProximity(v);
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



	public static PDFTableExporter make(CommandLine cmd) throws ParseException {
		PDFTableExporter proc = new PDFTableExporter(); 
		
		if(cmd.hasOption("t")) {
			proc.setMaxLineThickness(Double.parseDouble(cmd.getOptionValue("t")));
		}
		
		if(cmd.hasOption("p")) {
			proc.setMinProximity(Double.parseDouble(cmd.getOptionValue("p")));
		}
		
		if(cmd.hasOption("l")) {
			String filter = cmd.getOptionValue("l");
			if("black".equals(filter)) {
				proc.properties.setFilterColoredLines(true);
			} else {
				proc.properties.setFilterColoredLines(false);
			}
		}
		
		
        if(cmd.hasOption("c")) {
	    	proc.setClip(parseDimensions(cmd.getOptionValue("c")));
	    } else {
	    	System.out.println("NO CLIP");
	    }
		
		return proc;

	}

    @Deprecated
	public static PDFTableExporter parseOptions(String args[]) throws ParseException {

		Options options = new Options();

		/*
        Option output = new Option("o", "output", true, "output file");
        output.setRequired(false);
        options.addOption(output);
        */


        CommandLineParser parser = new DefaultParser();
        //HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        
        Option clip = new Option("c", "clip", true, "format: x,y,width,height");
        clip.setRequired(false);
        options.addOption(clip);

        Option optThickness = new Option("t", "thickness", true, "máximum line thickness");
        optThickness.setRequired(false);
        options.addOption(optThickness);

        Option optProximity = new Option("p", "proximity", true, "minimum distance between tables");
        optProximity.setRequired(false);
        options.addOption(optProximity);


    	cmd = parser.parse(options, args);
        //String remaining[] = cmd.getArgs();

		
    	return make(cmd);
	}
	
	public void setClip(Rectangle r) {
		properties.setClipRect(r);
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
    
    public void writeHTMLTables(List<Table> tables, OutputStreamWriter out) throws IOException {
    	
    	for(Table t: tables) {
    		String s = t.toHTML();
    		out.write(s);
			out.write("<br/>"+CommonInfo.NEW_LINE);

    	}
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
    	    int countFailed = 0;
    	    for(int i=0; i< doc.getNumberOfPages(); i++) {
    	    	System.out.println("Page " + i);
    	    	PDPage page = doc.getPage(i);
    	    	PDFPageTableExtractor engine =
    	    			new PDFPageTableExtractor(page, properties);
    	    	try {
	    	    	engine.run();
    	    	} catch(RuntimeException re) {
        	    	System.out.println("Page " + i + " failed!");
    	    		countFailed ++;
    	    		re.printStackTrace();
    	    	}
    	    	List<Table> tables = engine.getCleanTables();
        	    writeHTMLTables(tables, out);
    	    }
    	    doc.close();
    	    
    	    writeHTMLTail(out);
        	out.close();

        	if(countFailed > 0) {
        	    System.out.println(countFailed + " pages failed during conversion.");
        		
        	}
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
