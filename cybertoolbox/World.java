/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	World.java
*
******************************************************************/

import java.io.*;
import java.awt.Point;
import java.util.*;
import java.net.*;

import javax.swing.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.lw3d.Lw3dLoader;

import sun.tools.javac.*;

import cv97.*;
import cv97.node.*;
import cv97.node.Node;
import cv97.field.*;
import cv97.route.*;
import cv97.util.*;
import cv97.j3d.*;

public class World extends Object implements ModuleTypeConstants {

	public static final String RELEASE_NUMBER	= "1.3";
	public static final String RELEASE_DATE	= "2000/03/31";
	
	public static final String FILESEP	= System.getProperty("file.separator");

	public static final int RENDERING_WIRE		= 0;
	public static final int RENDERING_SHADE	= 1;
	public static final int RENDERING_TEXTURE	= 2;

	public static final int OPETATION_MODE_NONE						= 0;
	public static final int OPETATION_MODE_MODULETYPE_DRAGGED	= 1;

	public static final String SYSTEMNODE_NAMEHEADER				= "CTB_";
	
	public static final String BEHAVIOR_FILE_EXTENTION				= "cbf";
	public static final String BEHAVIOR_WORLDINFO_NODENAME		= SYSTEMNODE_NAMEHEADER + "BEHAVIRO_INFO";
		
	private SceneGraph	mSceneGraph		= null;
	private CtbApplet		mCtbApplet[]	= new CtbApplet[0];
	
	public static final String	MOUSE_FRAME_COORDINATE_STRING			= "Frame";
	public static final String	MOUSE_NORMALIZED_COORDINATE_STRING	= "Normalized";
	public static final int		MOUSE_FRAME_COORDINATE					= 0;
	public static final int		MOUSE_NORMALIZED_COORDINATE			= 1;
	
	public World() {
		mSceneGraph = new SceneGraph();
		loadEventTypes();
		addSystemEvents();
		setAppletMode(false);
		getSceneGraph().start();
	}

	//////////////////////////////////////////////////
	// Member
	//////////////////////////////////////////////////
	
	public void setSceneGraph(SceneGraph sg) {
		mSceneGraph = sg;
	}
	
	public SceneGraph getSceneGraph() {
		return mSceneGraph;
	}

	public void setApplets(CtbApplet applet[]) {
		mCtbApplet = applet;
	}
	
	public int getNApplets() {
		return mCtbApplet.length;
	}
	
	public CtbApplet[] getApplets() {
		return mCtbApplet;
	}

	public CtbApplet getApplet(int n) {
		return mCtbApplet[n];
	}

	//////////////////////////////////////////////////
	// Directory / URL
	//////////////////////////////////////////////////

	private String	mBaseDirectory	= null;
	private URL		mBaseURL			= null;
	
	public void setBaseDirectory(String dir) {
		mBaseDirectory = dir;
	}

	public String getBaseDirectory() {
		return mBaseDirectory;
	}
	
	public void setBaseURL(URL dir) {
		mBaseURL = dir;
	}

	public URL getBaseURL() {
		return mBaseURL;
	}

	static public String getModuleTypeDirectory() {
		return "modules" + FILESEP;
	}

	static public String getModuleTypeIconDirectory() {
		return getModuleTypeDirectory() + "images" + FILESEP;
	}

	static public String getModuleTypeScriptDirectory() {
		return getModuleTypeDirectory() + "scripts" + FILESEP;
	}

	static public String getImageDirectory() {
		return "images" + FILESEP;
	}

	static public String getImageToolbarDirectory() {
		return getImageDirectory() + "toolbar" + FILESEP;
	}

	static public String getImageToolbarWorldTreeDirectory() {
		return getImageToolbarDirectory() + "worldtree" + FILESEP;
	}
	
	static public String getImageToolbarPerspectiveviewDirectory() {
		return getImageToolbarDirectory() + "perspectiveview" + FILESEP;
	}

	static public String getImageToolbarDiagramDirectory() {
		return getImageToolbarDirectory() + "diagram" + FILESEP;
	}
	
	static public String getImageToolbarModuleEditorDirectory() {
		return getImageToolbarDirectory() + "module_editor" + FILESEP;
	}
	
	static public String getImageNodeDirectory() {
		return getImageDirectory() + "node" + FILESEP;
	}
	
	static public String getModuleTypeDefaultSouceFileName() {
		return getModuleTypeScriptDirectory() + "ClassName.java";
	}
	
	static public String getModuleTypeDefaultIconFileName() {
		return getModuleTypeIconDirectory() + "ClassName.gif";
	}
	
	//////////////////////////////////////////////////
	// Applet mode
	//////////////////////////////////////////////////
	
