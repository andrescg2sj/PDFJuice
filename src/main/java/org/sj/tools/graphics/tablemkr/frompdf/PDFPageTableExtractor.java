/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This file is a modified version from:
 *
 * https://apache.googlesource.com/pdfbox/+/trunk/examples/src/main/java/org/apache/pdfbox/examples/rendering
 *
 */


package org.sj.tools.graphics.tablemkr.frompdf;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
//import java.util.Iterator;

import org.sj.tools.graphics.sectorizer.GraphicString;
import org.sj.tools.graphics.sectorizer.HorizBandTransform;
import org.sj.tools.graphics.sectorizer.NormalComparator;
import org.sj.tools.graphics.sectorizer.PosRegionCluster;
import org.sj.tools.graphics.sectorizer.Positionable;
import org.sj.tools.graphics.sectorizer.ReverseYComparator;
import org.sj.tools.graphics.sectorizer.CompoundTransform;
import org.sj.tools.graphics.sectorizer.ContentRegion;
import org.sj.tools.graphics.sectorizer.ExpandTransform;
import org.sj.tools.graphics.sectorizer.GStringBuffer;


import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;
import org.sj.tools.pdfjuice.CommonInfo;
import org.sj.tools.graphics.sectorizer.StringRegion;
import org.sj.tools.graphics.tablemkr.GridTableMaker;
import org.sj.tools.graphics.tablemkr.TLine;
import org.sj.tools.graphics.tablemkr.Table;
import org.sj.tools.graphics.tablemkr.TableMaker;
import org.sj.tools.graphics.elements.ClippingArea;
import org.sj.tools.graphics.elements.GrPath;
import org.sj.tools.graphics.sectorizer.StrRegionCluster;

import java.util.logging.Logger;

/**
 * 
 */
public class PDFPageTableExtractor extends PDFGraphicsStreamEngine implements CommonInfo
{
	Logger log = Logger.getLogger("PDFTableExtractor");
	
	/*
	 * 
	 * 
	 * refs:
	 * PDFStreamEngine
	 * https://apache.googlesource.com/pdfbox/+/35025f10c2f5fff264a0b42a73e1d7133fd1c41b/pdfbox/src/main/java/org/apache/pdfbox/contentstream/PDFStreamEngine.java
	 * 
	 * PDFGraphicStreamEngine
	 * https://apache.googlesource.com/pdfbox/+/a3241d612d3ae387525d58d64b93b7804dde5939/pdfbox/src/main/java/org/apache/pdfbox/contentstream/PDFGraphicsStreamEngine.java
	 */
	
	
    GStringBuffer regionText = new GStringBuffer();
    LinkedList<TLine> lines = new LinkedList<TLine>();
    LinkedList<GraphicString> gstrings = new LinkedList<GraphicString>();
    
    LinkedList<Table> generatedTables = new LinkedList<Table>(); 
    
	//RegionCluster cluster;
	
	int rectCount = 0;
	int lineCount = 0;
	int strokeCount = 0;
	
    GrPath path = new GrPath();

    ExtractionProperties properties;
	
	//java.util.Vector<Line> lines = new java.util.Vector<Line>();
	//TableMaker tmaker = new SplitTableMaker();
    //TableMaker tmaker = new GridTableMaker();

    public List<Table> getTables()
    {
    	return generatedTables;
    }

    
    public PosRegionCluster<Positionable> organizeContents()
    {
		PosRegionCluster<Positionable> rc = new PosRegionCluster<Positionable>();
		
    	
    	for(TLine l: lines) {
    		if(l == null) {
    			log.warning("Attepmting ot add null line");
    		} else {
    			rc.push(l);
    		}
    	}
    	log.finest("lines added: "+rc.countRemaining());

    	for(GraphicString gs: gstrings) {
    		if(gs == null) {
    			log.warning("Attepmting ot add null GraphicString");
    		} else {
    			rc.push(gs);
    		}
    	}
    	log.finest("objects added: "+rc.countRemaining());
    	
    	
    	CompoundTransform ct = new CompoundTransform();
    	ct.add(new HorizBandTransform(rc.getBounds()));
    	ct.add(new ExpandTransform(0,properties.tableThreshold));
    	//ExpandTransform t = new ExpandTransform(tableThreshold);
    	//HorizBandTransform t = ;

    	rc.partitionContent(ct, NormalComparator.getInstance());
    	
    	//rc.sortRegions();
    	//rc.sortReverseYRegions();
    	
    	//rc.partitionContent(tableThreshold);
    	
    	return rc;
    }
    
