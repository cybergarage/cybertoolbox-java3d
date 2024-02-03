/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1999
*
*	File:	CtbViewer.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.*;
import java.net.*;
import java.util.Properties;

import javax.swing.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import cv97.*;
import cv97.node.*;
import cv97.j3d.*;

public class CtbViewer extends Object implements WindowListener {

	private	World					mWorld	= null;
	
	private	CtbApplet			mApplet[]	= null;
	private	Window				mWindow[]	= null;
	private	Canvas3D				mCanvas3D[]	= null;

	public void setWorld(World world) {
		 mWorld = world;
	}
	
	public World getWorld() {
		return mWorld;
	}

	public SceneGraph getSceneGraph() {
		return getWorld().getSceneGraph();
	}

	public CtbApplet[] getApplets() {
		return mApplet;
	}

	public int getNApplets() {
		return mApplet.length;
	}

	public CtbApplet getApplet(int n) {
		return mApplet[n];
	}

	public Window[] getWindows() {
		return mWindow;
	}

	public int getNWindows() {
		return mWindow.length;
	}

	public Window getWindow(int n) {
		return mWindow[n];
	}

	public CtbViewer(){
	
		loadProperties();

		World world = new World();
		setWorld(world);
		world.setModuleTypes();
		
		int nWindow = getPropertyNWindows();
		
		mWindow		= new Window[nWindow];
		mApplet		= new CtbApplet[nWindow];
		mCanvas3D	= new Canvas3D[nWindow];
		
		for (int n=0; n<nWindow; n++) {
			mCanvas3D[n] = createCanvas3D(n);
			mApplet[n] = createApplet(n, getWorld(), mCanvas3D[n]);
			mWindow[n] = createWindow(n, mApplet[n]);
		}

		SceneGraphJ3dObject sgObject = new SceneGraphJ3dObject(mCanvas3D, getSceneGraph());
		getSceneGraph().setObject(sgObject);
		getSceneGraph().setHeadlightState(true);

		for (int n=0; n<nWindow; n++) {
			Transform3D t = getPropertyCanvas3DTransform3D(n);
			sgObject.setCanvas3DTransformGroup(n, t);
		}
		
		getWorld().setApplets(mApplet);
				
		String startupFilename = getPropertyStartupFilename();
		if (startupFilename != null) {
			File file = new File(startupFilename);
			boolean isLoadingOK = false;
			if (file != null) {
				if (file.isDirectory() == false) {
					try {
						isLoadingOK = getWorld().add(file.toURL());
					} catch (MalformedURLException mue) {}
				}
			}
			if (isLoadingOK == true)
				getWorld().setReLoadFile(file);
		}

		for (int n=0; n<nWindow; n++) {
			mWindow[n].show();
			mWindow[n].toFront();
			mApplet[n].startThread();
		}
		
		getWorld().startSimulation();
	}

	/////////////////////////////////////////////
	//	load
	/////////////////////////////////////////////

	public boolean load(File file) {
		getWorld().stopSimulation();
		
		boolean isLoadingOK = false;
		if (file != null) {
			if (file.isDirectory() == false) {
				getWorld().stopAppletThread();				
				getWorld().clear();
				try {
					isLoadingOK = getWorld().add(file.toURL());
				} catch (MalformedURLException mue) {}
				getWorld().startAppletThread();				
			}
		}

		getWorld().startSimulation();
		
		return isLoadingOK;
	}

	/////////////////////////////////////////////
	//	Window
	/////////////////////////////////////////////

	public Canvas3D createCanvas3D(int numWindow) {

		Canvas3D canvas3D = new Canvas3D(null);
		
		boolean isStereo = getPropertyWindowStereo(numWindow);
		if (isStereo == true)
			canvas3D.setStereoEnable(true);
		
		return canvas3D;
	}

	public CtbApplet createApplet(int numWindow, World world, Canvas3D canvas3D) {
			
		CtbApplet applet = new CtbApplet();
		applet.init(world, canvas3D);

		return applet;
	}

	public Window createWindow(int numWindow, CtbApplet applet) {

		Window window;
			
		boolean hasBorder = getPropertyWindowBorder(numWindow);
		if (hasBorder	== true)
			window = new JFrame("CyberToolbox for Java3D Viewer");
		else
			window = new JWindow();

		if (hasBorder) {		
			JFrame frame = (JFrame)window;
			frame.getContentPane().setLayout(new BorderLayout());
			boolean hasToolbar = getPropertyWindowToolbar(numWindow);
			if (hasToolbar == true) {			
				CtbViewerToolbar toolBar = new CtbViewerToolbar(frame, mWorld, true);
				toolBar.setApplet(applet);
				frame.getContentPane().add("North", toolBar);
				frame.getContentPane().add("Center", applet);
			}
		}
		else {		
			JWindow jwindow = (JWindow)window;
			jwindow.getContentPane().setLayout(new BorderLayout());
			jwindow.getContentPane().add("Center", applet);
		}

		window.addWindowListener(this);
		
		int xpos	= getPropertyWindowXPos(numWindow);
		int ypos	= getPropertyWindowYPos(numWindow);
		window.setLocation(xpos, ypos);
		
		int width	= getPropertyWindowWidth(numWindow);
		int height	= getPropertyWindowHeight(numWindow);
		window.setSize(width, height);
		
		return window;
	}

	/////////////////////////////////////////////
	//	Property
	/////////////////////////////////////////////

	public static final String propertyFileName		= "ctbviewer.properties";
	public static final String propertyHeaderName	= "CtbViewer properties";

