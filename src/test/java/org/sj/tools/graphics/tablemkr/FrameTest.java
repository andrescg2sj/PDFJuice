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
package org.sj.tools.graphics.tablemkr;

import static org.junit.Assert.assertEquals;


import java.awt.geom.Rectangle2D;

import org.junit.Test;
import org.sj.tools.graphics.tablemkr.Area;
import org.sj.tools.graphics.tablemkr.CellLocation;
import org.sj.tools.graphics.tablemkr.Frame;
import org.sj.tools.graphics.tablemkr.RectArea;


public class FrameTest {

	@Test
	public void testingAreaToCell() {
		int x[] = {10, 50, 200};
		int y[] = {10, 100};
		
		Area a = new RectArea(new Rectangle2D.Float(10,10,40,90));
		Area b = new RectArea(new Rectangle2D.Float(50,10,140,90));
		
		Frame frame = new Frame(x,y);
		CellLocation cla = frame.areaToCellLoc(a, 5);
		CellLocation clb = frame.areaToCellLoc(b, 5);
		assertEquals("col a", 0, cla.getCol());
		assertEquals("row a", 0, cla.getRow());
		assertEquals("col b", 1, clb.getCol());
		assertEquals("row b", 0, clb.getRow());
		
	}
}
