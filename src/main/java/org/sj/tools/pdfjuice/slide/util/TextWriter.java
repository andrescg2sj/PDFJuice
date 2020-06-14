package org.sj.tools.pdfjuice.slide.util;

import java.io.PrintStream;

public class TextWriter {
	
	PrintStream out;
	
	public TextWriter(PrintStream dest) {
		this.out = dest;
	}
	
	
	public void write(String text) {
		out.print(text);
	}
	

}
