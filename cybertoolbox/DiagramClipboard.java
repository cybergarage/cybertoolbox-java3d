/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File: DiagramClipboard.java
*
******************************************************************/

import cv97.util.*;

public class DiagramClipboard extends Object {

	private LinkedList	mModuleList		= new LinkedList();
	private LinkedList	mDataflowList	= new LinkedList();
	private World			mWorld				= null;
	
	///////////////////////////////////////////////
	// Constructor
	///////////////////////////////////////////////
	
	public DiagramClipboard(World world) {
		setWorld(world);
	}

	public DiagramClipboard(World world, DiagramClipboard clipboard) {
		setWorld(world);
		
		for (Module module=clipboard.getModules(); module != null; module=module.next()) 
			addModule(module);

		for (Dataflow dataflow=clipboard.getDataflows(); dataflow != null; dataflow=dataflow.next()) 
			addDataflow(dataflow);
	}

	///////////////////////////////////////////////
	//	World
	///////////////////////////////////////////////

	public void setWorld(World world) {
		mWorld = world;
	}
	
	public World getWorld() {
		return mWorld;
	}
	
	///////////////////////////////////////////////
	//	ModuleNode
	///////////////////////////////////////////////
	
	private class ModuleCopy extends Module {
		public void initialize() {}
		public void shutdown() {}
		public void processData(ModuleNode inNode[], ModuleNode exeNode) {}
	}

	public void addModule(Module orgModule) {
		Module			copyModule = new ModuleCopy();
		
		String			className		= orgModule.getClassName();
		String			typeName		= orgModule.getTypeName();
		ModuleType	moduleType	= getWorld().getModuleType(className, typeName);
		
		copyModule.setModuleType(moduleType);

		copyModule.setName(orgModule.getName());
		copyModule.setXPosition(orgModule.getXPosition());
		copyModule.setYPosition(orgModule.getYPosition());
		copyModule.setValue(orgModule.getValue());
		
		mModuleList.addNode(copyModule);		
	}
	
	public Module getModules() {
		return (Module)mModuleList.getNodes();		
	}

	public Module getModule(int n) {
		return (Module)mModuleList.getNode(n);		
	}

	public int getNModules() {
		return mModuleList.getNNodes();
	}

	public void clearModuleNodeList() {
		mModuleList.deleteNodes();		
	}

	///////////////////////////////////////////////
	//	Dataflow
	///////////////////////////////////////////////

	public void addDataflow(Dataflow dataflow) {
		int nModule = getNModules();

		Module			outModule	= dataflow.getOutModule();
		ModuleNode	outNode		= null;
		
		for (int n=0; n<nModule; n++) {
			Module module = getModule(n);
			String moduleName = module.getName();
			if (moduleName != null) {
				if (moduleName.equals(outModule.getName()) == true) {
					outModule = module;
					outNode = outModule.getOutNode(dataflow.getOutNode().getNumber());
					break;
				}
			}
		}

		Module			inModule	= dataflow.getInModule();
		ModuleNode	inNode		= null;
		for (int n=0; n<nModule; n++) {
			Module module = getModule(n);
			String moduleName = module.getName();
			if (moduleName != null) {
				if (moduleName.equals(inModule.getName()) == true) {
					inModule = module;
					inNode = inModule.getInNode(dataflow.getInNode().getNumber());
					break;
				}
			}
		}
	
		Dataflow copyDataflow = new Dataflow(outModule, outNode, inModule, inNode);

		mDataflowList.addNode(copyDataflow);		
	}

	public Dataflow getDataflows() {
		return (Dataflow)mDataflowList.getNodes();		
	}

	public Dataflow getDataflow(int n) {
		return (Dataflow)mDataflowList.getNode(n);		
	}

	public int getNDataflows() {
		return mDataflowList.getNNodes();
	}

	public void clearDataflowList() {
		mDataflowList.deleteNodes();		
	}

	///////////////////////////////////////////////
	//	clear
	///////////////////////////////////////////////

	public void clear() {
		clearModuleNodeList();
		clearDataflowList();
	}
}
