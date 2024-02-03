/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	ModulePanel.java
*
******************************************************************/

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import cv97.*;
import cv97.node.*;
import cv97.util.*;

public class ModuleFrame extends JFrame implements MouseListener, MouseMotionListener {
	
	private CtbIDE			mCtbIDE;
	private JTabbedPane		mTabbedPane;
	
	private boolean 			useScrollPane = true;
	
	private JScrollPane		modTabScrPane[];
	private ModuleTabPanel	modTabPane[];
	
	public CtbIDE getCtbIDE() {
		return mCtbIDE;
	}
	
	public World getWorld() {
		return mCtbIDE;
	}
	
	public ModuleFrame(CtbIDE ctbIDE) {
		mCtbIDE = ctbIDE;

		getContentPane().setLayout(new BorderLayout());
		setSize(32*(24)+10, 100 + 16);
		//setLocation(screenSize.width/2 - WIDTH/2, screenSize.height/2 - HEIGHT/2);
//		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		JPanel panel = new JPanel();
		panel.setName("Module");
		panel.setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.CENTER);
		
		mTabbedPane = new JTabbedPane();
		panel.add(mTabbedPane, BorderLayout.CENTER);

		createModuleTabs(ctbIDE, mTabbedPane);

		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);

		setResizable(false);

		show();
	
		validate();
		repaint();
        //panel.requestDefaultFocus();

		createTabListener();
	}

	public void processWindowEvent(WindowEvent e) {
	}
	
	private void createModuleTabs(World world, JTabbedPane tabbedPane) {
		String className[] = {
				"String", 
				"Numeric",
				"Math", 
				"Filter",
				"Boolean",
				"Geometry",
				"Transform", 
				"Light", 
				"Material", 
				"Viewpoint",
				"Interpolator", 
				"Sensor", 
				"Misc",
				"User"};
		
		int classCount = className.length;
		
		modTabScrPane	= new JScrollPane[classCount];
		modTabPane		= new ModuleTabPanel[classCount];
		
		for (int n=0; n<classCount; n++) {
			ModuleTabPanel panel = new ModuleTabPanel(world, className[n]);
			if (useScrollPane == true) {
				JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				sp.getViewport().add(panel);
				tabbedPane.addTab(className[n], null, sp);
				modTabScrPane[n] = sp;
			}
			else 
				tabbedPane.addTab(className[n], null, panel);
			panel.addMouseListener(this);
			panel.addMouseMotionListener(this);
			modTabPane[n] = panel;
		}
	}
	
    void createTabListener() {
	// add listener to know when we've been shown
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
	/*
                JTabbedPane tab = (JTabbedPane) e.getSource();
                int index = tab.getSelectedIndex();
                Component currentPage = tab.getComponentAt(index);
		RepaintManager repaintManager = 
                    RepaintManager.currentManager(instance);

		if(!repaintManager.isDoubleBufferingEnabled()) {
		  repaintManager.setDoubleBufferingEnabled(true);
		}

		if(previousPage == debugGraphicsPanel) {
		    ((DebugGraphicsPanel)debugGraphicsPanel).resetAll();
		}
*/
            }
        };
        mTabbedPane.addChangeListener(changeListener);
    }

	////////////////////////////////////////////////
	//	ModuleTabPanel / JScrollPane
	////////////////////////////////////////////////
	
	private ModuleTabPanel getSelectedModuleTabPane() {
		Component comp = mTabbedPane.getSelectedComponent();
		if (useScrollPane == true) {
			for (int n=0; n<modTabScrPane.length; n++) {
				if (modTabScrPane[n] == comp)
					return modTabPane[n];
			}
		}
		else {
			for (int n=0; n<modTabPane.length; n++) {
				if (modTabPane[n] == comp)
					return modTabPane[n];
			}
		}
		return null;
	}

	private JScrollPane getSelectedModuleTabScrollPane() {
		Component comp = mTabbedPane.getSelectedComponent();
		for (int n=0; n<modTabScrPane.length; n++) {
			if (modTabScrPane[n] == comp)
				return modTabScrPane[n];
		}
		return null;
	}
	
	////////////////////////////////////////////////
	//	mouse
	////////////////////////////////////////////////
	
	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		Debug.message("mousePressed");
		
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		int mx = e.getX();
		int my = e.getY();
		
		Debug.message("mpos = " + mx + ", " + my);
		
		ModuleTabPanel modPanelTab = getSelectedModuleTabPane();
		ModuleType selectedModuleType = modPanelTab.getModuleType(mx, my);

		if (selectedModuleType != null) {
			getWorld().setOperationMode(World.OPETATION_MODE_MODULETYPE_DRAGGED);
			modPanelTab.setSelectedModuleType(selectedModuleType);
		}
		else {
			getWorld().setOperationMode(World.OPETATION_MODE_NONE);
			modPanelTab.setSelectedModuleType(null);
		}
		
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
		Debug.message("mouseClicked");

		int mouseButton = e.getModifiers();
		
		if (mouseButton == e.BUTTON3_MASK) {

			ModuleTabPanel modPanelTab = getSelectedModuleTabPane();
			ModuleType selectedModuleType = modPanelTab.getSelectedModuleType();
		
			if (selectedModuleType == null)
				return;
	
			if (selectedModuleType.getAttribute() != Module.ATTRIBUTE_USER) {
				Message.beep();
				return;
			}
		
			PopupMenu popupMenu = new PopupMenu(new PopupMenuAction());
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (getWorld().getOperationMode() == World.OPETATION_MODE_MODULETYPE_DRAGGED) {
			Point screenPos = getLocationOnScreen();
			int smx = e.getX() + screenPos.x;
			int smy = e.getY() + screenPos.y;
			if (useScrollPane == true) {
				JScrollPane	scrPane	= getSelectedModuleTabScrollPane();
				JViewport	viewport	= scrPane.getViewport();
				Point			viewPos	= viewport.getViewPosition();
				Debug.message("offset = " + viewPos.x + ", " + viewPos.y);
				smx -= viewPos.x;
				smy -= viewPos.y;
			}
			DiagramFrame dgmFrame = getCtbIDE().getDiagramFrame(smx, smy);
			ModuleTabPanel modPanelTab = getSelectedModuleTabPane();
			if (dgmFrame != null) {
				if (dgmFrame.getGridSnap() == true) {
					smx -= smx % dgmFrame.getGridSize();
					smy -= smy % dgmFrame.getGridSize();
				}
				ModuleType modType = modPanelTab.getSelectedModuleType();
				dgmFrame.addModuleOnScreen(modType, smx, smy);
				dgmFrame.getMainComponent().resetComponentSize();
			}
			getWorld().setOperationMode(World.OPETATION_MODE_NONE);
		}
		
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void mouseDragged(MouseEvent e) {
		if (getWorld().getOperationMode() == World.OPETATION_MODE_MODULETYPE_DRAGGED) {
			Point screenPos = getLocationOnScreen();
			int smx = e.getX() + screenPos.x;
			int smy = e.getY() + screenPos.y;
			DiagramFrame dgmFrame = getCtbIDE().getDiagramFrame(smx, smy);
			if (dgmFrame != null) 
				dgmFrame.toFront();
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	////////////////////////////////////////////////
	// Action
	////////////////////////////////////////////////
	
	private boolean editModuleType() {
		ModuleTabPanel modPanelTab = getSelectedModuleTabPane();
		ModuleType selectedModuleType = modPanelTab.getSelectedModuleType();

		if (selectedModuleType == null)
			return false;

		DialogModuleConfig dialog = new DialogModuleConfig(this, getCtbIDE(), selectedModuleType);
		if (dialog.doModal() == Dialog.OK_OPTION) 
			getCtbIDE().getModuleFrame().repaint();
		
		repaint();
		
		return true;
	}
	
	private boolean removeModuleType() {
		ModuleTabPanel modPanelTab = getSelectedModuleTabPane();
		ModuleType selectedModuleType = modPanelTab.getSelectedModuleType();

		if (selectedModuleType == null)
			return false;

		String className	= 	selectedModuleType.getClassName();
		String name			= 	selectedModuleType.getName();
				
		int result = Message.showConfirmDialog(this, "Are you sure you want to remove this module type (" + className + "::" + name + ") ?");

		if(result == Message.YES_OPTION) {
			CtbIDE ctbIDE = getCtbIDE();
			ctbIDE.removeModuleType(selectedModuleType);
		}
		
		repaint();
		
		return true;
	}
	
	////////////////////////////////////////////////
	// Popup Menu
	////////////////////////////////////////////////
	
	private String mPopupMenuString[] = {
		"Edit",
		"Remove"
	};
		
	public class PopupMenu extends JPopupMenu {
		public PopupMenu(AbstractAction action) {
			for (int n=0; n<mPopupMenuString.length; n++) {
				if (0 < mPopupMenuString[n].length()) {
					JMenuItem menuItem = new JMenuItem(mPopupMenuString[n]);
					menuItem.addActionListener(action);
					add(menuItem);
				}
				else
					addSeparator();
			}
		}
	}

	private class PopupMenuAction extends AbstractAction {
		public PopupMenuAction() {
		}
		
		public void actionPerformed(ActionEvent e) {
			Debug.message("PopupMenuAction.actionPerformed = " + e.getActionCommand());	
			if (mPopupMenuString[0].equals(e.getActionCommand())) 
				editModuleType();
			if (mPopupMenuString[1].equals(e.getActionCommand())) 
				removeModuleType();
		}
	}
}
