/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : ScriptClassLoder.java
*
******************************************************************/

package cv97.node;

import java.net.*;

import cv97.util.Debug;

public class ScriptURLClassLoader extends URLClassLoader {

	public ScriptURLClassLoader(URL urls[]) {
		super(urls);
	}
	
	public Class findClass(String className) throws ClassNotFoundException {
		return super.findClass(className);
	}
}