/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleNode.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import cv97.*;
import cv97.node.*;

public abstract class DialogModuleNode extends Dialog {

	private DialogModuleNodeComboBox	mComboBox	= null;
	private World							mWorld		= null;
			
	public void setWorld(World world) {
		mWorld = world;
	}
						
	public World getWorld() {
		return mWorld;
	}

	public void setComboBox(DialogModuleNodeComboBox combo) {
		mComboBox = combo;
	}
						
	public DialogModuleNodeComboBox getComboBox() {
		return mComboBox;
	}

	public DialogModuleNode(Frame parentComponent, String title, JComponent components[]) {
		super(parentComponent, title, components);
		setWorld(null);
	}

    public DialogModuleNode(Frame parentComponent, String title) {
		super(parentComponent, title);
		setWorld(null);
	}

    public DialogModuleNode(Frame parentComponent) {
		super(parentComponent);
		setWorld(null);
	}
		
	public void setNode(String nodeName) {
		getComboBox().setNode(nodeName);
	}
	
	public void setNode(Node node) {
		getComboBox().setNode(node);
	}
	
	abstract public Node getNode();
	
	public DialogModuleNodeComboBox createNodeComboBox(String borderName, Node firstNode) {
		DialogModuleNodeComboBox combo = new DialogModuleNodeComboBox(borderName, firstNode);
		setComboBox(combo);
		return combo;
	}

}
		
