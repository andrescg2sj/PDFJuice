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

import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

public class TestReverseYComparator {
	@Test
	public void testingCompare() 
	{
		GraphicString sa = TestNormalComparator.buildGString("a", 60,20);
		GraphicString sb = TestNormalComparator.buildGString("b", 40,40);
		GraphicString sc = TestNormalComparator.buildGString("c", 20,60);
		Vector<Positionable> vect = new Vector<Positionable>();
		vect.add(sa);
		vect.add(sb);
		vect.add(sc);
		
		ReverseYComparator nc = ReverseYComparator.getInstance(); 
		vect.sort(nc);
		Assert.assertEquals("string c", sc, vect.get(0));
		Assert.assertEquals("string b", sb, vect.get(1));
		Assert.assertEquals("string a", sa, vect.get(2));
	}
	

	@Test
	public void testingCompareHoriz() 
	{
		GraphicString sa = TestNormalComparator.buildGString("a", 20,10);
		GraphicString sb = TestNormalComparator.buildGString("b", 40,10);
		GraphicString sc = TestNormalComparator.buildGString("c", 60,10);
		Vector<Positionable> vect = new Vector<Positionable>();
		vect.add(sc);
		vect.add(sb);
		vect.add(sa);
		
		ReverseYComparator nc = ReverseYComparator.getInstance();
				
		vect.sort(nc);
		Assert.assertEquals("string a", sa, vect.get(0));
		Assert.assertEquals("string b", sb, vect.get(1));
		Assert.assertEquals("string c", sc, vect.get(2));
	}
	

}
