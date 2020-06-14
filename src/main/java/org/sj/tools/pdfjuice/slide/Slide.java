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
