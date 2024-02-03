/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	CtbIDE.java
*
******************************************************************/

import java.io.*;
import java.awt.*;
import java.util.Vector;
import java.util.Properties;
import java.net.*;

import javax.swing.*;

import sun.tools.javac.*;

import cv97.*;
import cv97.node.*;
import cv97.field.*;
import cv97.route.*;
import cv97.util.*;

public class CtbIDE extends World {

	private WorldTreeFrame					mWorldTreeFrame					= null;
	private PerspectiveViewJava3DFrame	mPerspectiveViewJava3DFrame	= null;
	private OrthoViewJava3D					mOrthoViewJava3D					= null;
	private ModuleFrame						mModuleFrame						= null;

	public CtbIDE() {
	}
	
	//////////////////////////////////////////////////
	// Member 
	//////////////////////////////////////////////////
	
	public void setWorldTreeFrame(WorldTreeFrame worldTreeFrame) {
		mWorldTreeFrame = worldTreeFrame;
	}
	
	public WorldTreeFrame getWorldTreeFrame() {
		return mWorldTreeFrame;
	}

	public void setPerspectiveViewJava3DFrame(PerspectiveViewJava3DFrame frame) {
		mPerspectiveViewJava3DFrame = frame;
	}
	
	public PerspectiveViewJava3DFrame getPerspectiveViewJava3DFrame() {
		return mPerspectiveViewJava3DFrame;
	}

	public PerspectiveViewJava3DApplet getPerspectiveViewJava3DApplet() {
		if (mPerspectiveViewJava3DFrame != null)
			return mPerspectiveViewJava3DFrame.getApplet();
		return null;
	}
		
	public void setModuleFrame(ModuleFrame frame) {
		mModuleFrame = frame;
	}
	
	public ModuleFrame getModuleFrame() {
		return mModuleFrame;
	}

	//////////////////////////////////////////////////
	// Override Function
	//////////////////////////////////////////////////

	public void runStartEvent() {
		super.runStartEvent();
		Event startEvent = getSystemEvent("Start");
		updateDiagramFrames(startEvent);
	}

	public void clearBehavior() {
		closeAllDiagramFrames();
		super.clearBehavior();
	}

	public void addEventBehaviors(EventBehaviorGroup ebGroup) {
		super.addEventBehaviors(ebGroup);
		
		int nEventBehabiors = ebGroup.getNEventBehaviors();
		for (int n=0; n<nEventBehabiors; n++) {
			EventBehavior eventBehavior = ebGroup.getEventBehavior(n);
			Event event = eventBehavior.getEvent();
			int nTotalDgmFrame = getNDiagramFrames(event);
			Component updateComponent[] = new Component[nTotalDgmFrame];
			int nDgmFrame = 0;
			for (DiagramFrame dgmFrame=getDiagramFrames(); dgmFrame != null; dgmFrame = dgmFrame.next()) {
				Event dgmEvent = dgmFrame.getDiagram().getEvent();
				if (event == dgmEvent) {
					updateComponent[nDgmFrame] = dgmFrame.getMainComponent();
					nDgmFrame++;
				}
			}
			//eventBehavior.setUpdateComponents(updateComponent);
		}
	}
	
	//////////////////////////////////////////////////
	// Diagram Frames
	//////////////////////////////////////////////////

	private LinkedList mDiagramFrameList = new LinkedList();

	public DiagramFrame getDiagramFrames() {
		return (DiagramFrame)mDiagramFrameList.getNodes();
	}

	public DiagramFrame getDiagramFrame(int n) {
		return (DiagramFrame)mDiagramFrameList.getNode(n);
	}

	public DiagramFrame getDiagramFrame(Diagram dgm) {
		for (int n=0; n<getNDiagramFrames(); n++) {
			DiagramFrame dgmFrame = getDiagramFrame(n);
			if (dgm == dgmFrame.getDiagram())
				return dgmFrame;
		}
		return null;
	}

	private boolean isRect(int x0, int y0, int x1, int y1, int x, int y) {
		if (x0 <= x && x <= x1 && y0 <= y && y <= y1)
			return true;
		else
			return false;
	}
		
