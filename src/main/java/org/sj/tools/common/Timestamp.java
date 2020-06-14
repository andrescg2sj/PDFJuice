package org.sj.tools.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timestamp {
	
	public static String getTimestamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
		Date date = new Date();
		return dateFormat.format(date); 
	}
	
	

}
