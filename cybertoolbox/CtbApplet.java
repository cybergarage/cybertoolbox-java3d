/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1999
*
*	File:	CtbApplet.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.File;
import java.net.*;

import javax.swing.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import cv97.*;
import cv97.node.*;
import cv97.j3d.*;

public class CtbApplet extends Applet implements Constants, MouseListener, MouseMotionListener, KeyListener, Runnable {

	static final public int	OPERATION_MODE_PICK	=	0;
	static final public int	OPERATION_MODE_WALK	=	1;
	static final public int	OPERATION_MODE_PAN	=	2;
	static final public int	OPERATION_MODE_ROT	=	3;
	
	private World						mWorld				= null;					
	private SceneGraphJ3dObject	mSceneGraphObject	= null;
	private Thread						mThread				= null;
	private Canvas3D					mCanvas3D			= null;
	
	public void setWorld(World world) {
		mWorld = world;
	}

	public World getWorld() {
		return mWorld;
	}

	public SceneGraph getSceneGraph() {
		return getWorld().getSceneGraph();
	}
	
	public SceneGraphJ3dObject getSceneGraphJ3dObject() {
		return (SceneGraphJ3dObject)getSceneGraph().getObject();
	}

	public void setCanvas3D(Canvas3D c) {
		 mCanvas3D = c;
	}
	
	public Canvas3D getCanvas3D() {
		return mCanvas3D;
	}
	
	////////////////////////////////////////////////
	// Constructor
	////////////////////////////////////////////////
	
	public CtbApplet() {
	}
	
	////////////////////////////////////////////////
	// Applet
	////////////////////////////////////////////////

	public void init(World world, Canvas3D c) {
		setWorld(world);
		setCanvas3D(c);

		//getContentPane().setLayout(new BorderLayout());
		setLayout(new BorderLayout());

		//getContentPane().add("Center", c);
		add("Center", c);

		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
		enableEvents(AWTEvent.KEY_EVENT_MASK);
		
		c.addMouseListener(this);
		c.addMouseMotionListener(this);
		addKeyListener(this);
	}
	
	public void init(World world) {
		Canvas3D c = new Canvas3D(null);
		
		init(world, c);
		
		SceneGraphJ3dObject sgObject = new SceneGraphJ3dObject(c, getSceneGraph());
		getSceneGraph().setObject(sgObject);
		getSceneGraph().setHeadlightState(true);
	}

	public void init() {
		Debug.message("CtbAppletApplet.init");
		
		World world = new World();
		CtbApplet applet[] = new CtbApplet[1];
		applet[0] = this;
		world.setApplets(applet);
		world.setAppletMode(true);
		world.setModuleTypes();
		
		init(world);
		
		URL docBaseURL = getDocumentBase();
		world.setBaseURL(docBaseURL);
		Debug.message("Document Base = " + docBaseURL.toString());
		Debug.message("\tfile = " + docBaseURL.getFile());
		Debug.message("\thost = " + docBaseURL.getHost());
		Debug.message("\tport = " + docBaseURL.getPort());
		Debug.message("\tprotocol = " + docBaseURL.getProtocol());
		Debug.message("\tref = " + docBaseURL.getRef());
		
		URL baseURL = World.getBaseURL(docBaseURL);
		Debug.message("Base URL = " + docBaseURL.toString());
		
		boolean isLoadingOk = false;
		
		String srcParam = getParameter("src");
		Debug.message("Paramete URL = " + srcParam);
		if (baseURL != null && srcParam != null) {
			try {
				isLoadingOk = world.add(new URL(srcParam));
			} catch (MalformedURLException mue) {
				Debug.warning("\tMalformedURLException");
				Debug.warning("\t\tCouldn't load a source file (" + srcParam + ")");
			}
			if (isLoadingOk == false) {
				try {
					isLoadingOk = world.add(new URL(baseURL.toString() + srcParam));
				} catch (MalformedURLException mue) {
					Debug.warning("\tMalformedURLException");
					Debug.warning("\t\tCouldn't load a source file (" + (baseURL.toString() + srcParam) + ")");
				}
			}
		}
		
		if (isLoadingOk == true) 
			world.startSimulation();
		else
			Debug.warning("Couldn't load a source file (" + srcParam + ")");

		startThread();
	}

	public void destroy() {
		Debug.message("CtbAppletApplet.destroy");
	}

	public void start() {
		Debug.message("CtbAppletApplet.start");
	}

	public void stop() {
		Debug.message("CtbAppletApplet.stop");
	}

	////////////////////////////////////////////////
	// Thread
	////////////////////////////////////////////////

	public void startThread() {
		if (mThread == null) {
			mThread = new Thread(this);
			mThread.start();
		}
		getSceneGraphJ3dObject().start(getSceneGraph());
	}

