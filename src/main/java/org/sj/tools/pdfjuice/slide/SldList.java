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


public class SldList extends SldGroup<SldListItem> {
	
	
	public SldList() {
		super();
	}
	
	public void addItem(SldText bullet, SldObject content) {
		elements.add(new SldListItem(bullet,content));
	}
	
	public void addItem(SldObject content) {
		SldText bullet = new SldText("*", null, null);
		elements.add(new SldListItem(bullet,content));
	}
	
	public String toHTML() {
		StringBuilder strb = new StringBuilder();
		strb.append("<ul>");
		
		for(SldListItem li: elements) {
			strb.append(li.toHTML());
		}
		strb.append("</ul>");
		return strb.toString();


	}




}
