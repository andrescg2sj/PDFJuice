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

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.sj.tools.graphics.tablemkr.Table;

public class PDFTableExtractor {
	
	File file;
	List<Table> tables = null;
	ExtractionProperties properties;
	
	public PDFTableExtractor(File file) {
		this.file = file;
		properties = new ExtractionProperties();
	}
	


	public PDFTableExtractor(File file, Rectangle2D clipRect, double thickness, double proximity)
	{
		this.file = file;
		properties = new ExtractionProperties(clipRect, thickness, proximity);
	}
	
	
	
	void generateTables(boolean clean) throws IOException {
	    PDDocument doc = PDDocument.load(file);
	    for(int i=0; i< doc.getNumberOfPages(); i++) {
	    	PDPage page = doc.getPage(i);
	    	PDFPageTableExtractor engine =
	    			new PDFPageTableExtractor(page, properties); 
	    		//TODO: specify extraction properties. 
	    	engine.run();
	    	
	    	List<Table> pageTables = engine.getTables(clean);
	    	//engine.getTables();
	    	tables.addAll(pageTables);
	    }
	    doc.close();
	    

	}
	
	public Iterator<List<Table>> getIterator() {
		//TODO: process pages on each iteration
		throw new UnsupportedOperationException("Not implemented");
	}
	
	public List<Table> getAllTables() throws IOException  {
		return getAllTables(true);
	}

	
	public List<Table> getAllTables(boolean clean) throws IOException  {
    	//File inFile = new File(srcPath);
		if(tables == null) {
			tables = new LinkedList<Table>();
			generateTables(clean);
		} 
		return tables;
	}

}
