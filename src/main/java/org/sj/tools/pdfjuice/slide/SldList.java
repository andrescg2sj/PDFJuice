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
