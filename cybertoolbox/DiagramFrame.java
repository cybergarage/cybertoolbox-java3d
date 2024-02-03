/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File: DiagramFrame.java
*
******************************************************************/

import java.awt.*;
import java.awt.font.*;
import java.awt.event.*;

import javax.swing.*;

import cv97.*;
import cv97.node.*;
import cv97.field.*;
import cv97.util.*;

/******************************************************************
*
*	DiagramFrameScrollPane
*
******************************************************************/

class DiagramScrollPane extends JScrollPane {

	public DiagramScrollPane(Component view) {
		super();
		setViewportView(view);
		//setRowHeaderView(verticalRule);
		//setColumnHeaderView(horizontalRule);
	}

	public Point toViewCoordinates(Point point) {
		JViewport viewport = getViewport();
		Point viewPos = viewport.getViewPosition();
		point.translate(viewPos.x, viewPos.y);
		return point;
	}

}

/******************************************************************
*
*	DiagramFrame
*
******************************************************************/

public class DiagramFrame extends LinkedListNode implements ScrollPaneConstants {

	private Frame					mFrame					= null;
	private Diagram				mDiagram					= null;
	private DiagramPanel			mMainComponent			= null;
	private DiagramScrollPane	mDiagramScrollPane	= null;
	
	public DiagramFrame(CtbIDE ctbIDE, Diagram diagram) {
		setCtbIDE(ctbIDE);
		setDiagram(diagram);
		
		//mFrame = new JFrame(diagram.getName());
		mFrame = new Frame(diagram.getName());
		
		mFrame.addWindowListener(new DiagramFrameListener());
		
		mFrame.setLayout(new BorderLayout());
		//mFrame.getContentPane().setLayout(new BoxLayout(mFrame.getContentPane(), BoxLayout.Y_AXIS));
		//mFrame.getContentPane().setLayout(new BorderLayout());

		mFrame.add("North", new DiagramFrameToolBar(this));
		//mFrame.getContentPane().add("North", new DiagramFrameToolBar(this));
		//mFrame.getContentPane().add(new DiagramFrameToolBar(this));
				
		mMainComponent = new DiagramPanel(this, diagram);
		mDiagramScrollPane = new DiagramScrollPane(mMainComponent);
		mFrame.add("Center", mDiagramScrollPane);
		//mFrame.getContentPane().setLayout(new BorderLayout());
		//mFrame.getContentPane().add("Center", dgmScrollPane);
		//mFrame.getContentPane().add(dgmScrollPane);
																				
		mFrame.addWindowListener(new DiagramFrameClosing());
		mFrame.addKeyListener(mMainComponent);
		
		mFrame.setLocation(diagram.getXPosition(), diagram.getYPosition());
		mFrame.setSize(diagram.getWidth(), diagram.getHeight());
		mFrame.show();
		
		getDiagram().setComponent(mMainComponent);
	}

	private class DiagramFrameClosing extends WindowAdapter {
		public DiagramFrameClosing() {
		}
		public void windowClosing(WindowEvent e) {
			Diagram diagram = getDiagram();
			Frame frame = getFrame();
			diagram.setPosition(frame.getLocation().x, frame.getLocation().y);
			diagram.setWidth(frame.getSize().width);
			diagram.setHeight(frame.getSize().height);
			getDiagram().setComponent(null);
			remove();
			frame.dispose();
		}
	}

	////////////////////////////////////////
	//	World / SceneGraph
	////////////////////////////////////////

	private CtbIDE mCtbIDE;
			
	public void setCtbIDE(CtbIDE ctbIDE) {
		mCtbIDE = ctbIDE;
	}
	
	public CtbIDE getCtbIDE() {
		return mCtbIDE;
	}
	
	public World getWorld() {
		return mCtbIDE;
	}
	
	public SceneGraph getSceneGraph() {
		return getWorld().getSceneGraph();
	}

	//////////////////////////////////////////////////
	// repaint
	//////////////////////////////////////////////////

	public void repaint() {
		getMainComponent().repaint();
	}

	//////////////////////////////////////////////////
	// toFront
	//////////////////////////////////////////////////

	public void toFront() {
		getFrame().toFront();
	}

	//////////////////////////////////////////////////
	// Frame
	//////////////////////////////////////////////////

	public void setFrame(Frame frame) {
		mFrame = frame;
	}

	public Frame getFrame() {
		return mFrame;
	}

	public void setTitle(String title) {
		mFrame.setTitle(title);
	}

	//////////////////////////////////////////////////
	// Position
	//////////////////////////////////////////////////
	
	public int getXPosition() {
		Point pos = mFrame.getLocationOnScreen();
		return pos.x;
	}
	
	public int getYPosition() {
		Point pos = mFrame.getLocationOnScreen();
		return pos.y;
	}
	
	//////////////////////////////////////////////////
	// Size
	//////////////////////////////////////////////////

	public int getWidth() {
		return mFrame.getBounds().width;
	}

	public int getHeight() {
		return mFrame.getBounds().height;
	}

	public Dimension getSize() {
		return mFrame.getSize();
	}

	//////////////////////////////////////////////////
	// Diagram Module
	//////////////////////////////////////////////////

	public void	setDiagram(Diagram diagram) {
		mDiagram = diagram;
	}
	
	public Diagram getDiagram() {
		return mDiagram;
	}

	//////////////////////////////////////////////////
	// Main Component
	//////////////////////////////////////////////////

