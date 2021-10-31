package org.sj.tools.pdfjuice.slide.build;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import org.sj.tools.graphics.sectorizer.ContentRegion;
import org.sj.tools.graphics.sectorizer.GraphicString;
import org.sj.tools.graphics.sectorizer.StrRegionCluster;
import org.sj.tools.graphics.sectorizer.StringRegion;
import org.sj.tools.graphics.sectorizer.geom.HorizBandTransform;
import org.sj.tools.graphics.sectorizer.geom.MultiplyTransform;
import org.sj.tools.graphics.sectorizer.geom.VertBandTransform;
import org.sj.tools.pdfjuice.slide.SldGroup;
import org.sj.tools.pdfjuice.slide.SldList;
import org.sj.tools.pdfjuice.slide.SldObject;
import org.sj.tools.pdfjuice.slide.SldTitle;
import org.sj.tools.pdfjuice.slide.Slide;

public class PartitionSlideBuilder extends BasicSlideBuilder {

	public PartitionSlideBuilder() throws IOException {
		super();
	}
	
	public SldTitle buildTitle(ContentRegion<GraphicString> titleCR) {
		if(titleCR.countElements() == 0)
			return null;
		GraphicString gs = titleCR.get(0);
		for(int i=1; i<titleCR.countElements(); i++) {
			gs.add(titleCR.get(i), " ");
		}
		return new SldTitle(buildPDFString(titleCR.get(0)));
	}
	
	public ContentRegion<GraphicString> getBullets(StrRegionCluster cluster) {
		cluster.clearRegions();
		cluster.remainingToRegions(0);
		//MultiplyTransform duplicate = new MultiplyTransform(2);
		//cluster.transformRegions(duplicate);
		
		Rectangle2D bounds = cluster.getBounds();
		System.out.println("bounds:"+bounds);
		VertBandTransform vt = new VertBandTransform(bounds);
		cluster.transformRegions(vt);
		cluster.meltRegions();
		System.out.println("  melted regions:"+cluster.getNumberOfRegions());
		cluster.sortRegions();
		
		for(int i=0;i<cluster.getNumberOfRegions(); i++) {
			ContentRegion<GraphicString> creg = cluster.getRegion(i);
			StringRegion sreg = new StringRegion(creg);
			if(sreg.fullString().trim().length() > 0) {
				return cluster.extract(i);
			}
		}
		return null;
	}
	
	public SldList buildListFromCluster(StrRegionCluster cluster) {
		System.out.println("building list");
		logCluster(cluster);
		
		ContentRegion<GraphicString> bullets = getBullets(cluster);
		if(bullets != null) {
			Rectangle2D bounds = cluster.getBounds();

			System.out.println("bullets:"+bullets.countElements());
			
			//ContentRegion<GraphicString> rest = cluster.getRegion(0);
			
			StrRegionCluster bltCluster = new StrRegionCluster();
			bltCluster.pushRegion(bullets);
			bltCluster.clearRegions();
			bltCluster.remainingToRegions(0);
			bltCluster.sortReverseYRegions();
			bltCluster.fillRegionGapsBackwards();
			
			HorizBandTransform ht = new HorizBandTransform(bounds);
			bltCluster.transformRegions(ht);
			ContentRegion<GraphicString> lastCR = bltCluster.getRegion(0); 
			Rectangle2D rect = ContentRegion.cloneRect2D(lastCR.getBounds());
			rect.add(new Point2D.Double(bounds.getMinX(),bounds.getMinY()));
			lastCR.setRegion(rect);
			
			cluster.clearRegions();
			
			for(int i=0;i<bltCluster.getNumberOfRegions();i++) {
				System.out.println("  blt reg("+i+") "+bltCluster.getRegion(i).getBounds().toString());
	
				ContentRegion<GraphicString> cr = bltCluster.getRegion(i);
				cluster.flexiblePushRegion(cr);
			}
			//cluster.sortReverseYRegions();
			//cluster.remainingToRegions(0);
			//cluster.meltRegions();
	
			cluster.sortReverseYRegions();
			System.out.println("final: num regions: "+cluster.getNumberOfRegions());
			System.out.println("         remaining: "+cluster.countRemaining());
			
			/*
			SldGroup<SldObject> group = new SldGroup<SldObject>();
			for(int i=0; i<cluster.getNumberOfRegions();i++) {
				System.out.println("  reg("+i+") "+cluster.getRegion(i).getBounds().toString());
				group.add(buildFromRegion(cluster.getRegion(i)));
			}*/
			SldList list = new SldList();
			for(int i=0; i<cluster.getNumberOfRegions();i++) {
				//System.out.println("  reg("+i+") "+cluster.getRegion(i).getBounds().toString());
				//list.add(buildFromRegion(cluster.getRegion(i)));
				list.addItem(buildFromRegion(cluster.getRegion(i)));
			}
			return list;
		
		} else {
			System.out.println("No content");
			return null;
		}
		
		
	}
	
