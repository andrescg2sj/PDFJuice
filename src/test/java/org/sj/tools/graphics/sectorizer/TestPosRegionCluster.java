package org.sj.tools.graphics.sectorizer;

import java.awt.geom.Rectangle2D;

import org.junit.Assert;
import org.junit.Test;

public class TestPosRegionCluster {

	@Test
	public void testingMeltWithFollowing() {
		PosRegionCluster<Positionable> cluster = new PosRegionCluster<Positionable>();
		
		//[x=105.0,y=331.0,w=592.0,h=36.0
		Rectangle2D r = new Rectangle2D.Double(105,331,592,36);
		Rectangle2D s = new Rectangle2D.Double(105,331,592,36);
		cluster.pushRegion(r);
		cluster.pushRegion(s);
		Assert.assertEquals("regionCount",2,cluster.getNumberOfRegions());
		cluster.meltRegions();
		Assert.assertEquals("regionCount",1,cluster.getNumberOfRegions());
		
	}


	
	@Test
	public void testingMeltManyRegions() {
		PosRegionCluster<Positionable> cluster = new PosRegionCluster<Positionable>();
		
		//[x=105.0,y=331.0,w=592.0,h=36.0
		for(int i=0; i <20;i++) {
			Rectangle2D r = new Rectangle2D.Double(105,331,592,36);
			cluster.pushRegion(r);
		}
		Assert.assertEquals("regionCount",20,cluster.getNumberOfRegions());
		cluster.meltRegions();
		Assert.assertEquals("regionCount",1,cluster.getNumberOfRegions());
		
	}

}