	public DiagramPanel getMainComponent() {
		return mMainComponent;
	}

	public Point getMainComponentLocationOnScreen() {
		return mFrame.getLocation();
	}

	public int getMainComponentWidth() {
		return mMainComponent.getWidth();
	}

	public int getMainComponentHeight() {
		return mMainComponent.getHeight();
	}

	public boolean getGridSnap() { 
		return getMainComponent().getGridSnap(); 
	}

	public boolean getGridDisplay() { 
		return getMainComponent().getGridDisplay(); 
	}

	public int getGridSize() { 
		return getMainComponent().getGridSize(); 
	}

	public void setLineStyle(int style) {
		getMainComponent().setLineStyle(style); 
	}
	
	public int getLineStyle() { 
		return getMainComponent().getLineStyle(); 
	}

	public DiagramScrollPane getDiagramScrollPane() {
		return mDiagramScrollPane;
	}
	
	//////////////////////////////////////////////////
	// Add Module
	//////////////////////////////////////////////////

	public void addModule(ModuleType moduleType, int localx, int localy) {
		Debug.message("DiagramFrame::addModule = " + localx + ", " + localy);
		Diagram diagram = getDiagram();
		Module module = diagram.addModule(moduleType);
		module.setPosition(localx, localy);
		addUndoAction(new DiagramAddModuleUndoAction(module));
		DiagramPanel diagramPanel = getMainComponent();
		diagramPanel.resetComponentSize();	
		diagramPanel.repaint();
	}

	public void addModuleOnScreen(ModuleType moduleType, int screenx, int screeny) {
		Debug.message("addModuleOnScreen");
		Point componentScreenPos = getMainComponentLocationOnScreen();
		int localx = screenx - componentScreenPos.x;
		int localy = screeny - componentScreenPos.y;
		Debug.message("    local = " + localx + ", " + localy);
		Point dgmPos = getDiagramScrollPane().toViewCoordinates(new Point(localx, localy));
		Debug.message("    local = " + dgmPos.x + ", " + dgmPos.y);
		addModule(moduleType, dgmPos.x, dgmPos.y);
	}

	//////////////////////////////////////////////////
	// Undo
	//////////////////////////////////////////////////
	
	private Undo mUndo = new Undo(50);

	public void addUndoAction(UndoObject undoObject) {
		mUndo.add(undoObject);
	}
	
	public void undo() {
		if (mUndo.undo() == false)
			Message.beep();
		getMainComponent().repaint();
	}

	//////////////////////////////////////////////////
	// UndoAction
	//////////////////////////////////////////////////
	
	private class DiagramAddModuleUndoAction implements UndoObject {
		private Module mModule = null;
		public DiagramAddModuleUndoAction(Module module) {
			mModule = module;
		}
		public void undo() {
			getDiagram().removeModule(mModule);
		}
	}

	//////////////////////////////////////////////////
	// Cut / Copy / Paste
	//////////////////////////////////////////////////

	public void cut() {
		getMainComponent().cut();
	}
	
	public void copy() {
		getMainComponent().copy();
	}
	
	public void paste() {
		getMainComponent().paste();
	}
	
	//////////////////////////////////////////////////
	// Update Position/Size
	//////////////////////////////////////////////////
	
	public void updateDiagramPositionSize() {
		if (mFrame.isShowing() == true) { 
			int x = getXPosition();
			int y = getYPosition();
			int width = getWidth();
			int height = getHeight();
			
			Diagram dgm = getDiagram();
			dgm.setXPosition(x);
			dgm.setYPosition(y);
			dgm.setWidth(width);
			dgm.setHeight(height);
		}
	}
	
	//////////////////////////////////////////////////
	// WindowListener
	//////////////////////////////////////////////////
	
	private class DiagramFrameListener implements WindowListener {
		public void windowActivated(WindowEvent e) {
		}
		public void windowClosed(WindowEvent e) {
		}
		public void windowClosing(WindowEvent e) {
			updateDiagramPositionSize();
		}
		public void windowDeactivated(WindowEvent e) {
		}
		public void windowDeiconified(WindowEvent e) {
		}
		public void windowIconified(WindowEvent e) {
		}
		public void windowOpened(WindowEvent e) {
		}
	}
	
	//////////////////////////////////////////////////
	// next
	//////////////////////////////////////////////////

	public DiagramFrame next() {
		return (DiagramFrame)getNextNode();
	}

	////////////////////////////////////////////////
	//	Thread
	////////////////////////////////////////////////
/*
	private Thread mThreadObject = null;
	
	public void setThreadObject(Thread obj) {
		mThreadObject = obj;
	}

	public Thread getThreadObject() {
		return mThreadObject;
	}

	public void run() {
		while (true) {
			getMainComponent().repaint();
			Thread threadObject = getThreadObject();
			if (threadObject != null) { 
//				threadObject.yield();
				try {
					threadObject.sleep(100);
				} catch (InterruptedException e) {}
			}
		}
	}
	
	public void start() {
		Thread threadObject = getThreadObject();
		if (threadObject == null) {
			threadObject = new Thread(this);
			setThreadObject(threadObject);
			threadObject.start();
		}
	}
	
	public void stop() {
		Thread threadObject = getThreadObject();
		if (threadObject != null) {
			//threadObject.destroy();
			threadObject.stop();
			setThreadObject(null);
		}
	}
*/
}

