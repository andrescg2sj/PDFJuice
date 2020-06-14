package org.sj.tools.graphics.sectorizer;

import java.util.Comparator;

public class NormalComparator implements Comparator<Positionable> {
	
	private static NormalComparator instance;
	
	private NormalComparator() {
	}
	
	public static NormalComparator getInstance() {
		if(instance == null) {
			instance = new NormalComparator();
		}
		return instance;
	}

	@Override
	public int compare(Positionable a, Positionable b) {
		if (a.getPosition().getY() < b.getPosition().getY()) {
			return -1;
		}else if (a.getPosition().getY() > b.getPosition().getY()) {
			return 1;
		} else {
			if (a.getPosition().getX() < b.getPosition().getX()) {
				return -1;
			} else if (a.getPosition().getX() > b.getPosition().getX()) {
				return 1;
			} 
			return 0;
		}
	}

	//TODO: better name
	public static boolean previousPos(Positionable a, Positionable b) {
		return (a.getPosition().getY() < b.getPosition().getY()) ||
				(a.getPosition().getY() == b.getPosition().getY()) &&
				(a.getPosition().getX() < b.getPosition().getX());
	
	}

}