    public <E> List<E> reverse(List<E> list) {
    	LinkedList<E> reversed = new LinkedList<E>();
    	Iterator<E> it = list.iterator();
    	while(it.hasNext()) {
    		reversed.push(it.next());
    	}
    	return reversed;
    }
    
    public List<Table> createTables() {
    	List<TableMaker> makers = createTableMakers();
    	LinkedList<Table> tables = new LinkedList<Table>();
    	for(TableMaker tm: makers) {
    		Table t = tm.makeTable();
    		if(t != null) {
        		t = t.trim();
        		if(t.countEmptyRows() > 0) {
        			java.util.Vector<Table> parts = t.divideOnEmptyRow();
        			for(Table p: parts) {
        				//TODO
        		    	log.info("Simplify: ");
        		    	log.info(p.toHTML());
        				p.simplifyTable();
        				tables.add(p);
        			}
        		} else {
        			tables.add(t);
        		}
    		}
    	}
    	log.finest("Tables created: "+tables.size());
    	return reverse(tables);
    }
    
    /*
    public StrRegionCluster getStrings(PosRegionCluster<Positionable> cluster) {
    	for(int i=0; i<)
    }*/
    
    protected void logRegion(ContentRegion<Positionable> reg) {
    	log.info("    elements: "+reg.countElements());
    	StringRegion sr = new StringRegion(reg.getBounds());
    	sr.filterCopy(reg);
    	if(sr.countElements() > 0) {
    		log.info("   Text:" + sr.fullString());
    	}
    }
    
    public List<TableMaker> createTableMakers() {
    	PosRegionCluster<Positionable> cluster = organizeContents();
    	LinkedList<TableMaker> makers = new LinkedList<TableMaker>();

    	log.info("Regions: "+cluster.getNumberOfRegions());
    	log.info("Remaining: "+cluster.countRemaining());
    	    	

    	for(int i=0;i<cluster.getNumberOfRegions(); i++) {
    		ContentRegion<Positionable> r = cluster.getRegion(i);
    		GridTableMaker tmaker = new GridTableMaker();
        	log.info("  Region*: "+i);
        	logRegion(r);

    		Iterator<Positionable> it = r.contentIterator();
    		
    		while(it.hasNext()) {
    			Positionable obj = it.next();
    			if(obj instanceof TLine) {
    				TLine line = (TLine) obj;
    				log.finest("      line: "+line.toString());
        			tmaker.add(line);
    			} else if(obj instanceof GraphicString) {
    				GraphicString gs = (GraphicString) obj;
    				log.finest("      gs: "+gs.toString());
    				tmaker.add(gs);
    			}
    		}
    		
    		makers.add(tmaker);
    	}

    	log.finest("Makers: "+makers.size());
   	
    	return makers;

    }
    
    public void writeHTMLTables(OutputStreamWriter out) throws IOException {
    	List<Table> tables = createTables();
    	
    	for(Table t: tables) {
    		Table clean = t.trim();
    		if(clean == null) {
    			out.write("null table");
    		} else if (clean.isEmpty()) {
    			out.write("empty table");
    		} else {
    			String s = clean.toHTML();
    			out.write(s);
    		}
			out.write("<br/>"+CommonInfo.NEW_LINE);

    	}

    }
    

    
    /**
     * Constructor.
     *
     * @param page PDF Page
     */
    protected PDFPageTableExtractor(PDPage page)
    {
        super(page);
        properties = new ExtractionProperties();
    }
    