	private boolean mAppletMode;
	
	public void setAppletMode(boolean mode) {
		mAppletMode = mode;
	}

	public boolean isAppletMode() {
		return mAppletMode;
	}
		
	//////////////////////////////////////////////////
	// Rendering mode
	//////////////////////////////////////////////////
	
	private int mRenderingMode = 0;

	public void setRenderingMode(int mode) {
		mRenderingMode = mode;
	}

	public int getRenderingMode() {
		return mRenderingMode;
	}

	//////////////////////////////////////////////////
	// Operation mode
	//////////////////////////////////////////////////
	
	private int mOperationMode = OPETATION_MODE_NONE;

	public void setOperationMode(int mode) {
		mOperationMode = mode;
	}

	public int getOperationMode() {
		return mOperationMode;
	}

	//////////////////////////////////////////////////
	//	Node Check
	//////////////////////////////////////////////////

	public boolean isSystemNode(Node node) {
		if (node == null)
			return false;
		try {
			String nodeHeaderName = new String(node.getName().toCharArray(), 0, SYSTEMNODE_NAMEHEADER.length());
			if (nodeHeaderName.equals(SYSTEMNODE_NAMEHEADER) == true)
				return true;
		} catch (Exception e) {}
		return false;
	}
	
	//////////////////////////////////////////////////
	// EventBehavior
	//////////////////////////////////////////////////

	private EventBehaviorGroup mEventBehaviorGroup = new EventBehaviorGroup(this);
	
	public EventBehaviorGroup getEventBehaviorGroup() {
		return mEventBehaviorGroup;
	}

	public void addEventBehaviors(EventBehaviorGroup ebGroup) {
		ebGroup.clear();

		Event frameEvent = getSystemEvent("Frame");
		ebGroup.addEventBehavior(new EventBehaviorFrame(frameEvent));
		
		for (Event event=getEvents(); event != null; event=event.next()) {
			EventBehavior eventBehavior = null;
			switch (event.getEventTypeNumber()) {
			case EventType.USER_CLOCK:
				eventBehavior = new EventBehaviorClock(event);
				break;
			case EventType.USER_TIMER:
				eventBehavior = new EventBehaviorTimer(event);
				break;
			case EventType.USER_AREA:
				eventBehavior = new EventBehaviorArea(event);
				break;
			case EventType.USER_COLLISION:
				eventBehavior = new EventBehaviorCollision(event);
				break;
			}
			if (eventBehavior != null)
				ebGroup.addEventBehavior(eventBehavior);
		}
	}
	
	//////////////////////////////////////////////////
	// Simulation
	//////////////////////////////////////////////////
	
	private void initializeSimulation() {
		for (Event event = getEvents(); event != null; event = event.next())
			event.initialize();
	}

	private void shutdownSimulation() {
		for (Event event = getEvents(); event != null; event = event.next())
			event.shutdown();
	}

	public void startAppletThread() {
		for (int n=0; n<getNApplets(); n++) 
			getApplet(n).startThread();
	}

	public void stopAppletThread() {
		for (int n=0; n<getNApplets(); n++) 
			getApplet(n).stopThread();
	}

	public void runStartEvent() {
		Event startEvent = getSystemEvent("Start");
		if (startEvent == null)
			return;
		startEvent.run();
	}
	
	public void startSimulation() {
		SceneGraph sg = getSceneGraph();
		EventBehaviorGroup ebGroup = getEventBehaviorGroup();
		if (sg.isSimulationRunning() == false) {
			getSceneGraph().initialize();
			initializeSimulation();
			
			sg.startSimulation();
			
			clearAllGlobalData();
			
			addEventBehaviors(ebGroup);
			ebGroup.initialize();
			
			ebGroup.start();
			
			runStartEvent();
		}
	}

	public void stopSimulation() {
		SceneGraph sg = getSceneGraph();
		EventBehaviorGroup ebGroup = getEventBehaviorGroup();
		if (sg.isSimulationRunning() == true) {
			sg.stopSimulation();
			ebGroup.stop();
			ebGroup.clear();
		}
		shutdownSimulation();
	}

	public boolean isSimulationRunning() {
		SceneGraph sg = getSceneGraph();
		return sg.isSimulationRunning();
	}
				
	//////////////////////////////////////////////////
	// clear
	//////////////////////////////////////////////////

	public void clearSceneGraph() {
		getSceneGraph().clear();
	}

	public void clearBehavior() {
		removeAllEvents();
		addSystemEvents();
	}

	public void clear() {
		clearSceneGraph();
		clearBehavior();
	}
	
