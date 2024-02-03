/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	WorldTree.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;

import cv97.*;
import cv97.node.*;
import cv97.route.*;

public class WorldTreeFrame extends JFrame implements MouseListener, MouseMotionListener, KeyListener, WindowListener {
	private		JTree					mTree;
	private		WorldTreeModel		mTreeModel;
	private		CtbIDE				mCtbIDE;					
	private		WorldTreeNode		mRootTreeNode;
	private		WorldTreeNode		mSceneGraphRootTreeNode;
	private		WorldTreeNode		mEventRootTreeNode;
	private		WorldTreeNode		mRouteRootTreeNode;

	public void setCtbIDE(CtbIDE ctbIDE) {
		mCtbIDE = ctbIDE;
	}

	public CtbIDE getCtbIDE() {
		return mCtbIDE;
	}

	public World getWorld() {
		return mCtbIDE;
	}

	public SceneGraph getSceneGraph() {
		return getWorld().getSceneGraph();
	}

	public WorldTreeModel getTreeModel() {
		return mTreeModel;
	}

	public JTree getTree() {
		return mTree;
	}

	/////////////////////////////////////////////
	//	Constructor
	/////////////////////////////////////////////

	public WorldTreeFrame(CtbIDE ctbIDE) {

		setCtbIDE(ctbIDE);

		getContentPane().add("North", new WorldTreeToolBar(this));
		
		JPanel		panel = new JPanel(true);

		setTitle("CyberToolbox for Java3D");
		
		getContentPane().add("Center", panel);
		setBackground(Color.lightGray);

		WorldTreeNode root = createRootTreeNode();
		mTreeModel = new WorldTreeModel(root);

		mTree = new JTree(mTreeModel);
		
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
		mTree.addMouseListener(this);
		mTree.addMouseMotionListener(this);

		ToolTipManager.sharedInstance().registerComponent(mTree);
		mTree.setCellRenderer(new WorldTreeCellRenderer());
		mTree.setRowHeight(-1);

		JScrollPane sp = new JScrollPane();
		sp.setPreferredSize(new Dimension(300, 300));
		sp.getViewport().add(mTree);

		panel.setLayout(new BorderLayout());
		panel.add("Center", sp);

		createSceneGraphRootTreeNode();
		createRouteRootTreeNode();
		createEventRootTreeNode();
		reset();
				
		addWindowListener(this); 
		addKeyListener(this);

		pack();
		show();
    }

	/////////////////////////////////////////////
	//	WindowListener
	/////////////////////////////////////////////

	public void windowActivated(WindowEvent e) {
	}
	
	public void windowClosed(WindowEvent e) {
	}
	
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
	
	public void windowDeactivated(WindowEvent e) {
	}
	
	public void windowDeiconified(WindowEvent e) {
	}
	
	public void windowIconified(WindowEvent e) {
	}
	
	public void windowOpened(WindowEvent e) {
	}
	
	/////////////////////////////////////////////
	//	TreeNode
	/////////////////////////////////////////////

	public void setSelectionPath(TreePath treePath) {
		getTree().setSelectionPath(treePath);
	}
	
	public TreePath getTreePathForMousePosition(int x, int y) {
		return getTree().getPathForLocation(x, y);
	}

	public WorldTreeNode getTreeNodeForTreePath(TreePath treePath) {
		if(treePath != null)
			return (WorldTreeNode)treePath.getLastPathComponent();
		return null;
	}
	
	public WorldTreeNode geTreeNodeForMousePosition(int x, int y) {
		return getTreeNodeForTreePath(getTreePathForMousePosition(x, y));
	}

	public WorldTreeNode getSelectedTreeNode() {
		TreePath selPath = getTree().getSelectionPath();
		if(selPath != null)
			return (WorldTreeNode)selPath.getLastPathComponent();
		return null;
	}
	
	public WorldTreeNode createNewTreeNode(String name, Object obj) {
		return new WorldTreeNode(new WorldTreeData(name, obj));
	}

	/////////////////////////////////////////////
	//	Root Node
	/////////////////////////////////////////////

	private WorldTreeNode createRootTreeNode() {
		mRootTreeNode = createNewTreeNode("World", new String("World"));
		return mRootTreeNode;
	}
		