	public DiagramFrame getDiagramFrame(int screenx, int screeny) {
		for (int n=0; n<getNDiagramFrames(); n++) {
			DiagramFrame dgmFrame = getDiagramFrame(n);
			Point screenPos = dgmFrame.getMainComponentLocationOnScreen();
			int   width     = dgmFrame.getMainComponentWidth();
			int   height    = dgmFrame.getMainComponentHeight();
			
			int x0 = screenPos.x;
			int y0 = screenPos.y;
			int x1 = x0 + width;
			int y1 = y0 + height;
			
			if (isRect(x0, y0, x1, y1, screenx, screeny) == true)
				return dgmFrame;
		}
		return null;
	}
	
	public int getNDiagramFrames() {
		return mDiagramFrameList.getNNodes();
	}

	public int getNDiagramFrames(Event event) {
		int nDgmFrame = 0;
		for (DiagramFrame dgmFrame=getDiagramFrames(); dgmFrame != null; dgmFrame = dgmFrame.next()) {
			Event dgmEvent = dgmFrame.getDiagram().getEvent();
			if (event == dgmEvent)
				nDgmFrame++;
		}
		return nDgmFrame;
	}
	
	public void addDiagramFrame(DiagramFrame dgmFrame) {
		mDiagramFrameList.addNode(dgmFrame);
	}

	public DiagramFrame openDiagramFrame(Diagram dgm) {
		DiagramFrame dgmFrame = new DiagramFrame(this, dgm);
		addDiagramFrame(dgmFrame);
		return dgmFrame;
	}
	
	public boolean isDiagramFrameOpened(Diagram dgm) {
		return (getDiagramFrame(dgm) != null ? true : false);
	}

	public boolean closeDiagramFrame(Diagram dgm) {
		DiagramFrame dgmFrame = getDiagramFrame(dgm);
		if (dgmFrame != null) {
			dgmFrame.getFrame().dispose();
			return true;
		}
		return false;
	}
	
	public void closeAllDiagramFrames() {
		DiagramFrame dgm = getDiagramFrames();
		while (dgm != null) {
			DiagramFrame nextDgm = dgm.next();
			dgm.getFrame().dispose();
			dgm = nextDgm;
		}
	}

	public void updateDiagramFrames(Event event) {
		for (DiagramFrame dgmFrame=getDiagramFrames(); dgmFrame != null; dgmFrame = dgmFrame.next()) {
			Event dgmEvent = dgmFrame.getDiagram().getEvent();
			if (event == dgmEvent)
				dgmFrame.repaint();
		}
	}

	//////////////////////////////////////////////////
	// Save (SceneGraph)
	//////////////////////////////////////////////////
	
	public boolean saveSceneGraph(File file) {
		return getSceneGraph().save(file);
	}

	public boolean saveSceneGraph(String filename) {
		return saveSceneGraph(new File(filename));
	}

	////////////////////////////////////////////////
	// Save (Behavior)
	////////////////////////////////////////////////

	private String getIndentString(int indent) {
		StringBuffer indentString = new StringBuffer();
		for (int n=0; n<indent; n++)
			indentString.append('\t');
		return indentString.toString();
	}
	
	public void printBehaviorDiagram(PrintWriter pw, Diagram dgm, int indent) throws IOException {
		dgm.print(pw, indent);
	}

	public void printBehaviorEvent(PrintWriter pw, Event event) throws IOException {
		pw.print("\t<EVENT NAME=\"" + event.getName() + "\" TYPE=\"" + event.getAttributeString() + "\"");
		if (event.getOptionString() != null)
			pw.print(" OPTION=\"" + event.getOptionString() + "\"");
		pw.println(">");
			
		for (Diagram dgm = event.getDiagrams(); dgm != null; dgm = dgm.next()) 
			printBehaviorDiagram(pw, dgm, 2);
			
		pw.println("\t</EVENT>");
	}
		
	public void printBehavior(PrintWriter pw) throws IOException {
		pw.println("<CBF VERSION=\"201\">");
		
		for (Event event = getEvents(); event != null; event = event.next()) {
			if (0 < event.getNDiagrams())
				printBehaviorEvent(pw, event);
		}
		
		pw.println("</CBF>");
		
		pw.flush();
	}

	public void printBehavior(OutputStream os) throws IOException {
		PrintWriter pw = new PrintWriter(os);
		printBehavior(pw);
		pw.close();
	}
	
	public void printBehavior() {
		try {
			printBehavior(System.out);
		}
		catch (IOException ioe) {}
	}