	public double rectDistY(Rectangle2D a, Rectangle2D b) {
		if(a.intersects(b)) return 0;
		if(a.getMinY() < b.getMinY()) {
			return b.getMinY() - a.getMaxY();
		} else {
			return rectDistY(b,a);
		}
	}
	
	
	public ContentRegion<GraphicString> getTitle(StrRegionCluster cluster) {
		double maxDist = 0;
		int i_max = -1;
		ContentRegion<GraphicString> last = null;
		
		if (cluster.getNumberOfRegions() == 0) {
			return null;
		}
		
		/* Title is probably not more than half of the slide */
		for(int i=0; i<cluster.getNumberOfRegions()/2;i++) {
			ContentRegion<GraphicString> creg = cluster.getRegion(i);
			if(last != null) {
				//double dist = last.getBounds().getY() - creg.getBounds().getMaxY();
				double dist = rectDistY(last.getBounds(),creg.getBounds());
				//last.getBounds().
				System.out.println("   dist="+dist+"(last:"+last.get(0).getText()+",curr:"+creg.get(0).getText()+")");
				if(dist > maxDist) {
					maxDist = dist;
					i_max = i;
				}
			}
			last = creg;
		}
		System.out.println("i_max="+i_max);
		
		if(i_max != -1) {
			ContentRegion<GraphicString> first = cluster.getRegion(0);
			for(int i=1; i< i_max;i++) {
				first.add(cluster.getRegion(i));
			}
		}
		ContentRegion<GraphicString> titleCR = cluster.extract(0);
		return titleCR;
	}

	public void logCluster(StrRegionCluster cluster) {
		for(int i=0; i<cluster.getNumberOfRegions();i++) {
			ContentRegion<GraphicString> creg = cluster.getRegion(i);
			System.out.println("  region("+i+")");
			for(int j=0; j<creg.countElements();j++) {
				GraphicString gs = creg.get(j);
				System.out.println("  "+j+": '"+gs.getText()+"'");
			}
			
		}
		
		for(int j=0; j<cluster.countRemaining();j++) {
			GraphicString gs = cluster.getRemaining(j);
			System.out.println("  rem"+j+": '"+gs.getText()+"'");
		}
	}


	@Override
	public Slide build(StrRegionCluster cluster) {
		System.out.println("building slide");
		System.out.println("  Regions:"+cluster.getNumberOfRegions());
		System.out.println("  remaining:"+cluster.countRemaining());
		
		cluster.remainingToRegions(0);
		System.out.println("  Regions:"+cluster.getNumberOfRegions());
		
		logCluster(cluster);

		//MultiplyTransform mt = new MultiplyTransform(1);
		//cluster.transformRegions(mt);
		HorizBandTransform hb = new HorizBandTransform(cluster.getBounds());
		cluster.transformRegions(hb);
		cluster.meltRegions();
		cluster.sortReverseYRegions();
		System.out.println("Regions:"+cluster.getNumberOfRegions());

		
		//ContentRegion<GraphicString> titleCR = cluster.extract(0);
		ContentRegion<GraphicString> titleCR = getTitle(cluster);

		
		Slide s = new Slide();
				
		if(titleCR != null)
			s.add(buildTitle(titleCR));
		//s.add(buildContent(contentCl));
		SldObject content = buildListFromCluster(cluster);
		if(content != null)
			s.add(content);
		
		return s;
	}

}
