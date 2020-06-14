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
