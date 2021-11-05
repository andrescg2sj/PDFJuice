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
package org.sj.tools.pdfjuice.slide;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.sj.tools.pdfjuice.CommonInfo;
import org.sj.tools.pdfjuice.slide.html.HtmlSldObject;

public class Slide implements CommonInfo {
	
	protected List<SldObject> objects;
	
	public Slide() {
		objects = new LinkedList<SldObject>();
	}
	
	public Slide(Slide s) {
		objects = s.objects;
	}
	
	public void add(SldObject obj) {
		objects.add(obj);
	}
	
	public Iterator<SldObject> objIterator() {
		return objects.iterator();
	}

	public int countElems()
	{
		return objects.size();
	}
	
	public SldTitle getTitle() {
		for(SldObject obj: objects) {
			if(obj instanceof SldTitle) {
				return (SldTitle) obj;
			}
		}
		return null;
	}
	
	public String plainText() {
		StringBuilder builder = new StringBuilder();
		for(SldObject obj: objects) {
			if(obj instanceof SldText) {
				SldText stext = (SldText) obj; 
				builder.append(stext.getText()+ NEW_LINE);
			}
		}
		return builder.toString();
	}
	
	//TODO: move to package .html
	public String toHTML()
	{
		StringBuilder strb = new StringBuilder();
		strb.append("<div>");
		
		for(SldObject obj: objects) {
			strb.append(obj.toHTML());
		}
		strb.append("</div>");
		return strb.toString();

	}

}
