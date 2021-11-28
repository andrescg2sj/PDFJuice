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
package org.sj.tools.graphics.tablemkr.frompdf;

import org.junit.Test;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.junit.Assert;


public class ExtractionPropertiesTest {
	
	
	@Test
	public void testingFilterColor() {
		int black = 0;
		int white = 0xffffff;
		ExtractionProperties ep = new ExtractionProperties();
		
		Assert.assertTrue(ep.filterColor(black));
		Assert.assertFalse(ep.filterColor(white));
		
	}
}
