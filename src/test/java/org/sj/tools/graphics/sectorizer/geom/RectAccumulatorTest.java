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

import java.util.List;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

public class RectAccumulatorTest {

    @Test
    public void testingAccumulateX() {
	List<Rectangle2D> rects = new LinkedList<Rectangle2D>();
	Rectangle2D a = new Rectangle2D.Double(0,0,20,10);
	Rectangle2D b = new Rectangle2D.Double(10,0,20,20);

	rects.add(a);
	rects.add(b);
	List<Rectangle2D> result = RectAccumulator.accumulateX(rects);

	Assert.assertEquals("number of rects", 3, result.size());

    }

}