	public WorldTreeNode getRootTreeNode() {
		return (WorldTreeNode)mTreeModel.getRoot();
	}

	private WorldTreeNode createSceneGraphRootTreeNode()
	{
		mSceneGraphRootTreeNode = createNewTreeNode("SceneGraph", null);
		mTreeModel.insertNodeInto(mSceneGraphRootTreeNode, getRootTreeNode(), mTreeModel.getChildCount(getRootTreeNode()));
		return mSceneGraphRootTreeNode;
	}

	public WorldTreeNode getSceneGraphRootTreeNode() {
		return mSceneGraphRootTreeNode;
	}


	private WorldTreeNode createEventRootTreeNode()
	{
		mEventRootTreeNode = createNewTreeNode("Event", null);
		mTreeModel.insertNodeInto(mEventRootTreeNode, getRootTreeNode(), mTreeModel.getChildCount(getRootTreeNode()));
		return mEventRootTreeNode;
	}

	public WorldTreeNode getEventRootTreeNode() {
		return mEventRootTreeNode;
	}

	private WorldTreeNode createRouteRootTreeNode()
	{
		mRouteRootTreeNode = createNewTreeNode("Route", null);
		mTreeModel.insertNodeInto(mRouteRootTreeNode, getRootTreeNode(), mTreeModel.getChildCount(getRootTreeNode()));
		return mRouteRootTreeNode;
	}

	public WorldTreeNode getRouteRootTreeNode() {
		return mRouteRootTreeNode;
	}

	/////////////////////////////////////////////
	//	Operation
	/////////////////////////////////////////////
	
	public void clear() {
		removeAllSceneGraphNodeTreeNodes();
		removeAllEventTreeNodes();
	}

	public void reset() {
		clear();
		addSceneGraphNodeTreeNodes();
		addRouteTreeNodes();
		addEventTreeNodes();
	}
	
	/////////////////////////////////////////////
	//	SceneGraph Node
	/////////////////////////////////////////////

	public String getNodeTreeText(Node node) {
		String treeNodeName;
		String nodeName = node.getName();
		if (nodeName != null && 0 < nodeName.length()) {
			if (node.isInstanceNode()) 
				treeNodeName = node.getType() + " - " + nodeName + " (Instance)";
			else
				treeNodeName = node.getType() + " - " + nodeName;
		} 
		else {
			if (node.isInstanceNode()) 
				treeNodeName = node.getType() + " (Instance)";
			else
				treeNodeName = node.getType();
		}
		return treeNodeName;
	}
	
	public void addSceneGraphNodeTreeNode(Node node, Node parentNode)
	{
		if (getWorld().isSystemNode(node) == true)
			return;
			
		WorldTreeNode parentTreeNode;
		if (parentNode == null || parentNode == getSceneGraph().getRootNode())
			parentTreeNode = getSceneGraphRootTreeNode();
		else
			parentTreeNode = (WorldTreeNode)parentNode.getData();

		String treeNodeName = getNodeTreeText(node);
		
		WorldTreeNode newNode = createNewTreeNode(treeNodeName, node);
		node.setData(newNode);
		mTreeModel.insertNodeInto(newNode, parentTreeNode, mTreeModel.getChildCount(parentTreeNode));

//		if (node->isInstanceNode() == false) {
			for (Node cnode=node.getChildNodes(); cnode!=null; cnode=cnode.next())
				addSceneGraphNodeTreeNode(cnode, node);
//		}
	}

	public void addSceneGraphNodeTreeNodes() {
		SceneGraph sg = getSceneGraph();
		for (Node node=sg.getNodes(); node!=null; node=node.next())
			addSceneGraphNodeTreeNode(node, null);
	}

	public void removeSceneGraphNodeTreeNode(Node node) {
		WorldTreeNode treeNode = (WorldTreeNode)node.getData();
		if (treeNode != null) {
			getTreeModel().removeNodeFromParent(treeNode);
			node.setData(null);
		}
	}

	public void removeAllSceneGraphNodeTreeNodes() {
		SceneGraph sg = getSceneGraph();
		for (Node node=sg.getNodes(); node!=null; node=node.next())
			removeSceneGraphNodeTreeNode(node);
	}

