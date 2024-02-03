/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DXFFileFilter.java
*
******************************************************************/

import java.io.*;
import javax.swing.filechooser.FileFilter;

public class DXFFileFilter extends FileFilter {
	final static String dxf = "dxf";
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if(i > 0 &&  i < s.length() - 1) {
			String extension = s.substring(i+1).toLowerCase();
			if (dxf.equals(extension) == true) 
				return true;
			else
				return false;
		}
		return false;
	}
	public String getDescription() {
		return "DXF Files (*.dxf)";
	}
}