	//////////////////////////////////////////////////
	// SceneGraph (Input)
	//////////////////////////////////////////////////
	
	public boolean addSceneGraph(SceneGraph sceneGraph) {
	
		if (sceneGraph.isLoadingOK() == false) {
			Debug.warning("Loading Error");
			return false;
		}

		// Node infomation		
		Node node = sceneGraph.getNodes();
		while (node != null) {
			Node nextNode = node.next();
			getSceneGraph().moveNode(node);
			node = nextNode;
		}

		// Route Infomation
		Route route = sceneGraph.getRoutes();
		while (route != null) {
			Route nextRoute = route.next();
			route.remove();
			getSceneGraph().addRoute(route.getEventOutNode().getName(), route.getEventOutField().getName(),
										route.getEventInNode().getName(), route.getEventInField().getName());
			route = nextRoute;
		}

		getSceneGraph().setBaseURL(sceneGraph.getBaseURL());

		getSceneGraph().initialize();

		getSceneGraph().saveViewpointStartPositionAndOrientation();
		
		return true;
	}

	public SceneGraph loadLW3DGeometryFile(URL url) {
		Loader			lw3dLoader	= new Lw3dLoader(Loader.LOAD_ALL);
		Scene			loaderScene	= null;
		VRML97Saver	vrmlSaver		= new VRML97Saver();
		
		try {
			loaderScene = lw3dLoader.load(url);
			Hashtable sceneHash = loaderScene.getNamedObjects();
			
			BranchGroup root = new BranchGroup();
			for (Enumeration e = sceneHash.elements(); e.hasMoreElements() ;) {
				TransformGroup obj = (TransformGroup)e.nextElement();
				System.out.println("Object = " + obj);
				if (obj.getParent() != null) {
					Group parentGroup = (Group)obj.getParent();
					int numChildren = parentGroup.numChildren(); 
					for (int n=0; n<numChildren; n++) {
						if (parentGroup.getChild(n) == obj) {
							parentGroup.removeChild(n);
							break;
						}
					}
				}
				root.addChild(obj);
			}
				
			vrmlSaver.setBranchGroup(root);
		}
		catch (Exception e) {
			return null;
		}
			
		return vrmlSaver;
	}

	public boolean addSceneGraph(URL url) {
		SceneGraph sceneGraph = null;
		
		int format = SceneGraph.getFileFormat(url.toString());
		if (format != SceneGraph.FILE_FORMAT_LWS && format != SceneGraph.FILE_FORMAT_LWO) {
			sceneGraph = new SceneGraph();
			sceneGraph.setOption(getSceneGraph().getOption());
			sceneGraph.load(url);
		}
		else {
			sceneGraph = loadLW3DGeometryFile(url);
			if (sceneGraph == null)
				return false;
		}
		
		return addSceneGraph(sceneGraph);
	}

	////////////////////////////////////////////////
	//	Behavior (Input)
	////////////////////////////////////////////////
	
	public void addEvent(CTBParser parser) {
		Debug.message("World.addEvent");
		
		Event event=parser.getEvents();
		while (event != null) {
			Debug.message("\tevent = " + event);
			Event nextEvent = event.next();
			if (hasEvent(event) == true) {
				Event currEvent = getEvent(event.getName(), event.getOptionString());
				Diagram dgm = event.getDiagrams();
				Debug.message("\t\tdgm = " + dgm);
				while (dgm != null) {
					Diagram nextDgm = dgm.next();
					currEvent.addDiagram(dgm);
					dgm = nextDgm;
				}
			}
			else
				addEvent(event);
			event = nextEvent;
		}
	}

	private int getCBFVersion(URL url) {
		int version = 200;
		try {
			InputStream inputStream = url.openStream();
			if (inputStream.read() == '#')
				version = 100;
			inputStream.close();
		}
		catch (IOException e) {
			Debug.warning("Behavior Loading Error (IOException) = " + url);
			System.out.println(e.getMessage());
		} 
		return version;
	}
	
	public boolean addBehavior(URL url) {
		int version = getCBFVersion(url);
		Debug.message("CBF Version = " + version);
		try {
			InputStream inputStream = url.openStream();
			CTBParser parser = null;
			if (version == 100)
				parser = new CTBParser100(inputStream);
			else
				parser = new CTBParser200(inputStream);
			parser.setWorld(this);
			parser.Input();
			inputStream.close();
			addEvent(parser);
		}
		catch (ParseException e) {
			Debug.warning("CBF Loading Error (ParseException) = " + url);
			System.out.println(e.getMessage());
			return false;
		}
		catch (TokenMgrError e) {
			Debug.warning("CBF Loading Error (TokenMgrError) = " + url);
			System.out.println(e.getMessage());
			return false;
		}
		catch (IOException e) {
			Debug.warning("CBF Loading Error (IOException) = " + url);
			System.out.println(e.getMessage());
			return false;
		} 
		catch (Exception e) {
			Debug.warning("CBF Loading Error (Exception) = " + url);
			e.printStackTrace();
			return false;
		} 
		
		return true;
	}