    /**
     * Constructor.
     *
     * @param page PDF Page
     */
    public PDFPageTableExtractor(PDPage page, Rectangle clipRect)
    {
        super(page);
        properties = new ExtractionProperties(clipRect);
    }


    public PDFPageTableExtractor(PDPage page, ExtractionProperties props) {
        super(page);
        properties = props;
    }

    public PDFPageTableExtractor(PDPage page, Rectangle clipRect, double thickness, double proximity)
    {
        super(page);
        properties = new ExtractionProperties(clipRect, proximity, thickness);
    }


    
    /**
     * Runs the engine on the current page.
     *
     * @throws IOException If there is an IO error while drawing the page.
     */
    public void run() throws IOException
    {
        processPage(getPage());
        for (PDAnnotation annotation : getPage().getAnnotations())
        {
            showAnnotation(annotation);
        }
        showStats();
        //java.util.Vector<org.sj.tools.graphics.tablemkr.Area> areas = tmaker.buildAreas();
        //logAreas(areas);
        //tmaker.toSVG(areas);
    }
    
    void showStats() {
    	log.finest("strokes:"+strokeCount);
    	log.finest("lines:"+lineCount);
    	log.finest("rectangles:"+rectCount);
    	log.finest("size H:"+getPage().getBBox().getHeight());
    }
    
    @Deprecated
    public double transf_Yd(double y) {
    	PDRectangle r = getPage().getBBox();
    	return r.getHeight() - y;
    }

    @Deprecated
    public float transf_Yf(float y) {
    	PDRectangle r = getPage().getBBox();
    	return r.getHeight() - y;
    }

    @Deprecated
    public Point2D transform(Point2D p) {
    	if(p instanceof Point2D.Float) {
    		return new Point2D.Float((float) p.getX(), transf_Yf((float)p.getY()));
    	} else {
    		return new Point2D.Double(p.getX(), transf_Yd(p.getY()));
    	}
    }
    
    //FIXME: not necessary anymore
    public TLine buildLine(Point2D a, Point2D b) {
    	//return new Line(transform(a), transform(b));
    	return new TLine(a, b);
    }
    
    public TLine buildVertLine(Rectangle2D rect) {
    	return new TLine(new Point2D.Double(rect.getCenterX(), rect.getY()),
    			new Point2D.Double(rect.getCenterX(), rect.getMaxY()));
    }

    public TLine buildHorizLine(Rectangle2D rect) {
    	return new TLine(new Point2D.Double(rect.getX(), rect.getCenterY()),
    			new Point2D.Double(rect.getMaxX(), rect.getCenterY()));
    }


    public Rectangle transfRect(Rectangle r) {
    	//Rectangle z = new Rectangle(1,2,3,4);
    	
    	/*return new Rectangle((int) r.getX(), (int) transf_Yd(r.getY()),
    			(int) r.getWidth(), (int) r.getHeight());*/
    	return r;
    }

    
    @Override
    public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException
    {
         log.finest(String.format("appendRectangle %.2f %.2f, %.2f %.2f, %.2f %.2f, %.2f %.2f\n",
                p0.getX(), p0.getY(), p1.getX(), p1.getY(),
                p2.getX(), p2.getY(), p3.getX(), p3.getY())); 
        //FIXME:
    	//strokeRectangle(p0,p1,p2,p3);
        /*
    	path.moveTo(p0);
    	path.lineTo(p1);
    	path.lineTo(p2);
    	path.lineTo(p3);
    	path.lineTo(p0);
    	*/
        path.appendRectangle(p0, p1, p2, p3);
    	
    	
    }

