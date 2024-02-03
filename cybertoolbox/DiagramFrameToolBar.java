/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DiagramFrameToolBar.java
*
******************************************************************/

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;

public class DiagramFrameToolBar extends ToolBar {

	private DiagramFrame mDiagramFrame = null;
	
	public DiagramFrameToolBar (DiagramFrame dgmFrame) {
		setDiagramFrame(dgmFrame);
		
		World		world		= getWorld();
		String	dir		= world.getImageToolbarDiagramDirectory();
		Diagram	dgm		= getDiagram();
		
		
		if (dgm.isInsideDiagram() == true) {
			addToolBarButton("Load", dir + "load.gif", new LoadAction());
			addToolBarButton("Save", dir + "save.gif", new SaveAction());
			addSeparator();
		}
		
		addToolBarButton("Cut", dir + "cut.gif", new CutAction());
		addToolBarButton("Copy", dir + "copy.gif", new CopyAction());
		addToolBarButton("Paste", dir + "paste.gif", new PasteAction());
		addSeparator();
		addToolBarButton("Undo", dir + "undo.gif", new UndoAction());
		addSeparator();
		addToolBarButton("Config", dir + "config.gif", new ConfigAction());
	}

	//////////////////////////////////////////////////
	// DiagramFrame / Diagram
	//////////////////////////////////////////////////

	public void	setDiagramFrame(DiagramFrame diagramFrame) {
		mDiagramFrame = diagramFrame;
	}
	
	public DiagramFrame getDiagramFrame() {
		return mDiagramFrame;
	}

	public Diagram getDiagram() {
		return mDiagramFrame.getDiagram();
	}

	public World getWorld() {
		return mDiagramFrame.getWorld();
	}

	//////////////////////////////////////////////////
	// Load
	//////////////////////////////////////////////////

	private class LoadAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getWorld().stopSimulation();
			
			String userDir = System.getProperty("user.dir");
			Debug.message("currentDirectoryPath = " + userDir);
			
			JFileChooser filechooser = new JFileChooser(new File(userDir));
			FileFilter dgmfileFilter = new DiagramFileFilter();
			filechooser.addChoosableFileFilter(dgmfileFilter);
			filechooser.setFileFilter(dgmfileFilter);
			
			Frame frame = getDiagramFrame().getFrame();
			if(filechooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				if (file != null) {
					if (file.isDirectory() == false) {
						try {
							getDiagram().load(file.toURL());
						} catch (MalformedURLException mue) {}
					}
				}
			}
			frame.repaint();
		}
	}

	//////////////////////////////////////////////////
	// Save
	//////////////////////////////////////////////////

	private class SaveAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getWorld().stopSimulation();
			
			String userDir = System.getProperty("user.dir");
			Debug.message("currentDirectoryPath = " + userDir);
			
			JFileChooser filechooser = new JFileChooser(new File(userDir));
			FileFilter dgmfileFilter = new DiagramFileFilter();
			filechooser.addChoosableFileFilter(dgmfileFilter);
			filechooser.setFileFilter(dgmfileFilter);
			
			Frame frame = getDiagramFrame().getFrame();
			if(filechooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				if (file != null) {
					if (file.isDirectory() == false) {
						getDiagram().save(file);
					}
				}
			}
			frame.repaint();
		}
	}
	
	//////////////////////////////////////////////////
	// Cut
	//////////////////////////////////////////////////

	private class CutAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getDiagramFrame().cut();
		}
	}
	
	//////////////////////////////////////////////////
	// Copy
	//////////////////////////////////////////////////

	private class CopyAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getDiagramFrame().copy();
		}
	}
	
	//////////////////////////////////////////////////
	// Paste
	//////////////////////////////////////////////////

	private class PasteAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getDiagramFrame().paste();
		}
	}

	//////////////////////////////////////////////////
	// Undo
	//////////////////////////////////////////////////

	private class UndoAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			getDiagramFrame().undo();
		}
	}

	//////////////////////////////////////////////////
	// Config
	//////////////////////////////////////////////////

	private class ConfigAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			DiagramFrame	dgmFrame		= getDiagramFrame();
			Diagram			dgm			= getDiagram();
			DialogDiagram	dgmDialog	= new DialogDiagram(dgmFrame.getFrame(), dgm, dgmFrame.getLineStyle());
			
			if (dgmDialog.doModal() == Dialog.OK_OPTION) {
				String dgmName = dgmDialog.getName();
				if (dgmName != null) {
					dgm.setName(dgmName);
					
					dgmFrame.setTitle(dgmName);
					
					WorldTreeNode treeNode = (WorldTreeNode)dgm.getData();
					if (treeNode != null)
						treeNode.setText(dgmName);
				}
				dgmFrame.setLineStyle(dgmDialog.getDataflowLineStyle());
			}			
		}
	}
}