	//////////////////////////////////////////////////
	// Add
	//////////////////////////////////////////////////

	static public URL getBaseURL(URL url) {
		String urlString = url.toString();
		int index = urlString.lastIndexOf('/');
		if (0 <= index) { 
			try {
				URL baseURL = new URL(new String(urlString.getBytes(), 0, index + 1));
				return baseURL;
			}
			catch (MalformedURLException mue) {
				return null;
			}
		}
		return null;
	}
	
	public boolean add(URL sgURL) {
		Debug.message("World.add");
		
		SceneGraph sg = getSceneGraph();

		boolean retSceneGraphLoad = addSceneGraph(sgURL);
		
		if (retSceneGraphLoad == false)
			return false;
		
		boolean retBehaviorLoad = false;
		
		WorldInfoNode winfoNode = sg.findWorldInfoNode(BEHAVIOR_WORLDINFO_NODENAME);
		if (winfoNode != null) {
			if (0 < winfoNode.getNInfos()) {
				String behaviorFileName = winfoNode.getInfo(0);
				if (behaviorFileName != null) {
					Debug.message("\tbehavior file name = " + behaviorFileName);
					URL baseURL = getBaseURL(sgURL);
					if (baseURL != null) {
						try {
							URL behaviorURL = new URL(baseURL.toString() + behaviorFileName);
							retBehaviorLoad = addBehavior(behaviorURL);
						} catch (MalformedURLException mue) {}
					}
				}
			}
		}
		
		if (retBehaviorLoad == false)
			return false;
		
		return true;
	}

	//////////////////////////////////////////////////
	//	ModuleType
	//////////////////////////////////////////////////

	private LinkedList mModuleTypeList = new LinkedList();

	public ModuleType loadModuleType(String filename) {
		ModuleType moduleType = new ModuleType();
		if (moduleType.load(filename) == true) 
			addModuleType(moduleType);
		return moduleType;
	}
	
	public int loadModuleTypes() {
		int	 nModule = 0;
		String dirName = getModuleTypeDirectory();
		for (int n=0; n<ModuleType.FILENAME.length; n++) {
			String filename = dirName + ModuleType.FILENAME[n];
			ModuleType moduleType = loadModuleType(filename);
			if (moduleType != null) {
				Debug.message("==== ModuleType" + n + " (" + ModuleType.FILENAME[n] + ") ====");
				nModule++;
			}
		}
		return nModule;
	}

	public ModuleType loadUserModuleType(String filename) {
		ModuleType moduleType = new ModuleType();
		if (moduleType.load(filename) == true) { 
			if (hasSameModuleType(moduleType) == false) {
				addModuleType(moduleType);
				return moduleType;
			}
		}
		return null;
	}

	public int loadUserModuleTypes() {
		int	 nModule = 0;

		String dirName = getModuleTypeDirectory();
		
		File moduleDir = new File(dirName);
		String moduleTypeFileName[] = moduleDir.list();

		for (int n=0; n<moduleTypeFileName.length; n++) {
			String filename = dirName + moduleTypeFileName[n];
			File file = new File(filename);
			if (file.isFile() == true) {
				ModuleType moduleType = loadUserModuleType(filename);
				if (moduleType != null) {
					Debug.message("==== ModuleType" + n + " (" + ModuleType.FILENAME[n] + ") ====");
					nModule++;
				}
			}
		}

		return nModule;
	}

	public int setModuleTypes() {
		int	 nModuleTypes = ModuleTypeDefine.getNModuleTypes();

		for (int n=0; n<nModuleTypes; n++) {
			ModuleType moduleType = new ModuleType();
			String	className		= ModuleTypeDefine.getClassName(n);
			String	moduleName		= ModuleTypeDefine.getModuleName(n);
			String	attribute		= ModuleTypeDefine.getAttribute(n);
			String	eventIn[]		= ModuleTypeDefine.getEventInNodes(n);
			String	eventOut[]		= ModuleTypeDefine.getEventOutNodes(n);
			boolean	executionNode	= ModuleTypeDefine.getExecutionNode(n);
			moduleType.set(className, moduleName, attribute, eventIn, eventOut, executionNode);
			addModuleType(moduleType);
		}
		
		return nModuleTypes;
	}
	
