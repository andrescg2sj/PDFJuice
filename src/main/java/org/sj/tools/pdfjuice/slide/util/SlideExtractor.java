/*
 * Apache License
 *
 * Copyright (c) 2021 Andres Gonzalez SJ
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

package org.sj.tools.pdfjuice.slide.util;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
//import java.util.Iterator;

import org.sj.tools.graphics.sectorizer.GraphicString;
import org.sj.tools.graphics.sectorizer.ContentRegion;


import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.Vector;
import org.sj.tools.graphics.pdf.PDFString;
import org.sj.tools.graphics.sectorizer.StringRegion;
import org.sj.tools.graphics.sectorizer.StrRegionCluster;
import org.sj.tools.pdfjuice.slide.SldImage;
import org.sj.tools.pdfjuice.slide.SldText;
import org.sj.tools.pdfjuice.slide.Slide;
import org.sj.tools.pdfjuice.slide.build.SlideBuilder;
import org.sj.tools.graphics.elements.ClippingArea;
import org.sj.tools.graphics.elements.GrPath;
import org.sj.tools.graphics.elements.ImageFrame;

public class SlideExtractor extends PDFGraphicsStreamEngine
{
	private static Logger log = Logger.getLogger("org.sj.tools.pdfjuice.slide.util.SlideExtractor");
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
	
    PDFStringBuffer regionText = new PDFStringBuffer();
	StrRegionCluster cluster = new StrRegionCluster();
	java.util.Vector<ImageFrame> images = new java.util.Vector<ImageFrame>();
	
	int rectCount = 0;
	int lineCount = 0;
	int strokeCount = 0;
	
	ClippingArea clip;
    GrPath path = new GrPath();
    Rectangle2D bounds;
	
	//java.util.Vector<Line> lines = new java.util.Vector<Line>();
	//TableMaker tmaker = new TableMaker();
	
	
    
    public void writeHTML(String filename) throws IOException {
    	File f = new File(filename);
    	FileOutputStream fos = new FileOutputStream(f);
    	//String s = cluster.toHTML();

    	//TODO: Make advanced  cluster that writes different html tags for different fonts 
    	String s = cluster.toHTML();
    	fos.write(s.getBytes());
    	fos.close();
    	
    }

    
    /**
     * Constructor.
     *
     * @param page PDF Page
     */
    protected SlideExtractor(PDPage page)
    {
        super(page);
        clip = new ClippingArea();
    }
    
    /**
     * Constructor.
     *
     * @param page PDF Page
     */
    public SlideExtractor(PDPage page, Rectangle clipRect)
    {
        super(page);
        clip = new ClippingArea(clipRect);
    }

    public Slide makeSlide(SlideBuilder sbuild) {
    	Slide s = sbuild.build(cluster);
    	for(ImageFrame img: images) {
    		SldImage si = new SldImage(img);
    		s.add(si);
    	}
    	return s;
    }
    
    public int countImages() {
    	return images.size();
    }
    
    public ImageFrame getImage(int n) {
    	return images.elementAt(n);
    }

    //Deprecated?
    public Slide makeSlide()
    {
    	Slide s = new Slide();
    	//TODO
    	
    	cluster.partitionContent(0.5f);
    	for(int i =0; i< cluster.getNumberOfRegions(); i++) {
    		ContentRegion<GraphicString> cr = cluster.getRegion(i);
    		
    		Iterator<GraphicString> it = cr.contentIterator();
    		while(it.hasNext()) {
    			GraphicString gs = it.next();
    			
    	        PDFString ps; 
    	        if(gs instanceof PDFString) {
    	        	ps = (PDFString) gs;
    	        } else {
    	        	//TODO
    	        	ps = new PDFString(gs, null); 
    	        }
    	        SldText stext = new SldText(ps);
    	        
    	        s.add(stext);   	        

    		}
    	}
    	return s;
    }

    
    /**
     * Runs the engine on the current page.
     * 
     *
     * @throws IOException If there is an IO error while drawing the page.
     */
    public void run() throws IOException
    {
    	/* TODO: This interface needs redesign. Rather than being called isolated,
     * this should be called either form the constructor, or from a method that 
     * returns some data. */

    	
        processPage(getPage());
        for (PDAnnotation annotation : getPage().getAnnotations())
        {
            showAnnotation(annotation);
        }
        
        
        //showStats();
        //java.util.Vector<org.sj.punidos.crminer.tablemkr.Area> areas = tmaker.buildAreas();
        //logAreas(areas);
        //tmaker.toSVG(areas);
    }
    
    void showStats() {
    	log.finest("strokes:"+strokeCount);
    	log.finest("lines:"+lineCount);
    	log.finest("rectangles:"+rectCount);
    }
    
    void addPoint(double x, double y) {
    	if(bounds == null) {
    		bounds = new Rectangle2D.Double(x,y,0,0);
    	} else {
    		bounds.add(x, y);
    	}
    }
    
    void addPoint(Point2D p) {
    	addPoint(p.getX(), p.getY());
    }

    
 
    
    @Override
    public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException
    {
	//strokeRectangle(p0,p1,p2,p3);
    }

    public void strokeRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException
    {

        Rectangle2D r = StringRegion.rectangleFromPoints(p0,p1,p2,p3);
        if(clip.clip(r)) {
            log.finest(String.format("appendRectangle %.2f %.2f, %.2f %.2f, %.2f %.2f, %.2f %.2f\n",
                    p0.getX(), p0.getY(), p1.getX(), p1.getY(),
                    p2.getX(), p2.getY(), p3.getX(), p3.getY()));
            /* add rectangle */
            /*
            tmaker.add(new Line(p0, p1));
            tmaker.add(new Line(p1, p2));
            tmaker.add(new Line(p2, p3));
            tmaker.add(new Line(p3, p0));
            */
            rectCount++;
            
            addPoint(p0);
            addPoint(p1);
            addPoint(p2);
            addPoint(p3);

        } else {
        	log.finest("discarded rect");
        }
        //cluster.pushRegion(r);
    }
    
    @Override
    public void drawImage(PDImage pdImage) throws IOException
    {
        log.finest("drawImage");
        ImageFrame img = new ImageFrame(path.getPosition(), pdImage.getImage());
        images.add(img);
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
        addPoint(x,y);

        log.finest(String.format("moveTo %.2f %.2f\n", x, y));
    }
    @Override
    public void lineTo(float x, float y) throws IOException
    {
    	//region.add(new Point((int)x,(int)y));
    	lineCount++;
    	path.lineTo(new Point2D.Float(x,y));
    	addPoint(x,y);

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

	Iterable<Shape> lines = path.getIterable();
	int linCnt = 0;
	for(Shape s : lines) {
		if(s instanceof Line2D) {
			//tmaker.add(new Line(l.getP1(), l.getP2()));
			linCnt++;
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
        log.finest("fillPath");
        
        if(log.getLevel() == Level.FINEST) {
        	PDGraphicsState gs = this.getGraphicsState();
        	PDColor clr = gs.getNonStrokingColor();
        	//gs.getNonStrokingColor()
        	log.finest("Stroking color: " +clr.toRGB());
        }
        
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
    /**
     * Overridden from PDFStreamEngine.
     */
    @Override
    public void showTextString(byte[] string) throws IOException
    {
        super.showTextString(string);
        
        Rectangle r = regionText.getRegion();
        if(r == null) {
        	System.err.println("No region");
        	throw new NullPointerException("regionText.region");
        }

        cluster.push(regionText.makeString());
        
        regionText.reset();
        
    }
    /**
     * Overridden from PDFStreamEngine.
     */
    @Override
    public void showTextStrings(COSArray array) throws IOException
    {
        
        super.showTextStrings(array);

        Rectangle r = regionText.getRegion();
        if(r == null) {
        	System.err.println("No region");
        	throw new NullPointerException("regionText.region");
        }
        
        if(cluster == null)
        	throw new NullPointerException("cluster");
        if(regionText == null) 
        	throw new NullPointerException("regionText");
        cluster.push(regionText.makeString());

        regionText.reset();
    }
    
    /**
     * Overridden from PDFStreamEngine.
     */
    @Override
    protected void showGlyph(Matrix textRenderingMatrix, PDFont font, int code, String unicode,
                             Vector displacement) throws IOException
    {

        super.showGlyph(textRenderingMatrix, font, code, unicode, displacement);

        // from CustomPageDrawer
        // bbox in EM -> user units
        Shape bbox = new Rectangle2D.Float(0, 0, font.getWidth(code) / 1000, 1);
        AffineTransform at = textRenderingMatrix.createAffineTransform();
        bbox = at.createTransformedShape(bbox);
        
        
        regionText.add(unicode);
        regionText.add(bbox.getBounds());
        regionText.setFont(font);

    }
    
    protected void showVector(Vector d)
    {
    	System.out.print(d.toString());
    }	
    
    public Rectangle2D getBounds() 
    {
    	//return cluster.getBounds();
    	return bounds;
    }
    
}

