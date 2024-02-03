/*************F*****************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	Diagram.java
*
******************************************************************/

import java.io.*;
import java.awt.*;
import java.net.*;

import cv97.SceneGraph;

public class Diagram extends LinkedListNode {

	public static final int LINE_STYLE_STRAIGHT	= 0x00;
	public static final int LINE_STYLE_ZIGZAG		= 0x01;

	public static final int EXTENTS_MIN = 0;
	public static final int EXTENTS_MAX = 1;
	public static final int X = 0;
	public static final int Y = 1;
	
	//////////////////////////////////////////////////
	// Constructor		
	//////////////////////////////////////////////////

	public Diagram() {
		Debug.message("Diagram.<init>");
		setHeaderFlag(false);
		setEvent(null);
		setName(null);
		setPosition(0, 0);
		setWidth(320);
		setHeight(240);
		setData(null);
		setParentModule(null);
		setComponent(null);
	}
	
	public Diagram(Event event) {
		this();
		setEvent(event);
		
		if (event.hasSystemtModule()) { 
			ModuleType systemModuleType = event.getSystemModuleType();
			Debug.message("\tSystemModuleType = " + systemModuleType);
			addModule(systemModuleType);
		}
		
		updateExtents();
	}
	
	public Diagram(Event event, String name) {
		this(event);
		setName(name);
	}

	////////////////////////////////////////
	//	World / SceneGraph
	////////////////////////////////////////

	public World getWorld() {
		Event event = getEvent();
		if (event != null)
			return event.getWorld();
		Module module = getParentModule();
		if (module != null)
			return module.getWorld();
		return null;
	}
	
	public SceneGraph getSceneGraph() {
		return getWorld().getSceneGraph();
	}

	//////////////////////////////////////////////////
	// Event		
	//////////////////////////////////////////////////
	
	private Event	mEvent = null;
	
	public void setEvent(Event event) {
		mEvent = event;
	}
	
	public Event getEvent() {
		return mEvent;
	}

	//////////////////////////////////////////////////
	// Module		
	//////////////////////////////////////////////////
	
	private Module	mParentModule = null;
	
	public void setParentModule(Module module) {
		mParentModule = module;
	}
	
	public Module getParentModule() {
		return mParentModule;
	}

	public boolean isInsideDiagram() {
		return (mParentModule != null) ? true : false;
	}
	
	//////////////////////////////////////////////////
	// Name
	//////////////////////////////////////////////////

	private String mName = null;
	
	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	//////////////////////////////////////////////////
	// Position
	//////////////////////////////////////////////////

	private int mXPosition = 0;
	private int mYPosition = 0;
	
	public void setPosition(int x, int y) {
		setXPosition(x);
		setYPosition(y);
	}

	public void setXPosition(int x) {
		mXPosition = x;
	}

	public void setYPosition(int y) {
		mYPosition = y;
	}

	public int getXPosition() {
		return mXPosition;
	}

	public int getYPosition() {
		return mYPosition;
	}

	//////////////////////////////////////////////////
	// Width/Height
	//////////////////////////////////////////////////

	private int mWidth = 0;
	private int mHeight = 0;
	
	public void setWidth(int width) {
		mWidth = width;
	}

