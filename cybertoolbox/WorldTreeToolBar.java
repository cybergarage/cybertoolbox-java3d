/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	WorldTreeToolBar.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;

import cv97.*;
import cv97.node.*;
import cv97.route.*;

public class WorldTreeToolBar extends ToolBar {

	private WorldTreeFrame mWorldTreeFrame = null;
	private JButton	mToolbarButton[] = new JButton[11];
	
	private WorldTreeFrame getWorldTreeFrame() {
		return mWorldTreeFrame;
	}
	
	private World getWorld() {
		return mWorldTreeFrame.getWorld();
	}

	private CtbIDE getCtbIDE() {
		return mWorldTreeFrame.getCtbIDE();
	}

	private SceneGraph getSceneGraph() {
		return getWorld().getSceneGraph();
	}
		
	public WorldTreeToolBar (WorldTreeFrame worldTreeFrame) {
		mWorldTreeFrame = worldTreeFrame;
	
		String dir = getWorld().getImageToolbarWorldTreeDirectory();
		
		mToolbarButton[0] = addToolBarButton("New World", dir + "simulation_new.gif", new NewSceneGraphAction());
		mToolbarButton[1] = addToolBarButton("Load World", dir + "simulation_load.gif", new LoadSceneGraphAction());
		mToolbarButton[2] = addToolBarButton("Save World", dir + "simulation_save.gif", new SaveSceneGraphAction());
		addSeparator();
		
		mToolbarButton[3] = addToolBarButton("Go Simulation", dir + "simulation_go.gif", new StartSimulationAction());
		mToolbarButton[4] = addToolBarButton("Stop Simulation", dir + "simulation_stop.gif", new StopSimulationAction());
		
		addSeparator();
		
		mToolbarButton[5] = addToolBarButton("New Node", dir + "node_new.gif", new NodeNewAction());
		mToolbarButton[6] = addToolBarButton("New Route", dir + "route_new.gif", new RouteNewAction());
		
		addSeparator();
		
		mToolbarButton[7] = addToolBarButton("New Event", dir + "event_new.gif", new EventNewAction());
		mToolbarButton[8] = addToolBarButton("New Diagram", dir + "diagram_new.gif", new DiagramNewAction());
		mToolbarButton[9] = addToolBarButton("New Module", dir + "module_new.gif", new ModuleNewAction());
		
		addSeparator();
		
		mToolbarButton[10] = addToolBarButton("About", dir + "about.gif", new AboutAction());
	}
		
	private class NewSceneGraphAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getWorld().stopSimulation();
			
