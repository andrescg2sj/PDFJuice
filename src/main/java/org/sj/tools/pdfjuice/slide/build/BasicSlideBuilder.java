package org.sj.tools.pdfjuice.slide.build;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.pdfbox.pdmodel.font.PDFontFactory;
import org.sj.tools.graphics.pdf.PDFString;
import org.sj.tools.graphics.sectorizer.ContentRegion;
import org.sj.tools.graphics.sectorizer.GraphicString;
import org.sj.tools.graphics.sectorizer.StrRegionCluster;
import org.sj.tools.graphics.sectorizer.StringRegion;
import org.sj.tools.pdfjuice.slide.SldGroup;
import org.sj.tools.pdfjuice.slide.SldList;
import org.sj.tools.pdfjuice.slide.SldObject;
import org.sj.tools.pdfjuice.slide.SldText;
import org.sj.tools.pdfjuice.slide.SldTitle;
import org.sj.tools.pdfjuice.slide.Slide;

public class BasicSlideBuilder extends SlideBuilder {
	
	/**
	 * percentage of title size compared to fullSilde
	 */
	double titleSizePercent;
	double titleMinY;
	
	public BasicSlideBuilder() throws IOException {
		defaultFont = PDFontFactory.createDefaultFont();
	}

	
	
	public BasicSlideBuilder(double percent) {
		titleSizePercent = percent;
	}
	
	public SldTitle buildTitle(StrRegionCluster titleCl) {
		if(titleCl.getNumberOfRegions() == 0) {
			if(titleCl.countRemaining() == 0 ) {
				return null;
			}
			return new SldTitle(buildPDFString(titleCl.getRemaining(0)));
		} else {
			ContentRegion<GraphicString> cr = titleCl.getRegion(0);
			if(cr.countElements() == 0)
				return null;
			return new SldTitle(buildPDFString(cr.get(0)));
		}
	}

	public SldList buildList(StrRegionCluster contentCl) {
		try {
			ListBuilder builder = new ListBuilder();
			return builder.buildFrom(contentCl);
		} catch(IOException ioe) {
			return null;
		} catch(ArrayIndexOutOfBoundsException ae) {
			return null;
		}
	}
	
	public SldGroup<SldText> buildContent(StrRegionCluster contentCl) {
		
		SldGroup<SldText> group = new SldGroup<SldText>();
		System.out.println("creating content");
		StringBuilder str = new StringBuilder();
		for(int i=0; i<contentCl.getNumberOfRegions();i++) {
			ContentRegion<GraphicString> cr = contentCl.getRegion(0);
			StringRegion sreg = new StringRegion(cr);
			Iterator<GraphicString> it = sreg.contentIterator();
			while(it.hasNext()) {
				GraphicString gs = it.next();
				SldText text = new SldText(buildPDFString(gs));
				System.out.println("creating:"+gs.getText());
				group.add(text);
			}
		}

		for(int i=0; i<contentCl.countRemaining();i++) {
			GraphicString gs = contentCl.getRemaining(i);
			SldText text = new SldText(buildPDFString(gs));
			System.out.println("creating:"+gs.getText());
			group.add(text);
		}
		
		return group;
	}

	
	
	public PDFString buildPDFString(GraphicString gs) {
		if(gs instanceof PDFString) {
			return (PDFString) gs;
		} else {
			return new PDFString(gs, defaultFont);
		}
	}

	public SldObject buildFromRegion(ContentRegion<GraphicString> cr) {
		if(cr.countElements() == 1) {
			return new SldText(buildPDFString(cr.get(0)));
		} else {
			SldGroup<SldText> group = new SldGroup<SldText>();
			cr.sortReverseY();
			for(int i=0; i<cr.countElements();i++) {
				SldText text = new SldText(buildPDFString(cr.get(i)));
				group.add(text);
			}
			return group;
		}
	}

	public SldGroup<SldObject> buildGroupFromCluster(StrRegionCluster cluster) {
		cluster.partitionContent(5, true);
		System.out.println("partitioned regions:"+cluster.getNumberOfRegions());
		SldGroup<SldObject> group = new SldGroup<SldObject>();
		for(int i=0; i<cluster.getNumberOfRegions();i++) {
			System.out.println("  reg("+i+") "+cluster.getRegion(i).getBounds().toString());
			group.add(buildFromRegion(cluster.getRegion(i)));
		}
		
		return group;
	}

	

	@Override
	public Slide build(StrRegionCluster cluster) {
		Rectangle2D area = cluster.getBounds();
		titleMinY = area.getMinY() + (area.getHeight()*titleSizePercent)/100;
		
		//StrRegionCluster title = new StrRegionCluster();
		System.out.println("Regions: "+cluster.getNumberOfRegions());
		System.out.println("Remaining: "+cluster.countRemaining());
		
		
		Vector<StrRegionCluster> vect = cluster.strRCDivideY(titleMinY);
		StrRegionCluster titleCl = vect.get(1);
		StrRegionCluster contentCl = vect.get(0);
		if(titleCl.getNumberOfRegions() == 0)
			titleCl.pushRegionForRemaining();
		
		System.out.println("title");
		System.out.println("  Regions: "+titleCl.getNumberOfRegions());
		System.out.println("  Remaining: "+titleCl.countRemaining());
		System.out.println("content");
		System.out.println("  Regions: "+contentCl.getNumberOfRegions());
		System.out.println("  Remaining: "+contentCl.countRemaining());

		
		Slide s = new Slide();
		s.add(buildTitle(titleCl));
		//s.add(buildContent(contentCl));
		s.add(buildGroupFromCluster(contentCl));
		
		return s;
	}

}
