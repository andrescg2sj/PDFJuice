package org.sj.tools.pdfjuice;

import java.awt.Rectangle;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.sj.tools.graphics.tablemkr.util.PDFTableExporter;
import org.sj.tools.pdfjuice.slide.util.PDFSlidesExporter;

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

		Option optInput = new Option("i", "input", true, "input file");
        optInput.setRequired(true);
        options.addOption(optInput);

		
        Option optOutput = new Option("o", "output", true, "output file");
        optOutput.setRequired(true);
        options.addOption(optOutput);
        
        Option optMode = new Option("m", "mode", true, "extraction mode: slide|table|text");
        optMode.setRequired(true);
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

        

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            
            String remaining[] = cmd.getArgs();
            
    		inFilename = cmd.getOptionValue("i");
    		outFilename = cmd.getOptionValue("o");
    		
    		if(cmd.hasOption("m")) {
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
    			} else if("text".equals(sMode)) {
    				slidesToText(inFilename, outFilename);
    			}
    		}
    		
    		
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            
            formatter.printHelp("utility-name", options);

            System.exit(1);
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