	public void setHeight(int height) {
		mHeight = height;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	//////////////////////////////////////////////////
	// Frame		
	//////////////////////////////////////////////////
	
	private Component	mComponent = null;
	
	public void setComponent(Component frame) {
		mComponent = frame;
	}
	
	public Component getComponent() {
		return mComponent;
	}

	public void repaintComponent() {
		if (mComponent != null)
			mComponent.repaint();
	}

	//////////////////////////////////////////////////
	// Module
	//////////////////////////////////////////////////

	private LinkedList mModuleList = new LinkedList();

	public void	addModule(Module module) {
		// Set a module number
		int moduleNumber = 0;
		for (Module mod = getModules(); mod != null; mod = mod.next()) {
			int number = mod.getNumber();
			if (moduleNumber < number)
				moduleNumber = number;
		}
		module.setNumber(moduleNumber+1);
		
		// Add the module into the module list
		mModuleList.addNode(module);
		module.setDiagram(this);
		
		// set Diagram
		module.setDiagram(this);
		
		// Update extents
		updateExtents();
	}

	public Module addModule(ModuleType moduleType) {
		Debug.message("Module.addModule");
		Debug.message("\tmoduleType = \n" + moduleType);
		Module module = moduleType.createModule();
		Debug.message("\tmodule     = " + module);
		if (module == null)
			return null;
		addModule(module);
		return module;
	}
	
	public int getNModules() {
		return mModuleList.getNNodes();
	}

	public int getNModules(Rectangle rect) {
		int nModuleNode = 0;
		for (Module module = getModules(); module != null; module=module.next()) {
				int mx = module.getXPosition();
				int my = module.getYPosition();
				int msize = module.getSize();
				if (rect.contains(mx, my) == true && rect.contains(mx+msize, my+msize) == true) 
					nModuleNode++;
		}
		return nModuleNode;
	}
	
	public Module getModules() {
		return (Module)mModuleList.getNodes();
	}
	
	public Module getModule(int n) {
		return (Module)mModuleList.getNode(n);
	}

	public Module getModule(String name) {
		int nModules = getNModules();
		for (int n=0; n<nModules; n++) {
			Module module = getModule(n);
			String moduleName = module.getName();
			if (moduleName != null) {
				if (moduleName.equalsIgnoreCase(name) == true)
					return module;
			}
		}
		return null;
	}

	public Module getSystemModule(String typeName) {
		int nModules = getNModules();
		for (int n=0; n<nModules; n++) {
			Module module = getModule(n);
			if (module.getAttribute() != Module.ATTRIBUTE_SYSTEM)
				continue;
			if (typeName == null)
				return module;
			if (typeName.equalsIgnoreCase(module.getTypeName()) == true)
				return module;
		}
		return null;
	}

	public Module getSystemModule() {
		return getSystemModule(null);
	}

	public Module getSystemInputModule() {
		return getSystemModule(Module.INPUTMODULE_TYPENAME);
	}

	public Module getSystemOutputModule() {
		return getSystemModule(Module.OUTPUTMODULE_TYPENAME);
	}
	
	public void removeModuleDataflows(Module module) {
		Dataflow dataflow = getDataflows(); 
		while (dataflow != null) {
			Dataflow nextDataflow = dataflow.next();
			if (dataflow.getOutModule() == module || dataflow.getInModule() == module)
				removeDataflow(dataflow);
			dataflow = nextDataflow;
		}
	}
		
	public void removeModule(Module module) {
		removeModuleDataflows(module);
		module.setDiagram(null);
		module.remove();
	}

	public void removeModule(int n) {
		removeModule(getModule(n));
	}

	public void removeAllModules() {
		Module module = getModules();
		while (module != null) {
			Module nextModule = module.next();
			removeModule(module);
			module = nextModule;
		}
	}

	public ModuleSelectedData getModule(int x, int y) {
		Module	selectedModule	= null;
		int		selectedPart	= Module.OUTSIDE;
		
		for (Module mod = getModules(); mod != null; mod = mod.next()) {
			int selectingPart = mod.isInside(x, y);
			if (selectingPart != Module.OUTSIDE) {
				selectedPart = selectingPart;
				selectedModule = mod;
				break;
			}
		}

		return  new ModuleSelectedData(selectedModule, selectedPart); 
	}

	public boolean hasModule(Module module) {
		if (module == null)
			return false;
		for (Module mod = getModules(); mod != null; mod = mod.next()) {
			if (module == mod)
				return true;
		}
		return false;
	}

	//////////////////////////////////////////////////
	// Dataflow
	//////////////////////////////////////////////////

	private LinkedList mDataflowList = new LinkedList();

	public void	addDataflow(Dataflow dataflow) {
		if (dataflow.getOutModule() == null || dataflow.getOutNode() == null || dataflow.getInModule() == null || dataflow.getInNode() == null)
			return;
		removeDataflow(dataflow.getInModule(), dataflow.getInNode());
		mDataflowList.addNode(dataflow);
	}

	public Dataflow addDataflow(Module outModule, ModuleNode outNode, Module inModule, ModuleNode inNode) {
		Dataflow dataflow = new Dataflow(outModule, outNode, inModule, inNode);
		addDataflow(dataflow);
		return dataflow;
	}

	public Dataflow addDataflow(String outModuleName, String outNodeName, String inModuleName, String inNodeName) {
		Module		outModule	= getModule(outModuleName);
		Module		inModule		= getModule(inModuleName);
		if (outModule == null || inModule == null)
			return null;
		
		ModuleNode	outNode		= outModule.getOutNode(outNodeName);
		ModuleNode	inNode		= inModule.getInNode(inNodeName);
		if (inNode == null) {
			if (inNodeName.equalsIgnoreCase(ModuleNode.EXECUTION_NODE_NAME) == true)
				inNode = inModule.getExecutionNode();
		}
		
		if (outNode == null || inNode == null)
			return null;
			
		return addDataflow(outModule, outNode, inModule, inNode);
	}

	public Dataflow addDataflow(String outModuleName, int outNodeNumber, String inModuleName, int inNodeNumber) {
		Module		outModule	= getModule(outModuleName);
		Module		inModule		= getModule(inModuleName);
		if (outModule == null || inModule == null)
			return null;

		ModuleNode	outNode = outModule.getOutNode(outNodeNumber);
		
		ModuleNode	inNode;
		if (0 <= inNodeNumber)
			inNode = inModule.getInNode(inNodeNumber);
		else
			inNode = inModule.getExecutionNode();
			
		if (outNode == null || inNode == null)
			return null;
			
		return addDataflow(outModule, outNode, inModule, inNode);
	}
	
	public int getNDataflows() {
		return mDataflowList.getNNodes();
	}

	public int getNModuleDataflows(Module module) {
		int nDataflows = getNDataflows();
		int nModuleDataflows = 0;
		for (int n=0; n<nDataflows; n++) {
			Dataflow dataflow = getDataflow(n);
			if (dataflow.getOutModule() == module || dataflow.getInModule() == module) 
				nModuleDataflows++;
		}
		return nModuleDataflows;
	}

	public int getNModuleInputDataflows(Module module) {
		int nDataflows = getNDataflows();
		int nModuleDataflows = 0;
		for (int n=0; n<nDataflows; n++) {
			Dataflow dataflow = getDataflow(n);
			if (dataflow.getInModule() == module) 
				nModuleDataflows++;
		}
		return nModuleDataflows;
	}

	public int getNModuleOutputDataflows(Module module) {
		int nDataflows = getNDataflows();
		int nModuleDataflows = 0;
		for (int n=0; n<nDataflows; n++) {
			Dataflow dataflow = getDataflow(n);
			if (dataflow.getOutModule() == module) 
				nModuleDataflows++;
		}
		return nModuleDataflows;
	}

	public boolean hasInputDataflow(Module inModule) {
		int nDataflows = getNDataflows();
		int nModuleDataflows = 0;
		for (int n=0; n<nDataflows; n++) {
			Dataflow dataflow = getDataflow(n);
			if (dataflow.getInModule() == inModule) 
				return true;
		}
		return false;
	}

	public boolean hasInputDataflow(Module inModule, ModuleNode inModuleNode) {
		int nDataflows = getNDataflows();
		int nModuleDataflows = 0;
		for (int n=0; n<nDataflows; n++) {
			Dataflow dataflow = getDataflow(n);
			if (dataflow.getInModule() == inModule && dataflow.getInNode() == inModuleNode) 
				return true;
		}
		return false;
	}
	
	public Dataflow getDataflows() {
		return (Dataflow)mDataflowList.getNodes();
	}
	
	public Dataflow getDataflow(int n) {
		return (Dataflow)mDataflowList.getNode(n);
	}
	
	public void removeDataflow(Dataflow module) {
		module.remove();
	}

	public void	removeDataflow(Module outModule, ModuleNode outNode, Module inModule, ModuleNode inNode) {
		Dataflow dataflow = getDataflows();
		while (dataflow != null) {
			Dataflow nextDataflow = dataflow.next();
			if (dataflow.getOutModule() == outModule && dataflow.getOutNode() == outNode && dataflow.getInModule() == inModule && dataflow.getInNode() == inNode)
				removeDataflow(dataflow);
			dataflow = nextDataflow;
		}
	}

	public void	removeDataflow(Module inModule, ModuleNode inNode) {
		Dataflow dataflow = getDataflows();
		while (dataflow != null) {
			Dataflow nextDataflow = dataflow.next();
			if (dataflow.getInModule() == inModule && dataflow.getInNode() == inNode)
				removeDataflow(dataflow);
			dataflow = nextDataflow;
		}
	}

	public void removeDataflow(int n) {
		removeDataflow(getDataflow(n));
	}

	public void removeAllDataflows() {
		Dataflow dataflow = getDataflows();
		while (dataflow != null) {
			Dataflow nextDataflow = dataflow.next();
			removeDataflow(dataflow);
			dataflow = nextDataflow;
		}
	}

	//////////////////////////////////////////////////
	// Execute Behavior
	//////////////////////////////////////////////////

	public void run(Module sysModule, String sysModuleParams[]) {
		initializeModules();
		avtivateTopModules();
		if (sysModule != null) {
			for (int n=0; n<sysModuleParams.length; n++)
				sysModule.sendOutNodeValue(n, sysModuleParams[n]);
		}
		repaintComponent();
	}
	
	public void run(String sysModuleParams[]) {
		run(getSystemModule(), sysModuleParams);
	}
	
	public void run() {
		run(null, null);
	}
		
	public void run(String sysModuleParam) {
		String sysModuleParams[] = new String[1];
		sysModuleParams[0] = sysModuleParam;
		run(sysModuleParams); 
	}

	public void initializeModules() {
		int nModules = getNModules();
		for (int n=0; n<nModules; n++) 
			getModule(n).clearEventData();
	}
	
	public void avtivateTopModules() {
		int nModules = getNModules();
		for (int n=0; n<nModules; n++) {
			Module module = getModule(n);
			if (module.hasInNode() == false || module.getInputDataflowCount() == 0)
				module.runProcessDataMethod();
		}
	}
	
	public void sendModuleOutNodeValue(Module outModule, ModuleNode outNode) {
		int nDataflows = getNDataflows();
		for (int n=0; n<nDataflows; n++) {
			Dataflow dataflow = getDataflow(n);
			if (dataflow.getOutModule() == outModule && dataflow.getOutNode() == outNode) {
				Module			inModule	= dataflow.getInModule();
				ModuleNode	inNode	= dataflow.getInNode();
				
				inNode.setValue(outNode);
				
				inModule.incrementRecievedDataCount();
				
				if (inModule.getInputDataflowCount() <= inModule.getRecievedDataCount())
					inModule.runProcessDataMethod();
			} 
		}
	}
	
	//////////////////////////////////////////////////
	// Extents
	//////////////////////////////////////////////////

	private int	mExtents[][] = new int[2][2];
	
	public void getExtents(int extents[][]) {
		extents[EXTENTS_MIN][X] = mExtents[EXTENTS_MIN][X];
		extents[EXTENTS_MIN][Y] = mExtents[EXTENTS_MIN][Y];
		extents[EXTENTS_MAX][X] = mExtents[EXTENTS_MAX][X];
		extents[EXTENTS_MAX][Y] = mExtents[EXTENTS_MAX][Y];
	}

	public void setExtents(int extents[][]) {
		mExtents[EXTENTS_MIN][X] = extents[EXTENTS_MIN][X];
		mExtents[EXTENTS_MIN][Y] = extents[EXTENTS_MIN][Y];
		mExtents[EXTENTS_MAX][X] = extents[EXTENTS_MAX][X];
		mExtents[EXTENTS_MAX][Y] = extents[EXTENTS_MAX][Y];
	}

	public void updateExtents() {
		int extents[][] = new int[2][2];

		if (getNModules() == 0) {
			extents[EXTENTS_MIN][X] = 0;
			extents[EXTENTS_MIN][Y] = 0;
			extents[EXTENTS_MAX][X] = 0;
			extents[EXTENTS_MAX][Y] = 0;
		}
		else {
			Module firstModule = getModule(0);
			int x = firstModule.getXPosition();
			int y = firstModule.getYPosition();
			int size = firstModule.getSize();
			extents[EXTENTS_MIN][X] = x;
			extents[EXTENTS_MIN][Y] = y;
			extents[EXTENTS_MAX][X] = x + size;
			extents[EXTENTS_MAX][Y] = y + size;
			for (int n=1; n<getNModules(); n++) {
				Module module = getModule(n);
				x = module.getXPosition();
				y = module.getYPosition();
				if (extents[EXTENTS_MIN][X] > x)
					extents[EXTENTS_MIN][X] = x;
				if (extents[EXTENTS_MIN][Y] > y)
					extents[EXTENTS_MIN][Y] = y;
				if (extents[EXTENTS_MAX][X] < (x + size))
					extents[EXTENTS_MAX][X] = x + size;
				if (extents[EXTENTS_MAX][Y] < (y + size))
					extents[EXTENTS_MAX][Y] = y + size;
			}
		}

		extents[EXTENTS_MAX][X] += Module.SIZE;
		extents[EXTENTS_MAX][Y] += Module.SIZE;

		setExtents(extents);
	}

	////////////////////////////////////////////////
	//	next node list
	////////////////////////////////////////////////

	public Diagram next () {
		return (Diagram)getNextNode();
	}

	//////////////////////////////////////////////////
	// Data
	//////////////////////////////////////////////////

	private Object mData = null;
	
	public void setData(Object data) {
		mData = data;
	}

	public Object getData() {
		return mData;
	}

	//////////////////////////////////////////////////
	// initialize / shutdown
	//////////////////////////////////////////////////
	
	public void initialize() {
		for (Module module = getModules(); module != null; module = module.next()) {
			module.initializeEventData();
			module.initialize();
		}
	}

	public void shutdown() {
		for (Module module = getModules(); module != null; module = module.next()) 
			module.shutdown();
	}

	//////////////////////////////////////////////////
	// print / save
	//////////////////////////////////////////////////

	private String getIndentString(int indent) {
		StringBuffer indentString = new StringBuffer();
		for (int n=0; n<indent; n++)
			indentString.append('\t');
		return indentString.toString();
	}

	public void print(PrintWriter pw, int indent){
		String indentString = getIndentString(indent);
		
		String	dgmName	= getName();
		int		dgmx		= getXPosition();
		int		dgmy		= getYPosition();
		int		dgmWidth		= getWidth();
		int		dgmHeight	= getHeight();
		
		pw.println(indentString + "<DIAGRAM NAME=\"" + dgmName + "\" XPOS=\"" + dgmx + "\" YPOS=\"" + dgmy + "\" WIDTH=\"" + dgmWidth + "\" HEIGHT=\"" + dgmHeight + "\">");
				
		for (Module module = getModules(); module != null; module = module.next()) {
			String	modClassName	= module.getClassName();
			String	modTypeName		= module.getTypeName();
			String	modName			= module.getName();
			int		modx				= module.getXPosition();
			int		mody				= module.getYPosition();
			String	value				= module.getValue();
				
			pw.print(indentString + "\t<MODULE CLASS=\"" + modClassName + "\" TYPE=\"" + modTypeName + "\" NAME=\"" + modName + "\" XPOS=\"" + modx + "\" YPOS=\"" + mody + "\"");
			if (value != null)
				pw.print(" VALUE=\"" + value + "\"");
				
			if (module.isInsideDiagram() != true) {
				pw.println("/>");
				continue;
			}
			
			// Inside Diagram
			pw.println(">");
			print(pw, indent+2);
			pw.println(indentString + "\t</MODULE>");
		}

		for (Dataflow dataflow = getDataflows(); dataflow != null; dataflow = dataflow.next()) {
			String outModuleName = dataflow.getOutModule().getName();
			ModuleNode outNode = dataflow.getOutNode();
			String outNodeName = Integer.toString(outNode.getNumber());
					
			String inModuleName = dataflow.getInModule().getName();
			ModuleNode inNode = dataflow.getInNode();
			String inNodeName;
			if (inNode.isExecutionNode() == false)
				inNodeName = Integer.toString(inNode.getNumber());
			else
				inNodeName = "E";
			
			pw.println(indentString + "\t<ROUTE OUT=\"" + outModuleName + "." + outNodeName + "\" IN=\"" + inModuleName + "." + inNodeName + "\"/>");
		}
		
		pw.println(indentString + "</DIAGRAM>");
	}

	public void print(PrintWriter pw) throws IOException {
		pw.println("<CDF VERSION=\"200\">");
		print(pw, 1);
		pw.println("</CDF>");
		pw.flush();
	}

	public void print(OutputStream os) throws IOException {
		PrintWriter pw = new PrintWriter(os);
		print(pw);
		pw.close();
	}
	
	public void print() {
		try {
			print(System.out);
		}
		catch (IOException ioe) {}
	}

	public boolean save(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			print(fos);
			fos.close();
		}
		catch (IOException ioe) {
			Debug.warning("Couldn't open the file (" + file + ")");
			return false;
		}
		return true;
	}

