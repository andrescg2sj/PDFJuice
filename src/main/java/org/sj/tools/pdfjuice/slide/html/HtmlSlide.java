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
package org.sj.tools.pdfjuice.slide.html;

import org.sj.tools.pdfjuice.slide.SldObject;
import org.sj.tools.pdfjuice.slide.SldTitle;
import org.sj.tools.pdfjuice.slide.Slide;

public class HtmlSlide extends Slide {
	
	public HtmlSlide(Slide slide) {
		super(slide);
	}

	public String toHTML() {
		
		StringBuilder strb = new StringBuilder();
		strb.append("<div>");
		
		
		for(SldObject obj: objects) {
			if(obj == null) {
				strb.append("--null--");

			} else if(obj instanceof SldTitle) {
				SldTitle title = (SldTitle) obj;
				strb.append("<h2>");
				strb.append(title.getText());
				strb.append("</h2>");
			} else if(obj instanceof HtmlSldObject) {
				HtmlSldObject sobj = (HtmlSldObject) obj;
				strb.append(sobj.toHTML());
			} else {
				strb.append(obj.toString());
			}
		}
		strb.append("</div>");
		return strb.toString();
	}

}