	public void stopThread() {
		if (mThread != null) {
			mThread.stop();
			mThread = null;
		}
		getSceneGraphJ3dObject().stop(getSceneGraph());
	}

	////////////////////////////////////////////////
	// Popup Menu
	////////////////////////////////////////////////
	
	public class PopupMenu extends JPopupMenu {
		private String menuString[] = {
			"Start View",
			"XY Plane View",
			"XZ Plane View",
			"YZ Plane View",
			"",
			"Wireframe",
			"Shading",
		};
		
		public PopupMenu() {
			for (int n=0; n<menuString.length; n++) {
				if (0 < menuString[n].length()) {
					JMenuItem menuItem = new JMenuItem(menuString[n]);
					menuItem.addActionListener(new PopupMenuAction());
					add(menuItem);
				}
				else
					addSeparator();
			}
		}
		
		private class PopupMenuAction extends AbstractAction {
  		 	public void actionPerformed(ActionEvent e) {
  	 			Debug.message("PopupMenuAction.actionPerformed = " + e.getActionCommand());	
				for (int n=0; n<menuString.length; n++) {
					if (menuString[n].equals(e.getActionCommand()) == true) {
						switch (n) {
						case 0:	getSceneGraph().resetViewpoint(); break;
						case 1:	getSceneGraph().resetViewpointAlongZAxis(); break;
						case 2:	getSceneGraph().resetViewpointAlongYAxis(); break;
						case 3:	getSceneGraph().resetViewpointAlongXAxis(); break;
						case 5: getSceneGraph().setRenderingMode(SceneGraph.RENDERINGMODE_LINE); break;
						case 6: getSceneGraph().setRenderingMode(SceneGraph.RENDERINGMODE_FILL); break;
						}
						repaint();
						break;
					}
				}
			}
		}
	}

	////////////////////////////////////////////////
	//	Operation Mode
	////////////////////////////////////////////////
	
	private String OPERATION_MODE_PICK_STRING	= "PICK";
	private String OPERATION_MODE_WALK_STRING	= "WALK";
	private String OPERATION_MODE_PAN_STRING	= "PAN";
	private String OPERATION_MODE_ROT_STRING	= "ROT";
	
	public void setOperationMode(int mode) {

		SceneGraph sg = getSceneGraph();
		if (sg == null)
			return;
			
		NavigationInfoNode navInfo = sg.getNavigationInfoNode();
		if (navInfo == null)
			navInfo = sg.getDefaultNavigationInfoNode();
		
		String typeString = null;
		switch (mode) {
		case OPERATION_MODE_PICK:
			typeString = OPERATION_MODE_PICK_STRING;
			break;
		case OPERATION_MODE_WALK:
			typeString = OPERATION_MODE_WALK_STRING;
			break;
		case OPERATION_MODE_PAN:
			typeString = OPERATION_MODE_PAN_STRING;
			break;
		case OPERATION_MODE_ROT:
			typeString = OPERATION_MODE_ROT_STRING;
			break;
		default:
			typeString = OPERATION_MODE_WALK_STRING;
			break;
		}
		
		int nTypes = navInfo.getNTypes();
		if (0 < nTypes)
			navInfo.setType(0, typeString);
		else
			navInfo.addType(typeString);
	}

	public int getOperationMode() {
		SceneGraph sg = getSceneGraph();
		if (sg == null)
			return OPERATION_MODE_WALK;
			
		NavigationInfoNode navInfo = sg.getNavigationInfoNode();
		if (navInfo == null)
			navInfo = sg.getDefaultNavigationInfoNode();

		int nTypes = navInfo.getNTypes();
		if (nTypes < 1)
			return OPERATION_MODE_WALK;

		String typeString = navInfo.getType(0);
		if (typeString == null)
			return OPERATION_MODE_WALK;
		if (typeString.equals(OPERATION_MODE_PICK_STRING) == true)
			return OPERATION_MODE_PICK;
		if (typeString.equals(OPERATION_MODE_WALK_STRING) == true)
			return OPERATION_MODE_WALK;
		if (typeString.equals(OPERATION_MODE_PAN_STRING) == true)
			return OPERATION_MODE_PAN;
		if (typeString.equals(OPERATION_MODE_ROT_STRING) == true)
			return OPERATION_MODE_ROT;
			
		return OPERATION_MODE_WALK;
	}

			
	////////////////////////////////////////////////
	//	mouse
	////////////////////////////////////////////////

	private ShapeNode	mSelectedShapeNode	= null;
	
	private void setSelectedShapeNode(ShapeNode shapeNode) {
		mSelectedShapeNode = shapeNode;
	}

	private ShapeNode getSelectedShapeNode() {
		return mSelectedShapeNode;
	}