	//////////////////////////////////////////////////
	// load
	//////////////////////////////////////////////////

	public void setDiagram(Diagram dgm) {
		int nModule = dgm.getNModules();
		for (int n=0; n<nModule; n++) {
			Module		dgmModule	= dgm.getModule(n);
			String		className	= dgmModule.getClassName();
			String		typeName		= dgmModule.getTypeName();
			ModuleType	moduleType	= getWorld().getModuleType(className, typeName);
			Module		newModule	= moduleType.createModule();
			newModule.setName(dgmModule.getName());
			newModule.setXPosition(dgmModule.getXPosition());
			newModule.setYPosition(dgmModule.getYPosition());
			newModule.setValue(dgmModule.getValue());
			addModule(newModule);
		}

		int nDataflow = dgm.getNDataflows();
		for (int n=0; n<nDataflow; n++) {
			Dataflow dataflow					= dgm.getDataflow(n);
			String 	outModuleName			= dataflow.getOutModule().getName();
			int	 	outModuleNodeNumber	= dataflow.getOutNode().getNumber();
			String 	inModuleName			= dataflow.getInModule().getName();
			int	 	inModuleNodeNumber	= dataflow.getInNode().getNumber();
			addDataflow(outModuleName, outModuleNodeNumber, inModuleName, inModuleNodeNumber);
		}

	}
	
	public void setDiagram(CTBParser parser) {
		Diagram dgm=parser.getDiagrams();
		if (dgm != null) {
			removeAllDataflows();
			removeAllModules();
			setDiagram(dgm);
		}
	}

	public boolean load(URL url) {
		try {
			InputStream inputStream = url.openStream();
			CTBParser parser = new CTBParser200(inputStream);
			parser.setWorld(getWorld());
			parser.Input();
			inputStream.close();
			Debug.message("CDF Version = " + parser.getVersionString());
			setDiagram(parser);
		}
		catch (ParseException e) {
			Debug.warning("CDF Loading Error (ParseException) = " + url);
			System.out.println(e.getMessage());
			return false;
		}
		catch (TokenMgrError e) {
			Debug.warning("CDF Loading Error (TokenMgrError) = " + url);
			System.out.println(e.getMessage());
			return false;
		}
		catch (IOException e) {
			Debug.warning("CDF Loading Error (IOException) = " + url);
			System.out.println(e.getMessage());
			return false;
		} 
		catch (Exception e) {
			Debug.warning("CDF Loading Error (Exception) = " + url);
			e.printStackTrace();
			return false;
		} 

		return true;
	}
}
