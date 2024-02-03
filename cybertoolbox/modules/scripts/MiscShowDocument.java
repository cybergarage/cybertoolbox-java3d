/*----------------------------------------------------------------
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	MiscShowDocument.java
*
----------------------------------------------------------------*/

import java.applet.*;
import java.net.*;

public class MiscShowDocument extends Module {

	Applet			applet = null;
	AppletContext	appletContext;
	URL				docURL;	
	String			docTarget;
	
	private URL getDocumentURL(String string) {
		URL url = null;
		try {
			url = new URL(string);
		}
		catch (MalformedURLException mue1) {
			if (applet != null) {
				try {
					url = new URL(applet.getDocumentBase(), string);
				}
				catch (MalformedURLException mue2) {}
				catch (Exception e) {}
			}
		}
		catch (Exception e) {}
		
		return url;
	}
	
	public void initialize() {
		Debug.message("MiscShowDocument.initialize()");
		
		applet = null;
		appletContext = null;

		World world = getWorld();
		if (world != null) {
			if (0 < world.getNApplets())
				applet = world.getApplet(0);
		}
		Debug.message("  applet = " + applet);
		
		if (applet != null) {
			try {
				appletContext = applet.getAppletContext();		
			}
			catch (Exception e) {};
		}
		Debug.message("  appletContext = " + appletContext);
			
		docURL		= null;
		docTarget	= null;
		
		Debug.message("  value = " + getStringValue());
		
		String value[] = getStringValues();
		if (value != null) {
			Debug.message("  value.length = " + value.length);
			if (0 < value.length) {
				Debug.message("  url           = " + value[0]);
				docURL = getDocumentURL(value[0]);
				Debug.message("  docURL        = " + docURL);
			}
			if (1 < value.length) {
				docTarget = value[1];
				Debug.message("  docTarget     = " + docTarget);
			}
		}
	}

	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		Debug.message("MiscShowDocument.processData()");
		
		if (exeNode.isConnected() == true) {
			if (exeNode.getBooleanValue() == false) 
				return;
		}

		if (inNode[0].isConnected() == true)
			docURL = getDocumentURL(inNode[0].getStringValue());

		if (inNode[1].isConnected() == true)
			docTarget = inNode[1].getStringValue();
		
		Debug.message("  appletContext = " + appletContext);
		Debug.message("  docURL        = " + docURL);
		Debug.message("  docTarget     = " + docTarget);
		
		if (appletContext == null)
			return;

		if (docURL == null)
			return;
			
		if (docTarget != null)
			appletContext.showDocument(docURL, docTarget);
		else
			appletContext.showDocument(docURL);
			
	}
	
}
