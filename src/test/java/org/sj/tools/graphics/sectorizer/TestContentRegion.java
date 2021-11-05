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


public class TestContentRegion {

	@Test
	public void testingSharedArea() {
		Rectangle2D a = new Rectangle2D.Double(10,10, 20,10);
		Rectangle2D b = new Rectangle2D.Double(20,10, 20,10);
		Rectangle2D c = new Rectangle2D.Double(35,10, 20,10);
		
		Assert.assertEquals("shared a-b", 0.5, ContentRegion.sharedArea(a, b),0.01);
		Assert.assertEquals("shared a-c", 0, ContentRegion.sharedArea(a, c),0.01);
	}
}
