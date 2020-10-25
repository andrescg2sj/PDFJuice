package org.sj.tools.graphics.sectorizer;


import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.sj.tools.graphics.sectorizer.geom.ExpandTransform;
import org.sj.tools.graphics.sectorizer.geom.MultiplyTransform;

public class ContentRegion<E extends Positionable> implements Positionable {

	Rectangle2D region;
	Vector<E> contents;
	
	public static double min(double a, double  b, double  c, double d) {
		return Math.min(Math.min(a, b), Math.min(c, d));
	}

	public static double max(double a, double  b, double c, double d) {
		return Math.max(Math.max(a, b), Math.max(c, d));
	}

	public static Rectangle2D rectangleFromPoints(Point2D p0, Point2D p1, Point2D p2, Point2D p3) {
		Point2D a = new Point2D.Double(
				min(p0.getX(),p1.getX(),p2.getX(),p3.getX()),
				min(p0.getY(),p1.getY(),p2.getY(),p3.getY()) );
		
		Point2D z = new Point2D.Double(
				max(p0.getX(),p1.getX(),p2.getX(),p3.getX()),
				max(p0.getY(),p1.getY(),p2.getY(),p3.getY()));
		return new Rectangle2D.Double(a.getX(),a.getY(),
				(z.getX()-a.getX()), (z.getY()-a.getY()));
		
	}
	
	public boolean isEmpty() 
	{
		return contents.isEmpty();
	}
	
	public ContentRegion(E obj, double border) {
		region = ExpandTransform.expandRect(obj.getBounds(), border);
		contents = new Vector<E>();
		contents.add(obj);
	}
	
	public ContentRegion(ContentRegion<E> cr) {
		region = cr.region;
		contents = cr.contents;
	}

	
	public ContentRegion(Rectangle2D r) {
		region = r;
		contents = new Vector<E>();
	}
	
	
	public boolean contains(Positionable gs) {
		return region.contains(gs.getBounds());
	}

	
	public boolean contains(Point2D p) {
		return region.contains(p);
	}
	
	public static double area(Rectangle2D rect) {
		return rect.getWidth()*rect.getHeight();
	}
	
	public boolean containsMost(Positionable r) {
		return containsMost(r.getBounds());
	}
	
	public static double sharedArea(Rectangle2D a, Rectangle2D b) {
		if(!a.intersects(b)) return 0;
		Rectangle2D i = a.createIntersection(b);
		return area(i)/area(b);
		
	}
	
	public static boolean containsMost(Rectangle2D a, Rectangle2D b) {
		return sharedArea(a,b) > 0.5;
		
	}
	
	public boolean containsMost(Rectangle2D rect) {
		/*
		Rectangle2D i = region.createIntersection(rect);
		if(i == null)
			return false;
		return area(i)/area(rect) > 0.5;
		*/
		return containsMost(region, rect);
	}

	public void setRegion(Rectangle2D r) {
		region = r;
	}
	
	public boolean intersects(ContentRegion<E> cr) {
		return region.intersects(cr.region);
	}
	
	public boolean adjacent(ContentRegion<E> cr) {
		return adjacent(region, cr.region);
	}
	
	public static boolean adjacent(Rectangle2D a, Rectangle2D b) {
		if(Math.min(a.getMaxX(), b.getMaxX()) >  Math.max(a.getMinX(), b.getMinX())) {
			return Math.min(a.getMaxY(), b.getMaxY()) >=  Math.max(a.getMinY(), b.getMinY());
		} else if(Math.min(a.getMaxX(), b.getMaxX()) >  Math.max(a.getMinX(), b.getMinX())) {
			return Math.min(a.getMaxX(), b.getMaxX()) >=  Math.max(a.getMinX(), b.getMinX());
		}
		return false;
	}
	
	public E get(int i) {
		return contents.get(i);
	}
 
	
	
	public int countElements()
	{
		return contents.size();
	}
	
	public void add(ContentRegion cr) {
		contents.addAll(cr.contents);
		region.add(cr.region);
	}

	
	public void add(E gs) {
		contents.add(gs);
	}
	
	//public static String getText(GraphicsString )
	
	
	public void mutiply(double factor) {
		region = MultiplyTransform.multiplyRect(region, factor);
	}
	
	public static Rectangle2D cloneRect2D(Rectangle2D r) {
		return new Rectangle2D.Double(r.getMinX(),r.getMinY(),r.getWidth(),r.getHeight());
	}
	
	
	public void sortContents(Comparator<Positionable> c) {
		contents.sort(c);
	}

	public void sortContents() {
		sortContents(NormalComparator.getInstance());
	}

	
	public void sortReverseY() {
		sortContents(ReverseYComparator.getInstance());
	}
	
	@Deprecated
	public void sortReverseY_old() {

		for(int i=0; i< contents.size(); i++) {
			
			E r = contents.get(i);
			
			for(int j=i+1; j<contents.size();j++) {
				E s = contents.get(j);
	
				//TODO: possible useless swaps when reverse sorting.
				if(previousPosRY(s,r) ) {
					//FIXME: Is this clear enough? Good design?
					swap(contents,i,j);
					r = contents.get(i);
				}
			}
			
		}
	}
	
	@Deprecated
	public void sortContents(boolean reverse) {
		//TODO: See https://stackoverflow.com/questions/2072032/what-function-can-be-used-to-sort-a-vector
		// See Comparable, Comparator
		
		/* bubble algorithm. Low efficiency, but acceptable for this purpose */
		for(int i=0; i< contents.size(); i++) {
			
			E r = contents.get(i);
			
			for(int j=i+1; j<contents.size();j++) {
				E s = contents.get(j);
	
				//TODO: possible useless swaps when reverse sorting.
				if(NormalComparator.previousPos(s,r) ^ reverse) {
					//FIXME: Is this clear enough? Good design?
					swap(contents,i,j);
					r = contents.get(i);
				}
			}
			
		}
		
	}


	@Override
	public Point2D getPosition() {
		return new Point2D.Double(region.getX(),region.getY());	
	}
	
	public Rectangle2D getBounds() {
		return region;
	}
	
	public Iterator<E> contentIterator()
	{
		return contents.iterator();
	}

	public static <E> void swap(Vector<E> v, int i, int j) {
		E s = v.get(i);
		v.set(i, v.get(j));
		v.set(j, s);
	}

	public static boolean previousPosRY(Positionable a, Positionable b) {
		return (a.getPosition().getY() > b.getPosition().getY()) ||
				(a.getPosition().getY() == b.getPosition().getY()) &&
				(a.getPosition().getX() < b.getPosition().getX());
	
	}

}