			int result = Message.showConfirmDialog(getWorldTreeFrame(), "Are you sure you want to clear all scenegraph objects ?");
			if(result == Message.YES_OPTION) {
				getWorld().stopAppletThread();				
				getWorldTreeFrame().clear();
				getWorld().clear();
				getWorldTreeFrame().reset();
				getWorld().startAppletThread();				
			}
		}
    }
    
	private class LoadSceneGraphAction extends AbstractAction {
		public void actionPerformed(ActionEvent event) {
			getWorld().stopSimulation();
			
			String userDir = System.getProperty("user.dir");
			Debug.message("currentDirectoryPath = " + userDir);
			
			JFileChooser filechooser = new JFileChooser(new File(userDir));
			FileFilter vrml97fileFilter = new VRML97FileFilter();
			filechooser.addChoosableFileFilter(vrml97fileFilter);
			filechooser.addChoosableFileFilter(new A3DSFileFilter());
			filechooser.addChoosableFileFilter(new OBJFileFilter());
			filechooser.addChoosableFileFilter(new NFFFileFilter());
			filechooser.addChoosableFileFilter(new LW3DFileFilter());
			filechooser.addChoosableFileFilter(new DXFFileFilter());
			filechooser.addChoosableFileFilter(new STLFileFilter());
			filechooser.addChoosableFileFilter(new X3DFileFilter());
			filechooser.setFileFilter(vrml97fileFilter);
			
			if(filechooser.showOpenDialog(getWorldTreeFrame()) == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				if (file != null) {
					if (file.isDirectory() == false) {
						getWorld().stopAppletThread();				
						try {
							getWorld().add(file.toURL());
						} catch (MalformedURLException mue) {}
						getWorldTreeFrame().reset();
						getWorld().startAppletThread();				
					}
				}
			}
		}
	}

    private class SaveSceneGraphAction extends AbstractAction {
        public void actionPerformed(ActionEvent event) {
			getWorld().stopSimulation();
			
			String userDir = System.getProperty("user.dir");
			Debug.message("currentDirectoryPath = " + userDir);
			
			JFileChooser filechooser = new JFileChooser(new File(userDir));
			FileFilter vrml97fileFilter = new VRML97FileFilter();
			filechooser.addChoosableFileFilter(vrml97fileFilter);
			filechooser.setFileFilter(vrml97fileFilter);
			
			if(filechooser.showSaveDialog(getWorldTreeFrame()) == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				if (file != null) {
					if (file.isDirectory() == false) {
						getWorld().stopAppletThread();				
						getCtbIDE().save(file);
						//getWorld().printBehavior();
						getWorld().startAppletThread();				
					}
				}
			}
		}
	}

	private void enableButtons() {
		mToolbarButton[0].setEnabled(true);
		mToolbarButton[1].setEnabled(true);
		mToolbarButton[2].setEnabled(true);
		mToolbarButton[3].setEnabled(true);
		mToolbarButton[4].setEnabled(true);
		mToolbarButton[5].setEnabled(true);
		mToolbarButton[6].setEnabled(true);
		mToolbarButton[7].setEnabled(true);
		mToolbarButton[8].setEnabled(true);
		mToolbarButton[9].setEnabled(true);
		mToolbarButton[10].setEnabled(true);
	}

	private void disableButtons() {
		mToolbarButton[0].setEnabled(false);
		mToolbarButton[1].setEnabled(false);
		mToolbarButton[2].setEnabled(false);
		mToolbarButton[3].setEnabled(false);
		//mToolbarButton[4].setEnabled(false);
		mToolbarButton[5].setEnabled(false);
		mToolbarButton[6].setEnabled(false);
		mToolbarButton[7].setEnabled(false);
		mToolbarButton[8].setEnabled(false);
		mToolbarButton[9].setEnabled(false);
		//mToolbarButton[10].setEnabled(false);
	}
	
	private class StartSimulationAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getWorld().startSimulation();
			disableButtons();
		}
	}

	private class StopSimulationAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getWorld().stopSimulation();
			enableButtons();
		}
	}

    private class NodeNewAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
			getWorld().stopAppletThread();				
			getWorld().stopSimulation();
			
			WorldTreeNode treeNode = getWorldTreeFrame().getSelectedTreeNode();
			Node parentNode = getWorldTreeFrame().findSceneGraphNode(treeNode);
			if (parentNode == null) {
				Message.beep();
				return;
			}

			DialogWorldNewNode dialog = new DialogWorldNewNode(getWorldTreeFrame(), parentNode);
			if (dialog.doModal() == Dialog.OK_OPTION) { 
				Node childNode = dialog.getSelectedNode();
				if (childNode != null)
					addNewNode(parentNode, childNode);
			}
			
			getWorld().startAppletThread();				
		}
		
		private boolean addNewNode(Node parentNode, Node childNode) {
			if (parentNode == getSceneGraph().getRootNode())
				getSceneGraph().addNode(childNode);
			else
				parentNode.addChildNode(childNode);
			getWorldTreeFrame().addSceneGraphNodeTreeNode(childNode, parentNode);
			getSceneGraph().initialize();
			return true;
		}
	}

	private class RouteNewAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getWorld().stopAppletThread();				
			getWorld().stopSimulation();
			
			DialogRoute dialog = new DialogRoute(getWorldTreeFrame(), getWorld());
			if (dialog.doModal() == Dialog.OK_OPTION) { 
				Route route = dialog.getRoute();
				if (route != null) {
					if (getSceneGraph().addRoute(route) != null)
						getWorldTreeFrame().addRouteTreeNode(route);
					else
						Message.beep();
				}
			}
			
			getWorld().startAppletThread();				
		}
	}

	private class EventNewAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getWorld().stopAppletThread();				
			getWorld().stopSimulation();
			
			DialogWorldNewEvent dialog = new DialogWorldNewEvent(getWorldTreeFrame(), getWorld());
			if (dialog.doModal() == Dialog.OK_OPTION) {
				if (addNewEvent(dialog) == false)
					Message.beep();
			}
			
			getWorld().startAppletThread();				
		}
		
		private boolean addNewEvent(DialogWorldNewEvent dialog) {
			Debug.message("EventNewAction::addNewEvent");
			int					eventType	= dialog.getSelectedEventType();
			DialogEventPanel	eventPanel	= dialog.getEventPanel(eventType);

			Debug.message("\teventType  = " + eventType);			
			Debug.message("\teventPanel = " + eventPanel);			
			
			EventType	newEventType	= null;
			String		newEventOption	= null;
			
			switch (eventType) {
			case EventType.USER_CLOCK: // CLOCK
				newEventType = getWorld().getEventType(EventType.USER_CLOCK);
				break;
			case EventType.USER_TIMER: // TIMER
				newEventType = getWorld().getEventType(EventType.USER_TIMER);
				break;
			case EventType.USER_PICKUP: // PICKUP
				newEventType = getWorld().getEventType(EventType.USER_PICKUP);
				break;
			case EventType.USER_KEYBOARD: // Keyboard
				newEventType = getWorld().getEventType(EventType.USER_KEYBOARD);
				break;
			case EventType.USER_AREA: // Area
				newEventType = getWorld().getEventType(EventType.USER_AREA);
				break;
			case EventType.USER_COLLISION: // Collision
				newEventType = getWorld().getEventType(EventType.USER_COLLISION);
				break;
			case EventType.USER_USER: // User
				newEventType = getWorld().getEventType(EventType.USER_USER);
				break;
			}
			
			newEventOption = eventPanel.getOptionString();
			
			if (newEventType == null || newEventOption == null) 
				return false;

			Event	newEvent = new Event(newEventType, newEventOption);
			if (getWorld().addEvent(newEvent) == false)
				return false;
			getCtbIDE().getWorldTreeFrame().addEventTreeNode(newEvent);
			
			return true;			
		}
	}

	private class DiagramNewAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			getWorld().stopAppletThread();				
			getWorld().stopSimulation();
			
			DialogWorldNewDiagram dialog = new DialogWorldNewDiagram(getWorldTreeFrame(), getWorld());
			if (dialog.doModal() == Dialog.OK_OPTION) {
				int eventIndex = dialog.getEventIndex();
				String dgmName = dialog.getDiagramName();
				if (addNewDiagram(eventIndex, dgmName) == false) 
					Message.showWarningDialog(getWorldTreeFrame(), "The same diagram is already added !!");
			}
			
			getWorld().startAppletThread();				
		}

		private boolean addNewDiagram(int eventIndex, String dgmName) {
			
			Event event	= getWorld().getEvent(eventIndex);
			
			if (dgmName == null || dgmName.length() <= 0) {
				int nDiagram = 0;
				dgmName = null;
				while (dgmName == null) {
					dgmName = "diagram" + nDiagram;
					if (event.getDiagram(dgmName) != null)
						dgmName = null;
					nDiagram++;
				}
			}
			
			Diagram dgm	= new Diagram(event, dgmName);
			
			event.addDiagram(dgm);
	
			getWorldTreeFrame().addDiagramTreeNode(event, dgm);
			getCtbIDE().openDiagramFrame(dgm);
							
			return true;
		}
	}

	private class ModuleNewAction extends AbstractAction {
   	public void actionPerformed(ActionEvent e) {
			DialogModuleConfig dialog = new DialogModuleConfig(getWorldTreeFrame(), getCtbIDE());
			if (dialog.doModal() == Dialog.OK_OPTION) 
				getCtbIDE().getModuleFrame().repaint();
		}
	}

	private class AboutAction extends AbstractAction {
   	public void actionPerformed(ActionEvent e) {
			DialogWorldAbout dialog = new DialogWorldAbout(getWorldTreeFrame());
			dialog.doModal();
		}
	}
}
