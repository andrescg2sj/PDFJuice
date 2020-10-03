package org.sj.tools.pdfjuice.slide.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.sj.tools.pdfjuice.slide.Slide;
import org.sj.tools.pdfjuice.slide.build.BasicSlideBuilder;
import org.sj.tools.pdfjuice.slide.build.PartitionSlideBuilder;
import org.sj.tools.pdfjuice.slide.build.SlideBuilder;
import org.sj.tools.pdfjuice.slide.html.HtmlSlide;

public class PDFSlidesExporter {
	
	File file;
	PDDocument doc;
	
	public PDFSlidesExporter(String filename) throws InvalidPasswordException, IOException  {
        file = new File(filename);
        doc = PDDocument.load(file);

	}
	
	
	public void getTitles() throws IOException {
		for(int n=0; n<doc.getNumberOfPages(); n++) {
	        PDPage page = doc.getPage(n);
	        SlideExtractor engine = new SlideExtractor(page);
	        engine.run();
	        Slide s = engine.makeSlide();
	        
		}
		
	}
	
	public void allPagesToText(String outFilename) throws IOException {
		LinkedList<Slide> slides = new LinkedList<Slide>();
		int countElems = 0;
		for(int i=0; i<doc.getNumberOfPages();i++) {
			PDPage page = doc.getPage(i);
			SlideExtractor engine = new SlideExtractor(page);
			// 108,0,720,583
			engine.run();
			Slide s = engine.makeSlide();
			countElems += s.countElems();
			slides.add(s);
			//System.out.println("------------------");
			//System.out.println(engine.toHTML());
        
		}
		System.out.println("Elements: "+countElems);
		doc.close();
		
		FileWriter w = new FileWriter(new File(outFilename));
		PrintWriter p = new PrintWriter(w);
		for(Slide s: slides) {
			p.print(s.plainText());
		}
		p.close();
		
	}
	
	public void allPagesToHtml(String outFilename) throws IOException {
		LinkedList<Slide> slides = new LinkedList<Slide>();
		int countElems = 0;
		for(int i=0; i<doc.getNumberOfPages();i++) {
			PDPage page = doc.getPage(i);
			SlideExtractor engine = new SlideExtractor(page);
			// 108,0,720,583
			engine.run();
			System.out.println("Building slide: "+ i);
			SlideBuilder sbuilder = new PartitionSlideBuilder();
			Slide s = engine.makeSlide(sbuilder);
			countElems += s.countElems();
			slides.add(s);
			//System.out.println("------------------");
			//System.out.println(engine.toHTML());
        
		}
		System.out.println("Elements: "+countElems);
		doc.close();
		
		FileWriter w = new FileWriter(new File(outFilename));
		PrintWriter p = new PrintWriter(w);
		p.println("<html><body>");
		for(Slide s: slides) {
			//HtmlSlide hs = new HtmlSlide(s);
			p.println(s.toHTML());
		}
		p.println("</body></html>");
		p.close();
		
	}

	
	public void testOnePageBuilder() throws IOException {
        PDPage page = doc.getPage(0);
        SlideExtractor engine = new SlideExtractor(page);
        // 108,0,720,583
        engine.run();
        doc.close();
        System.out.println("---building---");
        SlideBuilder builder = new BasicSlideBuilder(70);
        Slide s = engine.makeSlide(builder);
        //System.out.println("------------------");
        System.out.println("Bounds: " + engine.getBounds());
        
        writeSlideHTML(s, "out/doc1.htm");

	}
	
    public void writeSlideHTML(Slide slide, String filename) throws IOException {
    	File f = new File(filename);
    	FileOutputStream fos = new FileOutputStream(f);
    	//String s = cluster.toHTML();
    	
    	HtmlSlide hslide = new HtmlSlide(slide);

    	//TODO: Make advanced  cluster that writes different html tags for different fonts 
    	String s = hslide.toHTML();
    	fos.write(s.getBytes());
    	fos.close();
    	
    }


	
	public void testOnePage() throws IOException {
        PDPage page = doc.getPage(0);
        SlideExtractor engine = new SlideExtractor(page);
        // 108,0,720,583
        engine.run();
        doc.close();
        Slide s = engine.makeSlide();
        //System.out.println("------------------");
        System.out.println("Bounds: " + engine.getBounds());
        
        engine.writeHTML("out/doc1.htm");

	}

}
