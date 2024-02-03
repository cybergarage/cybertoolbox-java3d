/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DiagramModuleData.java
*
******************************************************************/

import cv97.*;
import cv97.node.*;

public class ModuleSelectedData extends Object {

	private Module mModule;
	private int		mParts;
	
	public ModuleSelectedData() {
		setParts(Module.OUTSIDE);
		setModule(null);
	}
	
	public ModuleSelectedData(Module module, int parts) {
		setModule(module);
		setParts(parts);
	}
	
	public void setParts(int parts) {
		mParts = parts;
	}
	
	public int getParts() {
		return mParts;
	}

	public void setModule(Module module) {
		mModule = module;
	}
	
	public Module getModule() {
		return mModule;
	}
	
}
