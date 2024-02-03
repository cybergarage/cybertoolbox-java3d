/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	CtbViewerToolbar.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;

import cv97.*;
import cv97.node.*;
import cv97.util.*;
import cv97.j3d.*;

public class CtbViewerToolbar extends ToolBar implements Constants {

	private	World			mWorld		= null;
	private	Frame			mFrame		= null;
	private	CtbApplet	mApplet		= null;
	
	private void setFrame(Frame frame) {
		mFrame = frame;
	}
	
	private Frame getFrame() {
		return mFrame;
	}

	private void setWorld(World world) {
		mWorld = world;
	}
	
	private World getWorld() {
		return mWorld;
	}

	public void setApplet(CtbApplet applet) {
		mApplet = applet;
	}

	public CtbApplet getApplet() {
		return mApplet;
	}

	private SceneGraph getSceneGraph() {
		return getWorld().getSceneGraph();
	}
		
	public CtbViewerToolbar(Frame frame, World world, boolean hasFileIcon) {
		setFrame(frame);
		setWorld(world);
		
		String dir = world.getImageToolbarPerspectiveviewDirectory();
		
		if (hasFileIcon == true) {
			addToolBarButton("Load", dir + "simulation_load.gif", new LoadSceneGraphAction());
			addToolBarButton("ReLoad", dir + "simulation_reload.gif", new ReLoadSceneGraphAction());
			addSeparator();
		}
		
		addToolBarButton("Pick", dir + "operation_pick.gif", new OperationPickAction());
		addToolBarButton("Walk", dir + "operation_walk.gif", new OperationWalkAction());
		addToolBarButton("Pan", dir + "operation_pan.gif", new OperationPanAction());
		addToolBarButton("Rot", dir + "operation_rot.gif", new OperationRotAction());
		addSeparator();
		addToolBarButton("Reset Viewpoint", dir + "reset_viewpoint.gif", new ResetViewpointAction());
		addToolBarButton("Headlight", dir + "headlight.gif", new HeadlightAction());
		addSeparator();
		addToolBarButton("Wireframe", dir + "rendering_wire.gif", new RenderingWireAction());
		addToolBarButton("Shading", dir + "rendering_shade.gif", new RenderingShadeAction());
		addSeparator();
		addToolBarButton("Config", dir + "config.gif", new ConfigAction());
	}

	public boolean load(File file) {
		getWorld().stopSimulation();
		
		boolean isLoadingOK = false;
		if (file != null) {
			if (file.isDirectory() == false) {
				getWorld().stopAppletThread();				
				getWorld().clear();
				try {
					isLoadingOK = getWorld().add(file.toURL());
				} catch (MalformedURLException mue) {}
				getWorld().startAppletThread();				
			}
		}

		getWorld().startSimulation();
		
		Debug.message("isLoadingOK = " + isLoadingOK);
		
		return isLoadingOK;
	}
	
	private class LoadSceneGraphAction extends AbstractAction {
		public void actionPerformed(ActionEvent event) {
			String userDir = System.getProperty("user.dir");
			Debug.message("currentDirectoryPath = " + userDir);
			
			JFileChooser filechooser = new JFileChooser(new File(userDir));
			FileFilter vrml97fileFilter = new VRML97FileFilter();
			filechooser.addChoosableFileFilter(vrml97fileFilter);
			filechooser.setFileFilter(vrml97fileFilter);
			filechooser.addChoosableFileFilter(new A3DSFileFilter());
			filechooser.addChoosableFileFilter(new OBJFileFilter());
			filechooser.addChoosableFileFilter(new NFFFileFilter());
			filechooser.addChoosableFileFilter(new LW3DFileFilter());
			
			if(filechooser.showOpenDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				if (load(file) == true)
					getWorld().setReLoadFile(file);
			}
		}
	}

	private class ReLoadSceneGraphAction extends AbstractAction {
		public void actionPerformed(ActionEvent event) {
			load(getWorld().getReLoadFile());
		}
	}

	private class OperationPickAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getApplet().setOperationMode(CtbApplet.OPERATION_MODE_PICK);
			Debug.message("Operation mode = PICK");
		}
	}

	private class OperationWalkAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getApplet().setOperationMode(CtbApplet.OPERATION_MODE_WALK);
			Debug.message("Operation mode = WALK");
		}
	}

	private class OperationPanAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getApplet().setOperationMode(CtbApplet.OPERATION_MODE_PAN);
			Debug.message("Operation mode = PAN");
		}
	}

	private class OperationRotAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getApplet().setOperationMode(CtbApplet.OPERATION_MODE_ROT);
			Debug.message("Operation mode = ROT");
		}
	}
	
	private class ResetViewpointAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			JPopupMenu.setDefaultLightWeightPopupEnabled(false); 
			PopupMenu popupMenu = new PopupMenu();
			Component comp = (Component)e.getSource();
			Dimension size = comp.getSize();
			popupMenu.show(comp, size.width/2, size.height/2);
			getFrame().repaint();
		}
		
		public void repaintFrame() {
			getFrame().repaint();
		}
		
		public class PopupMenu extends JPopupMenu {
			private String menuString[] = {
				"Start View",
				"XY Plane View",
				"XZ Plane View",
				"YZ Plane View",
			};
		
			public PopupMenu() {
				for (int n=0; n<menuString.length; n++) {
					JMenuItem menuItem = new JMenuItem(menuString[n]);
					menuItem.addActionListener(new PopupMenuAction());
					add(menuItem);
				}
			}
		
			private class PopupMenuAction extends AbstractAction {
				SceneGraph sg = getSceneGraph();
  			 	public void actionPerformed(ActionEvent e) {
  	 				Debug.message("PopupMenuAction.actionPerformed = " + e.getActionCommand());	
					for (int n=0; n<menuString.length; n++) {
						if (menuString[n].equals(e.getActionCommand()) == true) {
							switch (n) {
							case 0: sg.resetViewpoint(); break;
							case 1: sg.resetViewpointAlongZAxis(); break;
							case 2: sg.resetViewpointAlongYAxis(); break;
							case 3: sg.resetViewpointAlongXAxis(); break;
							}
							repaintFrame();
							break;
						}
					}
				}
			}
		}
	}

	private class HeadlightAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getSceneGraph().setHeadlightState(!getSceneGraph().isHeadlightOn());
			Debug.message("CtbViewerToolbar::HeadlightAction : headlight = " + getSceneGraph().isHeadlightOn());
		}
	}

	private class RenderingWireAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getSceneGraph().setRenderingMode(SceneGraph.RENDERINGMODE_LINE);
			Debug.message("Rendring mode = wire frame");
		}
	}

	private class RenderingShadeAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getSceneGraph().setRenderingMode(SceneGraph.RENDERINGMODE_FILL);
			Debug.message("Rendring mode = shading");
		}
	}

	private class ConfigAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			DialogPerspectiveView	pvDialog	= new DialogPerspectiveView(getFrame(), getWorld());
			
			if (pvDialog.doModal() == Dialog.OK_OPTION) {
				getSceneGraph().setRenderingMode(pvDialog.getRenderingStyle());
				getSceneGraph().setHeadlightState(pvDialog.isHeadlightOn());
				float speed = pvDialog.getNavigationSpeed();
				if (0 < speed)
					getSceneGraph().setNavigationSpeed(speed);
				else
					Message.beep();
			}			
		}
	}
}