	public int loadModuleTypesFromDirectory() {
		int	 nModule = 0;

		//ProgressDialog progressDialog = new ProgressDialog("Loading Module Types", 0, 0, 300, 100);
		
		String dirName = getModuleTypeDirectory();
		
		File moduleDir = new File(dirName);
		String moduleTypeFileName[] = moduleDir.list();

		//progressDialog.setMinimum(0);
		//progressDialog.setMaximum(moduleTypeFileName.length);
		//progressDialog.setValue(0);
		
		for (int n=0; n<moduleTypeFileName.length; n++) {
			//progressDialog.setValue(n);
			//progressDialog.setText("Loading " + moduleTypeFileName[n]);
			String filename = dirName + moduleTypeFileName[n];
			File file = new File(filename);
			if (file.isFile() == true) {
				ModuleType moduleType = loadModuleType(filename);
				Debug.message("==== ModuleType" + n + " (" + ModuleType.FILENAME[n] + ") ====");
				if (moduleType != null)
					nModule++;
			}
		}

		//progressDialog.dispose();
		
		return nModule;
	}

	public ModuleType getModuleTypes() {
		return (ModuleType)mModuleTypeList.getNodes();
	}

	public ModuleType getModuleTypes(String className) {
		for (ModuleType cmType=getModuleTypes(); cmType != null; cmType=cmType.next()) {
			if (className.equalsIgnoreCase(cmType.getClassName()) == true) 
				return cmType;
		}
		return null;
	}

	public ModuleType getModuleType(int n) {
		int nModule = 0;
		for (ModuleType cmType=getModuleTypes(); cmType != null; cmType=cmType.next()) {
			if (nModule == n)
				return cmType;
			nModule++;
		}
		return null;
	}
	
	public ModuleType getModuleType(String className, int n) {
		int nModule = 0;
		for (ModuleType cmType=getModuleTypes(className); cmType != null; cmType=cmType.next(className)) {
			if (nModule == n)
				return cmType;
			nModule++;
		}
		return null;
	}
	
	public ModuleType getModuleType(String className, String moduleName) {
		for (ModuleType cmType=getModuleTypes(); cmType != null; cmType=cmType.next()) {
			if (className.equalsIgnoreCase(cmType.getClassName()) && moduleName.equalsIgnoreCase(cmType.getName()) ) 
				return cmType;
		}
		return null;
	}

	public boolean hasSameModuleType(ModuleType modType) {
		String className	= modType.getClassName();
		String name			= modType.getName();
		if (getModuleType(className, name) != null)
			return true;
		return false;
	}
	
	public int getNModuleTypes() {
		int nModule = 0;
		for (ModuleType cmType=getModuleTypes(); cmType != null; cmType=cmType.next())
			nModule++;
		return nModule;
	}
	
	public int getNModuleTypes(String className) {
		int nModule = 0;
		for (ModuleType cmType=getModuleTypes(className); cmType != null; cmType=cmType.next(className))
			nModule++;
		return nModule;
	}

	public void addModuleType(ModuleType cmType) {
		cmType.setWorld(this);
		mModuleTypeList.addNode(cmType);
	}

	public String[] getModuleTypeClassNames() {
		Vector classNameVector = new Vector();
		for (ModuleType modType = getModuleTypes(); modType != null; modType = modType.next()) {
			String modClassName = modType.getClassName();
			boolean classNameEquals = false;
			for (int n=0; n<classNameVector.size(); n++) {
				String className = (String)classNameVector.elementAt(n);
				if (modClassName.equals(className) == true) {
					classNameEquals = true;
					continue;				
				}
			}
			if (classNameEquals == false)
				classNameVector.add(modClassName);
		}
		String className[] = new String[classNameVector.size()];
		for (int n=0; n<classNameVector.size(); n++) 
			className[n] = (String)classNameVector.elementAt(n);
		return className;
	}
	
	//////////////////////////////////////////////////
	// Event Types
	//////////////////////////////////////////////////

	private LinkedList mEventTypeList = new LinkedList();

	public boolean loadEventTypes() {
		for (int n=0; n<EventType.DATA.length; n++) {
			EventType event = new EventType(EventType.DATA[n][0], EventType.DATA[n][1], EventType.DATA[n][2], EventType.DATA[n][3]);
			addEventType(event);
		}
		return true;
	}
	
	public EventType getEventTypes() {
		return (EventType)mEventTypeList.getNodes();
	}

	public EventType getEventType(String name) {
		for (int n=0; n<getNEventTypes(); n++) {
			EventType eventType = getEventType(n);
			if (name.equals(eventType.getName()) == true)
				return eventType;
		}
		return null;
	}

