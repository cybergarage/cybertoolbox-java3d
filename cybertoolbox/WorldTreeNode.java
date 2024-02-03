/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	WorldTreeObject.java
*
******************************************************************/

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.tree.*;

public class WorldTreeNode extends DefaultMutableTreeNode
{
    public WorldTreeNode(WorldTreeData o) {
		super(o);
    }

    public boolean isLeaf() {
		return false;
    }

    public int getChildCount() {
		return super.getChildCount();
    }

	public void setObject(Object obj) {
		((WorldTreeData)getUserObject()).setObject(obj);
	}

	public Object getObject() {
		return ((WorldTreeData)getUserObject()).getObject();
	}
	
	public void setText(String text) {
		((WorldTreeData)getUserObject()).setText(text);
	}

	public String getText() {
		return ((WorldTreeData)getUserObject()).getText();
	}

	public String toString() {
		return ((WorldTreeData)getUserObject()).toString();
	}
}
