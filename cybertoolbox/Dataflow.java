/******************************************************************
*
*	CyberToolbox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File : Dataflow.java
*
******************************************************************/

public class Dataflow extends LinkedListNode {
	Module		mOutModule	= null;
	Module		mInModule	= null;
	ModuleNode	mOutNode		= null;
	ModuleNode	mInNode		= null;

	public Dataflow(Module outModule, ModuleNode outNode, Module inModule, ModuleNode inNode) {
		setHeaderFlag(false);
		setOutModule(outModule);
		setInModule(inModule);
		setOutNode(outNode);
		setInNode(inNode);
	}

	public void			setOutModule(Module module)		{ mOutModule = module; }
	public void			setInModule(Module module)		{ mInModule = module; }
	public Module		getOutModule()							{ return mOutModule; }
	public Module		getInModule()							{ return mInModule; }
	public void			setOutNode(ModuleNode node)		{ mOutNode = node; }
	public ModuleNode	getOutNode()								{ return mOutNode; }
	public void			setInNode(ModuleNode node)		{ mInNode = node; }
	public ModuleNode	getInNode()								{ return mInNode; }

	////////////////////////////////////////////////
	//	next node list
	////////////////////////////////////////////////

	public Dataflow next () {
		return (Dataflow)getNextNode();
	}

	////////////////////////////////////////////////
	//	update
	////////////////////////////////////////////////

	public void update() {
	}

	////////////////////////////////////////////////
	//	toString
	////////////////////////////////////////////////

	public String toString() {
		return "DATAFLOW " + getOutModule().getName() + "." + getOutNode().getName() + " TO " + getInModule().getName() + "." + getInNode().getName();
	}
};