	public EventType getSystemEventType(String name) {
		for (int n=0; n<getNEventTypes(); n++) {
			EventType eventType = getEventType(n);
			if (name.equals(eventType.getName()) == true) {
				if (eventType.getAttribute() == EventType.ATTRIBUTE_SYSTEM)
					return eventType;
			}
		}
		return null;
	}

	public EventType getUserEventType(String name) {
		for (int n=0; n<getNEventTypes(); n++) {
			EventType eventType = getEventType(n);
			if (name.equals(eventType.getName()) == true) {
				if (eventType.getAttribute() == EventType.ATTRIBUTE_USER)
					return eventType;
			}
		}
		return null;
	}

	public EventType getEventType(int eventType) {
		return (EventType)mEventTypeList.getNode(eventType);
	}

	public int getEventTypeNumber(EventType eventType) {
		for (int n=0; n<getNEventTypes(); n++) {
			if (eventType == getEventType(n))
				return n;
		}
		return -1;
	}

	public int getNEventTypes() {
		return mEventTypeList.getNNodes();
	}

	public void addEventType(EventType eventType) {
		mEventTypeList.addNode(eventType);
	}

	//////////////////////////////////////////////////
	// Event
	//////////////////////////////////////////////////

	private LinkedList mEventList = new LinkedList();

	public boolean addEvent(Event event) {
		if (getEvent(event.getName(), event.getOptionString()) != null)
			return false;
		event.setWorld(this);
		mEventList.addNode(event);
		return true;
	}
	
	public int getNEvents() {
		return mEventList.getNNodes();
	}
	
	public Event getEvents() {
		return (Event)mEventList.getNodes();
	}
	
	public Event getEvent(int n) {
		return (Event)mEventList.getNode(n);
	}

	public Event getSystemEvent(String name) {
		for (int n=0; n<getNEvents(); n++) {
			Event event = getEvent(n);
			if (event.getAttribute() != EventType.ATTRIBUTE_SYSTEM)
				continue;
			if (name.equalsIgnoreCase(event.getName()) == true) 
				return event;
		}
		return null;
	}

	public Event getUserEvent(String name, String optionString) {
		for (int n=0; n<getNEvents(); n++) {
			Event event = getEvent(n);
			if (event.getAttribute() != EventType.ATTRIBUTE_USER)
				continue;
			if (name.equalsIgnoreCase(event.getName()) == true) {
				if (optionString == null)
					return event;
				else if (optionString.equals(event.getOptionString()) == true)
					return event;
			}
		}
		return null;
	}

	public Event getUserEvent(String eventName) {
		if (eventName == null)
			return null;
		return getUserEvent("User", eventName);
	}

	public Event getEvent(String name, String optionString) {
		for (int n=0; n<getNEvents(); n++) {
			Event event = getEvent(n);
			if (name.equalsIgnoreCase(event.getName()) == true) {
				if (optionString == null)
					return event;
				else if (optionString.equals(event.getOptionString()) == true)
					return event;
			}
		}
		return null;
	}

	public void removeEvent(Event event) {
		event.remove();
	}

	public void removeEvent(int n) {
		removeEvent(getEvent(n));
	}

	public void removeAllEvents() {
		Event event = getEvents();
		while (event != null) {
			Event nextEvent = event.next();
			removeEvent(event);
			event = nextEvent;
		}
	}
	
	public boolean hasEvent(Event event) {
		Event sameEvent = getEvent(event.getName(), event.getOptionString());
		if (sameEvent != null)
			return true;
		return false;
	}
	
	private void addSystemEvents() {
		Debug.message("World.addSystemEvents");
		for (int n=0; n<getNEventTypes(); n++) {
			EventType eventType = getEventType(n);
			if (eventType.getAttribute() == EventType.ATTRIBUTE_SYSTEM) {
				Event event = new Event(eventType, null);
				boolean ret = addEvent(event);
				Debug.message("\tevent = " + eventType + " ( " + ret + " )");
			}
		}
	}


	//////////////////////////////////////////////////
	//	ReLoad
	/////////////////////////////////////////////
	
	private File mReLoadFile = null;

	public void setReLoadFile(File file) {
		mReLoadFile = file;
	}
	
	public File getReLoadFile() {
		return mReLoadFile;
	}

	//////////////////////////////////////////////////
	// Diagram
	//////////////////////////////////////////////////

	public void	addDiagram(Event event, Diagram dgm) {
		event.addDiagram(dgm);
	}
	
	public int getNDiagrams(Event event) {
		return event.getNDiagrams();
	}
	
	public Diagram getDiagrams(Event event) {
		return (Diagram)event.getDiagrams();
	}
	
