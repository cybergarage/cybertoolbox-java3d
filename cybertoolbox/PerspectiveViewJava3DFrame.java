/******************************************************************
*
*	Simple VRML Viewer for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File:	PerspectiveViewJava3DFrame.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import cv97.*;
import cv97.node.*;
import cv97.j3d.*;

public class PerspectiveViewJava3DFrame extends JFrame implements Constants {

	private PerspectiveViewJava3DToolbar	mToolBar	= null;
	private PerspectiveViewJava3DApplet		mApplet	= null;

	public PerspectiveViewJava3DToolbar getToolBar() {
		return mToolBar;
	}
	
	public PerspectiveViewJava3DApplet getApplet() {
		return mApplet;
	}

	public PerspectiveViewJava3DFrame(CtbIDE ctbIDE){
		super("");

		getContentPane().setLayout(new BorderLayout());
		//setLayout(new BorderLayout());

		mToolBar = new PerspectiveViewJava3DToolbar(this, ctbIDE);
		getContentPane().add("North", mToolBar);
		//add("North", mToolBar);

		mApplet = new PerspectiveViewJava3DApplet(ctbIDE);
		mApplet.init(ctbIDE);
		mApplet.startThread();
		
		getContentPane().add("Center", mApplet);
		//add("Center", c);

		mToolBar.setApplet(mApplet);

		setSize(320, 320);
		show();
	}

	public void startThread() {
		getApplet().startThread();
	}

	public void stopThread() {
		getApplet().stopThread();
	}
}
