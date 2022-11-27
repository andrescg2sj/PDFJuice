/*
 * Apache License
 *
 * Copyright (c) 2021 Andrés González SJ
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
package org.sj.tools.pdfjuice;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.sj.tools.graphics.tablemkr.util.PDFTableExporter;
import org.sj.tools.pdfjuice.gui.PDFJuiceWindow;
import org.sj.tools.pdfjuice.slide.util.PDFSlidesExporter;
import org.sj.tools.pdfjuice.util.PDFCreator;
import org.sj.tools.pdfjuice.util.RawExtractor; 

public class PDFJuice 
{
    static String DEFAULT_PATH = "res/CEPI-1-1.pdf";
    //static String DEFAULT_PATH = "res/test-1cell.pdf";
    
	/*static {
	      System.setProperty("java.util.logging.config.file",
	              "./logging.properties");
	      //must initialize loggers after setting above property
	      //LOGGER = Logger.getLogger(MyClass.class.getName());
	  }*/

	
    public static void main(String args[]) {
		String outFilename = "out/CEPI-1-1.htm";
		String inFilename = DEFAULT_PATH;
		
		Options options = new Options();
		
		Option optHelp = new Option("h", "help", false, "Shows this help message.");
        optHelp.setRequired(false);
        options.addOption(optHelp);

		Option optGui = new Option("g", "gui", false, "Launches graphic user interface.");
        optGui.setRequired(false);
        options.addOption(optGui);


		Option optInput = new Option("i", "input", true, "input file");
        optInput.setRequired(false);
        options.addOption(optInput);

		
        Option optOutput = new Option("o", "output", true, "output file");
        optOutput.setRequired(false);
        options.addOption(optOutput);
        
        Option optMode = new Option("m", "mode", true, "extraction mode: slide|table|image|text|bookmark");
        optMode.setRequired(false);
        options.addOption(optMode);
        
        Option clip = new Option("c", "clip", true, "format: x,y,width,height");
        clip.setRequired(false);
        options.addOption(clip);

        Option optThickness = new Option("t", "thickness", true, "máximum line thickness");
        optThickness.setRequired(false);
        options.addOption(optThickness);

        Option optProximity = new Option("p", "proximity", true, "minimum distance between tables");
        optProximity.setRequired(false);
        options.addOption(optProximity);

        Option optLineFilter = new Option("l", "lines", true, "line filtering: <color_name> | 0x<rrggbb> | all");
        optLineFilter.setRequired(false);
        options.addOption(optLineFilter);

        Option optTest = new Option("t", "test", false, "test create PDF");
        optTest.setRequired(false);
        options.addOption(optTest);
        
        Option optCount = new Option("c", "count", true, "Show PDF page count.");
        optCount.setRequired(false);
        options.addOption(optTest);


        

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            
            if(cmd.hasOption("h")) {
                formatter.printHelp("utility-name", options);
            	
            } else if(cmd.hasOption("g")) {
            	PDFJuiceWindow.createWindow();
            } else if(cmd.hasOption("t")) {
            	createTestPDF();
            } else if(cmd.hasOption("c")) {
            	inFilename = cmd.getOptionValue("c");
	            String remaining[] = cmd.getArgs();
            	showPageCount(inFilename);
	            for(String filename: remaining) {
	            	showPageCount(filename);
	            }

            } else {
            	final String required_opts[] = {"i","o","m"};
	            
	            for(String opt: required_opts) {
	            	if(!cmd.hasOption(opt)) {
		            	throw new ParseException("missing option: " + opt);
	            	}
	            }

	            inFilename = cmd.getOptionValue("i");
	            outFilename = cmd.getOptionValue("o");
	    		
    			String sMode = cmd.getOptionValue("m"); 
    			if("slide".equals(sMode)) {
    				slidesToHtml(inFilename, outFilename);
    			} else if("table".equals(sMode)) {
    	    		//PDFTableExporter proc = PDFTableExporter.parseOptions(remaining);
    				PDFTableExporter proc = PDFTableExporter.make(cmd);
    	    		//proc.setShapeDetection(true);
    	    		proc.setDestination(outFilename);
    	    		proc.run(inFilename);
    			} else if("poster".equals(sMode)) {
    				posterToHtml(inFilename, outFilename);
    			} else if("image".equals(sMode)) {
    				saveImages(inFilename, outFilename);
    			} else if("text".equals(sMode)) {
    				slidesToText(inFilename, outFilename);
    			} else if("bookmark".equals(sMode)) {
    				extractBookmarks(inFilename, outFilename);
    			}
    		
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
	
    }
    
    public static void extractBookmarks(String inFilename, String outFilename) {
    	RawExtractor be;
		try {
			be = new RawExtractor(inFilename, outFilename);
	    	be.extractBookmarks();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void createTestPDF() {
    	PDFCreator c = new PDFCreator("out/test.pdf");
    	c.create();
    }
    

	public static void saveImages(String inFilename, String outFilename) {
		try {
			PDFSlidesExporter proc = new PDFSlidesExporter(inFilename);
			proc.showPageCount();
			proc.exportImages();
			//TODO: all pages
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

    
	public static void posterToHtml(String inFilename, String outFilename) {
		try {
			PDFSlidesExporter proc = new PDFSlidesExporter(inFilename);
			//proc.testOnePageBuilder();
			//proc.allPagesToText();
			proc.firstPageToHtml(outFilename);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void showPageCount(String inFilename) {
		try {
			PDFSlidesExporter proc = new PDFSlidesExporter(inFilename);
			int pages = proc.countPages();
			System.out.println(String.format("%s: %d pages", inFilename, pages));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public static void slidesToText(String inFilename, String outFilename) {
		try {
			PDFSlidesExporter proc = new PDFSlidesExporter(inFilename);
			//proc.testOnePageBuilder();
			//proc.allPagesToText();
			proc.allPagesToText(outFilename);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void slidesToHtml(String inFilename, String outFilename) {
		try {
			PDFSlidesExporter proc = new PDFSlidesExporter(inFilename);
			//proc.testOnePageBuilder();
			//proc.allPagesToText();
			proc.allPagesToHtml(outFilename);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
