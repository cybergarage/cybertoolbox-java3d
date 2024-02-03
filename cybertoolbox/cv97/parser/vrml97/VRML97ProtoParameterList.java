/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : VRML97ProtoParameterList.java
*
******************************************************************/

package cv97.parser.vrml97;

import cv97.util.*;

public class VRML97ProtoParameterList extends LinkedList {

	public VRML97ProtoParameterList() {
	}
	
	////////////////////////////////////////////////
	//	Parameter
	////////////////////////////////////////////////

	public void addParameter(String type, String name, String defalutValue) {
		VRML97ProtoParameter param = new VRML97ProtoParameter(type, name, defalutValue);
		addNode(param);
	}

	public VRML97ProtoParameter getParameters() {
		return (VRML97ProtoParameter)getNodes();
	}
	
	public VRML97ProtoParameter getParameter(String name) {
		for (VRML97ProtoParameter param=getParameters(); param != null; param=param.next()) {
			String paramName = param.getName();
			if (paramName.compareTo(name) == 0)
				return param;
		}
		return null;
	}
}