	public Diagram getDiagram(Event event, int n) {
		return (Diagram)event.getDiagram(n);
	}

	public void removeDiagram(Event event, Diagram dgm) {
		event.removeDiagram(dgm);
	}

	public void removeDiagram(Event event, int n) {
		removeDiagram(event, event.getDiagram(n));
	}

	//////////////////////////////////////////////////
	// Event Types
	//////////////////////////////////////////////////

	private LinkedList mGlobalDataList = new LinkedList();

	public GlobalData getGlobalDatas() {
		return (GlobalData)mGlobalDataList.getNodes();
	}

	public GlobalData getGlobalData(String groupName, String dataName) {
		if (dataName == null)
			return null;
		
		int nGlobalDatas = getNGlobalDatas(); 			
		for (int n=0; n<nGlobalDatas; n++) {
			GlobalData gdata = getGlobalData(n);
			if (dataName.equals(gdata.getName()) == false)
				continue;
			if (groupName == null)
				return gdata;
			if (groupName.equals(gdata.getGroupName()) == true)
				return gdata;
		}
		
		return null;
	}

	public GlobalData getGlobalData(int gdata) {
		return (GlobalData)mGlobalDataList.getNode(gdata);
	}

	public int getNGlobalDatas() {
		return mGlobalDataList.getNNodes();
	}

	public void addGlobalData(GlobalData gdata) {
		mGlobalDataList.addNode(gdata);
	}

	public void setGlobalData(String groupName, String dataName, String value) {
		GlobalData gdata = getGlobalData(groupName, dataName);
		if (gdata == null) {
			gdata = new GlobalData(groupName, dataName);
			addGlobalData(gdata);
		} 
		gdata.setValue(value);
	}

	public void clearAllGlobalData() {
		mGlobalDataList.clear();
	}
				
	//////////////////////////////////////////////////
	// Node Operation
	//////////////////////////////////////////////////

	public void removeNode(Node node) {
		getSceneGraph().removeNode(node);
		Debug.message("World::removeNode = " + node);
	}

	////////////////////////////////////////////////
	//	ModuleTypeDefine
	////////////////////////////////////////////////
	
	private boolean doCompile = false;
	private String MODULETYPEDEFINE_CLASSNAME = "ModuleTypeDefine";
	