	public static final String startupFilenamePropertyName		= "startup.filename";
	public static final String windowPropertyName					= "window";
	public static final String windowXPosSubPropertyName			= "xpos";
	public static final String windowYPosSubPropertyName			= "ypos";
	public static final String windowWidthSubPropertyName			= "width";
	public static final String windowHeightSubPropertyName		= "height";
	public static final String windowToolbarSubPropertyName		= "toolbar";
	public static final String windowBorderSubPropertyName		= "border";
	public static final String windowStereoSubPropertyName		= "stereo";
	public static final String windowRollSubPropertyName			= "roll";
	public static final String windowPitchSubPropertyName			= "pitch";
	public static final String windowYawSubPropertyName			= "yaw";
	
	public Properties ctbviewProperties = new Properties();
	
	public void loadProperties() {
		Properties prop = ctbviewProperties;
		try {
			prop.load(new FileInputStream(propertyFileName));
		}
		catch (IOException ioe) {}
	}

	public void setProperty(String name, String value) {
		Properties prop = ctbviewProperties;
		prop.setProperty(name, value);
	}

	public String getProperty(String name) {
		Properties prop = ctbviewProperties;
		return prop.getProperty(name);
	}
	
	public boolean hasProperty(String name) {
		String value = getProperty(name);
		if (value == null)
			return false;
		return true;
	}

	public void storeProperties() {
		Properties prop = ctbviewProperties;
		try {
			prop.store(new FileOutputStream(propertyFileName), "CtbViewer properties");
		}
		catch (IOException ioe) {}
	}

	/////////////////////////////////////////////
	//	Property
	/////////////////////////////////////////////

	public boolean getBooleanProperty(String name) {
		String value = getProperty(name);
		if (value == null)
			return true;
		if (value.equalsIgnoreCase("true") == true)
			return true;
		return false;
	}
	
	public int getIntegerProperty(String name) {
		int value = 0;
		String propString = getProperty(name);
		if (propString != null) {
			try {
				value = Integer.parseInt(propString);
			}
			catch (NumberFormatException nfe) {}
		}
		return value;
	}

	public float getFloatProperty(String name) {
		float value = 0.0f;
		String propString = getProperty(name);
		if (propString != null) {
			try {
				value = Float.parseFloat(propString);
			}
			catch (NumberFormatException nfe) {}
		}
		return value;
	}

	/////////////////////////////////////////////
	//	Startup Property
	/////////////////////////////////////////////
	
	public String getPropertyStartupFilename() {
		return getProperty(startupFilenamePropertyName);
	}

	/////////////////////////////////////////////
	//	Window Property
	/////////////////////////////////////////////

	public int getPropertyNWindows() {
		int nWindow = 0;
		while (hasProperty(windowPropertyName + nWindow + "." + windowXPosSubPropertyName) == true)
			nWindow++;
		return nWindow;
	}
	
	public int getPropertyWindowXPos(int n) {
		return getIntegerProperty(windowPropertyName + n + "." + windowXPosSubPropertyName);
	}

	public int getPropertyWindowYPos(int n) {
		return getIntegerProperty(windowPropertyName + n + "." + windowYPosSubPropertyName);
	}

	public int getPropertyWindowWidth(int n) {
		return getIntegerProperty(windowPropertyName + n + "." + windowWidthSubPropertyName);
	}
	
	public int getPropertyWindowHeight(int n) {
		return getIntegerProperty(windowPropertyName + n + "." + windowHeightSubPropertyName);
	}

	public boolean getPropertyWindowToolbar(int n) {
		return getBooleanProperty(windowPropertyName + n + "." + windowToolbarSubPropertyName);
	}
	
	public boolean getPropertyWindowBorder(int n) {
		return getBooleanProperty(windowPropertyName + n + "." + windowBorderSubPropertyName);
	}

	public boolean getPropertyWindowStereo(int n) {
		return getBooleanProperty(windowPropertyName + n + "." + windowStereoSubPropertyName);
	}

	public float getPropertyWindowRoll(int n) {
		return (float)Math.toRadians((double)getFloatProperty(windowPropertyName + n + "." + windowRollSubPropertyName));
	}

	public float getPropertyWindowPitch(int n) {
		return (float)Math.toRadians((double)getFloatProperty(windowPropertyName + n + "." + windowPitchSubPropertyName));
	}

	public float getPropertyWindowYaw(int n) {
		return (float)Math.toRadians((double)getFloatProperty(windowPropertyName + n + "." + windowYawSubPropertyName));
	}

	public Transform3D getPropertyCanvas3DTransform3D(int numWindow) {
	
		Transform3D trans3D = new Transform3D();
		float roll = getPropertyWindowRoll(numWindow);
System.out.println("roll[" + numWindow + "] = " + roll);
		trans3D.rotY(roll);

		return trans3D;
	}

	/////////////////////////////////////////////
	//	WindowListener
	/////////////////////////////////////////////

	public void windowActivated(WindowEvent e) {
	}
	
	public void windowClosed(WindowEvent e) {
	}
	
	public void windowClosing(WindowEvent e) {
		//storeProperties();
		System.exit(0);
	}
	
	public void windowDeactivated(WindowEvent e) {
	}
	
	public void windowDeiconified(WindowEvent e) {
	}
	
	public void windowIconified(WindowEvent e) {
	}
	
	public void windowOpened(WindowEvent e) {
	}
	
	/////////////////////////////////////////////
	//	main
	/////////////////////////////////////////////
	
	public static void main(String args[]) {
		new CtbViewer();
	}
}	
