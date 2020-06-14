package org.sj.tools.pdfjuice.slide.build;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.sj.tools.graphics.sectorizer.StrRegionCluster;
import org.sj.tools.pdfjuice.slide.Slide;

public abstract class SlideBuilder {
	
	PDFont defaultFont;

	public abstract Slide build(StrRegionCluster obj);



}
