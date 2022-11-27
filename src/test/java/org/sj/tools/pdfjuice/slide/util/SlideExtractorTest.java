package org.sj.tools.pdfjuice.slide.util;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Assert;
import org.junit.Test;
import org.sj.tools.pdfjuice.slide.build.PosterSlideBuilder;

public class SlideExtractorTest {
	
    @Test
    public void testingCountImages() {
    	
    	try {
    		File file = new File("res/images01.pdf");
    		PDDocument doc = PDDocument.load(file);

    		PDPage page = doc.getPage(0);
		//	page.get
			SlideExtractor se = new SlideExtractor(page);
			se.run();
			int numImages = se.countImages();
			Assert.assertEquals("image count", 2, numImages);
			
    	} catch(IOException ioe) {
    		Assert.fail(ioe.getMessage());
    	}
	
	}


}