    public void strokeRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException
    {

        Rectangle2D r = StringRegion.rectangleFromPoints(p0,p1,p2,p3);
        if(properties.clip.clip(r)) {
            log.finest(String.format("strokeRectangle %.2f %.2f, %.2f %.2f, %.2f %.2f, %.2f %.2f\n",
                    p0.getX(), p0.getY(), p1.getX(), p1.getY(),
                    p2.getX(), p2.getY(), p3.getX(), p3.getY()));
            /* add rectangle */
            /*tmaker.add(buildLine(p0, p1));
            tmaker.add(buildLine(p1, p2));
            tmaker.add(buildLine(p2, p3));
            tmaker.add(buildLine(p3, p0));*/
            
            lines.add(buildLine(p0, p1));
            lines.add(buildLine(p1, p2));
            lines.add(buildLine(p2, p3));
            lines.add(buildLine(p3, p0));
            
            
            rectCount++;

        } else {
        	log.finest(String.format("discarded rect"));
        }
        //cluster.pushRegion(r);
    }
    
    @Override
    public void drawImage(PDImage pdImage) throws IOException
    {
        log.finest("drawImage");
    }
    
    @Override
    public void clip(int windingRule) throws IOException
    {
        log.finest("clip");
    }
    
    @Override
    public void moveTo(float x, float y) throws IOException
    {
    	//region.reset();
    	//region.add(new Point((int)x,(int)y));
	path.moveTo(new Point2D.Float(x,y));
        log.finest(String.format("moveTo %.2f %.2f\n", x, y));
    }
    @Override
    public void lineTo(float x, float y) throws IOException
    {
    	//region.add(new Point((int)x,(int)y));
    	lineCount++;
    	path.lineTo(new Point2D.Float(x,y));

        log.finest(String.format("lineTo %.2f %.2f\n", x, y));
    }
    @Override
    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) throws IOException
    {
    	//region.add(new Point((int)x1,(int)y1));
    	//region.add(new Point((int)x2,(int)y2));
    	//region.add(new Point((int)x3,(int)y3));
    	log.finest(String.format("curveTo %.2f %.2f, %.2f %.2f, %.2f %.2f\n", x1, y1, x2, y2, x3, y3));
    }
    
    @Override
    public Point2D getCurrentPoint() throws IOException
    {
        // if you want to build paths, you'll need to keep track of this like PageDrawer does
        return new Point2D.Float(0, 0);
    }
    
    @Override
    public void closePath() throws IOException
    {
        log.finest("closePath");
    }
    
    @Override
    public void endPath() throws IOException
    {
        log.finest("endPath");
    }
    
    void pushCurrent() {
    	/*Rectangle r = region.getRegion();
    	if(r != null) {
    		cluster.pushRegion(r);
    		region.reset();
    	}*/
    }
    
    @Override
    public void strokePath() throws IOException
    {
    	
    	//pushCurrent();o

	Iterable<Shape> elems = path.getIterable();
	int linCnt = 0;
	for(Shape s : elems) {
		if(s instanceof Line2D) {
			Line2D l = (Line2D) s;
			lines.add(buildLine(l.getP1(), l.getP2()));
			linCnt++;
		} else if(s instanceof Rectangle2D) {
			//TODO
		}
	}
	strokeCount += linCnt;
        log.finest("strokePath("+linCnt+")");
	path.clear();
    }
    
    @Override
    public void fillPath(int windingRule) throws IOException
    {
    	pushCurrent();
    	//strokePath();
    	
    	/*
    	 * TODO:
    	 * - detect closed sub-paths (repeated point...)
    	 * - detect rectangles.
    	 * 	 - detect thin rectangles -> generate lines.
    	 */
    	PDGraphicsState gs = this.getGraphicsState();
    	PDColor clr = gs.getStrokingColor();
        log.finest("fillPath (color="+clr.toRGB()+")");
        PDColor nsclr = gs.getNonStrokingColor();
        log.finest("         (non stroking="+nsclr.toRGB()+")");
        
        log.finest("         (path:"+path.numElements()+")");
    	Iterable<Shape> elems = path.getIterable();
    	int linCnt = 0;
    	for(Shape s : elems) {
    		if(s instanceof Line2D) {
    			//TODO
    		} else if(s instanceof Rectangle2D) {
    			//TODO
    			Rectangle2D rect = (Rectangle2D) s;
    			if(isVerticalStrip(rect,properties.maxLineThickness)) {
    				if(nsclr.toRGB() == 0) {
    					//tmaker.add(buildVertLine(rect));
    					lines.add(buildVertLine(rect));
    					this.lineCount++;
    				} else {
    					log.finest("Non black vert. line");
    				}
    			} else if(isHorizontalStrip(rect,properties.maxLineThickness)){
    				if(nsclr.toRGB() == 0) {
    					//tmaker.add(buildHorizLine(rect));
    					lines.add(buildHorizLine(rect));
    					this.lineCount++;
    				} else {
    					log.finest("Non black horiz. line");
    				}
    			}
    		}
    		
    	}

        path.clear();
    }
    
    public static boolean isVerticalStrip(Rectangle2D rect, double threshold) {
    	return (rect.getWidth() < threshold);
    }
    
    public static boolean isHorizontalStrip(Rectangle2D rect, double threshold) {
    	return (rect.getHeight() < threshold);
    }

    
    @Override
    public void fillAndStrokePath(int windingRule) throws IOException
    {
        log.finest("fillAndStrokePath");
    }
    
    @Override
    public void shadingFill(COSName shadingName) throws IOException
    {
        log.finest("shadingFill " + shadingName.toString());
    }
    
    
    void dbg_showbytes(byte data[]) {
    	for(byte b: data) {
    		log.finest(String.format("0x%x ",b));
    	}
    	
		//log.finest();
    }
    
    /**
     * Overridden from PDFStreamEngine.
     */
    
    
    @Override
    public void showTextString(byte[] string) throws IOException
    {
        //System.out.print("showTextString \"");
        super.showTextString(string);
        //System.out.println("\"");
        //COSArray str = new COSArray();
        
        Rectangle r = regionText.getRegion();
        if(r == null) {
        	System.err.println("No region");
        	throw new NullPointerException("regionText.region");
        }
        gstrings.add(new GraphicString(regionText.getText(), r));
        log.fine("add string:"+regionText.getText());
         
        regionText.reset();
    }
	

    /**
     * Overridden from PDFStreamEngine.
     */
    @Override
    public void showTextStrings(COSArray array) throws IOException
    {
        //System.out.print("showTextStrings \"");
        super.showTextStrings(array);
        //System.out.println("\"");

        Rectangle r = regionText.getRegion();
        if(r == null) {
        	System.err.println("No region");
        	throw new NullPointerException("regionText.region");
        }
        if(properties.clip.clip(r)) {
        	gstrings.add(new GraphicString(regionText.getText(), r));
        	log.fine("add strings: " +regionText.getText());
        }
        
        regionText.reset();

        //log.finest("  "+r.toString());
  
    }
    /**
     * Overridden from PDFStreamEngine.
     */
    @Override
    protected void showGlyph(Matrix textRenderingMatrix, PDFont font, int code, String unicode,
                             Vector displacement) throws IOException
    {
        //System.out.print(unicode);

        //showVector(displacement);
        //Vector w = font.getDisplacement(code);
       	//showVector(w);

        super.showGlyph(textRenderingMatrix, font, code, unicode, displacement);

        // from CustomPageDrawer
        // bbox in EM -> user units
        Shape bbox = new Rectangle2D.Float(0, 0, font.getWidth(code) / 1000, 1);
        AffineTransform at = textRenderingMatrix.createAffineTransform();
        bbox = at.createTransformedShape(bbox);
        
        regionText.add(unicode);
        regionText.add(transfRect(bbox.getBounds()));

    }
    
    protected void showVector(Vector d)
    {
    	System.out.print(d.toString());
    }	
    
    // NOTE: there are may more methods in PDFStreamEngine which can be overridden here too.
}
