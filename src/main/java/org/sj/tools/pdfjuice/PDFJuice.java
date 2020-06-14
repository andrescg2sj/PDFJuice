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
        
        Option optMode = new Option("m", "mode", true, "extraction mode: slide|table");
        optMode.setRequired(true);
        options.addOption(optMode);


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
    	    		PDFTableExporter proc = PDFTableExporter.parseOptions(remaining);
    	    		proc.setDestination(outFilename);
    	    		proc.run(inFilename);
    			}
    		}
    		
    		
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            
            formatter.printHelp("utility-name", options);

            System.exit(1);
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
