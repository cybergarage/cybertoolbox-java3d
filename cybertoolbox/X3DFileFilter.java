/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	VRML97FileFilter.java
*
******************************************************************/

import java.io.*;
import javax.swing.filechooser.FileFilter;

public class X3DFileFilter extends FileFilter {
	final static String x3d = "x3d";
	final static String xml = "xml";
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if(i > 0 &&  i < s.length() - 1) {
			String extension = s.substring(i+1).toLowerCase();
			if (x3d.equals(extension) == true) 
				return true;
			if (xml.equals(extension) == true) 
				return true;
			return false;
		}
		return false;
	}
	public String getDescription() {
		return "X3D Files (*.x3d, *.xml)";
	}
}

