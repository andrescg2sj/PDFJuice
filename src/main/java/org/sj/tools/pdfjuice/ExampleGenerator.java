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
package org.sj.tools.pdfjuice;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.sj.tools.graphics.tablemkr.util.PDFTableExporter;
import org.sj.tools.pdfjuice.slide.util.PDFSlidesExporter;


public class ExampleGenerator {

	String tablesDstPath = "examples/tables/html/";
	String slidesDstPath = "examples/slides/html/";
	
	public static String changeExtension(String path, String newExt) {
		String[] tokens = path.split("\\.(?=[^\\.]+$)");
		if(!newExt.startsWith("."))
			newExt = "." + newExt;
		return tokens[0]+ newExt;
	}
	
	public void processAllTables(String myDirectoryPath) {
		File dir = new File(myDirectoryPath);
		  File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File child : directoryListing) {
		      // Do something with child
		    	if(child.getName().endsWith(".pdf")) {
		    		String newFilename = changeExtension(child.getName(), "html");
		    		Path currentPath = (Path) Paths.get(System.getProperty("user.dir"));
		    		Path filePath = (Path) Paths.get(currentPath.toString(), tablesDstPath, newFilename);
		    		//System.out.print(child.getAbsolutePath() + " will be: ");
		    		PDFTableExporter proc = new PDFTableExporter();
		    		System.out.println("Writing " + filePath.toString());
		    		proc.setDestination(filePath.toString());
		    		proc.run(child.getAbsolutePath());
		    		
		    		//filterNotes(child);
		    		
		    		// TODO
		    		//convert()
		    	}
		    	
		    }
		  } else {
		    // Handle the case where dir is not really a directory.
		    // Checking dir.isDirectory() above would not be sufficient
		    // to avoid race conditions with another process that deletes
		    // directories.
		  }
	}
	
	public void processAllSlides(String myDirectoryPath) {
		try {

		File dir = new File(myDirectoryPath);
		  File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File child : directoryListing) {
		      // Do something with child
		    	if(child.getName().endsWith(".pdf")) {
		    		String newFilename = changeExtension(child.getName(), "html");
		    		Path currentPath = (Path) Paths.get(System.getProperty("user.dir"));
		    		Path filePath = (Path) Paths.get(currentPath.toString(), slidesDstPath, newFilename);
		    		//System.out.print(child.getAbsolutePath() + " will be: ");
		    		
		    		PDFSlidesExporter proc = new PDFSlidesExporter(child.getAbsolutePath());
			    	System.out.println("Writing " + filePath.toString());

		    		proc.allPagesToHtml(filePath.toString());
		    	}
		    	
		    }
		  } else {
		    // Handle the case where dir is not really a directory.
		    // Checking dir.isDirectory() above would not be sufficient
		    // to avoid race conditions with another process that deletes
		    // directories.
		  }
		} catch(Exception e) {
			e.printStackTrace();
		}

	}


	
	public static void main(String args[]) {
		ExampleGenerator eg = new ExampleGenerator();
		eg.processAllTables("examples/tables/pdf/");
		eg.processAllSlides("examples/slides/pdf/");
		
	}


}
