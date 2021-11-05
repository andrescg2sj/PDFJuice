/*
 * Apache License
 *
 * Copyright (c) 2021 Andrés González SJ
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
