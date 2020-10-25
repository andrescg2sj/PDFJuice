package org.sj.tools.graphics.sectorizer.geom;

public class Segmenter {
	
	double limits[];
	
	public Segmenter(NumberVector vector) {
		limits = vector.toArray();
	}

	public Segmenter(double values[]) {
		limits = sortedWithoutDuplicates(values);
	}
	
	public Segmenter(int values[]) {
		limits = sortedWithoutDuplicates(values);
	}

	private double[] sortedWithoutDuplicates(int values[]) {
		return new NumberVector(values).toArray();
	}
	
	private double[] sortedWithoutDuplicates(double values[]) {
		return new NumberVector(values).toArray();
	}
	


	

	public int getSegment(double value) {
		if(value < limits[0]) return -1;
		for(int i = limits.length-1;i>=0; i--) {
			if(value >= limits[i]) {
				return i;
			}
		}
		throw new IllegalStateException("Debug getSegment");
	}
	
	public int getClosestIndex(double value) {
		double min_dist = Math.abs(limits[0] - value);
		int closest = 0;
		for(int i=1;i<limits.length;i++) {
			double dist = Math.abs(limits[i] - value);
			if(dist < min_dist) {
				closest = i;
				min_dist = dist;
			}
			if(dist > min_dist) {
				return closest;
			}
		}
		return closest;
	}
	
    public int getLastIndexBelow(double value) 
    {
    	int i=0;
    	if(value < limits[0]) {
    		//throw new IllegalArgumentException("Value is too low:" +value);
    		//TODO: log
    		System.err.println("Value is too low: "+value
    				+ ". Min is: "+limits[0]);
    		return 0;
    	}
    	
    	if(value > limits[limits.length-1]) {
    		/*throw new IllegalArgumentException*/
    		//TODO: log
    		System.err.println("Value is too high: "+value
    				+ ". Max is: "+limits[limits.length-1]);
    		//FIXME
    		return limits.length-1;
    	}
    	while((i <= limits.length) && (limits[i] < value)) {
    		i++;
    	}
    	return i-1;
    }
    
    public int reverseIndex(int i) {
    	return limits.length-2-i;
    }
    
    public int getFirstIndexAbove(double value)
    {
    	return 1 + getLastIndexBelow(value);
    }
    
    public double getEdge(int i) {
    	return limits[i];
    }


    public int countEdges()  {
    	return limits.length;
    }
	
}
