package org.sj.tools.graphics.sectorizer;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;



public class PosRegionCluster<E extends Positionable>  {
	
	Vector<ContentRegion<E>> regions;
	Vector<E> remaining; 
	
	public PosRegionCluster() {
		regions = new Vector<ContentRegion<E>>();
		remaining = new Vector<E>();
	}
	
	public PosRegionCluster(PosRegionCluster<E> prc) {
		regions = prc.regions;
		remaining = prc.remaining;
	}

	public E getRemaining(int i) {
		return remaining.get(i);
	}

	
	
	
	public ContentRegion<E> push(E gs) {
		ContentRegion<E> selected = getContainingRegion(gs);
		if(selected != null) {
			selected.add(gs);
		} else {
			remaining.add(gs);
		}
		return selected;
	}
	
	public Vector<PosRegionCluster<E>> divideY(double y) {
		Vector<PosRegionCluster<E>> vect =  new Vector<PosRegionCluster<E>>(2);
		PosRegionCluster<E> below = new PosRegionCluster<E>(); 
		PosRegionCluster<E> above = new PosRegionCluster<E>(); 

		for(ContentRegion<E> cr: regions) {
			Rectangle2D r = cr.getBounds();
			if(r.getMinY() > y) {
				above.pushRegion(cr);
			} else {
				below.pushRegion(cr);
			}
		}
		
		for(E obj: remaining) {
			Rectangle2D r = obj.getBounds();
			if(r.getMinY() > y) {
				above.push(obj);
			} else {
				below.push(obj);
			}
		}
		vect.add(below);
		vect.add(above);
		
		return vect;
	}
	
	public ContentRegion<E> getContainingRegion(E gs) {
		ContentRegion<E> selected = null;
		for(ContentRegion<E> r: regions) {
			if(r.contains(gs)) {
				if(selected == null || selected.contains(r)) {
					selected = r;
				}
			}
		}
		return selected;
	}
	
	public static Rectangle2D cloneRect2D(Rectangle2D rect) {
		Object robj = rect.clone();
		if(!(robj instanceof RectangularShape)) {
			return null;
		}
		RectangularShape rs = (RectangularShape) robj;
		return rs.getBounds2D();
	
	}
	
	public static Rectangle2D cloneBounds(Positionable sobj) {
		return cloneRect2D(sobj.getBounds());
	}

	
	public static Rectangle2D getBounds(Collection<Positionable> col) {
		Rectangle2D r = null; 
		for(Positionable pos: col) {
			Rectangle2D bounds = pos.getBounds();
			if(r == null) {
				r = cloneRect2D(bounds);
			} else if(bounds != null) {
				r.add(bounds);
			}
		}
		return r;

	}

	public Rectangle2D getBounds() {
		LinkedList<Positionable> list = new LinkedList<Positionable>();
		list.addAll(regions);
		list.addAll(remaining);
		Rectangle2D r = getBounds(list);
		return r;
	}

	public void pushRegion(Rectangle2D r) {
		//ContentRegion<E><E> cr = new ContentRegion<E><E>(r);
		ContentRegion<E> cr = new ContentRegion<E>(r);
		pushRegion(cr);
	}
	
	/**
	 * Add content to region if most part of the content falls inside region.
	 * @param cr
	 */
	public void flexiblePushRegion(ContentRegion<E> cr) {
		int i=0;
		while(i < remaining.size()) {
			E s = remaining.get(i);
			if(cr.containsMost(s)) {
				cr.add(s);
				remaining.remove(i);
			} else {
				i++;
			}
		}
		regions.add(cr);
	}
	

	
	public void pushRegion(ContentRegion<E> cr) {
		// TODO: optimize?
		int i=0;
		while(i < remaining.size()) {
			E s = remaining.get(i);
			if(cr.contains(s)) {
				cr.add(s);
				remaining.remove(i);
			} else {
				i++;
			}
		}
		regions.add(cr);
	}
	
	public int getNumberOfRegions()
	{
		return regions.size();
	}

	public ContentRegion<E> getRegion(int i)
	{
		return regions.get(i);
	}
	
	public int countRemaining()
	{
		return remaining.size();
	}

	
	
	

	/*
	public void filterOutEmptyRegions() 
	{
		int i = 0;
		while(i < regions.size()) {
			ContentRegion<E> r = regions.get(i);
			if(r.isEmpty()) {
				regions.removeElementAt(i);
			} else {
				i++;
			}
		}
	}*/

	public void filterOutEmptyRegions() 
	{
		ListIterator<ContentRegion<E>> it = regions.listIterator();
		while(it.hasNext()) {
			ContentRegion<E> r = it.next();
			if(r.isEmpty()) {
				it.remove();
			}
		}
	}

	
	public void sortRegions()
	{
		sortRegions(false);
	}
	
	public void sortRegions(Comparator<Positionable> comp) {
		this.regions.sort(comp);
	}

	public void sortReverseYRegions() {
		sortRegions(ReverseYComparator.getInstance());
	}

	
	//TODO
	@Deprecated
	public void sortRegions(boolean reverse) {
		//TODO: See https://stackoverflow.com/questions/2072032/what-function-can-be-used-to-sort-a-vector
		// See Comparable, Comparator
		
		/* bubble algorithm. Low efficiency, but acceptable for this purpose */
		for(int i=0; i< regions.size(); i++) {
			
			ContentRegion<E> r = regions.get(i);
			
			for(int j=i+1; j<regions.size();j++) {
				ContentRegion<E> s = regions.get(j);
	
				//TODO: possible useless swaps when reverse sorting.
				if(NormalComparator.previousPos(s,r) ^ reverse) {
					//FIXME: Is this clear enough? Good design?
					ContentRegion.swap(regions,i,j);
					r = regions.get(i);
				}
			}
			
		}
		
	}
	
