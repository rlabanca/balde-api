package org.baldeapi.v1.resources.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;

public class Utils {
	
	final static Logger log = Logger.getLogger(Utils.class);
	
	static double distance(double x1, double y1, double x2, double y2)  {  
		return Math.sqrt( (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));  
      
	} 
	
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s) || "*".equals(s) || ".".equals(s);
	}
	
	
	public synchronized static boolean isDevEnv() {
		Boolean isDev = null;
		if(isDev == null) {
		
			URL url = Loader.getResource("env.properties");
			File f = new File(url.getFile());
			
			if(f.exists()) {
				
				try {
					ResourceBundle bundle = new PropertyResourceBundle(new FileInputStream(f));
					isDev = new Boolean("dev".equalsIgnoreCase( bundle.getString("env") ));
				}  catch (IOException e) {
					log.warn("error", e);
				}
				
			} else {
				isDev = new Boolean( "dev".equalsIgnoreCase(System.getProperty("app.environment")) );
			}
		
			
			System.out.println(">>>>>>>>>>>>>>>>>  devel env? " + isDev);
		}
		return isDev;
	}
	
	public static void main(String[] args) {
		System.out.println( String.format("%03d", 1) );
	}
}