	public boolean saveBehavior(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			printBehavior(fos);
			fos.close();
		}
		catch (IOException ioe) {
			Debug.warning("Couldn't open the file (" + file + ")");
			return false;
		}
		return true;
	}
	
	public boolean saveBehavior(String filename) {
		return saveBehavior(new File(filename));
	}

	//////////////////////////////////////////////////
	// Save
	//////////////////////////////////////////////////

	public boolean save(File sgFile) {
		
		SceneGraph sg = getSceneGraph();
		
		// Update Diagram frame position/size
		for (DiagramFrame dgmFrame=getDiagramFrames(); dgmFrame != null; dgmFrame = dgmFrame.next())
			dgmFrame.updateDiagramPositionSize();
	
		// Save SceneGraph/Behavior infomation
		WorldInfoNode winfoNode = sg.findWorldInfoNode(BEHAVIOR_WORLDINFO_NODENAME);
		if (winfoNode == null) {
			winfoNode = new WorldInfoNode();
			winfoNode.setName(BEHAVIOR_WORLDINFO_NODENAME);
			winfoNode.setTitle("CyberToolbox Behavior Format V2.0");
			sg.addNode(winfoNode);
		}
		
		// Add Defautl Viewpoint and NavigationInfo node
		ViewpointNode view = sg.getViewpointNode();
		if (view == null) {
			ViewpointNode newView = new ViewpointNode(sg.getDefaultViewpointNode()); 
			sg.addNode(newView);
			sg.setViewpointNode(newView, true);
			WorldTreeFrame worldTree = getWorldTreeFrame();
			if (worldTree != null)
				worldTree.addSceneGraphNodeTreeNode(newView, null);
		}
		NavigationInfoNode navInfo = sg.getNavigationInfoNode();
		if (navInfo == null) {
			NavigationInfoNode newNavInfo = new NavigationInfoNode(sg.getDefaultNavigationInfoNode());
			sg.addNode(newNavInfo);
			sg.setNavigationInfoNode(newNavInfo, true);
			WorldTreeFrame worldTree = getWorldTreeFrame();
			if (worldTree != null)
				worldTree.addSceneGraphNodeTreeNode(newNavInfo, null);
		}
			
		String sgFileName = sgFile.getName();
		if (sgFileName == null)
			return false;
			
		int periodIndex = sgFileName.lastIndexOf('.');
		String sgFileNameWithoutExtention = new String(sgFileName.getBytes(), 0, periodIndex);
		
		String behaviorFileName = sgFileNameWithoutExtention + "." + BEHAVIOR_FILE_EXTENTION;
		if (0 < winfoNode.getNInfos())
			winfoNode.setInfo(0, behaviorFileName);
		else
			winfoNode.addInfo(behaviorFileName);
		
		boolean retSceneGraphSave = saveSceneGraph(sgFile);
		
		File behaviorFile = new File(sgFile.getParent(), behaviorFileName);
		boolean retBehaviorSave = saveBehavior(behaviorFile); 
		
		if (retSceneGraphSave == false || retBehaviorSave == false)
			return false;
			
		return true;
	}

	public boolean save(String fileName) {
		return save(new File(fileName));
	}

	//////////////////////////////////////////////////
	// File Operation
	//////////////////////////////////////////////////
	
	public boolean moveFile(File srcFile, File destFile) {
		if (srcFile.equals(destFile) == false) {
			if (destFile.exists() == true)
				destFile.delete();
			return srcFile.renameTo(destFile);
		}
		return true;
	}

	public void copyFile(File srcFile, File destFile) {
		if (srcFile.equals(destFile) == true)
			return;
			
		if (destFile.exists() == true)
			destFile.delete();
			
		try {
			FileInputStream inStream = new FileInputStream(srcFile);
			FileOutputStream outStream = new FileOutputStream(destFile);
			
			try {
				byte buffer[] = new byte[512];
				int nReadByte = inStream.read(buffer);
				while (0 < nReadByte) {
					outStream.write(buffer, 0, nReadByte);		
					nReadByte = inStream.read(buffer);
				}
			}
			catch (IOException ioe) {}
			
			inStream.close();
			outStream.close();
		}
		catch (FileNotFoundException fnfe) {}
		catch (IOException ioe) {}
	}

	//////////////////////////////////////////////////
	// ModuleType
	//////////////////////////////////////////////////
	
	public void addModuleType(	String	className, 
										String	name, 
										String	inNodeName[], 
										String	outNodeName[], 
										boolean	hasExecutionNode, 
										int		attribute, 
										File		sourceFile, 
										File		classFile, 
										File		iconFile) {
									
		Debug.message("addModule");
		
		Debug.message("    className        = " + className);
		Debug.message("    name             = " + name);
		Debug.message("    inNodeName       = " + inNodeName.length);
		Debug.message("    outNodeName      = " + outNodeName.length);
		Debug.message("    hasExecutionNode = " + hasExecutionNode);
		Debug.message("    attribute        = " + attribute);
		Debug.message("    sourceFile       = " + sourceFile);
		Debug.message("    classFile        = " + classFile);
		Debug.message("    iconFile         = " + iconFile);
		
		ModuleType modType = new ModuleType(className, name, inNodeName, outNodeName, hasExecutionNode, attribute);
				
		String	moduleFileName = className + name + ".mod";
		String	sourceFileName = className + name + ".java";
		String	classFileName = className + name + ".class";
		String	iconFileName = className + name + ".gif";

		File	destModuleFile	= new File(getModuleTypeDirectory() + moduleFileName);
		File	destSourceFile	= new File(getModuleTypeScriptDirectory() + sourceFileName);
		File	destClassFile	= new File(getModuleTypeScriptDirectory() + classFileName);
		File	destIconFile	= new File(getModuleTypeIconDirectory() + iconFileName);
		
		if (destModuleFile.exists() == true)
			destModuleFile.delete();
		modType.save(destModuleFile.toString());

		moveFile(sourceFile,	destSourceFile);
		moveFile(classFile,	destClassFile);
		copyFile(iconFile,	destIconFile);

		ModuleType sameModType = getModuleType(className, name);
		if (sameModType != null)
			sameModType.remove();
			
		Debug.message("    sameModType       = " + sameModType);
		
		addModuleType(modType);
	}
	
	public boolean removeModuleType(ModuleType modType) {
		Debug.message("removeModuleType");
		
		String	className	= modType.getClassName();
		String	name			= modType.getName();
		
		Debug.message("    className        = " + className);
		Debug.message("    name             = " + name);
				
		String	moduleFileName = className + name + ".mod";
		String	sourceFileName = className + name + ".java";
		String	classFileName = className + name + ".class";
		String	iconFileName = className + name + ".gif";

		File	destModuleFile	= new File(getModuleTypeDirectory() + moduleFileName);
		File	destSourceFile	= new File(getModuleTypeScriptDirectory() + sourceFileName);
		File	destClassFile	= new File(getModuleTypeScriptDirectory() + classFileName);
		File	destIconFile	= new File(getModuleTypeIconDirectory() + iconFileName);
		
		if (destModuleFile.exists() == true)
			destModuleFile.delete();
		
		if (destSourceFile.exists() == true)
			destSourceFile.delete();
		
		if (destClassFile.exists() == true)
			destClassFile.delete();
		
		if (destIconFile.exists() == true)
			destIconFile.delete();

		modType.remove();
		
		return true;
	}
			
	//////////////////////////////////////////////////
	// main
	//////////////////////////////////////////////////
	
	public static void main(String args[]) {
		CtbIDE ctbIDE = new CtbIDE();
		
		ctbIDE.setAppletMode(false);
		ctbIDE.loadModuleTypes();
		ctbIDE.loadUserModuleTypes();
		
		WorldTreeFrame worldTreeFrame = new WorldTreeFrame(ctbIDE);
		worldTreeFrame.setLocation(0, 100 + 16);
		ctbIDE.setWorldTreeFrame(worldTreeFrame);
		
		PerspectiveViewJava3DFrame	j3Dframe = new PerspectiveViewJava3DFrame(ctbIDE);
		j3Dframe.setLocation(393, 100 + 16);
		CtbApplet applets[] = new CtbApplet[1];
		applets[0] = j3Dframe.getApplet();
		ctbIDE.setApplets(applets);
		ctbIDE.setPerspectiveViewJava3DFrame(j3Dframe);
		
		ModuleFrame modFrame = new ModuleFrame(ctbIDE);
		modFrame.setLocation(0, 0);
		ctbIDE.setModuleFrame(modFrame);
		
		ctbIDE.createModuleClassCreatorClassFile();
		ctbIDE.createModuleTypeDefineClassFile();
	}
	
}
