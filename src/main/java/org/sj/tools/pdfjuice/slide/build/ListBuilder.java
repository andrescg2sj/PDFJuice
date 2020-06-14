package org.sj.tools.pdfjuice.slide.build;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontFactory;
import org.sj.tools.graphics.pdf.PDFString;
import org.sj.tools.graphics.sectorizer.ContentRegion;
import org.sj.tools.graphics.sectorizer.GraphicString;
import org.sj.tools.graphics.sectorizer.HorizBandTransform;
import org.sj.tools.graphics.sectorizer.StrRegionCluster;
import org.sj.tools.graphics.sectorizer.StringRegion;
import org.sj.tools.pdfjuice.slide.SldGroup;
import org.sj.tools.pdfjuice.slide.SldList;
import org.sj.tools.pdfjuice.slide.SldObject;
import org.sj.tools.pdfjuice.slide.SldText;

public class ListBuilder {
	
	//TODO: reference to SlideBuilder
	PDFont defaultFont; 
	
	public ListBuilder() throws IOException {
		defaultFont = PDFontFactory.createDefaultFont();
	}
	
	public boolean isBullet(GraphicString gs) {
		String str = gs.getText();
		if(str.trim().length() == 0)
			return false;
		
		return (str.length() == 1 && !Character.isLetterOrDigit(str.charAt(0)));
	}
	
	//TODO: SlideBuilder
	public SldText textFromGString(GraphicString gs) {
		if(gs instanceof PDFString) {
			PDFString ps = (PDFString) gs;
			return new SldText(ps);
		} else {
			return new SldText(gs, defaultFont);
		}
	}
	
	
	public double[] getYValues(StrRegionCluster rc) {
		double y[] = new double[rc.getNumberOfRegions()];
		for(int i=0; i<rc.getNumberOfRegions();i++) {
			y[i] = rc.getRegion(i).getPosition().getY();
		}
		return y;
	}
	
	public StrRegionCluster partitionY(Rectangle2D area, double y[]) {
		StrRegionCluster cluster = new StrRegionCluster();
		for(int i=0;i<y.length-1;i++) {
			Rectangle2D r = new Rectangle2D.Double(area.getMinX(), y[i],
					area.getWidth(),y[i+1]-y[i]);
			cluster.pushRegion(r);
		}
		Rectangle2D r = new Rectangle2D.Double(area.getMinX(), y[y.length-1],
				area.getWidth(),area.getMaxY()-y[y.length-1]);
		cluster.pushRegion(r);
		return cluster;
		
	}
	
	public SldObject buildContent(ContentRegion<GraphicString> cr) {
		if(cr.countElements() == 1) {
			return textFromGString(cr.get(0));
		} else {
			Iterator<GraphicString> it = cr.contentIterator();
			SldGroup<SldText> group = new SldGroup<SldText>();
			while(it.hasNext())  {
				GraphicString gs = it.next();
				group.add(textFromGString(gs));
			}
			return group;
		}
	}
	

	public SldList buildFrom(StrRegionCluster cluster) {
		cluster.partitionContent(1, true);
		System.out.println("partitioned regions:"+cluster.getNumberOfRegions());
		for(int i=0; i<cluster.getNumberOfRegions();i++) {
			System.out.println("  reg("+i+") "+cluster.getRegion(i).getBounds().toString());
		}
		return buildFrom(cluster.getRegion(0));
	}
	
	public SldList buildFrom(ContentRegion<GraphicString> cr) {
		SldList slist = new SldList();
		LinkedList<GraphicString> bullets = new LinkedList<GraphicString>();
		LinkedList<GraphicString> other = new LinkedList<GraphicString>();
		
		Iterator<GraphicString> it = cr.contentIterator();
		while(it.hasNext()) {
			GraphicString gs = it.next();
			if(isBullet(gs)) {
				System.out.println("bullet:"+gs.getText());
				bullets.add(gs);
			} else {
				other.add(gs);
			}
		}
		
		Rectangle2D region = cr.getBounds();
		StrRegionCluster cluster = new StrRegionCluster();
		
		for(GraphicString b: bullets) {
			//StringRegion r =
			Rectangle2D bounds = b.getBounds();
			Rectangle2D r = HorizBandTransform.mixXY(region, bounds);
			for(GraphicString gs: other) {
				if(r.intersects(gs.getBounds())) {
					r.add(gs.getBounds());
				}
			}
			cluster.pushRegion(r);
		}
		
		cluster.sortRegions(true);
		double y[] = getYValues(cluster);
		/*System.out.println("Y values:");
		for(int i=0; i<y.length;i++)
			System.out.println("  "+y[i]);*/
			
		StrRegionCluster part = partitionY(region, y);
		
		for(GraphicString gs: other) {
			part.push(gs);
			
		}
		for(GraphicString b: bullets) {
			ContentRegion<GraphicString> reg = part.getContainingRegion(b);
			SldObject content = buildContent(reg);
			//TODO: font
			slist.addItem(new SldText(b, null), content);
		}
		
		
		return slist;
	}
}
