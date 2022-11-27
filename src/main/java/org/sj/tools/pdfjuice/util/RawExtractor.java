package org.sj.tools.pdfjuice.util;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;


/*
 *  See: https://svn.apache.org/repos/asf/pdfbox/site/userguide/bookmarks.pdf?p=900000
 */

public class RawExtractor {
	
	String inFilename;
	String outFilename;
	PDDocument doc;
	
	public RawExtractor(String _inFilename, String _outFilename) throws IOException {
		this.inFilename = _inFilename;
		this.outFilename = _outFilename;
				
		File file = new File(inFilename);  
		doc = PDDocument.load(file);
		
		
	}
	
	public int getPageNum(PDPage page) {
		for(int i=0; i< doc.getNumberOfPages(); i++) {
			PDPage current = doc.getPage(i);
			if(current.equals(page)) return i;
		}
		throw new NoSuchElementException(page.toString());
	}
	
	public void extractBookmarks() throws IOException 
	{
		
	    	PDDocumentOutline root = doc.getDocumentCatalog().getDocumentOutline();

	    	PDOutlineItem item = root.getFirstChild();
	    	while( item != null )
	    	{

				try {
					PDPage page = item.findDestinationPage(doc);
					int pageNum = getPageNum(page);
					System.out.println( "Item:" + item.getTitle()+", " +pageNum);
				} catch (NoSuchElementException e) {
					System.out.println( "Item:" + item.getTitle()+ ", ?");
				}
				PDOutlineItem child = item.getFirstChild();
	    		while( child != null )
	    		{
					try {
						PDPage page = child.findDestinationPage(doc);
		    			int pageNum = getPageNum(page);
		    			System.out.println( " Child:" + child.getTitle() + ", "+ pageNum);
					} catch (NoSuchElementException e) {
						// TODO Auto-generated catch block
		    			System.out.println( " Child:" + child.getTitle());
					} catch (IOException e) {
						e.printStackTrace();
					}
	    			child = child.getNextSibling();
	    		}
	    		item = item.getNextSibling();
	    	}
	}
}