	/**
	 * Remove all regions, but preserve contents as remaining.
	 */
	
	public void clearRegions() {
		for(ContentRegion<E> cr: regions) {
			remaining.addAll(cr.contents);
		}
		regions.clear();
	}
	
	
	
	
	/*
	private boolean previous(ContentRegion<E> r, ContentRegion<E> s) {
		ContentRegion<Positionable> a = (ContentRegion<Positionable>) r;
		ContentRegion<Positionable> b = s;
		return previousPos(a,b);
	}*/

	public ContentRegion<E> extract(int i) {
		ContentRegion<E> cr = regions.get(i);
		regions.remove(i);
		return cr;
	}
	
	public void meltRegions() {
		int i = 0;
		while(i < regions.size()) {
			meltWithFollowing(i);
			i++;
		}
	}
	
	
	public void transformRegions(RectTransformation rt) {
		for(ContentRegion<E> cr: regions ) {
			cr.setRegion(rt.transform(cr.getBounds()));
		}
	}
	
	public void meltWithFollowing(int i) {
		//System.out.println("mwf:"+i);
		ContentRegion<E> r = regions.get(i);
		int j = i+1;
		while(j < regions.size()) {
			ContentRegion<E> s = regions.get(j);
			if(r.intersects(s) || r.adjacent(s)) {
				//System.out.println("  inters:"+j);
				r.add(s);
				regions.remove(j);
			} else {
				//System.out.println("  no inter:"+j);
				j++;
			}
		}
	}
	
	public void pushRegionForRemaining()
	{
		LinkedList<Positionable> list = new LinkedList<Positionable>();
		list.addAll(remaining);
		Rectangle2D r = getBounds(list);
		pushRegion(r);
	}
	
	@Deprecated
	public void remainingToRegions(double threshold)
	{
		Vector<ContentRegion<E>> newRegions = new Vector<ContentRegion<E>>();
		for(E obj: remaining) {
			Rectangle2D r = ExpandTransform.expandRect(obj.getBounds(), threshold);
			ContentRegion<E> cr = new ContentRegion<E>(r);
			cr.add(obj);
			newRegions.add(cr);
		}
		remaining.clear();
		regions.addAll(newRegions);
	}
	
	public void remainingToRegions(RectTransformation transf)
	{
		Vector<ContentRegion<E>> newRegions = new Vector<ContentRegion<E>>();
		for(E obj: remaining) {
			Rectangle2D r = transf.transform(obj.getBounds());
			ContentRegion<E> cr = new ContentRegion<E>(r);
			cr.add(obj);
			if(cr.countElements() == 0) {
				throw new IllegalStateException("0 elements");
			}
			newRegions.add(cr);
			
		}
		remaining.clear();
		regions.addAll(newRegions);
	}

	
	public void partitionContent(double threshold)
	{
		partitionContent(threshold, false);
	}
	
	
	public void partitionContent(RectTransformation aggregator, Comparator<Positionable> comp)
	{
		remainingToRegions(aggregator);
		sortRegions(comp);

		System.out.println("Before melting: "+regions.size());

		meltRegions();
		System.out.println("After melting: "+regions.size());
		
	}
	
	

	@Deprecated
	public void partitionContent(double threshold, boolean reverse)
	{
		remainingToRegions(threshold);
		sortRegions(reverse);
		//sortRegions(ReverseYComparator.getInstance());
		//sortRegions(NormalComparator.getInstance());
		/*System.out.println("Sorted: ");
		for(ContentRegion<E> cr: regions) {
			System.out.println("  "+cr.getBounds());
		}*/
		System.out.println("Before melting: "+regions.size());
		/*
		int last = regions.size()+1;
		while(regions.size() < last) {
			System.out.println("  melting: "+regions.size());
			last = regions.size();
			meltRegions();
		}*/
		meltRegions();
		System.out.println("After melting: "+regions.size());
	}
	
	public void fillRegionGaps() {
		sortRegions();
		for(int i=0; i<(regions.size()-1);i++) {
			Rectangle2D current = regions.get(i).getBounds();
			Rectangle2D next = regions.get(i+1).getBounds();

			if(current.getMaxX() < next.getMinX() || current.getMaxY() < next.getMinY()) {
				Rectangle2D rect = ContentRegion.cloneRect2D(current);
				rect.add(regions.get(i+1).getPosition());
				regions.get(i).setRegion(rect);
			}
		}
		
	}

	public void fillRegionGapsBackwards() {
		sortRegions();
		for(int i=1; i<regions.size();i++) {
			Rectangle2D previous= regions.get(i-1).getBounds();
			Rectangle2D current = regions.get(i).getBounds();

			if(previous.getMaxX() < current.getMinX() || previous.getMaxY() < current.getMinY()) {
				Rectangle2D rect = ContentRegion.cloneRect2D(current);
				rect.add(new Point2D.Double(previous.getMaxX(), previous.getMaxY()));
				regions.get(i).setRegion(rect);
			}
		}
		
	}

	


}