	private String mouseEventString[] = new String[2];
	private String pickupEventString[] = new String[3];
	
	public void sendMouseEvent(boolean button, int x, int y) {
		if (getWorld().isSimulationRunning() == false)
			return;
			
		Event mouseEvent = getWorld().getSystemEvent("Mouse");

		if (mouseEvent == null)
			return;

		mouseEventString[0] = button ? "true" : "false";	
		mouseEventString[1] = Integer.toString(x) + "," + Integer.toString(y);		
		mouseEvent.run(mouseEventString);
	}

	public void sendPickupEvent(boolean button, ShapeNode shapeNode, int offsetx, int offsety) {
		if (getWorld().isSimulationRunning() == false)
			return;
			
		String shapeName = shapeNode.getName();
		
		for (Event event=getWorld().getEvents(); event != null; event=event.next()) {
			int eventTypeNumber = event.getEventTypeNumber();
			if (eventTypeNumber != EventType.SYSTEM_PICKUP && eventTypeNumber != EventType.USER_PICKUP)
				continue;
			if (eventTypeNumber == EventType.USER_PICKUP) {
				String optionString = event.getOptionString();
				if (optionString.equals(shapeName) == false)
					continue;
			}
			
			pickupEventString[0] = button ? "true" : "false";	
			pickupEventString[1] = (shapeName != null) ? shapeName : "No Name";		
			pickupEventString[2] = Integer.toString(offsetx) + "," + Integer.toString(offsety);		
			event.run(pickupEventString);
		}
	}
	
	private int	mStartMouseX = 0;
	private int	mStartMouseY = 0;
	private int	mMouseX = 0;
	private int	mMouseY = 0;
	private int	mMouseButton = 0;
	
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		Debug.message("CtbApplet::mousePressed : " + e.getModifiers());
		
		mMouseButton = e.getModifiers();

		int mx, my;
		
		mx = mStartMouseX = e.getX();
		my = mStartMouseY = e.getY();
		
		if (e.getModifiers() == e.BUTTON1_MASK) {
			if (getOperationMode() == OPERATION_MODE_PICK) {
				ShapeNode shapeNode = getSceneGraphJ3dObject().pickShapeNode(getSceneGraph(), getCanvas3D(), e.getX(), e.getY());
				if (shapeNode != null) {
					getSceneGraph().shapePressed(shapeNode, mx, my);
					sendPickupEvent(true, shapeNode, 0, 0);
				}
				setSelectedShapeNode(shapeNode);
			}
			sendMouseEvent(true, mx, my);
		}
		
