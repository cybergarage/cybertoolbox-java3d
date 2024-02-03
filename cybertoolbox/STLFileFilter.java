/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	STLFileFilter.java
*
******************************************************************/

import java.io.*;
import javax.swing.filechooser.FileFilter;

public class STLFileFilter extends FileFilter {
	final static String stl = "stl";
	final static String slp = "slp";
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if(i > 0 &&  i < s.length() - 1) {
			String extension = s.substring(i+1).toLowerCase();
			if (stl.equals(extension) == true) 
				return true;
			if (slp.equals(extension) == true) 
				return true;
		}
		return false;
	}
	public String getDescription() {
		return "STL Files (*.stl, *.slp)";
	}
}