	public Node findSceneGraphNode(WorldTreeNode treeNode) {
		if (treeNode == null)
			return null;
		
		if (treeNode == getSceneGraphRootTreeNode()) 
			return getSceneGraph().getRootNode();
			
		for (Node node=getSceneGraph().getNodes(); node!=null; node=node.nextTraversal()) {
			if (node.getData() == treeNode)
				return node;
		}
		return null;
	}

	/////////////////////////////////////////////
	//	Route Node
	/////////////////////////////////////////////

	public String getRouteTreeText(Route route) {
		Node		eventOutNode		= route.getEventOutNode();
		Field	eventOutField	= route.getEventOutField();
		Node		eventInNode		= route.getEventInNode();
		Field	eventInField		= route.getEventInField();
		
		if (eventOutNode == null || eventOutField == null || eventInNode == null || eventInField == null)
			return null;
		
		if (eventOutNode.hasName() == false || eventInNode.hasName() == false)
			return null;
		
		return eventOutNode.getName() + "." + eventOutField.getName() + " TO " + eventInNode.getName() + "." + eventInField.getName();
	}
	
	public void addRouteTreeNode(Route route) {
		WorldTreeNode newNode = createNewTreeNode(getRouteTreeText(route), null);
		route.setData(newNode);
		mTreeModel.insertNodeInto(newNode, getRouteRootTreeNode(), mTreeModel.getChildCount(getRouteRootTreeNode()));
	}
	
	public void addRouteTreeNodes() {
		for (Route route=getWorld().getSceneGraph().getRoutes(); route != null; route=route.next()) 
			addRouteTreeNode(route);
	}

	public void removeRouteTreeNode(Route route) {
		WorldTreeNode treeNode = (WorldTreeNode)route.getData();
		if (treeNode != null) {
			getTreeModel().removeNodeFromParent(treeNode);
			route.setData(null);
		}
	}
	
	public void removeRouteTreeNodes() {
		for (Route route=getWorld().getSceneGraph().getRoutes(); route != null; route=route.next()) 
			removeRouteTreeNode(route);
	}

	public Route findRoute(WorldTreeNode treeNode) {
		for (Route route=getWorld().getSceneGraph().getRoutes(); route != null; route=route.next()) {
			if (route.getData() == treeNode)
				return route;
		}	
		return null;
	}

	/////////////////////////////////////////////
	//	Event Node
	/////////////////////////////////////////////

	public String getEventTreeText(Event event) {
		String eventName = null;
		if (event.getOptionString() != null) 
			eventName = event.getName() + " ( " + event.getOptionString() + " )";
		else
			eventName = event.getName();
		return eventName;
	}
	
	public void addEventTreeNode(Event event) {
		Debug.message("WorldTree.addEventTreeNode");
		Debug.message("\tevent = " + event);
		WorldTreeNode newNode = createNewTreeNode(getEventTreeText(event), event);
		Debug.message("\tnewNode = " + newNode);
		event.setData(newNode);
		mTreeModel.insertNodeInto(newNode, getEventRootTreeNode(), mTreeModel.getChildCount(getEventRootTreeNode()));
		addDiagramTreeNodes(event);
	}
	
	public void addEventTreeNodes() {
		for (Event event=getWorld().getEvents(); event != null; event=event.next())
			addEventTreeNode(event);
	}

	public void removeEventTreeNode(Event event) {
		Debug.message("WorldTree.removeEventTreeNode");
		removeAllDiagramTreeNodes(event);
		WorldTreeNode treeNode = (WorldTreeNode)event.getData();
		Debug.message("\ttreeNode = " + treeNode);
		if (treeNode != null) {
			getTreeModel().removeNodeFromParent(treeNode);
			event.setData(null);
		}
	}
	
	public void removeAllEventTreeNodes() {
		for (Event event=getWorld().getEvents(); event != null; event = event.next()) 
			removeEventTreeNode(event);
	}

	public Event findEvent(WorldTreeNode treeNode) {
		for (Event event=getWorld().getEvents(); event != null; event=event.next()) {
			if (event.getData() == treeNode)
				return event;
		}
		return null;
	}

	/////////////////////////////////////////////
	//	Diagram Node
	/////////////////////////////////////////////

	public void addDiagramTreeNode(Event event, Diagram dgm) {
		WorldTreeNode newNode = createNewTreeNode(dgm.getName(), dgm);
		dgm.setData(newNode);
		WorldTreeNode parentTreeNode = (WorldTreeNode)event.getData();
		mTreeModel.insertNodeInto(newNode, parentTreeNode, mTreeModel.getChildCount(parentTreeNode));
	}
	
