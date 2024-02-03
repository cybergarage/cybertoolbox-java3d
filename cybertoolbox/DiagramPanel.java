/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File: DiagramFrame.java
*
******************************************************************/

import java.io.*;
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
*	DiagramPanel
*
******************************************************************/

class DiagramPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	public static final int OPERATIONMODE_NONE							= 0x00;
	public static final int OPERATIONMODE_MOVING_MODULE				= 0x01;
	public static final int OPERATIONMODE_SELECTING_BOX				= 0x02;
	public static final int OPERATIONMODE_MOVING_SELECTING_BOX		= 0x04;
	
	public static final int OPERATIONMODE_SELECTING_MODULE			= Module.INSIDE;
	public static final int OPERATIONMODE_SELECTING_MODULE_INNODE	= Module.INSIDE_INNODE;
	public static final int OPERATIONMODE_SELECTING_MODULE_OUTNODE	= Module.INSIDE_OUTNODE;

	public static final int DRAWMODE_NORMAL								= 0x00;
	public static final int DRAWMODE_DRAGGING_MODULE					= 0x01;
	public static final int DRAWMODE_DRAGGING_MODULELINE				= 0x02;
	public static final int DRAWMODE_DRAGGING_SELECTINGBOX			= 0x04;
	public static final int DRAWMODE_MOVING_SELECTINGBOX				= 0x08;

	private static DiagramClipboard mDiagramClipboard = null;
	
	private boolean			mGridSnap;
	private boolean			mGridDisplay;
	private int					mLineStyle;
	private int					mGridSize;

	private DiagramFrame		mDiagramFrame		= null;
	private Diagram			mDiagram				= null;
	private Module				mSelectingModule	= null;
	private int					mMode					= OPERATIONMODE_NONE;

	private Color				selectNodeColor	= Color.blue;
	
	public DiagramPanel(DiagramFrame diagramFrame, Diagram diagram) {
		setDiagramFrame(diagramFrame);
		setDiagram(diagram);
		setDrawMode(DRAWMODE_NORMAL);

		setLayout(new BorderLayout());
		setBackground(Color.white);
		addMouseListener(this);
		addMouseMotionListener(this);
		//addKeyListener(this);

		setGridDisplay(true);
		setGridSnap(true);
		setLineStyle(Diagram.LINE_STYLE_ZIGZAG);
		setGridSize(Module.SIZE/4);
		
		if (mDiagramClipboard == null)
			mDiagramClipboard = new DiagramClipboard(diagram.getWorld());
		
		resetComponentSize();
	}

	//////////////////////////////////////////////////
	// Diagram
	//////////////////////////////////////////////////

	public void	setDiagram(Diagram diagram) {
		mDiagram = diagram;
	}
	
	public Diagram getDiagram() {
		return mDiagram;
	}

	//////////////////////////////////////////////////
	// Diagram Frame
	//////////////////////////////////////////////////

	public void	setDiagramFrame(DiagramFrame diagramFrame) {
		mDiagramFrame = diagramFrame;
	}
	
	public DiagramFrame getDiagramFrame() {
		return mDiagramFrame;
	}

	public Frame getTopFrame() {
		return mDiagramFrame.getFrame();
	}

	////////////////////////////////////////
	//	World / SceneGraph
	////////////////////////////////////////

	public World getWorld() {
		return getDiagramFrame().getWorld();
	}

	public CtbIDE getCtbIDE() {
		return getDiagramFrame().getCtbIDE();
	}
	
	public SceneGraph getSceneGraph() {
		return getWorld().getSceneGraph();
	}

	//////////////////////////////////////////////////
	// Undo
	//////////////////////////////////////////////////
	
	private void addUndoAction(UndoObject undoObject) {
		getDiagramFrame().addUndoAction(undoObject);
	}
	
	private void undo() {
		getDiagramFrame().undo();
	}

	//////////////////////////////////////////////////
	// Line Style
	//////////////////////////////////////////////////

	public void setLineStyle(int style) {
		mLineStyle = style; 
	}
	
	public int getLineStyle() { 
		return mLineStyle; 
	}

	//////////////////////////////////////////////////
	// Grid
	//////////////////////////////////////////////////
	
	private void setGridSnap(boolean on) { 
		mGridSnap = on; 
	}
	
	public boolean getGridSnap() { 
		return mGridSnap; 
	}

	private void setGridDisplay(boolean on) { 
		mGridDisplay = on; 
	}
	
	public boolean getGridDisplay() { 
		return mGridDisplay; 
	}

	private void setGridSize(int size) {
		mGridSize = size; 
	}
	
	public int getGridSize() { 
		return mGridSize; 
	}

	public int toGridSnapPosition(int pos) {
		if (getGridSnap() == true) 
			pos -= pos % getGridSize();
		return pos;
	}

	//////////////////////////////////////////////////
	// Selecting Box
	//////////////////////////////////////////////////

	private boolean	mSelectingBox;
	private Rectangle	mSelectingBoxRect = new Rectangle();
	
	private void selectingBoxOn() {
		mSelectingBox = true;
	}
	
	private void selectingBoxOff() {
		mSelectingBox = false;
	}
	
	private boolean isSelectingBoxOn() {
		return mSelectingBox;
	}

	private void setSelectingBoxRect(int x0, int y0, int x1, int y1) {
		if (x1 < x0) {
			int tmp = x0;
			x0 = x1;
			x1 = tmp;
		}
		if (y1 < y0) {
			int tmp = y0;
			y0 = y1;
			y1 = tmp;
		}
		mSelectingBoxRect.setRect(x0, y0, x1-x0, y1-y0);
	}

	private void setSelectingBoxLocation(int x, int y) {
		mSelectingBoxRect.setLocation(new Point(x, y));
	}

	private Rectangle getSelectingBoxRectangle() {
		return mSelectingBoxRect;
	}

	private boolean isInsideSelectingBox(int x, int y) {
		Rectangle rect = getSelectingBoxRectangle();
		if (x < rect.x)
			return false;
		if (y < rect.y)
			return false;
		if ((rect.x + rect.width) < x)
			return false;
		if ((rect.y + rect.height) < y)
			return false;
		return true;
	}
	
	private void deleteModulesInSelectingBox() {
	
		LinkedList	routeList	= new LinkedList();
		LinkedList	nodeList	= new LinkedList();

		Diagram dgm = getDiagram();

		SceneGraph sg = getWorld().getSceneGraph();
		
		Dataflow dataflow = dgm.getDataflows();
		while (dataflow != null) {
			Dataflow nextDataflow = dataflow.next();

			Module	moduleIn	= dataflow.getInModule();
			Module	moduleOut	= dataflow.getOutModule();
			
			int sx = moduleOut.getOutNodeXPosition(dataflow.getOutNode());
			int sy = moduleOut.getOutNodeYPosition(dataflow.getOutNode());
				
			int ex = moduleIn.getInNodeXPosition(dataflow.getInNode());
			int ey = moduleIn.getInNodeYPosition(dataflow.getInNode());
				
			int nodeSize = moduleOut.getNodeSize();
	
			if (isInsideSelectingBox(sx, sy) == false || isInsideSelectingBox(sx+nodeSize, sy+nodeSize) == false) {
				dataflow = nextDataflow;
				continue;
			}
			
			if (isInsideSelectingBox(ex, ey) == false || isInsideSelectingBox(ex+nodeSize, ey+nodeSize) == false) {
				dataflow = nextDataflow;
				continue;
			}
				
			dgm.removeDataflow(dataflow);
			
			dataflow = nextDataflow;
		}

		Module module = dgm.getModules();
		while (module != null) {
			Module nextModule = module.next();

			int mx = module.getXPosition();
			int my = module.getYPosition();
			int msize = module.getSize();
			if (isInsideSelectingBox(mx, my) == true && isInsideSelectingBox(mx+msize, my+msize) == true) 
				dgm.removeModule(module);
					
			module = nextModule;
		}
	}

	void copyModulesInSelectingBox(DiagramClipboard clipboard) {

		clipboard.clear();

		Diagram dgm = getDiagram();

		int nModule = dgm.getNModules();
		for (int n=0; n<nModule; n++) {
			Module module = dgm.getModule(n);
			int mx = module.getXPosition();
			int my = module.getYPosition();
			int msize = module.getSize();
			if (isInsideSelectingBox(mx, my) == true && isInsideSelectingBox(mx+msize, my+msize) == true) 
				clipboard.addModule(module);
		}

		for (Dataflow dataflow = dgm.getDataflows(); dataflow != null; dataflow = dataflow.next()) {
				Module	moduleIn	= dataflow.getInModule();
				Module	moduleOut = dataflow.getOutModule();
			
				int sx = moduleOut.getOutNodeXPosition(dataflow.getOutNode());
				int sy = moduleOut.getOutNodeYPosition(dataflow.getOutNode());
				
				int ex = moduleIn.getInNodeXPosition(dataflow.getInNode());
				int ey = moduleIn.getInNodeYPosition(dataflow.getInNode());
				
				int nodeSize = moduleOut.getNodeSize();

				if (isInsideSelectingBox(sx, sy) == false || isInsideSelectingBox(sx+nodeSize, sy+nodeSize) == false) 
					continue;

				if (isInsideSelectingBox(ex, ey) == false || isInsideSelectingBox(ex+nodeSize, ey+nodeSize) == false) 
					continue;
			
				clipboard.addDataflow(dataflow);
		}
	}

	//////////////////////////////////////////////////
	// Clipboard
	//////////////////////////////////////////////////

	public DiagramClipboard getClipboard() {
		return mDiagramClipboard;
	}

	//////////////////////////////////////////////////
	// Cut / Copy / Paste / DEL
	//////////////////////////////////////////////////
	
	public void copy() {
		copyModulesInSelectingBox(getClipboard());
	}

	public void del() {

		DiagramClipboard clipboard = new DiagramClipboard(getWorld());
		copyModulesInSelectingBox(clipboard);
		addUndoAction(new DiagramCutUndoAction(clipboard));
		
		deleteModulesInSelectingBox();

		repaint();
	}

	public void cut() {
		copy();
		
		deleteModulesInSelectingBox();

		// Undo Action
		DiagramClipboard copyClipboard = new DiagramClipboard(getWorld(), getClipboard());
		addUndoAction(new DiagramCutUndoAction(copyClipboard));
		
		repaint();
	}

	public void paste() {
		Debug.message("DiagramPanel.paste");
		
		DiagramClipboard clipboard = getClipboard();	

		Diagram dgm = getDiagram();

		int nModule = clipboard.getNModules();
	
		Module module[][]		= new Module[nModule][2];
		Module undoModule[]	= new Module[nModule];
	
		for (int n=0; n<nModule; n++) {
			Module			orgModule		= clipboard.getModule(n);
			String			className		= orgModule.getClassName();
			String			typeName		= orgModule.getTypeName();
			ModuleType	moduleType	= getWorld().getModuleType(className, typeName);
			Debug.message("\tclassName = " + className);
			Debug.message("\ttypeName = " + typeName);
			Debug.message("\tmoduleType = " + moduleType);
			Module			copyModule	= moduleType.createModule();
			copyModule.setXPosition(orgModule.getXPosition());
			copyModule.setYPosition(orgModule.getYPosition());
			copyModule.setValue(orgModule.getValue());
			dgm.addModule(copyModule);
			undoModule[n] = copyModule;
			module[n][0] = orgModule;	
			module[n][1] = copyModule;
		}

		int nDataflow = clipboard.getNDataflows();

		Dataflow undoDataflow[] = new Dataflow[nDataflow];

		for (int n=0; n<nDataflow; n++) {
			Dataflow dataflow = clipboard.getDataflow(n);
			
			Module			dataFlowOutModule = dataflow.getOutModule();
			ModuleNode	dataFlowOutNode = dataflow.getOutNode();
			Module			outModule = null;
			ModuleNode	outNode = null;
			for (int i=0; i<nModule; i++) {
				if (module[i][0] == dataFlowOutModule) {
					outModule = module[i][1];
					if (dataFlowOutNode.isExecutionNode() == true)
						outNode = outModule.getExecutionNode();
					else
						outNode = outModule.getOutNode(dataFlowOutNode.getNumber());
					break;
				}
			}
			
			Module			dataFlowInModule = dataflow.getInModule();
			ModuleNode	dataFlowInNode = dataflow.getInNode();
			Module			inModule = null;
			ModuleNode	inNode = null;
			for (int i=0; i<nModule; i++) {
				if (module[i][0] == dataFlowInModule) {
					inModule = module[i][1];
					if (dataFlowInNode.isExecutionNode() == true)
						inNode = inModule.getExecutionNode();
					else
						inNode = inModule.getInNode(dataFlowInNode.getNumber());
					break;
				}
			}
			undoDataflow[n] = dgm.addDataflow(outModule, outNode, inModule, inNode);
		}

		// Undo Action
		addUndoAction(new DiagramPasteUndoAction(undoModule, undoDataflow));

		repaint();
	}
		
	//////////////////////////////////////////////////
	// Draw
	//////////////////////////////////////////////////

	private void drawGridLines(Graphics2D g, int width, int height) {
		g.setPaintMode();
		int x, y;

		g.setColor(new Color(0xc0, 0xc0, 0xff));
		for (x=0; x<width; x+=getGridSize())
			g.drawLine(x, 0, x, height); 
		for (y=0; y<height; y+=getGridSize())
			g.drawLine(0, y, width, y); 

		g.setColor(new Color(0xa0, 0xa0, 0xff));
		for (x=0; x<width; x+=getGridSize()*4)
			g.drawLine(x, 0, x, height); 
		for (y=0; y<height; y+=getGridSize()*4)
			g.drawLine(0, y, width, y); 
	}

	private void drawDraggingModule(Graphics2D g, boolean xorDrawing) {
		Module	module = getSelectingModule();
		int		moduleSize = module.getSize();
				
		g.setColor(Color.black);
		if (xorDrawing == true) {
			g.setXORMode(Color.gray);
			int xpre = getDraggingModulePrevXPostion();
			int ypre = getDraggingModulePrevYPostion();
			if (xpre != -1 && ypre != -1)
				g.drawRect(xpre, ypre, moduleSize, moduleSize);
		}
		int x = getDraggingModuleXPostion();
		int y = getDraggingModuleYPostion();
		g.drawRect(x, y, moduleSize, moduleSize);
	}

	private void drawDraggingModuleLine(Graphics2D g, boolean xorDrawing) {
		g.setColor(selectNodeColor);
		if (xorDrawing == true) {
			g.setXORMode(Color.gray);
			int sxpre = getDraggingMousePrevPostionStartX();
			int sypre = getDraggingMousePrevPostionStartY();
			int expre = getDraggingMousePrevPostionEndX();
			int eypre = getDraggingMousePrevPostionEndY();
			if (sxpre != -1 && sypre != -1 && expre != -1 && eypre != -1) 
				g.drawLine(sxpre, sypre, expre, eypre);
		}
		int sx = getDraggingMousePostionStartX();
		int sy = getDraggingMousePostionStartY();
		int ex = getDraggingMousePostionEndX();
		int ey = getDraggingMousePostionEndY();
		g.drawLine(sx, sy, ex, ey);
	}

	private void drawRect(Graphics2D g, int sx, int sy, int ex, int ey) {
		if (ex < sx) {
			int tmp = sx;
			sx = ex;
			ex = tmp;
		}
		if (ey < sy) {
			int tmp = sy;
			sy = ey;
			ey = tmp;
		}
		g.drawRect(sx, sy, ex-sx, ey-sy);
	}
	
	private void drawDraggingSelectingBox(Graphics2D g, boolean xorDrawing) {
		g.setColor(Color.black);
		int sx = getMouseStartXPosition();
		int sy = getMouseStartYPosition();
		if (xorDrawing == true) {
			g.setXORMode(Color.gray);
			int expre = getDraggingMousePrevXPostion();
			int eypre = getDraggingMousePrevYPostion();
			if (expre != -1 && eypre != -1) 
				drawRect(g, sx, sy, expre, eypre);
		}
		int ex = getDraggingMouseXPostion();
		int ey = getDraggingMouseYPostion();
		drawRect(g, sx, sy, ex, ey);
	}

	private void drawMovingSelectingBox(Graphics2D g, boolean xorDrawing) {
		Rectangle rect = getSelectingBoxRectangle();
		int offsetx = rect.x - getMouseStartXPosition();
		int offsety = rect.y - getMouseStartYPosition();
		g.setColor(Color.black);
		if (xorDrawing == true) {
			g.setXORMode(Color.gray);
			int xpre = getDraggingMousePrevXPostion();
			int ypre = getDraggingMousePrevYPostion();
			if (xpre != -1 && ypre != -1) 
				g.drawRect(xpre + offsetx, ypre + offsety, rect.width, rect.height);
		}
		int x = getDraggingMouseXPostion();
		int y = getDraggingMouseYPostion();
		g.drawRect(x + offsetx, y + offsety, rect.width, rect.height);
	}

	private void clear(Graphics2D g) {
		g.setPaintMode();
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	private void drawLine(Graphics2D g, int sx, int sy, int ex, int ey, boolean isXBold, boolean isYBold) {
		g.drawLine(sx, sy, ex, ey);
	
		if (isXBold == true)
			g.drawLine(sx+1, sy, ex+1, ey);

		if (isYBold == true)
			g.drawLine(sx+1, sy+1, ex, ey+1);
	}

	private void drawZigzagLine(Graphics2D g, int outModuleX, int outModuleY, int outNodeX, int outNodeY, int inModuleX, int inModuleY, int inNodeX, int inNodeY, boolean isExecutionNode) {
		int NODE_LINE_MARGIN = Module.SIZE/2;
		
		if (inNodeX < outNodeX) {
			drawLine(g, outNodeX, outNodeY, outNodeX+NODE_LINE_MARGIN, outNodeY, false, true);
			if (inNodeY < outNodeY) {
				drawLine(g, outNodeX+NODE_LINE_MARGIN, outNodeY, outNodeX+NODE_LINE_MARGIN, outModuleY-NODE_LINE_MARGIN, true, false);
				outNodeX = outNodeX+NODE_LINE_MARGIN;
				outNodeY = outModuleY-NODE_LINE_MARGIN;
			}
			else {
				drawLine(g, outNodeX+NODE_LINE_MARGIN, outNodeY, outNodeX+NODE_LINE_MARGIN, outModuleY+Module.SIZE+NODE_LINE_MARGIN, true, false);
				outNodeX = outNodeX+NODE_LINE_MARGIN;
				outNodeY = outModuleY+Module.SIZE+NODE_LINE_MARGIN;
			}
		}

		if (isExecutionNode == false) {
			int midx = (outNodeX + inNodeX)/2;
			drawLine(g, outNodeX, outNodeY, midx, outNodeY, false, true);
			if (inNodeX < outNodeX) {
				if (inNodeY < outNodeY) {
					drawLine(g, midx, outNodeY, midx, inModuleY + Module.SIZE + NODE_LINE_MARGIN, true, false);
					drawLine(g, midx, inModuleY + Module.SIZE + NODE_LINE_MARGIN, inModuleX - NODE_LINE_MARGIN, inModuleY + Module.SIZE + NODE_LINE_MARGIN, false, true);
					drawLine(g, inModuleX - NODE_LINE_MARGIN, inModuleY + Module.SIZE + NODE_LINE_MARGIN, inModuleX - NODE_LINE_MARGIN, inNodeY, true, false);
					drawLine(g, inModuleX - NODE_LINE_MARGIN, inNodeY, inNodeX, inNodeY, false, true);
				}
				else {
					drawLine(g, midx, outNodeY, midx, inModuleY - NODE_LINE_MARGIN, true, false);
					drawLine(g, midx, inModuleY - NODE_LINE_MARGIN, inModuleX - NODE_LINE_MARGIN, inModuleY - NODE_LINE_MARGIN, false, true);
					drawLine(g, inModuleX - NODE_LINE_MARGIN, inModuleY - NODE_LINE_MARGIN, inModuleX - NODE_LINE_MARGIN, inNodeY, true, false);
					drawLine(g, inModuleX - NODE_LINE_MARGIN, inNodeY, inNodeX, inNodeY, false, true);
				}
			}
			else {
				drawLine(g, midx, outNodeY, midx, inNodeY, true, false);
				drawLine(g, midx, inNodeY, inNodeX, inNodeY, false, true);
			}
		}
		else {
			if (inNodeY < outNodeY) {
				int midx = (outNodeX + inNodeX)/2;
				drawLine(g, outNodeX, outNodeY, midx, outNodeY, false, true);
				drawLine(g, midx, outNodeY, midx, inNodeY - NODE_LINE_MARGIN, true, false);
				drawLine(g, midx, inNodeY - NODE_LINE_MARGIN, inNodeX, inNodeY - NODE_LINE_MARGIN, false, true);
				drawLine(g, inNodeX, inNodeY - NODE_LINE_MARGIN, inNodeX, inNodeY, true, false);
			}
			else {
				drawLine(g, outNodeX, outNodeY, inNodeX, outNodeY, false, true);
				drawLine(g, inNodeX, outNodeY, inNodeX, inNodeY, true, false);
			}
		}
	}

	private void drawStraightLine(Graphics2D g, int sx, int sy, int ex, int ey) {
		drawLine(g, sx, sy, ex, ey, false, true);
	}

	private void drawModuleValue(Graphics2D g, String valueString, int mx, int my) {
		if (valueString == null)
			return;
		if (valueString.length() <= 0)
			return;
		TextLayout textLayout = new TextLayout(valueString, g.getFont(), g.getFontRenderContext());
		int textWidth = (int)textLayout.getBounds().getWidth();
		int textHeight = (int)textLayout.getBounds().getHeight();
		g.drawString(valueString, mx + Module.SIZE/2 - (textWidth/2), my + Module.SIZE + textHeight);
	}

	private void drawModuleValue(Graphics2D g, Module module) {
		drawModuleValue(g, module.getValue(), module.getXPosition(), module.getYPosition());
	}

	private void drawSelectingBox(Graphics2D g) {
		float dash[] = { 3.0f };
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
		Rectangle r = getSelectingBoxRectangle();
		g.setColor(Color.blue);
		g.drawRect(r.x , r.y, r.width, r.height);
		g.setStroke(new BasicStroke());
	}
	
	private void draw(Graphics2D g) {
		g.setPaintMode();
		g.setColor(Color.black);

		Diagram diagram = getDiagram();

		// Draw Grid Line
		if (getGridDisplay() == true) {
			Dimension size = getComponentSize();
			drawGridLines(g, size.width, size.height);
		}
		
		// Draw Module Connected Line
		g.setColor(Color.black);
		for (Dataflow dataflow = getDiagram().getDataflows(); dataflow != null; dataflow = dataflow.next()) {
			Module	moduleIn		= dataflow.getInModule();
			Module	moduleOut	= dataflow.getOutModule();

			int	nodeHalfSize = moduleIn.getNodeSize() / 2;

			int	moduleOutNodeNumber = moduleOut.getOutNodeNumber(dataflow.getOutNode());
			int sx = moduleOut.getOutNodeXPosition(moduleOutNodeNumber) + nodeHalfSize;
			int sy = moduleOut.getOutNodeYPosition(moduleOutNodeNumber) + nodeHalfSize;

			int		ex, ey;
			boolean	isExecutionNode;
			if (dataflow.getInNode() != moduleIn.getExecutionNode()) {
				int	moduleInNodeNumber = moduleIn.getInNodeNumber(dataflow.getInNode());
				ex = moduleIn.getInNodeXPosition(moduleInNodeNumber) + nodeHalfSize;
				ey = moduleIn.getInNodeYPosition(moduleInNodeNumber) + nodeHalfSize;
				isExecutionNode = false;
			}
			else {
				ex = moduleIn.getExecutionNodeXPosition() + nodeHalfSize;
				ey = moduleIn.getExecutionNodeYPosition() + nodeHalfSize;
				isExecutionNode = true;
			}
	
			if (getLineStyle() == Diagram.LINE_STYLE_STRAIGHT)
				drawStraightLine(g, sx, sy, ex, ey);
			else {
				int ix = moduleIn.getXPosition();
				int iy = moduleIn.getYPosition();
				int ox = moduleOut.getXPosition();
				int oy = moduleOut.getYPosition();
				drawZigzagLine(g, ox, oy, sx, sy, ix, iy, ex, ey, isExecutionNode);
			}
		}

		// Draw Module
		g.setColor(Color.black);
		for (int n=0; n<diagram.getNModules(); n++) {

			g.setColor(Color.black);
			
			Module	module = diagram.getModule(n);
			Image		moduleIcon = module.getIcon();

			int	nodeSize = module.getNodeSize();
			
			int mx = module.getXPosition();
			int my = module.getYPosition();
			g.drawImage(moduleIcon, mx, my, null);

			int nInNodes = module.getNInNodes();
			for (int nNode=0; nNode<nInNodes; nNode++) {
				int nodex = module.getInNodeXPosition(nNode);
				int nodey = module.getInNodeYPosition(nNode);
				g.fillRect(nodex, nodey, nodeSize, nodeSize);
			}

			int nOutNodes = module.getNOutNodes();
			for (int nNode=0; nNode<nOutNodes; nNode++) {
				int nodex = module.getOutNodeXPosition(nNode);
				int nodey = module.getOutNodeYPosition(nNode);
				g.fillRect(nodex, nodey, nodeSize, nodeSize);
				String string = module.getOutNodeValue(nNode);
				if (string != null)
					g.drawString(string, nodex + nodeSize, nodey + nodeSize/2);
			}
			
			drawModuleValue(g, module);
			
			if (module.hasExecutionNode() == true) {
				int x = module.getExecutionNodeXPosition();
				int y = module.getExecutionNodeYPosition();
				g.fillRect(x, y, nodeSize, nodeSize);
			}

			if (getSelectingModule() == module) {
				if (isOperationMode(OPERATIONMODE_SELECTING_MODULE) == true) {
					if (true) {
						g.drawImage(moduleIcon, mx, my, null);
						g.drawImage(moduleIcon, mx-1, my-1, null);
					}
					else {
						int moduleSize = module.getSize();
						g.drawRect(mx, my, moduleSize, moduleSize);
					}
				}
				else if (isOperationMode(OPERATIONMODE_SELECTING_MODULE_OUTNODE) == true) {
					int nodex = module.getOutNodeXPosition(getSelectingOutNode());
					int nodey = module.getOutNodeYPosition(getSelectingOutNode());
					g.setColor(selectNodeColor);
					g.drawRect(nodex-1, nodey-1, nodeSize+1, nodeSize+1);
				}
				else if (isOperationMode(OPERATIONMODE_SELECTING_MODULE_INNODE) == true) {
					int nodex = module.getInNodeXPosition(getSelectingInNode());
					int nodey = module.getInNodeYPosition(getSelectingInNode());
					g.setColor(selectNodeColor);
					g.drawRect(nodex-1, nodey-1, nodeSize+1, nodeSize+1);
				}
				else if (isOperationMode(Module.INSIDE_EXECUTIONNODE) == true) {
					int nodex = module.getExecutionNodeXPosition();
					int nodey = module.getExecutionNodeYPosition();
					g.setColor(selectNodeColor);
					g.drawRect(nodex-1, nodey-1, nodeSize+1, nodeSize+1);
				}
			}
		}
		
		if (isSelectingBoxOn() == true)
			drawSelectingBox(g);
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		boolean	xorMode = true;
		
		switch (getDrawMode()) {
		case DRAWMODE_NORMAL:
			clear(g2d);
			draw(g2d);
			break;			
		case DRAWMODE_DRAGGING_MODULE:
			drawDraggingModule(g2d, xorMode);
			break;
		case DRAWMODE_DRAGGING_MODULELINE:
			drawDraggingModuleLine(g2d, xorMode);
			break;
		case DRAWMODE_DRAGGING_SELECTINGBOX:
			drawDraggingSelectingBox(g2d, xorMode);
			break;
		case DRAWMODE_MOVING_SELECTINGBOX:
			drawMovingSelectingBox(g2d, xorMode);
			break;
		}
		
		//updateComponentSize();
	}


	//////////////////////////////////////////////////
	// Selecting Module
	//////////////////////////////////////////////////

	private void setSelectingModule(Module node) {
		mSelectingModule = node;
	}
	
	private Module getSelectingModule() {
		return mSelectingModule;
	}

	//////////////////////////////////////////////////
	// Selecting Module
	//////////////////////////////////////////////////

	private void setOperationMode(int mode) {
		Debug.message("DiagramFrame::setOperationMode = " + mode);
		mMode = mode;
	}
	
	private int getOperationMode() {
		return mMode;
	}

	private boolean isOperationMode(int mode) {
		return ((mMode & mode) != 0 ? true : false);
	}

	private int getSelectingInNode() {
		return (getOperationMode() & Module.NODE_NUMBER_MASK); 
	}
	
	private int getSelectingOutNode() { 
		return (getOperationMode() & Module.NODE_NUMBER_MASK); 
	}

	//////////////////////////////////////////////////
	// Draw Mode
	//////////////////////////////////////////////////
	
	private int mDrawMode;
	
	private void setDrawMode(int mode) {
		mDrawMode = mode;
	}
	
	private int getDrawMode() {
		return mDrawMode;
	}

	//////////////////////////////////////////////////
	// Mouse Position
	//////////////////////////////////////////////////
	
	private int mDraggingModuleXPosition;
	private int mDraggingModuleYPosition;
	private int mDraggingModulePrevXPosition;
	private int mDraggingModulePrevYPosition;
	
	private int mDraggingMouseXPosition;
	private int mDraggingMouseYPosition;
	private int mDraggingMousePrevXPosition;
	private int mDraggingMousePrevYPosition;
	
	private int mDraggingMousePositionStartX;
	private int mDraggingMousePositionStartY;
	private int mDraggingMousePositionEndX;
	private int mDraggingMousePositionEndY;

	private int mDraggingMousePrevPositionStartX;
	private int mDraggingMousePrevPositionStartY;
	private int mDraggingMousePrevPositionEndX;
	private int mDraggingMousePrevPositionEndY;
		
	private void setDraggingModulePostion(int x, int y) {
		mDraggingModulePrevXPosition = mDraggingModuleXPosition;
		mDraggingModulePrevYPosition = mDraggingModuleYPosition;
		
		mDraggingModuleXPosition = x;
		mDraggingModuleYPosition = y;
	}

	private int getDraggingModuleXPostion() {
		return mDraggingModuleXPosition;
	}

	private int getDraggingModuleYPostion() {
		return mDraggingModuleYPosition;
	}

	private int getDraggingModulePrevXPostion() {
		return mDraggingModulePrevXPosition;
	}

	private int getDraggingModulePrevYPostion() {
		return mDraggingModulePrevYPosition;
	}

	private void setDraggingMousePostion(int x, int y) {
		mDraggingMousePrevXPosition = mDraggingMouseXPosition;
		mDraggingMousePrevYPosition = mDraggingMouseYPosition;
		
		mDraggingMouseXPosition = x;
		mDraggingMouseYPosition = y;
	}

	private int getDraggingMouseXPostion() {
		return mDraggingMouseXPosition;
	}

	private int getDraggingMouseYPostion() {
		return mDraggingMouseYPosition;
	}

	private int getDraggingMousePrevXPostion() {
		return mDraggingMousePrevXPosition;
	}

	private int getDraggingMousePrevYPostion() {
		return mDraggingMousePrevYPosition;
	}
		
	private void setDraggingMousePostion(int sx, int sy, int ex, int ey) {
		mDraggingMousePrevPositionStartX = mDraggingMousePositionStartX;
		mDraggingMousePrevPositionStartY = mDraggingMousePositionStartY;
		mDraggingMousePrevPositionEndX = mDraggingMousePositionEndX;
		mDraggingMousePrevPositionEndY = mDraggingMousePositionEndY;
		
		mDraggingMousePositionStartX = sx;
		mDraggingMousePositionStartY = sy;
		mDraggingMousePositionEndX = ex;
		mDraggingMousePositionEndY = ey;
	}

	private int getDraggingMousePostionStartX() {
		return mDraggingMousePositionStartX;
	}

	private int getDraggingMousePostionStartY() {
		return mDraggingMousePositionStartY;
	}

	private int getDraggingMousePostionEndX() {
		return mDraggingMousePositionEndX;
	}

	private int getDraggingMousePostionEndY() {
		return mDraggingMousePositionEndY;
	}

	private int getDraggingMousePrevPostionStartX() {
		return mDraggingMousePrevPositionStartX;
	}

	private int getDraggingMousePrevPostionStartY() {
		return mDraggingMousePrevPositionStartY;
	}

	private int getDraggingMousePrevPostionEndX() {
		return mDraggingMousePrevPositionEndX;
	}

	private int getDraggingMousePrevPostionEndY() {
		return mDraggingMousePrevPositionEndY;
	}
	
	//////////////////////////////////////////////////
	// Extents
	//////////////////////////////////////////////////

	public Dimension getComponentSize() {			
		Diagram diagram = getDiagram();
		int extents[][] = new int[2][2];
		diagram.getExtents(extents);
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		if (extents[Diagram.EXTENTS_MAX][0] < panelWidth)
			extents[Diagram.EXTENTS_MAX][0] = panelWidth; 
		if (extents[Diagram.EXTENTS_MAX][1] < panelHeight)
			extents[Diagram.EXTENTS_MAX][1] = panelHeight; 
		return new Dimension(extents[Diagram.EXTENTS_MAX][0], extents[Diagram.EXTENTS_MAX][1]);
	}
		
	public void updateComponentSize() {			
		Dimension size = getComponentSize();
		if (size.width != getWidth() || size.height != getHeight()) {
			Debug.message("Update component size (" + size + ") in DiagramFrame::updateComponentSize()");
			setPreferredSize(size);
		}
	}

	public void resetComponentSize() {			
		Diagram diagram = getDiagram();
		diagram.updateExtents();
		updateComponentSize();
	}

	//////////////////////////////////////////////////
	// UndoAction
	//////////////////////////////////////////////////
	
	private class DiagramMoveModuleUndoAction implements UndoObject {
		Module		mModule = null;
		int			mx = 0;
		int			my = 0;	
		public DiagramMoveModuleUndoAction(Module module, int x, int y) {
			mModule = module;
			mx = x; my = y;
		}
		public void undo() {
			mModule.setPosition(mx, my);
		}
	}

	private class DiagramDeleteModuleUndoAction implements UndoObject {
		Module	mModule		 	= null;
		int		mnDataflowInfo		= 0;
		Dataflow		mDataflowInfo[]	= null;	
		public DiagramDeleteModuleUndoAction(Module module) {
			mnDataflowInfo = getDiagram().getNModuleDataflows(module);
			if (mnDataflowInfo > 0) {
				mDataflowInfo = new Dataflow[mnDataflowInfo];
				int nDataflow = 0;
				for (Dataflow dataflow=getDiagram().getDataflows(); dataflow != null; dataflow=dataflow.next()) {
					if (module == dataflow.getOutModule() || module == dataflow.getInModule()) {
						mDataflowInfo[nDataflow] = new Dataflow(dataflow.getInModule(), dataflow.getInNode(), dataflow.getOutModule(), dataflow.getOutNode());
						nDataflow++;
					}
				}
			}
		}
		public void undo() {
			for (int n=0; n<mnDataflowInfo; n++)
				getDiagram().addDataflow(mDataflowInfo[n]);
		}
	}

	private class DiagramCreateModuleLineUndoAction implements UndoObject {
		private Module			mInModuleNode	= null;
		private ModuleNode	mInNode			= null;	
		private Module			mOutModuleNode	= null;
		private ModuleNode	mOutNode			= null;
		public DiagramCreateModuleLineUndoAction(Module outModuleNode, ModuleNode outNode, Module inModuleNode, ModuleNode inNode) {
			mInModuleNode	= inModuleNode;
			mInNode			= inNode;
			mOutModuleNode	= outModuleNode;
			mOutNode			= outNode;
		}
		public void undo() {
			getDiagram().removeDataflow(mOutModuleNode, mOutNode, mInModuleNode, mInNode);
		}
	}

	private class DiagramDeleteModuleLineUndoAction implements UndoObject {
		private Module			mInModuleNode	= null;
		private ModuleNode	mInNode			= null;	
		private Module			mOutModuleNode	= null;
		private ModuleNode	mOutNode			= null;
		public DiagramDeleteModuleLineUndoAction(Module outModuleNode, ModuleNode outNode, Module inModuleNode, ModuleNode inNode) {
			mInModuleNode	= inModuleNode;
			mInNode			= inNode;
			mOutModuleNode	= outModuleNode;
			mOutNode			= outNode;
		}
		public void undo() {
			getDiagram().addDataflow(mOutModuleNode, mOutNode, mInModuleNode, mInNode);
		}
	}

	private class DiagramMoveSelectingBoxUndoAction implements UndoObject {
	
		private Module	mModule[];
		private Point	mPos[];
				
		public DiagramMoveSelectingBoxUndoAction(Module module[], Point pos[]) {
			mModule = module;
			mPos = pos;
		}
	
		public void undo() {
			for (int n=0; n<mModule.length; n++) 
				mModule[n].setPosition(mPos[n].x, mPos[n].y);
		}

	}

	private class DiagramCutUndoAction implements UndoObject {
		
		private DiagramClipboard	mDiagramClipboard;
		
		public DiagramCutUndoAction(DiagramClipboard clipboard) {
			mDiagramClipboard = clipboard;
		}
		
		public void undo() {
			
			DiagramClipboard	clipboard = mDiagramClipboard;
			Diagram dgm = getDiagram();

			int nModule = clipboard.getNModules();
			Module module[][] = new Module[nModule][2];
			for (int n=0; n<nModule; n++) {
				Module 			orgModule		= clipboard.getModule(n);
				String			className		= orgModule.getClassName();
				String			typeName		= orgModule.getTypeName();
				ModuleType	moduleType	= getWorld().getModuleType(className, typeName);
				Module			copyModule	= moduleType.createModule();
				copyModule.setXPosition(orgModule.getXPosition());
				copyModule.setYPosition(orgModule.getYPosition());
				dgm.addModule(copyModule);
				module[n][0] = orgModule;
				module[n][1] = copyModule;
			}

			int nDataflow = clipboard.getNDataflows();
			for (int n=0; n<nDataflow; n++) {
				Dataflow dataflow = clipboard.getDataflow(n);
				
				Module			dataFlowOutModule = dataflow.getOutModule();
				ModuleNode	dataFlowOutNode = dataflow.getOutNode();
				Module			outModule = null;
				ModuleNode	outNode = null;
				for (int i=0; i<nModule; i++) {
					if (module[i][0] == dataFlowOutModule) {
						outModule = module[i][1];
						if (dataFlowOutNode.isExecutionNode() == true)
							outNode = outModule.getExecutionNode();
						else
							outNode = outModule.getOutNode(dataFlowOutNode.getNumber());
						break;
					}
				}

				Module			dataFlowInModule = dataflow.getInModule();
				ModuleNode	dataFlowInNode = dataflow.getInNode();
				Module			inModule = null;
				ModuleNode	inNode = null;
				for (int i=0; i<nModule; i++) {
					if (module[i][0] == dataFlowInModule) {
						inModule = module[i][1];
						if (dataFlowInNode.isExecutionNode() == true)
							inNode = inModule.getExecutionNode();
						else
							inNode = inModule.getInNode(dataFlowInNode.getNumber());
						break;
					}
				}
				
				dgm.addDataflow(outModule, outNode, inModule, inNode);
			}
		}
	}

	private class DiagramPasteUndoAction implements UndoObject {
		
		private Module		mModule[];
		private Dataflow	mDataflow[];
		
		public DiagramPasteUndoAction(Module module[], Dataflow dataflow[]) {
			mModule = module;
			mDataflow = dataflow;
		}

		public void undo() {
			Diagram dgm = getDiagram();
			for (int n=0; n<mDataflow.length; n++) 
				dgm.removeDataflow(mDataflow[n]);
			for (int n=0; n<mModule.length; n++) 
				dgm.removeModule(mModule[n]);
		}

	}		

	//////////////////////////////////////////////////
	// MouseListener
	//////////////////////////////////////////////////

	private int mMouseXBeforeOneFrame = 0;
	private int mMouseYBeforeOneFrame = 0;
	private int mMouseStartX = 0;
	private int mMouseStartY = 0;
	private int mMouseButton = 0;
	
	private void setMousePositionBeforeOneFrame(int x, int y) {
		mMouseXBeforeOneFrame = x;
		mMouseYBeforeOneFrame = y;
	}
	
	private int getMouseXPositionBeforeOneFrame() {
		return mMouseXBeforeOneFrame;
	}
	
	private int getMouseYPositionBeforeOneFrame() {
		return mMouseYBeforeOneFrame;
	}

	private void setMouseStartPosition(int x, int y) {
		mMouseStartX = x;
		mMouseStartY = y;
	}
	
	private int getMouseStartXPosition() {
		return mMouseStartX;
	}
	
	private int getMouseStartYPosition() {
		return mMouseStartY;
	}

	private void setMouseButton(int button) {
		mMouseButton = button;
	}
	
	private int getMouseButton() {
		return mMouseButton;
	}
		
	private int setOperationModeAndSelectedModuleFromMousePosition(int mx, int my) {
		Diagram diagram = getDiagram();
		
		ModuleSelectedData data = diagram.getModule(mx, my);
		
		int		mode		= data.getParts();
		Module	module	= data.getModule();
		
		setOperationMode(mode);
		setSelectingModule(module);
		
		return mode;
	}

	public void openModuleDialog(Module module) {
		Debug.message("DiagramFrame.openModuleDialog");
		
		String	moduleClassName	= module.getClassName();
		String	moduleTypeName		= module.getTypeName();
		
		Debug.message("\tclassName = " + moduleClassName);
		Debug.message("\ttypeName  = " + moduleTypeName);

		SceneGraph sg = getSceneGraph();

		//// SYSTEM /////////////////////////////////////////////////////////
		if (moduleClassName.equalsIgnoreCase("SYSTEM") == true) {
			if (moduleTypeName.equalsIgnoreCase("PICKUP") == true) {
				DialogModuleSystemPickup dialog = new DialogModuleSystemPickup(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getCoordinate());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("MOUSE") == true) {
				DialogModuleSystemMouse dialog = new DialogModuleSystemMouse(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getCoordinate());
					repaint();
				}
			}
		}
				
		//// STRING /////////////////////////////////////////////////////////
		if (moduleClassName.equalsIgnoreCase("STRING") == true) {
			if (moduleTypeName.equalsIgnoreCase("VALUE") == true) {
				DialogModuleStringValue dialog = new DialogModuleStringValue(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getStringValue());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("POSITION") == true) {
				DialogModuleStringPosition dialog = new DialogModuleStringPosition(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getVectorValue());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("DIRECTION") == true) {
				DialogModuleStringDirection dialog = new DialogModuleStringDirection(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getVectorValue());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("ROTATION") == true) {
				DialogModuleStringRotation dialog = new DialogModuleStringRotation(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getRotationValue());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("COLOR") == true) {
				DialogModuleStringColor dialog = new DialogModuleStringColor(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getColorValue());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("BOOL") == true) {
				DialogModuleStringBool dialog = new DialogModuleStringBool(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getBoolValue());
					repaint();
				}
			}
		}
			
		//// TRANSFORM / VIEWPOINT / MATERIAL / LIGHT /////////////////////////////////////////////////////////
		if (moduleClassName.equalsIgnoreCase("TRANSFORM") == true || moduleClassName.equalsIgnoreCase("VIEWPOINT") == true ||
			moduleClassName.equalsIgnoreCase("LIGHT") == true  || moduleClassName.equalsIgnoreCase("MATERIAL") == true ) {
			
			String nodeName = module.getValue();
			
			DialogModuleNode dialog = null;
			if (moduleClassName.equalsIgnoreCase("TRANSFORM") == true)
				dialog = new DialogModuleTransformNode(getTopFrame(), getWorld(), sg.findTransformNode(nodeName));
			else if (moduleClassName.equalsIgnoreCase("VIEWPOINT") == true)
				dialog = new DialogModuleViewpointNode(getTopFrame(), getWorld(), sg.findViewpointNode(nodeName));
			else if (moduleClassName.equalsIgnoreCase("LIGHT") == true)
				dialog = new DialogModuleLightNode(getTopFrame(), getWorld(), sg.findLightNode(nodeName));
			else if (moduleClassName.equalsIgnoreCase("MATERIAL") == true)
				dialog = new DialogModuleMaterialNode(getTopFrame(), getWorld(), sg.findMaterialNode(nodeName));
			
			if (dialog.doModal() == Dialog.OK_OPTION) {
				Node node = dialog.getNode();
				if (node != null) {
					Debug.message("Selected node = " + node.getName());
					module.setValue(node.getName());
					repaint();
				}
			}
		}
		
		//// FILTER /////////////////////////////////////////////////////////
		if (moduleClassName.equalsIgnoreCase("FILTER") == true) {
			if (moduleTypeName.equalsIgnoreCase("HIGH") == true) {
				DialogModuleFilterHigh dialog = new DialogModuleFilterHigh(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getHighValue());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("LOW") == true) {
				DialogModuleFilterLow dialog = new DialogModuleFilterLow(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getLowValue());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("OFFSET") == true) {
				DialogModuleFilterOffset dialog = new DialogModuleFilterOffset(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getOffsetValue());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("SCALE") == true) {
				DialogModuleFilterScale dialog = new DialogModuleFilterScale(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getScaleValue());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("MUL") == true) {
				DialogModuleFilterMul dialog = new DialogModuleFilterMul(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getMulValue());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("DIV") == true) {
				DialogModuleFilterDiv dialog = new DialogModuleFilterDiv(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getDivValue());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("RANGE") == true) {
				DialogModuleFilterRange dialog = new DialogModuleFilterRange(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getHighValue() + "," + dialog.getLowValue());
					repaint();
				}
			}
		}

		//// MISC /////////////////////////////////////////////////////////
		if (moduleClassName.equalsIgnoreCase("MISC") == true) {
			if (moduleTypeName.equalsIgnoreCase("PLAYSOUND") == true) {
				String userDir = System.getProperty("user.dir");
				Debug.message("currentDirectoryPath = " + userDir);
				JFileChooser filechooser = new JFileChooser(new File(userDir));
				filechooser.addChoosableFileFilter(new AudioClipFileFilter());
				if(filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					File file = filechooser.getSelectedFile();
					if (file != null) {
						if (file.isDirectory() == false) {
							Debug.message("\tgetName() = " + file.getName());
							module.setValue(file.getName());
						}
					}
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("SETDATA") == true || moduleTypeName.equalsIgnoreCase("GETDATA") == true) {
				DialogModuleMiscDataName dialog = new DialogModuleMiscDataName(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getDataName());
					repaint();
				}
			}			
			else if (moduleTypeName.equalsIgnoreCase("SETARRAY") == true || moduleTypeName.equalsIgnoreCase("GETARRAY") == true) {
				DialogModuleMiscArrayName dialog = new DialogModuleMiscArrayName(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					String option[] = new String[2];
					option[0] = dialog.getGroupName();
					option[1] = dialog.getDataName();
					module.setValue(option);
					repaint();
				}
			}			
			else if (moduleTypeName.equalsIgnoreCase("SENDMESSAGE") == true) {
				DialogModuleUserEvent dialog = new DialogModuleUserEvent(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					String eventName = dialog.getEventName();
					module.setValue(eventName);
					repaint();
				}
			}			
			else if (moduleTypeName.equalsIgnoreCase("SHOWDOCUMENT") == true) {
				DialogModuleMiscShowDocument dialog = new DialogModuleMiscShowDocument(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					String value[] = new String[2];
					value[0] = dialog.getURLString();
					value[1] = dialog.getTargetString();
					if (value[1] != null)
						module.setValue(value);
					else
						module.setValue(value[0]);
					repaint();
				}
			}			
			else if (moduleTypeName.equalsIgnoreCase("SHOWSTATUS") == true) {
				DialogModuleMiscShowStatus dialog = new DialogModuleMiscShowStatus(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					String statusString = dialog.getStatusString();
					module.setValue(statusString);
					repaint();
				}
			}			
		}

		//// INTERPOLATOR /////////////////////////////////////////////////////////
		if (moduleClassName.equalsIgnoreCase("INTERPOLATOR") == true) {
			if (moduleTypeName.equalsIgnoreCase("PLAY") == true) {
				DialogModuleInterpolatorPlay dialog = new DialogModuleInterpolatorPlay(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getOptionValue());
					repaint();
				}
			}
			else {
				DialogModuleInterpolatorNode dialog = new DialogModuleInterpolatorNode(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					Node node = dialog.getNode();
					if (node != null) {
						Debug.message("Selected node = " + node.getName());
						module.setValue(node.getName());
						repaint();
					}
				}
			}		
		}

		//// SENSOR /////////////////////////////////////////////////////////
		if (moduleClassName.equalsIgnoreCase("SENSOR") == true) {
			if (moduleTypeName.equalsIgnoreCase("MAGELLAN") == true) {
				DialogModuleSensorMagellan dialog = new DialogModuleSensorMagellan(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getSerialPost());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("FASTRAK") == true) {
				DialogModuleSensorFastrak dialog = new DialogModuleSensorFastrak(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					String value[] = new String[3];
					value[0] = dialog.getSerialPort();
					value[1] = dialog.getBaud();
					value[2] = dialog.getReciver();
					module.setValue(value);
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("ISOTRAK2") == true) {
				DialogModuleSensorIsotrak2 dialog = new DialogModuleSensorIsotrak2(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					String value[] = new String[3];
					value[0] = dialog.getSerialPort();
					value[1] = dialog.getBaud();
					value[2] = dialog.getReciver();
					module.setValue(value);
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("IS300") == true) {
				DialogModuleSensorIS300 dialog = new DialogModuleSensorIS300(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					String value[] = new String[3];
					value[0] = dialog.getSerialPort();
					value[1] = dialog.getBaud();
					value[2] = dialog.getReciver();
					module.setValue(value);
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("JOYSTICK") == true) {
				DialogModuleSensorJoystick dialog = new DialogModuleSensorJoystick(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getGamePort());
					repaint();
				}
			}
			else if (moduleTypeName.equalsIgnoreCase("BEEBOX") == true) {
				DialogModuleSensorBeeBox dialog = new DialogModuleSensorBeeBox(getTopFrame(), module);
				if (dialog.doModal() == Dialog.OK_OPTION) {
					module.setValue(dialog.getSerialPort());
					repaint();
				}
			}
		}

		//// MISC (NODE) /////////////////////////////////////////////////////////
		if (moduleClassName.equalsIgnoreCase("MISC") == true) {
			DialogModuleNode dialog = null;
			String nodeName = module.getValue();
			
			if (moduleTypeName.equalsIgnoreCase("SetSkyColor") == true) 
				dialog = new DialogModuleBackgroundNode(getTopFrame(), getWorld(), sg.findBackgroundNode(nodeName));
			else if (moduleTypeName.equalsIgnoreCase("SetImageTexture") == true) 
				dialog = new DialogModuleImageTextureNode(getTopFrame(), getWorld(), sg.findImageTextureNode(nodeName));
			else if (moduleTypeName.equalsIgnoreCase("SetAudioClip") == true) 
				dialog = new DialogModuleAudioClipNode(getTopFrame(), getWorld(), sg.findAudioClipNode(nodeName));
			else if (moduleTypeName.equalsIgnoreCase("SetText") == true) 
				dialog = new DialogModuleTextNode(getTopFrame(), getWorld(), sg.findTextNode(nodeName));
			else if (moduleTypeName.equalsIgnoreCase("SetSwitch") == true) 
				dialog = new DialogModuleSwitchNode(getTopFrame(), getWorld(), sg.findSwitchNode(nodeName));
			
			if (dialog != null) {
				if (dialog.doModal() == Dialog.OK_OPTION) {
					Node node = dialog.getNode();
					if (node != null) {
						Debug.message("Selected node = " + node.getName());
						module.setValue(node.getName());
						repaint();
					}
				}
			}
		}
	}	

	public void openModuleInsideDiagram(Module module) {
		Diagram dgm = module.getInsideDiagram();
		if (getCtbIDE().isDiagramFrameOpened(dgm) == false) {
			DiagramFrame dgmFrame = getCtbIDE().openDiagramFrame(dgm);
			dgmFrame.repaint();
		}
	}	
	
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		int mode = setOperationModeAndSelectedModuleFromMousePosition(mx, my);
		
		if (e.getClickCount() == 2 &&  mode != OPERATIONMODE_NONE) {
			Module	module = getSelectingModule();
			if (module != null) {
				if (module.isInsideDiagram() == true)
					openModuleInsideDiagram(module);
				else
					openModuleDialog(module);
			}
		}
		
		setMouseButton(e.getModifiers());
		setMouseStartPosition(mx, my);

	}
	
	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	} 

	public void mousePressed(MouseEvent e) {
		setDraggingModulePostion(-1, -1);
		setDraggingMousePostion(-1, -1);
		setDraggingMousePostion(-1, -1, -1, -1);

		int mx = e.getX();
		int my = e.getY();
		
		int mode = setOperationModeAndSelectedModuleFromMousePosition(e.getX(), e.getY());
		
		if (mode != OPERATIONMODE_NONE) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			repaint();	
		}
		else {
			if (isSelectingBoxOn() == true) {
				if (isInsideSelectingBox(mx, my) == true)
					setOperationMode(OPERATIONMODE_MOVING_SELECTING_BOX);
				else 
					selectingBoxOff();
			}
			else
				setOperationMode(OPERATIONMODE_SELECTING_BOX);			
		}		

		setMouseButton(e.getModifiers());
		setMouseStartPosition(mx, my);
	}

	public void mouseReleased(MouseEvent e) {
		setDrawMode(DRAWMODE_NORMAL);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		int mx = e.getX();
		int my = e.getY();
		
		Diagram dgm = getDiagram();

		if (isOperationMode(OPERATIONMODE_SELECTING_MODULE) == true) {
			Module module = getSelectingModule();

			if (mx == getMouseStartXPosition() && my == getMouseStartYPosition())
				return;
			
			int x = module.getXPosition();
			int y = module.getYPosition();
			
			if (getGridSnap() == true) {
				mx = toGridSnapPosition(mx);
				my = toGridSnapPosition(my);
			}
			
			if (x != mx || y != my) {
				// Undo Action
				addUndoAction(new DiagramMoveModuleUndoAction(module, x, y));
				module.setPosition(mx, my);
				// Update Screen Size
				resetComponentSize();		
			}
		}
		else if (isOperationMode(OPERATIONMODE_SELECTING_MODULE_OUTNODE) == true) {
			Module		outModule	= getSelectingModule();
			ModuleNode	outNode		= outModule.getOutNode(getSelectingOutNode());

			ModuleSelectedData dgmModuleData = dgm.getModule(mx, my);
			setOperationMode(dgmModuleData.getParts());
			setSelectingModule(dgmModuleData.getModule());

			if (isOperationMode(OPERATIONMODE_SELECTING_MODULE_INNODE) == true) {
				Module		inModule	= getSelectingModule();
				ModuleNode	inNode	= inModule.getInNode(getSelectingInNode());

				getDiagram().addDataflow(outModule, outNode, inModule, inNode);

				// Undo Action
				addUndoAction(new DiagramCreateModuleLineUndoAction(outModule, outNode, inModule, inNode));
			}
			else if (isOperationMode(Module.INSIDE_EXECUTIONNODE) == true) {
				Module		inModule	= getSelectingModule();
				ModuleNode	inNode	= inModule.getExecutionNode();

				getDiagram().addDataflow(outModule, outNode, inModule, inNode);

				// Undo Action
				addUndoAction(new DiagramCreateModuleLineUndoAction(outModule, outNode, inModule, inNode));
			}
		}
		else if (isOperationMode(OPERATIONMODE_SELECTING_MODULE_INNODE) == true) {
			Module		inModule	= getSelectingModule();
			ModuleNode	inNode	= inModule.getInNode(getSelectingInNode());

			ModuleSelectedData dgmModuleData = dgm.getModule(mx, my);
			setOperationMode(dgmModuleData.getParts());
			setSelectingModule(dgmModuleData.getModule());

			if (isOperationMode(OPERATIONMODE_SELECTING_MODULE_OUTNODE) == true) {
				Module		outModule	= getSelectingModule();
				ModuleNode	outNode		= outModule.getOutNode(getSelectingOutNode());

				getDiagram().addDataflow(outModule, outNode, inModule, inNode);

				// Undo Action
				addUndoAction(new DiagramCreateModuleLineUndoAction(outModule, outNode, inModule, inNode));
			}
		}
		else if (isOperationMode(Module.INSIDE_EXECUTIONNODE) == true) {
			Module 		inModule	= getSelectingModule();
			ModuleNode	inNode	= inModule.getExecutionNode();

			ModuleSelectedData dgmModuleData = dgm.getModule(mx, my);
			setOperationMode(dgmModuleData.getParts());
			setSelectingModule(dgmModuleData.getModule());

			if (isOperationMode(OPERATIONMODE_SELECTING_MODULE_OUTNODE) == true) {
				Module		outModule	= getSelectingModule();
				ModuleNode	outNode		= outModule.getOutNode(getSelectingOutNode());

				getDiagram().addDataflow(outModule, outNode, inModule, inNode);

				// Undo Action
				addUndoAction(new DiagramCreateModuleLineUndoAction(outModule, outNode, inModule, inNode));
			}
		}
		
		//////////////////////////////////////////////////////
		// SelectingBox Operation
		//////////////////////////////////////////////////////
		
		switch (getOperationMode()) {
		case OPERATIONMODE_SELECTING_BOX:
			{		
				int mxStart = getMouseStartXPosition();
				int myStart = getMouseStartYPosition();
			
				if (mxStart != mx && myStart != my) {

					if (getGridSnap()) {
						mxStart = toGridSnapPosition(mxStart);
						myStart = toGridSnapPosition(myStart);
						mx = toGridSnapPosition(mx);
						my = toGridSnapPosition(my);
					}

					setSelectingBoxRect(mxStart, myStart, mx, my);
					selectingBoxOn();
				}
				else
					selectingBoxOff();
			}
			break;
		case OPERATIONMODE_MOVING_SELECTING_BOX:
			{
				Rectangle selBoxRect = getSelectingBoxRectangle();
				
				int offsetx = (mx - getMouseStartXPosition());
				int offsety = (my - getMouseStartYPosition());
				
				int nSelBoxModule = dgm.getNModules(selBoxRect);

				Module	undoModule[]			= new Module[nSelBoxModule];
				Point	undoModulePos[]	= new Point[nSelBoxModule];

				int moduleNum = dgm.getNModules();
				int nModule = 0;
				for (int n=0; n<moduleNum; n++) {
					Module module = dgm.getModule(n);
					int modulex = module.getXPosition();
					int moduley = module.getYPosition();
					int moduleSize = module.getSize();
					if (selBoxRect.contains(modulex, moduley) == true && selBoxRect.contains(modulex+moduleSize, moduley+moduleSize) == true) {
						undoModule[nModule] = dgm.getModule(n);
						undoModulePos[nModule] = new Point(modulex, moduley);
						module.setPosition(modulex + offsetx, moduley + offsety);
						nModule++;
					}
				}

				addUndoAction(new DiagramMoveSelectingBoxUndoAction(undoModule, undoModulePos));

				setSelectingBoxLocation(selBoxRect.x + offsetx, selBoxRect.y + offsety);
			}
			break;
		}
		
		setMouseButton(0);
		setDrawMode(DRAWMODE_NORMAL);
		
		repaint();
	} 

	public void mouseDragged(MouseEvent e) {
		if (getMouseButton() == 0)
			return;
		
		int mx = e.getX();
		int my = e.getY();
		
		if (mx == getMouseStartXPosition() && my == getMouseStartYPosition())
			return;
			
		Module selectedModule = getSelectingModule();
		if (selectedModule != null) {
			if (isOperationMode(OPERATIONMODE_SELECTING_MODULE) == true) { 
//				Module module = new Module(getWorld(), selectedModule);
//				module.setPosition(e.getX(), e.getY());
				if (getGridSnap() == true) {
					mx = toGridSnapPosition(mx);
					my = toGridSnapPosition(my);
				}
				setDraggingModulePostion(mx, my);
				setDrawMode(DRAWMODE_DRAGGING_MODULE);
				repaint();
			}
			
			if (isOperationMode(OPERATIONMODE_SELECTING_MODULE_OUTNODE) || isOperationMode(OPERATIONMODE_SELECTING_MODULE_INNODE) || isOperationMode(Module.INSIDE_EXECUTIONNODE)) {
				Module module = selectedModule;
				int nodeHalfSize = module.getNodeSize() / 2;
				int	sx, sy;
			
				if (isOperationMode(OPERATIONMODE_SELECTING_MODULE_OUTNODE)) {
					sx = module.getOutNodeXPosition(getSelectingOutNode());
					sy = module.getOutNodeYPosition(getSelectingOutNode());
				}
				else if (isOperationMode(OPERATIONMODE_SELECTING_MODULE_INNODE)) {
					sx = module.getInNodeXPosition(getSelectingInNode());
					sy = module.getInNodeYPosition(getSelectingInNode());
				}
				else { //(isOperationMode(Module.INSIDE_EXECUTIONNODE)) {
					sx = module.getExecutionNodeXPosition();
					sy = module.getExecutionNodeYPosition();
				}
				setDraggingMousePostion(sx, sy, mx, my);
				setDrawMode(DRAWMODE_DRAGGING_MODULELINE);
				repaint();
			}
		}
		else {
			switch (getOperationMode()) {
			case OPERATIONMODE_SELECTING_BOX:
				{	
					if (getGridSnap() == true) {
						mx = toGridSnapPosition(mx);
						my = toGridSnapPosition(my);
					}
					setDraggingMousePostion(mx, my);
					setDrawMode(DRAWMODE_DRAGGING_SELECTINGBOX);
					repaint();
				}
				break;
			case OPERATIONMODE_MOVING_SELECTING_BOX:
				{	
					if (getGridSnap() == true) {
						mx = toGridSnapPosition(mx);
						my = toGridSnapPosition(my);
					}
					setDraggingMousePostion(mx, my);
					setDrawMode(DRAWMODE_MOVING_SELECTINGBOX);
					repaint();
				}
				break;
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	//////////////////////////////////////////////////
	// KeyListener
	//////////////////////////////////////////////////

	public void deleteObject() {
		Debug.message("DiagramFrame::deleteObject");
		
		int		operationMode = getOperationMode();
		Module	selectedModule = getSelectingModule();
		
		Debug.message("\tmode   = " + operationMode);
		Debug.message("\tmodule = " + selectedModule);
		
		if (selectedModule != null) {
			if (isOperationMode(OPERATIONMODE_SELECTING_MODULE) == true) {
				Debug.message("\tmode = OPERATIONMODE_SELECTING_MODULE");
				Diagram		diagram		= getDiagram();
				Module		module	= selectedModule;
				if (module.isSystemModule() == false) {
					addUndoAction(new DiagramDeleteModuleUndoAction(module));
					diagram.removeModule(module);
					if (module.isInsideDiagram() == true) {
						Diagram insideDgm = module.getInsideDiagram();
						getCtbIDE().closeDiagramFrame(insideDgm);
					}
				}
				else
					Message.beep();
			}
			else if (isOperationMode(OPERATIONMODE_SELECTING_MODULE_OUTNODE) == true) {
				Debug.message("\tmode = OPERATIONMODE_SELECTING_MODULE_OUTNODE");
				Module	outModule = selectedModule;
				ModuleNode		outNode = outModule.getOutNode(getSelectingOutNode());
				Dataflow		dataflow = getDiagram().getDataflows();
				while (dataflow != null) {
					Dataflow nextDataflow = dataflow.next();
					if (dataflow.getOutModule() == outModule && dataflow.getOutNode() == outNode) {
						addUndoAction(new DiagramDeleteModuleLineUndoAction(dataflow.getOutModule(), dataflow.getOutNode(), dataflow.getInModule(), dataflow.getInNode()));
						getDiagram().removeDataflow(dataflow);
					}
					dataflow = nextDataflow;
				}
			}
			else if (isOperationMode(OPERATIONMODE_SELECTING_MODULE_INNODE) == true) {
				Debug.message("\tmode = OPERATIONMODE_SELECTING_MODULE_INNODE");
				Module		inModule = selectedModule;
				ModuleNode	inNode = inModule.getInNode(getSelectingInNode());
				Dataflow		dataflow = getDiagram().getDataflows();
				while (dataflow != null) {
					Dataflow nextDataflow = dataflow.next();
					if (dataflow.getInModule() == inModule && dataflow.getInNode() == inNode) {
						addUndoAction(new DiagramDeleteModuleLineUndoAction(dataflow.getOutModule(), dataflow.getOutNode(), dataflow.getInModule(), dataflow.getInNode()));
						getDiagram().removeDataflow(dataflow);
					}
					dataflow = nextDataflow;
				}
			}
			else if (isOperationMode(Module.INSIDE_EXECUTIONNODE) == true) {
				Debug.message("\tmode = Module.INSIDE_EXECUTIONNODE");
				Module		inModule	= selectedModule;
				ModuleNode	inNode = inModule.getExecutionNode();
				Dataflow		dataflow = getDiagram().getDataflows();
				while (dataflow != null) {
					Dataflow nextDataflow = dataflow.next();
					if (dataflow.getInModule() == inModule && dataflow.getInNode() == inNode) {
						addUndoAction(new DiagramDeleteModuleLineUndoAction(dataflow.getOutModule(), dataflow.getOutNode(), dataflow.getInModule(), dataflow.getInNode()));
						getDiagram().removeDataflow(dataflow);
					}
					dataflow = nextDataflow;
				}
			}
			else 
				Debug.message("\tmode = NONE");
			setSelectingModule(null);
			setOperationMode(OPERATIONMODE_NONE);			
			repaint();
		}
		else {
			if (isSelectingBoxOn())
				del();
		}
	}
		
	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		Debug.message("DiagramFrame::keyPressed = " + e.getKeyText(e.getKeyCode()));
		if (e.getKeyCode() == e.VK_DELETE)
			deleteObject();
	}

	public void keyReleased(KeyEvent e) {
	}
}
