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
import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;


public class TestNormalComparator {
	
	public static GraphicString buildGString(String text, double x, double y) {
		Rectangle2D r = new Rectangle2D.Double(x, y, 10,10);
		return new GraphicString(text, r);
	}
	
	@Test
	public void testingCompare() 
	{
		GraphicString sa = buildGString("a", 60,20);
		GraphicString sb = buildGString("b", 40,40);
		GraphicString sc = buildGString("c", 20,60);
		Vector<Positionable> vect = new Vector<Positionable>();
		vect.add(sc);
		vect.add(sb);
		vect.add(sa);
		
		NormalComparator nc = NormalComparator.getInstance(); 
		vect.sort(nc);
		Assert.assertEquals("string a", sa, vect.get(0));
		Assert.assertEquals("string b", sb, vect.get(1));
		Assert.assertEquals("string c", sc, vect.get(2));
	}
	

	@Test
	public void testingCompareHoriz() 
	{
		GraphicString sa = buildGString("a", 20,10);
		GraphicString sb = buildGString("b", 40,10);
		GraphicString sc = buildGString("c", 60,10);
		Vector<Positionable> vect = new Vector<Positionable>();
		vect.add(sc);
		vect.add(sb);
		vect.add(sa);
		
		NormalComparator nc = NormalComparator.getInstance();
		vect.sort(nc);
		Assert.assertEquals("string a", sa, vect.get(0));
		Assert.assertEquals("string b", sb, vect.get(1));
		Assert.assertEquals("string c", sc, vect.get(2));
	}
	

}
