/*----------------------------------------------------------------
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscPlaySound.java
*
----------------------------------------------------------------*/

import java.applet.*;
import java.net.*;

import cv97.*;

public class MiscPlaySound extends Module {

	Applet			applet = null;
	SceneGraph	sg = null;
		
	public void initialize() {
		World world = getWorld();
		applet = null;
		sg = null;
		if (world != null) {
			if (0 < world.getNApplets())
				applet = world.getApplet(0);
			sg = world.getSceneGraph();
		}
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		
		if (exeNode.isConnected() == false)
			return;
			
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) 
				return;
		}

		if (applet != null && sg != null) {
			URL baseURL = sg.getBaseURL();
			String soundFileName = getValue();
			if (baseURL != null && soundFileName != null) {
				URL audioURL = null;
				try {
					audioURL = new URL(baseURL.toString() + soundFileName);
					AudioClip aclip = applet.newAudioClip(audioURL);
					if (aclip != null)
						aclip.play();		
				}
				catch (NullPointerException npe) {
					Debug.warning("Couldn't play a sound (" + audioURL + ")");
				}
				catch (MalformedURLException mue) {
					Debug.warning("Couldn't play a sound (" + soundFileName + ")");
				}
			}
		}
	
	}
	
}
