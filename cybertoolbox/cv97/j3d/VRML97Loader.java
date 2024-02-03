/******************************************************************
*
*	VRML97Saver for CyberVRML97
*
*	Copyright (C) Satoshi Konno 1999
*
*	File:	VRML97Loader.java
*
******************************************************************/

package cv97.j3d;

import javax.media.j3d.*;

import cv97.*;
import cv97.node.*;
import cv97.j3d.*;

public class VRML97Loader extends cv97.SceneGraph {
	
	public VRML97Loader() {
		SceneGraphJ3dObject sgObject = new SceneGraphJ3dObject(this);
		setObject(sgObject);		
	}
	
	public BranchGroup getBranchGroup() {
		return ((SceneGraphJ3dObject)getObject()).getBranchGroup();
	}
}