	public boolean createModuleTypeDefineClassFile() {
		String srcFileName[] = {MODULETYPEDEFINE_CLASSNAME + ".java"};
		Debug.message("Creating \"" + srcFileName[0] + "\" ....");
		try {
			File srcFile = new File(srcFileName[0]);
			FileOutputStream srcOutStream = new FileOutputStream(srcFile);
			PrintWriter srcWriter = new PrintWriter(srcOutStream);
		
			int nModuleTypes = getNModuleTypes();
			
			srcWriter.println("public class " + MODULETYPEDEFINE_CLASSNAME + " extends Object {");

			// getNModuleTypes  			
			srcWriter.println("    static public int getNModuleTypes() {");
			srcWriter.println("        return " + nModuleTypes + ";");
			srcWriter.println("    }");
			
			// Class Name
			srcWriter.println("    static public String getClassName(int n) {");
			srcWriter.println("        return className[n];");
			srcWriter.println("    }");
			srcWriter.println("    static public String className[] = {");
			for (int n=0; n<nModuleTypes; n++) {
				ModuleType modType = getModuleType(n);
			srcWriter.println("        \"" + modType.getClassName() + "\",");
			}
			srcWriter.println("    };");

			// Module Name
			srcWriter.println("    static public String getModuleName(int n) {");
			srcWriter.println("        return moduleName[n];");
			srcWriter.println("    }");
			srcWriter.println("    static public String moduleName[] = {");
			for (int n=0; n<nModuleTypes; n++) {
				ModuleType modType = getModuleType(n);
			srcWriter.println("        \"" + modType.getName() + "\",");
			}
			srcWriter.println("    };");
			
			// Attribute Name
			srcWriter.println("    static public String getAttribute(int n) {");
			srcWriter.println("        return attribute[n];");
			srcWriter.println("    }");
			srcWriter.println("    static public String attribute[] = {");
			for (int n=0; n<nModuleTypes; n++) {
				ModuleType modType = getModuleType(n);
			srcWriter.println("        \"" + modType.getAttributeString() + "\",");
			}
			srcWriter.println("    };");

			// EventIn
			srcWriter.println("    static public String[] getEventInNodes(int n) {");
			srcWriter.println("        return eventInNode[n];");
			srcWriter.println("    }");
			srcWriter.println("    static public String getEventInNode(int n, int nNode) {");
			srcWriter.println("        return eventInNode[n][nNode];");
			srcWriter.println("    }");
			srcWriter.println("    static public String eventInNode[][] = {");
			for (int n=0; n<nModuleTypes; n++) {
				ModuleType modType = getModuleType(n);
			srcWriter.print  ("        {");
				for (int i=0; i<Module.NODE_MAXNUM; i++) {
					String nodeName =modType.getInNodeName(i);
			srcWriter.print("\"" + ((nodeName != null) ? nodeName : "NULL") + "\",");
				}
			srcWriter.println("},");
			}
			srcWriter.println("    };");
			
			// EventOut
			srcWriter.println("    static public String[] getEventOutNodes(int n) {");
			srcWriter.println("        return eventOutNode[n];");
			srcWriter.println("    }");
			srcWriter.println("    static public String getEventOutNode(int n, int nNode) {");
			srcWriter.println("        return eventOutNode[n][nNode];");
			srcWriter.println("    }");
			srcWriter.println("    static public String eventOutNode[][] = {");
			for (int n=0; n<nModuleTypes; n++) {
				ModuleType modType = getModuleType(n);
			srcWriter.print  ("        {");
				for (int i=0; i<Module.NODE_MAXNUM; i++) {
					String nodeName =modType.getOutNodeName(i);
			srcWriter.print("\"" + ((nodeName != null) ? nodeName : "NULL") + "\",");
				}
			srcWriter.println("},");
			}
			srcWriter.println("    };");
			
			// Module Name
			srcWriter.println("    static public boolean getExecutionNode(int n) {");
			srcWriter.println("        return executionNode[n];");
			srcWriter.println("    }");
			srcWriter.println("    static public boolean executionNode[] = {");
			for (int n=0; n<nModuleTypes; n++) {
				ModuleType modType = getModuleType(n);
			srcWriter.println("        " + modType.hasExecutionNode() + ",");
			}
			srcWriter.println("    };");
			
			srcWriter.println("}");
		
			srcWriter.close();
			srcOutStream.close();
			
			if (doCompile == false)
				return false;
				
			Debug.message("Compiling \"" + srcFileName[0] + "\" ....");
			
			Main javac = new Main(System.out, "javac");
			boolean ret = javac.compile(srcFileName);
			if (ret == true)
				Debug.message("Compiling is OK");
			else
				Debug.warning("Couldn't Compile \"" + srcFileName[0] + "\"");
			return ret;			
		}
		catch (IOException ioe) {
			Debug.warning("Couldn't create \"" + srcFileName[0] + "\"");
			return false;
		}
	}

	////////////////////////////////////////////////
	//	ModuleClassCreator
	////////////////////////////////////////////////
	
	private String MODULECLASSCREATOR_CLASSNAME = "ModuleClassCreator";
	
	public boolean createModuleClassCreatorClassFile() {
		String srcFileName[] = {MODULECLASSCREATOR_CLASSNAME + ".java"};
		Debug.message("Creating \"" + srcFileName[0] + "\" ....");
		try {
			File srcFile = new File(srcFileName[0]);
			FileOutputStream srcOutStream = new FileOutputStream(srcFile);
			PrintWriter srcWriter = new PrintWriter(srcOutStream);
		
			srcWriter.println("public class " + MODULECLASSCREATOR_CLASSNAME + " extends Object {");
			srcWriter.println("    static public Module createModule(ModuleType moduleType) throws NoClassDefFoundError{");
			srcWriter.println("        String className = moduleType.getClassName();");
			srcWriter.println("        String name = moduleType.getName();");
		
			for (ModuleType moduleType = getModuleTypes(); moduleType != null; moduleType=moduleType.next()) {
			String className = moduleType.getClassName();
			String name = moduleType.getName();
			srcWriter.println("        if (className.equals(\"" + className + "\") && name.equals(\"" + name + "\"))");
			srcWriter.println("            return new " + className + name + "();");
			}
			srcWriter.println("        Debug.warning(\"\\tCouldn't create module \" + moduleType);");
			srcWriter.println("        return null;");
		
			srcWriter.println("    }");
			srcWriter.println("}");
		
			srcWriter.close();
			srcOutStream.close();
			
			if (doCompile == false)
				return false;
				
			Debug.message("Compiling \"" + srcFileName[0] + "\" ....");
			
			Main javac = new Main(System.out, "javac");
			boolean ret = javac.compile(srcFileName);
			if (ret == true)
				Debug.message("Compiling is OK");
			else
				Debug.warning("Couldn't Compile \"" + srcFileName[0] + "\"");
			return ret;			
		}
		catch (IOException ioe) {
			Debug.warning("Couldn't create \"" + srcFileName[0] + "\"");
			return false;
		}
	}
}