	public void addDiagramTreeNodes(Event event) {
		for (Diagram dgm=event.getDiagrams(); dgm != null; dgm=dgm.next()) 
			addDiagramTreeNode(event, dgm);
	}

	public void removeDiagramTreeNode(Diagram dgm) {
		WorldTreeNode treeNode = (WorldTreeNode)dgm.getData();
		if (treeNode != null) {
			getTreeModel().removeNodeFromParent(treeNode);
			dgm.setData(null);
		}
	}
	
	public void removeAllDiagramTreeNodes(Event event) {
		for (Diagram dgm=event.getDiagrams(); dgm != null; dgm = dgm.next())  
			removeDiagramTreeNode(dgm);
	}

	public Diagram findDiagram(WorldTreeNode treeNode) {
		for (Event event=getWorld().getEvents(); event != null; event=event.next()) {
			for (Diagram dgm=event.getDiagrams(); dgm != null; dgm=dgm.next()) {
				if (dgm.getData() == treeNode)
					return dgm;
			}
		}
		return null;
	}
		
	/////////////////////////////////////////////
	//	ToolBar
	/////////////////////////////////////////////

	private Image loadImageIcon(String filename) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(filename);
		MediaTracker mt = new MediaTracker(this);
		mt.addImage (image, 0);
		try { mt.waitForAll(); }
		catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		if (mt.isErrorAny()) {
			Debug.warning("Couldn't loading a image file (" + filename + ") in WorldTree::loadImageIcon" );
		}
		return image;
	}
	
	///////////////////////////////////////////////
	//  Dragging Infomation
	///////////////////////////////////////////////
	
	private WorldTreeNode mDragedTreeNode = null;
	
	private void setDragedTreeNode(WorldTreeNode treeNode) {
		mDragedTreeNode = treeNode;
	}

	private WorldTreeNode getDragedTreeNode() {
		return mDragedTreeNode;
	}

	private WorldTreeNode mDropedTreeNode = null;
	
	private void setDropedTreeNode(WorldTreeNode treeNode) {
		mDropedTreeNode = treeNode;
	}

	private WorldTreeNode getDropedTreeNode() {
		return mDropedTreeNode;
	}

	public boolean isTreeNodeDraged () {
		if (getDragedTreeNode() == null)
			return false;
		if (getDropedTreeNode() == null)
			return false;
		if (getDragedTreeNode() == getDropedTreeNode())
			return false;
		return true;
	}
	
	///////////////////////////////////////////////
	//  mousePressed / mouseReleased
	///////////////////////////////////////////////
	
	public void mousePressed(MouseEvent e) {
		Debug.message("mousePressed");
		setDragedTreeNode(geTreeNodeForMousePosition(e.getX(), e.getY()));
		setDropedTreeNode(null);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public boolean moveTreeNode(WorldTreeNode dragedTreeNode, WorldTreeNode dropedTreeNode) {
		
		// Are SceneGraph nodes ?
		Node dragedNode = findSceneGraphNode(dragedTreeNode);
		Node dropedNode = findSceneGraphNode(dropedTreeNode);
		if (dragedNode != null && dropedNode != null) {
			if (dropedNode.isChildNodeType(dragedNode) == true) {
				removeSceneGraphNodeTreeNode(dragedNode);
				if (dropedNode.isRootNode() == true) 
					getSceneGraph().moveNode(dragedNode);
				else
					dropedNode.moveChildNode(dragedNode);
				addSceneGraphNodeTreeNode(dragedNode, dropedNode);
				return true;
			}
		}

		return false;		
	}

	public void mouseReleased(MouseEvent e) {
		Debug.message("mouseReleased");
		
		if (isTreeNodeDraged() == true) {
			if (moveTreeNode(getDragedTreeNode(), getDropedTreeNode()) == false)
				Toolkit.getDefaultToolkit().beep();
		}
		
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	} 
	
	///////////////////////////////////////////////
	//  mouseClicked
	///////////////////////////////////////////////

	public void openSceneGraphNodeDialog(WorldTreeNode treeNode, Node sgNode) {
		boolean isSimulationRunning = getWorld().isSimulationRunning();
		if (isSimulationRunning == true)
			getWorld().stopSimulation();
		getWorld().stopAppletThread();				
			
		DialogNode dialog = new DialogNode(this, sgNode);
		if (dialog.doModal() == Dialog.OK_OPTION) {
			sgNode.setName(dialog.getNodeName());
			String fieldValues[] = dialog.getFieldValues();
			int n;
			int nFields = sgNode.getNFields();
			for (n=0; n<nFields; n++) {
				Field field = sgNode.getField(n);
				Debug.message("\t" + field.getName() + " : " + fieldValues[n]);
				field.setValue(fieldValues[n]);
			}
			int nExposedFields = sgNode.getNExposedFields();
			for (n=0; n<nExposedFields; n++) {
				Field field = sgNode.getExposedField(n);
				Debug.message(field.getName() + " : " + fieldValues[n+nFields]);
				field.setValue(fieldValues[n+nFields]);
			}
		}			
			
		String nodeText = getNodeTreeText(sgNode);
		treeNode.setText(nodeText);

		if (isSimulationRunning == true)
			getWorld().startSimulation();
		getWorld().startAppletThread();				
	}

	public void openRouteDialog(WorldTreeNode treeNode, Route route) {
		boolean isSimulationRunning = getWorld().isSimulationRunning();
		if (isSimulationRunning == true)
			getWorld().stopSimulation();
		getWorld().stopAppletThread();				
		
		DialogRoute dialog = new DialogRoute(this, getWorld(), route);
		if (dialog.doModal() == Dialog.OK_OPTION) { 
			Route changedRoute = dialog.getRoute();
			if (changedRoute!= null) {
				route.set(changedRoute);
				treeNode.setText(getRouteTreeText(route));
			}
		}
				
		if (isSimulationRunning == true)
			getWorld().startSimulation();
		getWorld().startAppletThread();		
	}		

	public void openEventDialog(WorldTreeNode treeNode, Event event) {
		boolean isSimulationRunning = getWorld().isSimulationRunning();
		if (isSimulationRunning == true)
			getWorld().stopSimulation();
		getWorld().stopAppletThread();				
		
		DialogEvent dialog = new DialogEvent(this, event);
		if (dialog.doModal() == Dialog.OK_OPTION) {
			String optionString = dialog.getOptionString();
			if (optionString != null) {
				event.setOptionString(optionString);
				String nodeText = getEventTreeText(event);
				treeNode.setText(nodeText);
			}
			else
				Message.beep();
		}
				
		if (isSimulationRunning == true)
			getWorld().startSimulation();
		getWorld().startAppletThread();				
	}
	
	public void openDiagramFrame(Diagram dgm) {
		DiagramFrame dgmFrame;
		if (getCtbIDE().isDiagramFrameOpened(dgm) == false) {
			dgmFrame = getCtbIDE().openDiagramFrame(dgm);
			Event dgmEvent = dgmFrame.getDiagram().getEvent();
/*
			EventBehavior eventBehavior = getWorld().getEventBehaviorGroup().getEventBehavior(dgmEvent);
			if (eventBehavior != null)
				eventBehavior.addUpdateComponent(dgmFrame.getMainComponent());
*/
		}
		else 
			dgmFrame = getCtbIDE().getDiagramFrame(dgm);
		dgmFrame.toFront();
		dgmFrame.repaint();
	}
		
	//////////////////////////////////////////////////
	// Remove Action
	//////////////////////////////////////////////////

	private boolean removeSceneGraphNode(Node sgNode) {
		if (getSceneGraph().getRootNode() != sgNode) {
			int result = Message.showConfirmDialog(this, "Are you sure you want to remove this node ( " + sgNode + " ) ?");
			repaint();
			if(result == Message.YES_OPTION) {
				getWorld().stopAppletThread();				
				removeSceneGraphNodeTreeNode(sgNode);
				sgNode.remove();
				getWorld().startAppletThread();				
				return true;
			}
		}
		return false;
	}

	private boolean removeRoute(Route route) {
		int result = Message.showConfirmDialog(this, "Are you sure you want to remove this route ?");
		repaint();
		if(result == Message.YES_OPTION) {
			getWorld().stopAppletThread();				
			removeRouteTreeNode(route);
			route.remove();
			getWorld().startAppletThread();				
			return true;
		}
		return false;
	}

	private boolean removeEvent(Event event) {	
		if (event.getAttribute() == EventType.ATTRIBUTE_SYSTEM)
			return false;
	
		int result = Message.showConfirmDialog(this, "Are you sure you want to remove this event ( " + getEventTreeText(event) + " ) ?");
		repaint();
		if(result == Message.YES_OPTION) {
			getWorld().stopAppletThread();				
			for (Diagram dgm = event.getDiagrams(); dgm != null; dgm=dgm.next()) 
				getCtbIDE().closeDiagramFrame(dgm);
			removeEventTreeNode(event);
			event.remove();
			getWorld().startAppletThread();				
			return true;
		}
		return false;
	}
		
	private boolean removeDiagram(Diagram dgm) {
		int result = Message.showConfirmDialog(this, "Are you sure you want to remove this diagram ( " + dgm.getName() + " ) ?");
		repaint();
		if(result == Message.YES_OPTION) {
			getWorld().stopAppletThread();				
			getCtbIDE().closeDiagramFrame(dgm);
			removeDiagramTreeNode(dgm);
			dgm.remove();
			getWorld().startAppletThread();				
			return true;
		}
		return false;
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

	private class PopupMenuSceneGraphNodeAction extends AbstractAction {
		private WorldTreeNode	mTreeNode;	
		private Node				mNode;
		
		public PopupMenuSceneGraphNodeAction(WorldTreeNode treeNode, Node node) {
			mTreeNode = treeNode;
			mNode = node;
		}
		
		public void actionPerformed(ActionEvent e) {
			Debug.message("PopupMenuAction.actionPerformed = " + e.getActionCommand());	
			if (mPopupMenuString[0].equals(e.getActionCommand())) 
				openSceneGraphNodeDialog(mTreeNode, mNode);
			if (mPopupMenuString[1].equals(e.getActionCommand())) 
				removeSceneGraphNode(mNode);
		}
	}

	private class PopupMenuRouteAction extends AbstractAction {
		private WorldTreeNode	mTreeNode;	
		private Route				mRoute;
		
		public PopupMenuRouteAction(WorldTreeNode treeNode, Route route) {
			mTreeNode = treeNode;
			mRoute = route;
		}
		
		public void actionPerformed(ActionEvent e) {
			Debug.message("PopupMenuAction.actionPerformed = " + e.getActionCommand());	
			if (mPopupMenuString[0].equals(e.getActionCommand())) 
				openRouteDialog(mTreeNode, mRoute);
			if (mPopupMenuString[1].equals(e.getActionCommand())) 
				removeRoute(mRoute);
		}
	}

	private class PopupMenuEventNodeAction extends AbstractAction {
		private WorldTreeNode	mTreeNode;	
		private Event				mEvent;
		
		public PopupMenuEventNodeAction(WorldTreeNode treeNode, Event event) {
			mTreeNode = treeNode;
			mEvent = event;
		}
		
		public void actionPerformed(ActionEvent e) {
			Debug.message("PopupMenuAction.actionPerformed = " + e.getActionCommand());	
			if (mPopupMenuString[0].equals(e.getActionCommand())) 
				openEventDialog(mTreeNode, mEvent);
			if (mPopupMenuString[1].equals(e.getActionCommand())) 
				removeEvent(mEvent);
		}
	}

	private class PopupMenuDiagramNodeAction extends AbstractAction {
		private WorldTreeNode	mTreeNode;	
		private Diagram			mDiagram;
		
		public PopupMenuDiagramNodeAction(WorldTreeNode treeNode, Diagram dgm) {
			mTreeNode = treeNode;
			mDiagram = dgm;
		}
		
		public void actionPerformed(ActionEvent e) {
			Debug.message("PopupMenuAction.actionPerformed = " + e.getActionCommand());	
			if (mPopupMenuString[0].equals(e.getActionCommand())) 
				openDiagramFrame(mDiagram);
			if (mPopupMenuString[1].equals(e.getActionCommand())) 
				removeDiagram(mDiagram);
		}
	}
		
	///////////////////////////////////////////////
	//  mouseClicked
	///////////////////////////////////////////////
	
	public void mouseClicked(MouseEvent e) {

		Debug.message("mouseClicked");
		
		TreePath treePath = getTreePathForMousePosition(e.getX(), e.getY());

		if (treePath == null)
			return;
		
		setSelectionPath(treePath);
			
		WorldTreeNode treeNode = getTreeNodeForTreePath(treePath);
		
		if (treeNode == null)
			return;

		int mouseButton = e.getModifiers();
					
		boolean dblClick		= false;
		boolean singleClick	= false;
		
		if(e.getClickCount() == 1)
			singleClick = true;
		if(e.getClickCount() == 2)
			dblClick = true;
				
		Node sgNode = findSceneGraphNode(treeNode);
		if (sgNode != null && getSceneGraph().getRootNode() != sgNode) {
			Debug.message("SceneGraph Node Clicked : " + sgNode);
			if (mouseButton == e.BUTTON1_MASK && dblClick == true)
				openSceneGraphNodeDialog(treeNode, sgNode);
			if (mouseButton == e.BUTTON3_MASK && singleClick == true) {
				PopupMenu popupMenu = new PopupMenu(new PopupMenuSceneGraphNodeAction(treeNode, sgNode));
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		Route route = findRoute(treeNode);
		if (route != null) {
			Debug.message("Route Node Clicked : " + route);
			if (mouseButton == e.BUTTON1_MASK && dblClick == true)
				openRouteDialog(treeNode, route);
			if (mouseButton == e.BUTTON3_MASK && singleClick == true) {
				PopupMenu popupMenu = new PopupMenu(new PopupMenuRouteAction(treeNode, route));
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
		
		Event event = findEvent(treeNode);
		if (event != null) {
			Debug.message("Event Clicked (" + event.getName() + ")");
			if (event.getAttribute() == EventType.ATTRIBUTE_USER) {
				if (mouseButton == e.BUTTON1_MASK && dblClick == true)
					openEventDialog(treeNode, event);
				if (mouseButton == e.BUTTON3_MASK && singleClick == true) {
					PopupMenu popupMenu = new PopupMenu(new PopupMenuEventNodeAction(treeNode, event));
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
		
		Diagram dgm = findDiagram(treeNode);
		if (dgm != null) {
			Debug.message("Diagram Clicked (" + dgm.getName() + ")");
			if (mouseButton == e.BUTTON1_MASK && dblClick == true)
				openDiagramFrame(dgm);
			if (mouseButton == e.BUTTON3_MASK && singleClick == true) {
				PopupMenu popupMenu = new PopupMenu(new PopupMenuDiagramNodeAction(treeNode, dgm));
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	///////////////////////////////////////////////
	//  mousDragged
	///////////////////////////////////////////////
	
	public void mouseDragged(MouseEvent e) {
		setDropedTreeNode(geTreeNodeForMousePosition(e.getX(), e.getY()));
		getTree().setSelectionPath(getTree().getPathForLocation(e.getX(), e.getY()));
	}

	///////////////////////////////////////////////
	//  MouseListener
	///////////////////////////////////////////////
	
	public void mouseEntered(MouseEvent e) {
		Debug.message("mouseEntered");
	}

	public void mouseExited(MouseEvent e) {
		Debug.message("mouseExited");
	} 

	public void mouseMoved(MouseEvent e) {
		//Debug.message("mouseMoved");
	}

	//////////////////////////////////////////////////
	// KeyListener
	//////////////////////////////////////////////////

	private boolean removeTreeNode(WorldTreeNode treeNode) {
		Node sgNode = findSceneGraphNode(treeNode);
		if (sgNode != null && getSceneGraph().getRootNode() != sgNode) {
			return removeSceneGraphNode(sgNode);
		}

		Route route = findRoute(treeNode);
		if (route != null) {
			return removeRoute(route);
		}
		
		Event event = findEvent(treeNode);
		if (event != null) {
			return removeEvent(event);
		}
		
		Diagram dgm = findDiagram(treeNode);
		if (dgm != null) {
			return removeDiagram(dgm);
		}
		
		return false;
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DELETE:
			{
				if (removeTreeNode(getSelectedTreeNode()) == false)
					Message.beep();
			}
			break;
		case KeyEvent.VK_P:
			{
				getSceneGraph().print();
			}
			break;
		}
	}


	public void keyReleased(KeyEvent e) {
	}
}

