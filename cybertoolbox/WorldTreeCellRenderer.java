/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	WorldTreeCellRenderer.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

import cv97.*;
import cv97.node.*;
import cv97.util.Debug;

public class WorldTreeCellRenderer extends JLabel implements TreeCellRenderer {

	static private		Font				mDefaultFont;
	static private		Color				mSelectedBackgroundColor = Color.yellow;

	static {
		try {
			mDefaultFont = new Font("SansSerif", Font.BOLD, 12);
		} catch (Exception e) {}
	}

	static public ImageIcon getImageIcon(WorldTreeData treeData) {
	
		if (treeData == null)
			return null;
		
		Object obj = treeData.getObject();
		
		if (obj instanceof Node) { 
			Node node = (Node)obj;
			return NodeImage.getImageIcon(node);
		}

		if (obj instanceof Event) { 
			return NodeImage.getEventImageIcon();
		}

		if (obj instanceof Diagram) { 
			return NodeImage.getDiagramImageIcon();
		}

		if (obj instanceof String) {
			String string = (String)obj;
			if (string.equalsIgnoreCase("World") == true) 
				return NodeImage.getWorldImageIcon();
		}
		
		return NodeImage.getCommonImageIcon();
	}
	
	public WorldTreeCellRenderer() {
	}
	
	private	boolean	selected;

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		
		String          stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
		WorldTreeData	treeData = (WorldTreeData)((DefaultMutableTreeNode)value).getUserObject();

		setFont(mDefaultFont);

		setText(stringValue + "    ");
		setToolTipText(stringValue);

		setIcon(getImageIcon(treeData));

		if(hasFocus)
		    setForeground(Color.blue);
		else
			setForeground(Color.black);

		this.selected = selected;
	
		return this;
    }

    public void paint(Graphics g) {
		Color            bColor;
		Icon             currentI = getIcon();

		if(selected)
			bColor = mSelectedBackgroundColor;
		else if(getParent() != null)
		    bColor = getParent().getBackground();
		else
			bColor = getBackground();

		g.setColor(bColor);
		
		if(currentI != null && getText() != null) {
			int offset = (currentI.getIconWidth() + getIconTextGap());
			g.fillRect(getX() + offset, getY(), getWidth() - 1 - offset, getHeight() - 1);
		}
		else
			g.fillRect(getX(), getY(), getWidth()-1, getHeight()-1);
		
		super.paint(g);
    }

}
