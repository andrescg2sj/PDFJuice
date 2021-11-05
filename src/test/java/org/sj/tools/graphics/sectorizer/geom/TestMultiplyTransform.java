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

import java.awt.geom.Rectangle2D;

import org.junit.Assert;
import org.junit.Test;

public class TestMultiplyTransform {

	@Test
	public void testingMultiply() {
		double delta = 0.001;
		Rectangle2D r = new Rectangle2D.Double(0,0,10,10);
		
		Rectangle2D s = MultiplyTransform.multiplyRect(r, 2);
		Assert.assertEquals("2 x", -5, s.getX(), delta);
		Assert.assertEquals("2 y", -5, s.getY(), delta);
		Assert.assertEquals("2 w", 20, s.getWidth(), delta);
		Assert.assertEquals("2 h", 20, s.getHeight(), delta);
		
		s = MultiplyTransform.multiplyRect(r, 0);
		Assert.assertEquals("0 x", 5, s.getX(), delta);
		Assert.assertEquals("0 y", 5, s.getY(), delta);
		Assert.assertEquals("0 w", 0, s.getWidth(), delta);
		Assert.assertEquals("0 h", 0, s.getHeight(), delta);

		s = MultiplyTransform.multiplyRect(r, 1);
		Assert.assertEquals("1 x", 0, s.getX(), delta);
		Assert.assertEquals("1 y", 0, s.getY(), delta);
		Assert.assertEquals("1 w", 10, s.getWidth(), delta);
		Assert.assertEquals("1 h", 10, s.getHeight(), delta);
		
		s = MultiplyTransform.multiplyRect(r, 1, 0);
		Assert.assertEquals("1,0 x", 0, s.getX(), delta);
		Assert.assertEquals("1,0 y", 5, s.getY(), delta);
		Assert.assertEquals("1,0 w", 10, s.getWidth(), delta);
		Assert.assertEquals("1,0 h", 0, s.getHeight(), delta);

		s = MultiplyTransform.multiplyRect(r, 0, 1);
		Assert.assertEquals("0,1 x", 5, s.getX(), delta);
		Assert.assertEquals("0,1 y", 0, s.getY(), delta);
		Assert.assertEquals("0,1 w", 0, s.getWidth(), delta);
		Assert.assertEquals("0,1 h", 10, s.getHeight(), delta);

				
	}
}