		if (e.getModifiers() == e.BUTTON3_MASK) {
			JPopupMenu.setDefaultLightWeightPopupEnabled(false); 
			PopupMenu popupMenu = new PopupMenu();
			popupMenu.show(this, mx, my);
		}
	}

	public void mouseReleased(MouseEvent e) {
		Debug.message("CtbApplet::mouseReleased : " + e.getModifiers());
		
		if (e.getModifiers() == e.BUTTON1_MASK)
			mMouseButton = 0;
		
		mMouseX = e.getX();
		mMouseY = e.getY();

		int smx = getStartMouseX();
		int smy = getStartMouseY();
		
		if (e.getModifiers() == e.BUTTON1_MASK) {
			if (getOperationMode() == OPERATION_MODE_PICK) {
				ShapeNode shapeNode = getSelectedShapeNode();
				if (shapeNode != null) {
					getSceneGraph().shapeReleased(shapeNode, mMouseX, mMouseY);
					sendPickupEvent(false, shapeNode, mMouseX-smx, mMouseY-smy);
				}
			}
			sendMouseEvent(false, mMouseX, mMouseY);
		}
	}

	public void mouseDragged(MouseEvent e) {
		mMouseX = e.getX();
		mMouseY = e.getY();
		int smx = getStartMouseX();
		int smy = getStartMouseY();
		if (getOperationMode() == OPERATION_MODE_PICK) {
			ShapeNode shapeNode = getSelectedShapeNode();
			if (shapeNode != null) {
				getSceneGraph().shapeReleased(shapeNode, mMouseX, mMouseY);
				sendPickupEvent(true, shapeNode, mMouseX-smx, mMouseY-smy);
			}
		}
		sendMouseEvent(true, mMouseX, mMouseY);
	}

	public void mouseMoved(MouseEvent e) {
		if (getWorld().isSimulationRunning() == true && hasFocus() == false)
			requestFocus();
		mMouseX = e.getX();
		mMouseY = e.getY();
		sendMouseEvent(false, mMouseX, mMouseY);
	}

	public int getMouseX() {
		return mMouseX;
	}

	public int getMouseY() {
		return mMouseY;
	}

	public int getStartMouseX() {
		return mStartMouseX;
	}

	public int getStartMouseY() {
		return mStartMouseY;
	}

	public int getMouseButton() {
		return mMouseButton;
	}

	////////////////////////////////////////////////
	// Keyboard
	////////////////////////////////////////////////

	public void sendKeyboardEvent(boolean pressed, String keyText) {
		
		if (getWorld().isSimulationRunning() == false)
			return;
		
		for (Event event=getWorld().getEvents(); event != null; event=event.next()) {
			int eventTypeNumber = event.getEventTypeNumber();
			if (eventTypeNumber != EventType.SYSTEM_KEYBOARD && eventTypeNumber != EventType.USER_KEYBOARD)
				continue;
			if (eventTypeNumber == EventType.USER_KEYBOARD) {
				String optionString = event.getOptionString();
				if (optionString.equals(keyText) == false)
					continue;
			}
			
			Debug.message("\tsend event = " + event);
			
			String eventString[] = new String[2];
			eventString[0] = pressed ? "true" : "false";	
			eventString[1] = keyText;		
			event.run(eventString);
		}
	}
		
	public void keyPressed(KeyEvent e) {
		String keyText = KeyEvent.getKeyText(e.getKeyCode());
		Debug.message("KeyPressed = " + keyText);
		sendKeyboardEvent(true, keyText);

		if (Debug.isEnabled() == true) {
			if (e.getKeyCode() == KeyEvent.VK_P)
				getSceneGraph().print();
		}
	} 
	
	public void keyReleased(KeyEvent e) {
		String keyText = KeyEvent.getKeyText(e.getKeyCode());
		Debug.message("KeyReleased = " + keyText);
		sendKeyboardEvent(false, keyText);
	}
	
	public void keyTyped(KeyEvent e) {
	}

	////////////////////////////////////////////////
	//	Viewpoint
	////////////////////////////////////////////////

	public void updateViewpoint() {

		// get mouse infomations
		int		mx = getMouseX();
		int		my = getMouseY();
		int		mbutton = getMouseButton();
		
		if (mbutton != MouseEvent.BUTTON1_MASK) 
			return;

		float	width2 = (float)getWidth() / 2.0f;
		float	height2 = (float)getHeight() /2.0f;

		float	vector[]		= {0.0f, 0.0f, 0.0f};
		float	axisRot[]	= {0.0f, 0.0f, 0.0f};

		SceneGraph sg = getSceneGraph();
		if (sg == null)
			return;
			
		NavigationInfoNode navInfo = sg.getNavigationInfoNode();
		if (navInfo == null)
			navInfo = sg.getDefaultNavigationInfoNode();
			
		float speed = navInfo.getSpeed();
		 
		switch (getOperationMode()) {
		case OPERATION_MODE_WALK:
			vector[Z]	= ((float)my - height2) / height2 * 0.1f * speed;
			axisRot[Y]	= -((float)mx - width2) / width2 * 0.02f;
			break;			
		case OPERATION_MODE_PAN:
			vector[X]	= ((float)mx - width2) / width2 * 0.1f * speed;
			vector[Y]	= -((float)my - height2) / height2 * 0.1f * speed;
			break;			
		case OPERATION_MODE_ROT:
			axisRot[X]	= -((float)my - height2) / height2 * 0.02f;
			axisRot[Z]	= -(((float)mx - width2) / width2 * 0.02f);
			break;			
		}
					
		ViewpointNode viewNode = sg.getViewpointNode();
		if (viewNode == null)
			viewNode = sg.getDefaultViewpointNode();
		
		float viewOrienataion[] = new float[4];
		for (int n=0; n<3; n++) {
			if (axisRot[n] != 0.0f) {
				viewOrienataion[0] = viewOrienataion[1] = viewOrienataion[2] = 0.0f;
				viewOrienataion[n] = 1.0f;
				viewOrienataion[3] = axisRot[n];
				viewNode.addOrientation(viewOrienataion);
			}
		}	

		float viewFrame[][] = new float[3][3];
		viewNode.getFrame(viewFrame);
		for (int n=X; n<=Z; n++) {
			viewFrame[n][X] *= vector[n];
			viewFrame[n][Y] *= vector[n];
			viewFrame[n][Z] *= vector[n];
			viewNode.addPosition(viewFrame[n]);
		}
	}

	////////////////////////////////////////////////
	//	runnable
	////////////////////////////////////////////////

	public void run() {
		int count = 0;
		while (true) {
			updateViewpoint();
			repaint();
			
			try {
				mThread.sleep(100);
			} catch (InterruptedException e) {}
/*			
			if (getWorld().isSimulationRunning() == true) {
				if (5 <= count) {
					requestFocus();
					count = 0;
				}
				count++;
			}
*/
		}
	}
}	
