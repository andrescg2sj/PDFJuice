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
package org.sj.tools.graphics.sectorizer.geom;

import org.junit.Assert;
import org.junit.Test;

public class TestNumberVector {
	
	private static final double delta = 0.001;
	
	@Test
	public void testingInsert() {
		
		NumberVector v = new NumberVector();
		v.insert(3);
		v.insert(1);
		v.insert(2);
		Assert.assertEquals("first",1, v.get(0),delta); 
		Assert.assertEquals("first",2, v.get(1),delta); 
		Assert.assertEquals("first",3, v.get(2),delta);
		
		NumberVector a = new NumberVector();
		double x[] = {5, 6,1,10,4,4,7,5};
		for(int i=0; i<x.length; i++) {
			a.insert(x[i]); 
		}
		Assert.assertEquals("count", 6, a.size());
		for(int i=1; i<a.size(); i++) {
			Assert.assertTrue("a order", a.get(i) > a.get(i-1));
		}
	}

}
