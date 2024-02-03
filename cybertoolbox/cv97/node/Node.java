/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File: Node.java
*
******************************************************************/

package cv97.node;

import java.util.Vector;
import java.io.*;
import cv97.*;
import cv97.util.*;
import cv97.route.*;
import cv97.field.*;
import cv97.field.SFMatrix;

public abstract class Node extends BaseNode implements Runnable, NodeConstatns { 

	public static final int		RUNNABLE_TYPE_NONE					= 0;
	public static final int		RUNNABLE_TYPE_ALWAYS					= 1;
	public static final int		RUNNABLE_DEFAULT_INTERVAL_TIME	= 100;

	////////////////////////////////////////////////
	//	Member
	////////////////////////////////////////////////

	private Vector			mField					= new Vector();
	private Vector			mPrivateField			= new Vector();
	private Vector			mExposedField			= new Vector();
	private Vector			mEventInField			= new Vector();
	private Vector			mEventOutField			= new Vector();
	private boolean		mInitializationFlag	= false;
	private NodeObject	mObject					= null;
	private Thread			mThreadObject			= null;

	private boolean		mRunnable				= false;			
	private int				mRunnableType			= RUNNABLE_TYPE_NONE;			
	private int				mRunnableIntervalTime	= RUNNABLE_DEFAULT_INTERVAL_TIME;
				
	private Node			mParentNode				= null;
	private LinkedList	mChildNodes				= new LinkedList();
	private SceneGraph	mSceneGraph				= null;
	private Object			mUserData				= null;
	private Node			mReferenceNode			= null;
	
	////////////////////////////////////////////////
	//	abstract functions
	////////////////////////////////////////////////

	abstract public boolean	isChildNodeType(Node node);
	abstract public void	update();
	
	public void recreateNodeObject() {
		if (isRootNode() == false) {
			SceneGraph sg = getSceneGraph();
			if (sg == null)
				return;
			SceneGraphObject sgObject = sg.getObject();
			if (sgObject == null)
				return;
			NodeObject nodeObject = getObject();
			if (nodeObject != null)
				sgObject.removeNode(sg, this);
			nodeObject = sg.createNodeObject(this);
			setObject(nodeObject);
			if (nodeObject != null)
				sgObject.addNode(sg, this);
			Debug.message("Node::recreateNodeObject = " + this + ", " + nodeObject);
		}
	}
	
	public void initialize() {
		//recreateNodeObject();
	}
	
	abstract public void	uninitialize();

	////////////////////////////////////////////////
	//	Constractor
	////////////////////////////////////////////////

	public Node() {
		setHeaderFlag(true);
		setParentNode(null);
		setSceneGraph(null);
		setReferenceNode(null);
		setObject(null);
		setInitializationFlag(false);
		setThreadObject(null);
		setRunnable(false);
	}
	
	public Node(String nodeType, String nodeName) {
		this();
		setType(nodeType);
		setName(nodeName);
	}

	public Node(Node node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	//	List management
	////////////////////////////////////////////////

	public void removeChildNodes() {
		Node node=getChildNodes();
		while (node != null) {
			Node nextNode = node.next();
			node.remove();
			node = nextNode;
		}
	}

	public void removeRoutes() {
		SceneGraph sg = getSceneGraph();
		if (sg != null) {
			Route route=sg.getRoutes();
			while (route != null) {
				Route nextRoute = route.next();
				if (route.getEventInNode() == this || route.getEventOutNode() == this)
					route.remove();
				route = nextRoute;
			}
		}
	}

	public void removeSFNodes() {
		SceneGraph sg = getSceneGraph();
		if (sg != null) {
			for (ScriptNode script = sg.findScriptNode(); script != null; script=(ScriptNode)script.nextTraversalSameType()) {
				for (int n=0; n<script.getNFields(); n++) {
					Field field = script.getField(n);
					if (field.getType() == fieldTypeSFNode) {
						SFNode sfnode = (SFNode)field;
						if (sfnode.getValue() == this)
							sfnode.setValue((Node)null);
					}
				}
			}
		}
	}

	public void removeInstanceNodes() {
		SceneGraph sg = getSceneGraph();
		if (sg != null && isInstanceNode() == false) {
			Node node = sg.getNodes();
			while (node != null) {
				Node nextNode = node.nextTraversal();
				if (node.isInstanceNode() == true) {
					Node refNode = node.getReferenceNode();
					while (refNode.isInstanceNode() == true)
						refNode = refNode.getReferenceNode();
					if (refNode == this) {
						node.removeChildNodes();
						nextNode = node.nextTraversal();
						node.remove();
					}
				}
				node = nextNode;
			}
		}
	}

	public void remove() { 
		SceneGraph sg = getSceneGraph();
		
		if (sg != null) 
			sg.removeNodeObject(this);
		
		super.remove();

		if (isInstanceNode() == false) {
			removeRoutes();
			removeSFNodes();
			removeInstanceNodes();

			if (isBindableNode() == true) {
				if (sg != null)
					sg.setBindableNode((BindableNode)this, false);			
			}
		}

		setParentNode(null);
		setSceneGraph(null);
	}

	////////////////////////////////////////////////
	//	Field
	////////////////////////////////////////////////

	// Get an exposed field by name. 
	//   Throws an InvalidExposedFieldException if fieldName isn't a valid
	//   exposed field name for a node of this type.

	public final Field getField(String fieldName) {
		if (fieldName == null)
			throw new InvalidFieldException(fieldName + " is not found.");
		for (int n=0; n<getNFields(); n++) {
			Field field = getField(n);
			if (fieldName.compareTo(field.getName()) == 0)
				return field;
		}
		throw new InvalidFieldException(fieldName + " is not found.");
//		return null;
	}

	public final int getNFields() {
		return mField.size();
	}

	public final void addField(Field field) {
		mField.addElement(field);
	}

	public final void addField(String name, Field field) {
		field.setName(name);
		mField.addElement(field);
	}

	public final Field getField(int index) {
		return (Field)mField.elementAt(index);
	}

	public final boolean removeField(Field removeField) {
		for (int n=0; n<getNFields(); n++) {
			Field field = getField(n);
			if (field == removeField) {
				mField.removeElement(field);
				return true;
			}
		}
		return false;
	}

	public final boolean removeField(String fieldName) {
		return removeField(getField(fieldName));
	}

	public int getFieldNumber(Field field) {
		for (int n=0; n<getNFields(); n++) {
			if (getField(n) == field)
				return n;
		}
		return -1;
	}
		
	////////////////////////////////////////////////
	//	Private Field
	////////////////////////////////////////////////

	public final Field getPrivateField(String fieldName) {
		if (fieldName == null)
			throw new InvalidPrivateFieldException(fieldName + " is not found.");
		for (int n=0; n<getNPrivateFields(); n++) {
			Field field = getPrivateField(n);
			if (fieldName.compareTo(field.getName()) == 0)
				return field;
		}
		throw new InvalidPrivateFieldException(fieldName + " is not found.");
//		return null;
	}

	public final int getNPrivateFields() {
		return mPrivateField.size();
	}

	public final void addPrivateField(Field field) {
		mPrivateField.addElement(field);
	}

	public final void addPrivateField(String name, Field field) {
		field.setName(name);
		mPrivateField.addElement(field);
	}

	public final Field getPrivateField(int index) {
		return (Field)mPrivateField.elementAt(index);
	}

	public final boolean removePrivateField(Field removeField) {
		for (int n=0; n<getNPrivateFields(); n++) {
			Field field = getPrivateField(n);
			if (field == removeField) {
				mPrivateField.removeElement(field);
				return true;
			}
		}
		return false;
	}

	public final boolean removePrivateField(String fieldName) {
		return removePrivateField(getPrivateField(fieldName));
	}

	public int getPrivateFieldNumber(Field field) {
		for (int n=0; n<getNPrivateFields(); n++) {
			if (getPrivateField(n) == field)
				return n;
		}
		return -1;
	}

	////////////////////////////////////////////////
	//	ExposedField
	////////////////////////////////////////////////

	// Get an exposed field by name. 
	//   Throws an InvalidExposedFieldException if fieldName isn't a valid
	//   exposed field name for a node of this type.

	public final Field getExposedField(String fieldName) {
		if (fieldName == null)
			throw new InvalidExposedFieldException(fieldName + " is not found.");
			
		for (int n=0; n<getNExposedFields(); n++) {
			Field field = getExposedField(n);
			if (fieldName.compareTo(field.getName()) == 0)
				return field;
			if (fieldName.startsWith(eventInStripString)) {
				if (fieldName.endsWith(field.getName()))
					return field;
			}
			if (fieldName.endsWith(eventOutStripString)) {
				if (fieldName.startsWith(field.getName()))
					return field;
			}
		}
		throw new InvalidExposedFieldException(fieldName + " is not found.");
//		return null;
	}

	public final int getNExposedFields() {
		return mExposedField.size();
	}

	public final void addExposedField(Field field) {
		mExposedField.addElement(field);
	}

	public final void addExposedField(String name, Field field) {
		field.setName(name);
		mExposedField.addElement(field);
	}

	public final Field getExposedField(int index) {
		return (Field)mExposedField.elementAt(index);
	}

	public final boolean removeExposedField(Field removeField) {
		for (int n=0; n<getNExposedFields(); n++) {
			Field field = getExposedField(n);
			if (field == removeField) {
				mExposedField.removeElement(field);
				return true;
			}
		}
		return false;
	}

	public final boolean removeExposedField(String fieldName) {
		return removeExposedField(getExposedField(fieldName));
	}

	public int getExposedFieldNumber(Field field) {
		for (int n=0; n<getNExposedFields(); n++) {
			if (getExposedField(n) == field)
				return n;
		}
		return -1;
	}

	////////////////////////////////////////////////
	//	EventIn
	////////////////////////////////////////////////

	// Get an EventIn by name. Return value is write-only.
	//   Throws an InvalidEventInException if eventInName isn't a valid
	//   event in name for a node of this type.

	public final Field getEventIn(String fieldName) {
		if (fieldName == null)
			throw new InvalidEventInException(fieldName + " is not found.");

		for (int n=0; n<getNEventIn(); n++) {
			Field field = getEventIn(n);
			if (fieldName.compareTo(field.getName()) == 0)
				return field;
			if (fieldName.startsWith(eventInStripString)) {
				if (fieldName.endsWith(field.getName()))
					return field;
			}
		}
		throw new InvalidEventInException(fieldName + " is not found.");
		//return null;
	}

	public final int getNEventIn() {
		return mEventInField.size();
	}

	public final void addEventIn(Field field) {
		mEventInField.addElement(field);
	}

	public final void addEventIn(String name, Field field) {
		field.setName(name);
		mEventInField.addElement(field);
	}

	public final Field getEventIn(int index) {
		return (Field)mEventInField.elementAt(index);
	}

	public final boolean removeEventIn(Field removeField) {
		for (int n=0; n<getNEventIn(); n++) {
			Field field = getEventIn(n);
			if (field == removeField) {
				mEventInField.removeElement(field);
				return true;
			}
		}
		return false;
	}

	public final boolean removeEventIn(String fieldName) {
		return removeEventIn(getEventIn(fieldName));
	}

	public int getEventInNumber(Field field) {
		for (int n=0; n<getNEventIn(); n++) {
			if (getEventIn(n) == field)
				return n;
		}
		return -1;
	}

	////////////////////////////////////////////////
	//	EventOut
	////////////////////////////////////////////////

	// Get an EventOut by name. Return value is read-only.
	//   Throws an InvalidEventOutException if eventOutName isn't a valid
	//   event out name for a node of this type.

	public final ConstField getEventOut(String fieldName) {
		if (fieldName == null)
			throw new InvalidEventOutException(fieldName + " is not found.");
		for (int n=0; n<getNEventOut(); n++) {
			ConstField field = getEventOut(n);
			if (fieldName.compareTo(field.getName()) == 0)
				return field;
			if (fieldName.endsWith(eventOutStripString)) {
				if (fieldName.startsWith(field.getName()))
					return field;
			}
		}
		throw new InvalidEventOutException(fieldName + " is not found.");
//		return null;
	}

	public final int getNEventOut() {
		return mEventOutField.size();
	}

	public final void addEventOut(ConstField field) {
		mEventOutField.addElement(field);
	}

	public final void addEventOut(String name, ConstField field) {
		field.setName(name);
		mEventOutField.addElement(field);
	}

	public final ConstField getEventOut(int index) {
		return (ConstField)mEventOutField.elementAt(index);
	}

	public final boolean removeEventOut(ConstField removeField) {
		for (int n=0; n<getNEventOut(); n++) {
			ConstField field = getEventOut(n);
			if (field == removeField) {
				mEventOutField.removeElement(field);
				return true;
			}
		}
		return false;
	}

	public final boolean removeEventOut(String fieldName) {
		return removeEventOut(getEventOut(fieldName));
	}

	public int getEventOutNumber(Field field) {
		for (int n=0; n<getNEventOut(); n++) {
			if (getEventOut(n) == field)
				return n;
		}
		return -1;
	}

	////////////////////////////////////////////////
	//	Parent node
	////////////////////////////////////////////////

	public void setParentNode(Node parentNode) {
		mParentNode = parentNode;
	}

	public Node getParentNode() {
		return mParentNode;
	}
	
	public boolean isParentNode(Node node) {
		return (getParentNode() == node) ? true : false;
	}

	public boolean isAncestorNode(Node node) {
		for (Node parentNode = getParentNode(); parentNode != null; parentNode = parentNode.getParentNode()) {
			if (node == parentNode)
					return true;
		}
		return false;
	}

	////////////////////////////////////////////////
	//	find node list
	////////////////////////////////////////////////

	public Node nextTraversal() {
		Node nextNode = getChildNodes();
		if (nextNode != null)
			return nextNode;

		nextNode = next();
		if (nextNode == null) {
			Node parentNode = getParentNode();
			while (parentNode != null) { 
				Node parentNextNode = parentNode.next();
				if (parentNextNode != null)
					return parentNextNode;
				parentNode = parentNode.getParentNode();
			}
		}
		return nextNode;
	}

	public Node nextTraversalByType(String type) {
		if (type == null)
			return null;

		for (Node node = nextTraversal(); node != null; node = node.nextTraversal()) {
			if (node.getType() != null) {
				if (type.compareTo(node.getType()) == 0)
					return node;
			}
		}
		return null;
	}

	public Node nextTraversalByName(String name) {
		if (name == null)
			return null;

		for (Node node = nextTraversal(); node != null; node = node.nextTraversal()) {
			if (node.getName() != null) {
				if (name.compareTo(node.getName()) == 0)
					return node;
			}
		}
		return null;
	}

	public Node nextTraversalSameType() {
		return nextTraversalByType(getType());
	}

	////////////////////////////////////////////////
	//	next node list
	////////////////////////////////////////////////

	public Node next() {
		return (Node)getNextNode();
	}

	public Node next(String type) {
		for (Node node = next(); node != null; node = node.next()) {
			if (type.compareTo(node.getType()) == 0)
				return node;
		}
		return null;
	}

	public Node nextSameType() {
		return next(getType());
	}

	////////////////////////////////////////////////
	//	child node list
	////////////////////////////////////////////////

	public Node getChildNodes() {
		return (Node)mChildNodes.getNodes();
	}

	public boolean hasChildNodes() {
		if (getChildNodes() == null)
			return false;
		return true;
	}

	public Node getFirstChildNodes() {
		int numChild = getNChildNodes();
		if (0 < numChild)
			return getChildNode(0);
		return null;
	}

	public Node getLastChildNodes() {
		int numChild = getNChildNodes();
		if (0 < numChild)
			return getChildNode(numChild - 1);
		return null;
	}

	public Node getChildNodes(String type) {
		for (Node node = getChildNodes(); node != null; node = node.next()) {
			if (type.equals(node.getType()) == true)
				return node;
		}
		return null;
	}

	public Node getChildNodes(String type, String name) {
		for (Node node = getChildNodes(); node != null; node = node.next()) {
			if (type.equals(node.getType()) == false)
				continue;
			String nodeName = node.getName();
			if (name == null)
				continue;
			if (nodeName.equals(name) == true)
				return node;
		}
		return null;
	}

	public Node getChildNode(int n) {
		return (Node)mChildNodes.getNode(n);
	}

	public int getNChildNodes() {
		return mChildNodes.getNNodes();
	}

	////////////////////////////////////////////////
	//	Add children 
	////////////////////////////////////////////////
	
	public void addChildNode(Node node) {
		moveChildNode(node);
		node.initialize();
	}

	public void addChildNodeAtFirst(Node node) {
		moveChildNodeAtFirst(node);
		node.initialize();
	}

	////////////////////////////////////////////////
	//	Move children 
	////////////////////////////////////////////////

	public void moveChildNode(Node node) {
		SceneGraph sg = getSceneGraph();
		mChildNodes.addNode(node); 
		node.setParentNode(this);
		node.setSceneGraph(sg);
		if (sg != null) {
			sg.removeNodeObject(this);
			sg.addNodeObject(this);
		}
	}

	public void moveChildNodeAtFirst(Node node) {
		SceneGraph sg = getSceneGraph();
		mChildNodes.addNodeAtFirst(node); 
		node.setParentNode(this);
		node.setSceneGraph(sg);
		if (sg != null) {
			sg.removeNodeObject(this);
			sg.addNodeObject(this);
		}
	}

	////////////////////////////////////////////////
	//	Add / Remove children (for Groupingnode)
	////////////////////////////////////////////////

	public boolean isChild(Node parentNode, Node node) {
		for (Node cnode = parentNode.getChildNodes(); cnode != null; cnode = cnode.next()) {
			if (cnode == node)
				return true;
		}
		return false;
	}

	public boolean isChild(Node node) {
		if (getChildNodes() != null) {
			for (Node cnode = getChildNodes(); cnode != null; cnode = cnode.next()) {
				if (cnode == node)
					return true;
				if (isChild(cnode, node))
					return true;
			}
		}
		return false;
	}

	public void	addChildren(Node node[]) {
		for (int n=0; n<node.length; n++) {
			if (!isChild(node[n]))
				addChildNode(node[n]);
		}

	}

	public void	removeChildren(Node node[]) {
		for (int n=0; n<node.length; n++) {
			if (isChild(node[n]))
				node[n].remove();
		}

	}

	////////////////////////////////////////////////
	//	get child node list
	////////////////////////////////////////////////

	public GroupingNode getGroupingNodes() {
		for (Node node = getChildNodes(); node != null; node = node.next()) {
			if (node.isGroupingNode())
				return (GroupingNode)node;
		}
		return null;
	}

	public GeometryNode getGeometryNode() {
		for (Node node = getChildNodes(); node != null; node = node.next()) {
			if (node.isGeometryNode())
				return (GeometryNode)node;
		}
		return null;
	}

	public TextureNode getTextureNode() {
		for (Node node = getChildNodes(); node != null; node = node.next()) {
			if (node.isTextureNode())
				return (TextureNode)node;
		}
		return null;
	}

	public AnchorNode getAnchorNodes() {
		return (AnchorNode)getChildNodes(anchorTypeName);
	}

	public AppearanceNode getAppearanceNodes() {
		return (AppearanceNode)getChildNodes(appearanceTypeName);
	}

	public AudioClipNode getAudioClipNodes() {
		return (AudioClipNode)getChildNodes(audioClipTypeName);
	}

	public BackgroundNode getBackgroundNodes() {
		return (BackgroundNode)getChildNodes(backgroundTypeName);
	}

	public BillboardNode getBillboardNodes() {
		return (BillboardNode)getChildNodes(billboardTypeName);
	}

	public BoxNode getBoxNodes() {
		return (BoxNode)getChildNodes(boxTypeName);
	}

	public CollisionNode getCollisionNodes() {
		return (CollisionNode)getChildNodes(collisionTypeName);
	}

	public ColorNode getColorNodes() {
		return (ColorNode)getChildNodes(colorTypeName);
	}

	public ColorInterpolatorNode getColorInterpolatorNodes() {
		return (ColorInterpolatorNode)getChildNodes(colorInterpolatorTypeName);
	}

	public ConeNode getConeNodes() {
		return (ConeNode)getChildNodes(coneTypeName);
	}

	public CoordinateNode getCoordinateNodes() {
		return (CoordinateNode)getChildNodes(coordinateTypeName);
	}

	public CoordinateInterpolatorNode getCoordinateInterpolatorNodes() {
		return (CoordinateInterpolatorNode)getChildNodes(coordinateInterpolatorTypeName);
	}

	public CylinderNode getCylinderNodes() {
		return (CylinderNode)getChildNodes(cylinderTypeName);
	}

	public CylinderSensorNode getCylinderSensorNodes() {
		return (CylinderSensorNode)getChildNodes(cylinderSensorTypeName);
	}

	public DirectionalLightNode getDirectionalLightNodes() {
		return (DirectionalLightNode)getChildNodes(directionalLightTypeName);
	}

	public ElevationGridNode getElevationGridNodes() {
		return (ElevationGridNode)getChildNodes(elevationGridTypeName);
	}

	public ExtrusionNode getExtrusionNodes() {
		return (ExtrusionNode)getChildNodes(extrusionTypeName);
	}

	public FogNode getFogNodes() {
		return (FogNode)getChildNodes(fogTypeName);
	}

	public FontStyleNode getFontStyleNodes() {
		return (FontStyleNode)getChildNodes(fontStyleTypeName);
	}

	public GroupNode getGroupNodes() {
		return (GroupNode)getChildNodes(groupTypeName);
	}

	public ImageTextureNode getImageTextureNodes() {
		return (ImageTextureNode)getChildNodes(imageTextureTypeName);
	}

	public IndexedFaceSetNode getIndexedFaceSetNodes() {
		return (IndexedFaceSetNode)getChildNodes(indexedFaceSetTypeName);
	}

	public IndexedLineSetNode getIndexedLineSetNodes() {
		return (IndexedLineSetNode)getChildNodes(indexedLineSetTypeName);
	}

	public InlineNode getInlineNodes() {
		return (InlineNode)getChildNodes(inlineTypeName);
	}

	public LODNode getLODNodes() {
		return (LODNode)getChildNodes(lodTypeName);
	}

	public MaterialNode getMaterialNodes() {
		return (MaterialNode)getChildNodes(materialTypeName);
	}

	public MovieTextureNode getMovieTextureNodes() {
		return (MovieTextureNode)getChildNodes(movieTextureTypeName);
	}

	public NavigationInfoNode getNavigationInfoNodes() {
		return (NavigationInfoNode)getChildNodes(navigationInfoTypeName);
	}

	public NormalNode getNormalNodes() {
		return (NormalNode)getChildNodes(normalTypeName);
	}

	public NormalInterpolatorNode getNormalInterpolatorNodes() {
		return (NormalInterpolatorNode)getChildNodes(normalInterpolatorTypeName);
	}

	public OrientationInterpolatorNode getOrientationInterpolatorNodes() {
		return (OrientationInterpolatorNode)getChildNodes(orientationInterpolatorTypeName);
	}

	public PixelTextureNode getPixelTextureNodes() {
		return (PixelTextureNode)getChildNodes(pixelTextureTypeName);
	}

	public PlaneSensorNode getPlaneSensorNodes() {
		return (PlaneSensorNode)getChildNodes(planeSensorTypeName);
	}

	public PointLightNode getPointLightNodes() {
		return (PointLightNode)getChildNodes(pointLightTypeName);
	}

	public PointSetNode getPointSetNodes() {
		return (PointSetNode)getChildNodes(pointSetTypeName);
	}

	public PositionInterpolatorNode getPositionInterpolatorNodes() {
		return (PositionInterpolatorNode)getChildNodes(positionInterpolatorTypeName);
	}

	public ProximitySensorNode getProximitySensorNodes() {
		return (ProximitySensorNode)getChildNodes(proximitySensorTypeName);
	}

	public ScalarInterpolatorNode getScalarInterpolatorNodes() {
		return (ScalarInterpolatorNode)getChildNodes(scalarInterpolatorTypeName);
	}

	public ScriptNode getScriptNodes() {
		return (ScriptNode)getChildNodes(scriptTypeName);
	}

	public ShapeNode getShapeNodes() {
		return (ShapeNode)getChildNodes(shapeTypeName);
	}

	public SoundNode getSoundNodes() {
		return (SoundNode)getChildNodes(soundTypeName);
	}

	public SphereNode getSphereNodes() {
		return (SphereNode)getChildNodes(sphereTypeName);
	}

	public SphereSensorNode getSphereSensorNodes() {
		return (SphereSensorNode)getChildNodes(sphereSensorTypeName);
	}

	public SpotLightNode getSpotLightNodes() {
		return (SpotLightNode)getChildNodes(spotLightTypeName);
	}

	public SwitchNode getSwitchNodes() {
		return (SwitchNode)getChildNodes(switchTypeName);
	}

	public TextNode getTextNodes() {
		return (TextNode)getChildNodes(textTypeName);
	}

	public TextureCoordinateNode getTextureCoordinateNodes() {
		return (TextureCoordinateNode)getChildNodes(textureCoordinateTypeName);
	}

	public TextureTransformNode getTextureTransformNodes() {
		return (TextureTransformNode)getChildNodes(textureTransformTypeName);
	}

	public TimeSensorNode getTimeSensorNodes() {
		return (TimeSensorNode)getChildNodes(timeSensorTypeName);
	}

	public TouchSensorNode getTouchSensorNodes() {
		return (TouchSensorNode)getChildNodes(touchSensorTypeName);
	}

	public TransformNode getTransformNodes() {
		return (TransformNode)getChildNodes(transformTypeName);
	}

	public ViewpointNode getViewpointNodes() {
		return (ViewpointNode)getChildNodes(viewpointTypeName);
	}

	public VisibilitySensorNode getVisibilitySensorNodes() {
		return (VisibilitySensorNode)getChildNodes(visibilitySensorTypeName);
	}

	public WorldInfoNode getWorldInfoNodes() {
		return (WorldInfoNode)getChildNodes(worldInfoTypeName);
	}

	////////////////////////////////////////////////
	//	get child node list (with name)
	////////////////////////////////////////////////

	public GroupingNode getGroupingNode(String name) {
		for (Node node = getChildNodes(); node != null; node = node.next()) {
			if (node.isGroupingNode() == false)
				continue;
			String nodeName = node.getName();
			if (nodeName == null)
				continue;
			if (nodeName.equals(name) == true)
				return (GroupingNode)node;
		}
		return null;
	}

	public GeometryNode getGeometryNode(String name) {
		for (Node node = getChildNodes(); node != null; node = node.next()) {
			if (node.isGeometryNode() == false)
				continue;
			String nodeName = node.getName();
			if (nodeName == null)
				continue;
			if (nodeName.equals(name) == true)
				return (GeometryNode)node;
		}
		return null;
	}

	public TextureNode getTextureNode(String name) {
		for (Node node = getChildNodes(); node != null; node = node.next()) {
			if (node.isTextureNode() == false)
				continue;
			String nodeName = node.getName();
			if (nodeName == null)
				continue;
			if (nodeName.equals(name) == true)
				return (TextureNode)node;
		}
		return null;
	}

	public AnchorNode getAnchorNode(String name) {
		return (AnchorNode)getChildNodes(anchorTypeName, name);
	}

	public AppearanceNode getAppearanceNode(String name) {
		return (AppearanceNode)getChildNodes(appearanceTypeName, name);
	}

	public AudioClipNode getAudioClipNode(String name) {
		return (AudioClipNode)getChildNodes(audioClipTypeName, name);
	}

	public BackgroundNode getBackgroundNode(String name) {
		return (BackgroundNode)getChildNodes(backgroundTypeName, name);
	}

	public BillboardNode getBillboardNode(String name) {
		return (BillboardNode)getChildNodes(billboardTypeName, name);
	}

	public BoxNode getBoxNode(String name) {
		return (BoxNode)getChildNodes(boxTypeName, name);
	}

	public CollisionNode getCollisionNode(String name) {
		return (CollisionNode)getChildNodes(collisionTypeName, name);
	}

	public ColorNode getColorNode(String name) {
		return (ColorNode)getChildNodes(colorTypeName, name);
	}

	public ColorInterpolatorNode getColorInterpolatorNode(String name) {
		return (ColorInterpolatorNode)getChildNodes(colorInterpolatorTypeName, name);
	}

	public ConeNode getConeNode(String name) {
		return (ConeNode)getChildNodes(coneTypeName, name);
	}

	public CoordinateNode getCoordinateNode(String name) {
		return (CoordinateNode)getChildNodes(coordinateTypeName, name);
	}

	public CoordinateInterpolatorNode getCoordinateInterpolatorNode(String name) {
		return (CoordinateInterpolatorNode)getChildNodes(coordinateInterpolatorTypeName, name);
	}

	public CylinderNode getCylinderNode(String name) {
		return (CylinderNode)getChildNodes(cylinderTypeName, name);
	}

	public CylinderSensorNode getCylinderSensorNode(String name) {
		return (CylinderSensorNode)getChildNodes(cylinderSensorTypeName, name);
	}

	public DirectionalLightNode getDirectionalLightNode(String name) {
		return (DirectionalLightNode)getChildNodes(directionalLightTypeName, name);
	}

	public ElevationGridNode getElevationGridNode(String name) {
		return (ElevationGridNode)getChildNodes(elevationGridTypeName, name);
	}

	public ExtrusionNode getExtrusionNode(String name) {
		return (ExtrusionNode)getChildNodes(extrusionTypeName, name);
	}

	public FogNode getFogNode(String name) {
		return (FogNode)getChildNodes(fogTypeName, name);
	}

	public FontStyleNode getFontStyleNode(String name) {
		return (FontStyleNode)getChildNodes(fontStyleTypeName, name);
	}

	public GroupNode getGroupNode(String name) {
		return (GroupNode)getChildNodes(groupTypeName, name);
	}

	public ImageTextureNode getImageTextureNode(String name) {
		return (ImageTextureNode)getChildNodes(imageTextureTypeName, name);
	}

	public IndexedFaceSetNode getIndexedFaceSetNode(String name) {
		return (IndexedFaceSetNode)getChildNodes(indexedFaceSetTypeName, name);
	}

	public IndexedLineSetNode getIndexedLineSetNode(String name) {
		return (IndexedLineSetNode)getChildNodes(indexedLineSetTypeName, name);
	}

	public InlineNode getInlineNode(String name) {
		return (InlineNode)getChildNodes(inlineTypeName, name);
	}

	public LODNode getLODNode(String name) {
		return (LODNode)getChildNodes(lodTypeName, name);
	}

	public MaterialNode getMaterialNode(String name) {
		return (MaterialNode)getChildNodes(materialTypeName, name);
	}

	public MovieTextureNode getMovieTextureNode(String name) {
		return (MovieTextureNode)getChildNodes(movieTextureTypeName, name);
	}

	public NavigationInfoNode getNavigationInfoNode(String name) {
		return (NavigationInfoNode)getChildNodes(navigationInfoTypeName, name);
	}

	public NormalNode getNormalNode(String name) {
		return (NormalNode)getChildNodes(normalTypeName, name);
	}

	public NormalInterpolatorNode getNormalInterpolatorNode(String name) {
		return (NormalInterpolatorNode)getChildNodes(normalInterpolatorTypeName, name);
	}

	public OrientationInterpolatorNode getOrientationInterpolatorNode(String name) {
		return (OrientationInterpolatorNode)getChildNodes(orientationInterpolatorTypeName, name);
	}

	public PixelTextureNode getPixelTextureNode(String name) {
		return (PixelTextureNode)getChildNodes(pixelTextureTypeName, name);
	}

	public PlaneSensorNode getPlaneSensorNode(String name) {
		return (PlaneSensorNode)getChildNodes(planeSensorTypeName, name);
	}

	public PointLightNode getPointLightNode(String name) {
		return (PointLightNode)getChildNodes(pointLightTypeName, name);
	}

	public PointSetNode getPointSetNode(String name) {
		return (PointSetNode)getChildNodes(pointSetTypeName, name);
	}

	public PositionInterpolatorNode getPositionInterpolatorNode(String name) {
		return (PositionInterpolatorNode)getChildNodes(positionInterpolatorTypeName, name);
	}

	public ProximitySensorNode getProximitySensorNode(String name) {
		return (ProximitySensorNode)getChildNodes(proximitySensorTypeName, name);
	}

	public ScalarInterpolatorNode getScalarInterpolatorNode(String name) {
		return (ScalarInterpolatorNode)getChildNodes(scalarInterpolatorTypeName, name);
	}

	public ScriptNode getScriptNode(String name) {
		return (ScriptNode)getChildNodes(scriptTypeName, name);
	}

	public ShapeNode getShapeNode(String name) {
		return (ShapeNode)getChildNodes(shapeTypeName, name);
	}

	public SoundNode getSoundNode(String name) {
		return (SoundNode)getChildNodes(soundTypeName, name);
	}

	public SphereNode getSphereNode(String name) {
		return (SphereNode)getChildNodes(sphereTypeName, name);
	}

	public SphereSensorNode getSphereSensorNode(String name) {
		return (SphereSensorNode)getChildNodes(sphereSensorTypeName, name);
	}

	public SpotLightNode getSpotLightNode(String name) {
		return (SpotLightNode)getChildNodes(spotLightTypeName, name);
	}

	public SwitchNode getSwitchNode(String name) {
		return (SwitchNode)getChildNodes(switchTypeName, name);
	}

	public TextNode getTextNode(String name) {
		return (TextNode)getChildNodes(textTypeName, name);
	}

	public TextureCoordinateNode getTextureCoordinateNode(String name) {
		return (TextureCoordinateNode)getChildNodes(textureCoordinateTypeName, name);
	}

	public TextureTransformNode getTextureTransformNode(String name) {
		return (TextureTransformNode)getChildNodes(textureTransformTypeName, name);
	}

	public TimeSensorNode getTimeSensorNode(String name) {
		return (TimeSensorNode)getChildNodes(timeSensorTypeName, name);
	}

	public TouchSensorNode getTouchSensorNode(String name) {
		return (TouchSensorNode)getChildNodes(touchSensorTypeName, name);
	}

	public TransformNode getTransformNode(String name) {
		return (TransformNode)getChildNodes(transformTypeName, name);
	}

	public ViewpointNode getViewpointNode(String name) {
		return (ViewpointNode)getChildNodes(viewpointTypeName, name);
	}

	public VisibilitySensorNode getVisibilitySensorNode(String name) {
		return (VisibilitySensorNode)getChildNodes(visibilitySensorTypeName, name);
	}

	public WorldInfoNode getWorldInfoNode(String name) {
		return (WorldInfoNode)getChildNodes(worldInfoTypeName, name);
	}


	////////////////////////////////////////////////
	//	get child node list
	////////////////////////////////////////////////

	public boolean hasGroupingNodes() {
		for (Node node = getChildNodes(); node != null; node = node.next()) {
			if (node.isGroupingNode())
				return true;
		}
		return false;
	}

	public boolean hasGeometryNode() {
		for (Node node = getChildNodes(); node != null; node = node.next()) {
			if (node.isGeometryNode())
				return true;
		}
		return false;
	}

	public boolean hasTextureNode() {
		for (Node node = getChildNodes(); node != null; node = node.next()) {
			if (node.isTextureNode())
				return true;
		}
		return false;
	}

	public boolean hasAnchorNodes() {
		return (getChildNodes(anchorTypeName) != null ? true : false);
	}

	public boolean hasAppearanceNodes() {
		return (getChildNodes(appearanceTypeName) != null ? true : false);
	}

	public boolean hasAudioClipNodes() {
		return (getChildNodes(audioClipTypeName) != null ? true : false);
	}

	public boolean hasBackgroundNodes() {
		return (getChildNodes(backgroundTypeName) != null ? true : false);
	}

	public boolean hasBillboardNodes() {
		return (getChildNodes(billboardTypeName) != null ? true : false);
	}

	public boolean hasBoxNodes() {
		return (getChildNodes(boxTypeName) != null ? true : false);
	}

	public boolean hasCollisionNodes() {
		return (getChildNodes(collisionTypeName) != null ? true : false);
	}

	public boolean hasColorNodes() {
		return (getChildNodes(colorTypeName) != null ? true : false);
	}

	public boolean hasColorInterpolatorNodes() {
		return (getChildNodes(colorInterpolatorTypeName) != null ? true : false);
	}

	public boolean hasConeNodes() {
		return (getChildNodes(coneTypeName) != null ? true : false);
	}

	public boolean hasCoordinateNodes() {
		return (getChildNodes(coordinateTypeName) != null ? true : false);
	}

	public boolean hasCoordinateInterpolatorNodes() {
		return (getChildNodes(coordinateInterpolatorTypeName) != null ? true : false);
	}

	public boolean hasCylinderNodes() {
		return (getChildNodes(cylinderTypeName) != null ? true : false);
	}

	public boolean hasCylinderSensorNodes() {
		return (getChildNodes(cylinderSensorTypeName) != null ? true : false);
	}

	public boolean hasDirectionalLightNodes() {
		return (getChildNodes(directionalLightTypeName) != null ? true : false);
	}

	public boolean hasElevationGridNodes() {
		return (getChildNodes(elevationGridTypeName) != null ? true : false);
	}

	public boolean hasExtrusionNodes() {
		return (getChildNodes(extrusionTypeName) != null ? true : false);
	}

	public boolean hasFogNodes() {
		return (getChildNodes(fogTypeName) != null ? true : false);
	}

	public boolean hasFontStyleNodes() {
		return (getChildNodes(fontStyleTypeName) != null ? true : false);
	}

	public boolean hasGroupNodes() {
		return (getChildNodes(groupTypeName) != null ? true : false);
	}

	public boolean hasImageTextureNodes() {
		return (getChildNodes(imageTextureTypeName) != null ? true : false);
	}

	public boolean hasIndexedFaceSetNodes() {
		return (getChildNodes(indexedFaceSetTypeName) != null ? true : false);
	}

	public boolean hasIndexedLineSetNodes() {
		return (getChildNodes(indexedLineSetTypeName) != null ? true : false);
	}

	public boolean hasInlineNodes() {
		return (getChildNodes(inlineTypeName) != null ? true : false);
	}

	public boolean hasLODNodes() {
		return (getChildNodes(lodTypeName) != null ? true : false);
	}

	public boolean hasMaterialNodes() {
		return (getChildNodes(materialTypeName) != null ? true : false);
	}

	public boolean hasMovieTextureNodes() {
		return (getChildNodes(movieTextureTypeName) != null ? true : false);
	}

	public boolean hasNavigationInfoNodes() {
		return (getChildNodes(navigationInfoTypeName) != null ? true : false);
	}

	public boolean hasNormalNodes() {
		return (getChildNodes(normalTypeName) != null ? true : false);
	}

	public boolean hasNormalInterpolatorNodes() {
		return (getChildNodes(normalInterpolatorTypeName) != null ? true : false);
	}

	public boolean hasOrientationInterpolatorNodes() {
		return (getChildNodes(orientationInterpolatorTypeName) != null ? true : false);
	}

	public boolean hasPixelTextureNodes() {
		return (getChildNodes(pixelTextureTypeName) != null ? true : false);
	}

	public boolean hasPlaneSensorNodes() {
		return (getChildNodes(planeSensorTypeName) != null ? true : false);
	}

	public boolean hasPointLightNodes() {
		return (getChildNodes(pointLightTypeName) != null ? true : false);
	}

	public boolean hasPointSetNodes() {
		return (getChildNodes(pointSetTypeName) != null ? true : false);
	}

	public boolean hasPositionInterpolatorNodes() {
		return (getChildNodes(positionInterpolatorTypeName) != null ? true : false);
	}

	public boolean hasProximitySensorNodes() {
		return (getChildNodes(proximitySensorTypeName) != null ? true : false);
	}

	public boolean hasScalarInterpolatorNodes() {
		return (getChildNodes(scalarInterpolatorTypeName) != null ? true : false);
	}

	public boolean hasScriptNodes() {
		return (getChildNodes(scriptTypeName) != null ? true : false);
	}

	public boolean hasShapeNodes() {
		return (getChildNodes(shapeTypeName) != null ? true : false);
	}

	public boolean hasSoundNodes() {
		return (getChildNodes(soundTypeName) != null ? true : false);
	}

	public boolean hasSphereNodes() {
		return (getChildNodes(sphereTypeName) != null ? true : false);
	}

	public boolean hasSphereSensorNodes() {
		return (getChildNodes(sphereSensorTypeName) != null ? true : false);
	}

	public boolean hasSpotLightNodes() {
		return (getChildNodes(spotLightTypeName) != null ? true : false);
	}

	public boolean hasSwitchNodes() {
		return (getChildNodes(switchTypeName) != null ? true : false);
	}

	public boolean hasTextNodes() {
		return (getChildNodes(textTypeName) != null ? true : false);
	}

	public boolean hasTextureCoordinateNodes() {
		return (getChildNodes(textureCoordinateTypeName) != null ? true : false);
	}

	public boolean hasTextureTransformNodes() {
		return (getChildNodes(textureTransformTypeName) != null ? true : false);
	}

	public boolean hasTimeSensorNodes() {
		return (getChildNodes(timeSensorTypeName) != null ? true : false);
	}

	public boolean hasTouchSensorNodes() {
		return (getChildNodes(touchSensorTypeName) != null ? true : false);
	}

	public boolean hasTransformNodes() {
		return (getChildNodes(transformTypeName) != null ? true : false);
	}

	public boolean hasViewpointNodes() {
		return (getChildNodes(viewpointTypeName) != null ? true : false);
	}

	public boolean hasVisibilitySensorNodes() {
		return (getChildNodes(visibilitySensorTypeName) != null ? true : false);
	}

	public boolean hasWorldInfoNodes() {
		return (getChildNodes(worldInfoTypeName) != null ? true : false);
	}

	////////////////////////////////////////////////
	//	is(XML|VRML|X3D)Node
	////////////////////////////////////////////////
	
	public boolean isXMLNode() {
		return isNode(genericXMLNodeTypeName);
	}
	
	public boolean isVRMLNode() {
		return !isXMLNode();
	}

	public boolean isX3DNode() {
		return !isXMLNode();
	}

	////////////////////////////////////////////////
	//	
	////////////////////////////////////////////////

	public boolean isNode(String nodeType) {
		String nodeString = getType();
		if (nodeString.compareTo(nodeType) == 0)
			return true;
		else
			return false;
	}

	public boolean isGroupingNode() {
		if (isAnchorNode() || isBillboardNode() || isCollisionNode() || isGroupNode() || isTransformNode() || isInlineNode() || isSwitchNode())
			return true;
		else
			return false;
	}

	public boolean isSpecialGroupNode() {
		if (isInlineNode() || isLODNode() || isSwitchNode())
			return true;
		else
			return false;
	}

	public boolean isCommonNode() {
		if (isLightNode() || isAudioClipNode() || isScriptNode() || isShapeNode() || isSoundNode() || isWorldInfoNode())
			return true;
		else
			return false;
	}

	public boolean isLightNode() {
		if (isDirectionalLightNode() || isSpotLightNode() || isPointLightNode())
			return true;
		else
			return false;
	}

	public boolean isGeometryNode() {
		if (isBoxNode() || isConeNode() || isCylinderNode() || isElevationGridNode() || isExtrusionNode() || isIndexedFaceSetNode() || isIndexedLineSetNode() || isPointSetNode() || isSphereNode() || isTextNode())
			return true;
		else
			return false;
	}

	public boolean isGeometryPropertyNode() {
		if (isColorNode() || isCoordinateNode() || isNormalNode() || isTextureCoordinateNode())
			return true;
		else
			return false;
	}

	public boolean isTextureNode() {
		if (isMovieTextureNode() || isPixelTextureNode() || isImageTextureNode() )
			return true;
		else
			return false;
	}

	public boolean isSensorNode() {
		if (isCylinderSensorNode() || isPlaneSensorNode() || isSphereSensorNode() || isProximitySensorNode() || isTimeSensorNode() || isTouchSensorNode() || isVisibilitySensorNode())
			return true;
		else
			return false;
	}

	public boolean isInterpolatorNode() {
		if (isColorInterpolatorNode() || isCoordinateInterpolatorNode() || isNormalInterpolatorNode() || isOrientationInterpolatorNode() || isPositionInterpolatorNode() || isScalarInterpolatorNode())
			return true;
		else
			return false;
	}

	public boolean isAppearanceInfoNode() {
		if (isAppearanceNode() || isFontStyleNode() || isMaterialNode() || isTextureTransformNode() || isTextureNode())
			return true;
		else
			return false;
	}

	public boolean isBindableNode() {
		if (isBackgroundNode() || isFogNode() || isNavigationInfoNode() || isViewpointNode())
			return true;
		else
			return false;
	}


	public boolean isRootNode() {
		return isNode(rootTypeName);
	}
	
	public boolean isAnchorNode() {
		return isNode(anchorTypeName);
	}

	public boolean isAppearanceNode() {
		return isNode(appearanceTypeName);
	}

	public boolean isAudioClipNode() {
		return isNode(audioClipTypeName);
	}

	public boolean isBackgroundNode() {
		return isNode(backgroundTypeName);
	}

	public boolean isBillboardNode() {
		return isNode(billboardTypeName);
	}

	public boolean isBoxNode() {
		return isNode(boxTypeName);
	}

	public boolean isCollisionNode() {
		return isNode(collisionTypeName);
	}

	public boolean isColorNode() {
		return isNode(colorTypeName);
	}

	public boolean isColorInterpolatorNode() {
		return isNode(colorInterpolatorTypeName);
	}

	public boolean isConeNode() {
		return isNode(coneTypeName);
	}

	public boolean isCoordinateNode() {
		return isNode(coordinateTypeName);
	}

	public boolean isCoordinateInterpolatorNode() {
		return isNode(coordinateInterpolatorTypeName);
	}

	public boolean isCylinderNode() {
		return isNode(cylinderTypeName);
	}

	public boolean isCylinderSensorNode() {
		return isNode(cylinderSensorTypeName);
	}

	public boolean isDirectionalLightNode() {
		return isNode(directionalLightTypeName);
	}

	public boolean isElevationGridNode() {
		return isNode(elevationGridTypeName);
	}

	public boolean isExtrusionNode() {
		return isNode(extrusionTypeName);
	}

	public boolean isFogNode() {
		return isNode(fogTypeName);
	}

	public boolean isFontStyleNode() {
		return isNode(fontStyleTypeName);
	}

	public boolean isGroupNode() {
		return isNode(groupTypeName);
	}

	public boolean isImageTextureNode() {
		return isNode(imageTextureTypeName);
	}

	public boolean isIndexedFaceSetNode() {
		return isNode(indexedFaceSetTypeName);
	}

	public boolean isIndexedLineSetNode() {
		return isNode(indexedLineSetTypeName);
	}

	public boolean isInlineNode() {
		return isNode(inlineTypeName);
	}

	public boolean isLODNode() {
		return isNode(lodTypeName);
	}

	public boolean isMaterialNode() {
		return isNode(materialTypeName);
	}

	public boolean isMovieTextureNode() {
		return isNode(movieTextureTypeName);
	}

	public boolean isNavigationInfoNode() {
		return isNode(navigationInfoTypeName);
	}

	public boolean isNormalNode() {
		return isNode(normalTypeName);
	}

	public boolean isNormalInterpolatorNode() {
		return isNode(normalInterpolatorTypeName);
	}

	public boolean isOrientationInterpolatorNode() {
		return isNode(orientationInterpolatorTypeName);
	}

	public boolean isPixelTextureNode() {
		return isNode(pixelTextureTypeName);
	}

	public boolean isPlaneSensorNode() {
		return isNode(planeSensorTypeName);
	}

	public boolean isPointLightNode() {
		return isNode(pointLightTypeName);
	}

	public boolean isPointSetNode() {
		return isNode(pointSetTypeName);
	}

	public boolean isPositionInterpolatorNode() {
		return isNode(positionInterpolatorTypeName);
	}

	public boolean isProximitySensorNode() {
		return isNode(proximitySensorTypeName);
	}

	public boolean isScalarInterpolatorNode() {
		return isNode(scalarInterpolatorTypeName);
	}

	public boolean isScriptNode() {
		return isNode(scriptTypeName);
	}

	public boolean isShapeNode() {
		return isNode(shapeTypeName);
	}

	public boolean isSoundNode() {
		return isNode(soundTypeName);
	}

	public boolean isSphereNode() {
		return isNode(sphereTypeName);
	}

	public boolean isSphereSensorNode() {
		return isNode(sphereSensorTypeName);
	}

	public boolean isSpotLightNode() {
		return isNode(spotLightTypeName);
	}

	public boolean isSwitchNode() {
		return isNode(switchTypeName);
	}

	public boolean isTextNode() {
		return isNode(textTypeName);
	}

	public boolean isTextureCoordinateNode() {
		return isNode(textureCoordinateTypeName);
	}

	public boolean isTextureTransformNode() {
		return isNode(textureTransformTypeName);
	}

	public boolean isTimeSensorNode() {
		return isNode(timeSensorTypeName);
	}

	public boolean isTouchSensorNode() {
		return isNode(touchSensorTypeName);
	}

	public boolean isTransformNode() {
		return isNode(transformTypeName);
	}

	public boolean isViewpointNode() {
		return isNode(viewpointTypeName);
	}

	public boolean isVisibilitySensorNode() {
		return isNode(visibilitySensorTypeName);
	}

	public boolean isWorldInfoNode() {
		return isNode(worldInfoTypeName);
	}

	////////////////////////////////////////////////
	//	output
	////////////////////////////////////////////////

	public String getSpaceString(int nSpaces) {
		StringBuffer str = new StringBuffer();
		for (int n=0; n<nSpaces; n++)
			str.append(' ');
		return str.toString();
	}

	public String getIndentLevelString(int nIndentLevel) {
		char indentString[] = new char[nIndentLevel];
		for (int n=0; n<nIndentLevel; n++)
			indentString[n] = '\t' ;
		return new String(indentString);
	}

	public void outputHead(PrintWriter ps, String indentString) {
		String nodeName = getName();
		if (nodeName != null && 0 < nodeName.length())
			ps.println(indentString + "DEF " + nodeName + " " + getType() + " {");
		else
			ps.println(indentString +  getType() + " {");
	}

	public void outputTail(PrintWriter ps, String indentString) {
		ps.println(indentString + "}");
	}

	////////////////////////////////////////////////
	//	output
	////////////////////////////////////////////////

	abstract public void outputContext(PrintWriter ps, String indentString);

	public void output(PrintWriter ps, int indentLevel) {

		String indentString = getIndentLevelString(indentLevel);

		if (isInstanceNode() == false) {

			outputHead(ps, indentString);
			outputContext(ps, indentString);
	
			if (!isElevationGridNode() && !isShapeNode() && !isSoundNode() && !isPointSetNode() && !isIndexedFaceSetNode() && 
				!isIndexedLineSetNode() && !isTextNode() && !isAppearanceNode() && !isScriptNode()) {
				if (getChildNodes() != null) {
					if (isLODNode()) 
						ps.println(indentString + "\tlevel [");
					else if (isSwitchNode()) 
						ps.println(indentString + "\tchoice [");
					else
						ps.println(indentString + "\tchildren [");
			
					for (Node cnode = getChildNodes(); cnode != null; cnode = cnode.next()) {
						cnode.output(ps, indentLevel+2);
					}
			
					ps.println(indentString + "\t]");
				}
			}
			outputTail(ps, indentString);
		}
		else
			ps.println(indentString + "USE " + getName());
	}

	public void save(FileOutputStream outputStream){
		PrintWriter pr = new PrintWriter(outputStream);
		output(pr, 0);
		pr.close();
	}

	public void save(String filename) {
		try {
			FileOutputStream outputStream = new FileOutputStream(filename);
			save(outputStream);
			outputStream.close();
		}
		catch (IOException e) {
			System.out.println("Couldn't open the file (" + filename + ")");
		}
	}

	////////////////////////////////////////////////
	//	output XML
	////////////////////////////////////////////////

	public boolean hasString(String str) 
	{
		if (str == null)
			return false;
		if (str.length() <= 0)
			return false;
		return true;
	}

	public boolean isOutputXMLField(Field field) 
	{
		if (field instanceof SFNode || field instanceof MFNode)
			return false;
		return true;
	}
	
	public void outputXMLField(PrintWriter ps, Field field, int indentLevel) 
	{
//		String indentString		= getIndentLevelString(indentLevel) + " ";
		String indentString		= getIndentLevelString(indentLevel+1);
		String fieldName			= field.getName();
		String tagString 			= indentString + fieldName + "=\"";
		String tagSpaceString	= indentString + getSpaceString(fieldName.length()+2);

		if (isOutputXMLField(field) == false)
			return;
					
		if (field.isSingleField() == true) {
			ps.print(tagString + field.toXMLString() + "\"");
			return;
		}
		
		if (field instanceof MField) {
			MField	mfield			= (MField)field;
			int		fieldSize 		= mfield.getSize();

			if (fieldSize == 0) {
				ps.print(tagString + "\"");
				return;
			}
			
			for (int n=0; n<fieldSize; n++) {
				if (n==0)
					ps.print(tagString);
				else
					ps.print(tagSpaceString);
					
				Field		eleField 	= (Field)mfield.get(n);
				String	eleString	= eleField.toXMLString();
				
				ps.print(eleString);
				
				if (n < (fieldSize-1)) {
					if (mfield.isSingleValueMField() == true)
						ps.println("");
					else
						ps.println(",");
				}
				else
					ps.print("\"");
			}
			return;
		}

		if (field instanceof ConstMField) {
			MField	mfield			= (MField)field;
			int		fieldSize 		= mfield.getSize();
			
			if (fieldSize == 0) {
				ps.print(tagString + "\"");
				return;
			}
			
			for (int n=0; n<fieldSize; n++) {
				if (n==0)
					ps.print(tagString);
				else
					ps.print(tagSpaceString);
					
				Field		eleField 	= (Field)mfield.get(n);
				String	eleString	= eleField.toXMLString();
				
				ps.print(eleString);
				
				if (n < (fieldSize-1)) {
					if (mfield.isSingleValueMField() == true)
						ps.println("");
					else
						ps.println(",");
				}
				else
					ps.print("\"");
			}
			return;
		}

	}
	
	public void outputXML(PrintWriter ps, int indentLevel) 
	{
		String indentString = getIndentLevelString(indentLevel);

		String tagName;

		if (isInstanceNode() == true) {
			String typeName = getType();
			String nodeName = getName(); 			
			ps.println(indentString + "<" + typeName + " USE=\"" + nodeName + "\"/>");
			return;
		}
				
		if (isVRMLNode() == true) {
			tagName = getType();
			String nodeName	= getName(); 			
			if (hasString(nodeName) == true)
				ps.print(indentString + "<" + tagName + " DEF=\"" + nodeName + "\"");
			else
				ps.print(indentString + "<" + tagName);
		}
		else {
			tagName = getName();
			ps.print(indentString + "<" + tagName);
		}
		
		int fieldSize			= getNFields();
		int exposedfieldSize	= getNExposedFields();
		int eventInSize		= getNEventIn();
		int eventOutSize		= getNEventOut();
		
		for (int n=0; n<fieldSize; n++) {
			Field field = getField(n);
			if (isOutputXMLField(field) == false)
				continue;
			ps.println("");
			outputXMLField(ps, field, indentLevel);
		}

		for (int n=0; n<exposedfieldSize; n++) {
			Field field = getExposedField(n);
			if (isOutputXMLField(field) == false)
				continue;
			ps.println("");
			outputXMLField(ps, field, indentLevel);
		}

		for (int n=0; n<eventInSize; n++) {
			Field field = getEventIn(n);
			if (isOutputXMLField(field) == false)
				continue;
			ps.println("");
			outputXMLField(ps, field, indentLevel);
		}

		for (int n=0; n<eventOutSize; n++) {
			Field field = getEventOut(n);
			if (isOutputXMLField(field) == false)
				continue;
			ps.println("");
			outputXMLField(ps, field, indentLevel);
		}
		
		if (hasChildNodes() == false) {
			ps.println("/>");
			return;
		}

		ps.println(">");
		
		for (Node cnode = getChildNodes(); cnode != null; cnode = cnode.next())
			cnode.outputXML(ps, indentLevel+1);

		ps.println(indentString + "</" + tagName + ">");
	}

	public void saveXML(FileOutputStream outputStream){
		PrintWriter pr = new PrintWriter(outputStream);
		outputXML(pr, 0);
		pr.close();
	}

	public void saveXML(String filename) {
		try {
			FileOutputStream outputStream = new FileOutputStream(filename);
			save(outputStream);
			outputStream.close();
		}
		catch (IOException e) {
			System.out.println("Couldn't open the file (" + filename + ")");
		}
	}

	////////////////////////////////////////////////
	//	getTransformMatrix
	////////////////////////////////////////////////

	public void getTransformMatrix(SFMatrix mxOut) {
		mxOut.init();
		for (Node node=this; node != null ; node=node.getParentNode()) {
			if (node.isTransformNode()) {
				SFMatrix mxTransform = ((TransformNode)node).getSFMatrix();
				mxTransform.add(mxOut);
				mxOut.setValue(mxTransform);
			}
			else if (node.isBillboardNode()) {
				SFMatrix mxBillboard = ((BillboardNode)node).getSFMatrix();
				mxBillboard.add(mxOut);
				mxOut.setValue(mxBillboard);
			}
		}
	}

	public SFMatrix getTransformMatrix() {
		SFMatrix	mx = new SFMatrix();
		getTransformMatrix(mx);
		return mx;
	}

	public void getTransformMatrix(float value[][]) {
		SFMatrix	mx = new SFMatrix();
		getTransformMatrix(mx);
		mx.getValue(value);
	}

	////////////////////////////////////////////////
	//	SceneGraph
	////////////////////////////////////////////////

	public void setSceneGraph(SceneGraph sceneGraph) {
		mSceneGraph = sceneGraph;
		for (Node node = getChildNodes(); node != null; node = node.next())
			node.setSceneGraph(sceneGraph);
	}
	
	public SceneGraph getSceneGraph() {
		return mSceneGraph;
	}

	////////////////////////////////////////////////
	//	Route
	////////////////////////////////////////////////

	public void sendEvent(Field eventOutField) {
		SceneGraph sg = getSceneGraph();
		if (sg != null)
			sg.updateRoute(this, eventOutField);
	}

	////////////////////////////////////////////////
	//	Initialized
	////////////////////////////////////////////////

	public void setInitializationFlag(boolean flag) {
		mInitializationFlag = flag; 
	}

	public boolean isInitialized() {
		return mInitializationFlag; 
	}

	////////////////////////////////////////////////
	//	user data
	////////////////////////////////////////////////

	public void setData(Object data) {
		mUserData = data;
	}

	public Object getData() {
		return mUserData;
	}

	////////////////////////////////////////////////
	//	Instance node
	////////////////////////////////////////////////

	public boolean isInstanceNode() {
		return (getReferenceNode() != null ? true : false);
	}

	public void setReferenceNodeMember(Node node) {
		if (node == null)
			return;
			
		mName					= node.mName;

		mField					= node.mField;
		mPrivateField			= node.mPrivateField;
		mExposedField			= node.mExposedField;
		mEventInField			= node.mEventInField;
		mEventOutField			= node.mEventOutField;
		mInitializationFlag		= node.mInitializationFlag;
		mObject					= node.mObject;
		mThreadObject			= node.mThreadObject;
	}
	
	public void setReferenceNode(Node node) {
		mReferenceNode = node;
	}
	
	public Node getReferenceNode() {
		return mReferenceNode;
	}

	public void setAsInstanceNode(Node node) {
		setReferenceNode(node);
		setReferenceNodeMember(node);
	}
	
	public Node createInstanceNode() {
		Node instanceNode = null;
		
		if (isAnchorNode())
			instanceNode = new AnchorNode();
		else if (isAppearanceNode()) 
			instanceNode = new AppearanceNode();
		else if (isAudioClipNode())
			instanceNode = new AudioClipNode();
		else if (isBackgroundNode())
			instanceNode = new BackgroundNode();
		else if (isBillboardNode())
			instanceNode = new BillboardNode();
		else if (isBoxNode())
			instanceNode = new BoxNode();
		else if (isCollisionNode())
			instanceNode = new CollisionNode();
		else if (isColorNode())
			instanceNode = new ColorNode();
		else if (isColorInterpolatorNode())
			instanceNode = new ColorInterpolatorNode();
		else if (isConeNode())
			instanceNode = new ConeNode();
		else if (isCoordinateNode())
			instanceNode = new CoordinateNode();
		else if (isCoordinateInterpolatorNode())
			instanceNode = new CoordinateInterpolatorNode();
		else if (isCylinderNode())
			instanceNode = new CylinderNode();
		else if (isCylinderSensorNode())
			instanceNode = new CylinderSensorNode();
		else if (isDirectionalLightNode())
			instanceNode = new DirectionalLightNode();
		else if (isElevationGridNode())
			instanceNode = new ElevationGridNode();
		else if (isExtrusionNode())
			instanceNode = new ExtrusionNode();
		else if (isFogNode())
			instanceNode = new FogNode();
		else if (isFontStyleNode())
			instanceNode = new FontStyleNode();
		else if (isGroupNode())
			instanceNode = new GroupNode();
		else if (isImageTextureNode())
			instanceNode = new ImageTextureNode();
		else if (isIndexedFaceSetNode())
			instanceNode = new IndexedFaceSetNode();
		else if (isIndexedLineSetNode()) 
			instanceNode = new IndexedLineSetNode();
		else if (isInlineNode()) 
			instanceNode = new InlineNode();
		else if (isLODNode())
			instanceNode = new LODNode();
		else if (isMaterialNode())
			instanceNode = new MaterialNode();
		else if (isMovieTextureNode())
			instanceNode = new MovieTextureNode();
		else if (isNavigationInfoNode())
			instanceNode = new NavigationInfoNode();
		else if (isNormalNode())
			instanceNode = new NormalNode();
		else if (isNormalInterpolatorNode())
			instanceNode = new NormalInterpolatorNode();
		else if (isOrientationInterpolatorNode())
			instanceNode = new OrientationInterpolatorNode();
		else if (isPixelTextureNode())
			instanceNode = new PixelTextureNode();
		else if (isPlaneSensorNode())
			instanceNode = new PlaneSensorNode();
		else if (isPointLightNode())
			instanceNode = new PointLightNode();
		else if (isPointSetNode())
			instanceNode = new PointSetNode();
		else if (isPositionInterpolatorNode())
			instanceNode = new PositionInterpolatorNode();
		else if (isProximitySensorNode())
			instanceNode = new ProximitySensorNode();
		else if (isScalarInterpolatorNode())
			instanceNode = new ScalarInterpolatorNode();
		else if (isScriptNode())
			instanceNode = new ScriptNode();
		else if (isShapeNode())
			instanceNode = new ShapeNode();
		else if (isSoundNode())
			instanceNode = new SoundNode();
		else if (isSphereNode())
			instanceNode = new SphereNode();
		else if (isSphereSensorNode())
			instanceNode = new SphereSensorNode();
		else if (isSpotLightNode())
			instanceNode = new SpotLightNode();
		else if (isSwitchNode())
			instanceNode = new SwitchNode();
		else if (isTextNode())
			instanceNode = new TextNode();
		else if (isTextureCoordinateNode())
			instanceNode = new TextureCoordinateNode();
		else if (isTextureTransformNode())
			instanceNode = new TextureTransformNode();
		else if (isTimeSensorNode())
			instanceNode = new TimeSensorNode();
		else if (isTouchSensorNode())
			instanceNode = new TouchSensorNode();
		else if (isTransformNode())
			instanceNode = new TransformNode();
		else if (isViewpointNode())
			instanceNode = new ViewpointNode();
		else if (isVisibilitySensorNode())
			instanceNode = new VisibilitySensorNode();
		else if (isWorldInfoNode())
			instanceNode = new WorldInfoNode();
			
		if (instanceNode != null) {
			instanceNode.setAsInstanceNode(this);
			for (Node cnode=getChildNodes(); cnode != null; cnode = cnode.next()) {
				Node childInstanceNode = cnode.createInstanceNode();
				instanceNode.addChildNode(childInstanceNode);
			}
		}		
		else
			System.out.println("Node::createInstanceNode : this = " + this + ", instanceNode = null");
		
		return instanceNode;
	}

	////////////////////////////////////////////////
	//	Node Object
	////////////////////////////////////////////////
	
	public void setObject(NodeObject object) {
		mObject = object;
	}
	
	public NodeObject getObject() {
		return mObject;
	}
	
	public boolean hasObject() {
		return (mObject != null ? true : false);
	}
	
	public boolean initializeObject() {
		if (hasObject() == true) {
			boolean ret;
			synchronized (mObject) {
				ret = mObject.initialize(this);
			}
			return ret;
		}
		return false;
	}
	
	public boolean uninitializeObject() {
		if (hasObject() == true) {
			boolean ret;
			synchronized (mObject) {
				ret = mObject.uninitialize(this);
			}
			return ret;
		}
		return false;
	}
	
	public boolean updateObject() {
		if (hasObject() == true) {
			boolean ret;
			synchronized (mObject) {
				ret = mObject.update(this);
			}
			return ret;
		}
		return false;
	}
	
	public boolean addObject() {
		Debug.message("Node::addObject = " + this + ", " + mObject);
		if (isRootNode() == true) {
			Debug.warning("\tThis node is a root node !!");
			return false;
		}
		if (hasObject() == true)
			return mObject.add(this);
		return false;
	}

	public boolean removeObject() {
		Debug.message("Node::addObject = " + this + ", " + mObject);
		if (isRootNode() == true) {
			Debug.warning("\tThis node is a root node !!");
			return false;
		}
		if (hasObject() == true)
			return mObject.remove(this);
		return false;
	}

	////////////////////////////////////////////////
	//	Runnable
	////////////////////////////////////////////////
	
	public void setRunnable(boolean value) {
		mRunnable = value;
	}
	
	public boolean isRunnable() {
		if (isInstanceNode() == true)
			return false;
		return mRunnable;
	}
	
	public void setRunnableType(int type) {
		mRunnableType = type;
	}

	public int getRunnableType() {
		return mRunnableType;
	}

	public void setRunnableIntervalTime(int time) {
		mRunnableIntervalTime = time;
	}

	public int getRunnableIntervalTime() {
		return mRunnableIntervalTime;
	}
				
	public void setThreadObject(Thread obj) {
		mThreadObject = obj;
	}

	public Thread getThreadObject() {
		return mThreadObject;
	}

	////////////////////////////////////////////////
	//	Thread
	////////////////////////////////////////////////

	public void run() {
		while (true) {
			update();
			updateObject();
			Thread threadObject = getThreadObject();
			if (threadObject != null) { 
//				threadObject.yield();
				try {
					threadObject.sleep(getRunnableIntervalTime());
				} catch (InterruptedException e) {}
			}
		}
	}
	
	public void start() {
		Thread threadObject = getThreadObject();
		if (threadObject == null) {
			threadObject = new Thread(this);
			setThreadObject(threadObject);
			threadObject.start();
		}
	}
	
	public void stop() {
		Thread threadObject = getThreadObject();
		if (threadObject != null) {
			//threadObject.destroy();
			threadObject.stop();
			setThreadObject(null);
		}
	}

	////////////////////////////////////////////////
	//	Field Operation
	////////////////////////////////////////////////

	protected void setFieldValues(Node node) {
		Field	thisField, nodeField;
		
		int nNodeFields = node.getNFields();
		for (int n=0; n<nNodeFields; n++) {
			nodeField = node.getField(n);
			thisField = getField(nodeField.getName());
			if (thisField != null)
				thisField.setValue(nodeField);
		}

		int nNodePrivateFields = node.getNPrivateFields();
		for (int n=0; n<nNodePrivateFields; n++) {
			nodeField = node.getPrivateField(n);
			thisField = getPrivateField(nodeField.getName());
			if (thisField != null)
				thisField.setValue(nodeField);
		}

		int nNodeExposedFields = node.getNExposedFields();
		for (int n=0; n<nNodeExposedFields; n++) {
			nodeField = node.getExposedField(n);
			thisField = getExposedField(nodeField.getName());
			if (thisField != null)
				thisField.setValue(nodeField);
		}

		int nNodeEventIn = node.getNEventIn();
		for (int n=0; n<nNodeEventIn; n++) {
			nodeField = node.getEventIn(n);
			thisField = getEventIn(nodeField.getName());
			if (thisField != null)
				thisField.setValue(nodeField);
		}

		int nNodeEventOut = node.getNEventOut();
		for (int n=0; n<nNodeEventOut; n++) {
			nodeField = node.getEventOut(n);
			thisField = getEventOut(nodeField.getName());
			if (thisField != null)
				thisField.setValue(nodeField);
		}
	}
	
	private Field createCopyField(Field field) {
		switch (field.getType()) {
		// Field
		case fieldTypeSFBool			: return new SFBool((SFBool)field);
		case fieldTypeSFColor		: return new SFColor((SFColor)field);
		case fieldTypeSFFloat		: return new SFFloat((SFFloat)field);
		case fieldTypeSFInt32		: return new SFInt32((SFInt32)field);
		case fieldTypeSFRotation	: return new SFRotation((SFRotation)field);
		case fieldTypeSFString		: return new SFString((SFString)field);
		case fieldTypeSFTime			: return new SFTime((SFTime)field);
	  	case fieldTypeSFVec2f		: return new SFVec2f((SFVec2f)field);
	  	case fieldTypeSFVec3f		: return new SFVec3f((SFVec3f)field);
	  	case fieldTypeSFNode			: return new SFNode((SFNode)field);
		//case fieldTypeMFBool		: return new MFBool((MFBool)field);
		case fieldTypeMFColor		: return new MFColor((MFColor)field);
		case fieldTypeMFFloat		: return new MFFloat((MFFloat)field);
		case fieldTypeMFInt32		: return new MFInt32((MFInt32)field);
		case fieldTypeMFRotation	: return new MFRotation((MFRotation)field);
		case fieldTypeMFString		: return new MFString((MFString)field);
		case fieldTypeMFTime			: return new MFTime((MFTime)field);
	  	case fieldTypeMFVec2f		: return new MFVec2f((MFVec2f)field);
	  	case fieldTypeMFVec3f		: return new MFVec3f((MFVec3f)field);
	  	case fieldTypeMFNode			: return new MFNode((MFNode)field);
		// ConstField
		case fieldTypeConstSFBool		: return new ConstSFBool((ConstSFBool)field);
		case fieldTypeConstSFColor		: return new ConstSFColor((ConstSFColor)field);
		case fieldTypeConstSFFloat		: return new ConstSFFloat((ConstSFFloat)field);
		case fieldTypeConstSFInt32		: return new ConstSFInt32((ConstSFInt32)field);
		case fieldTypeConstSFRotation	: return new ConstSFRotation((ConstSFRotation)field);
		case fieldTypeConstSFString	: return new ConstSFString((ConstSFString)field);
		case fieldTypeConstSFTime		: return new ConstSFTime((ConstSFTime)field);
	  	case fieldTypeConstSFVec2f		: return new ConstSFVec2f((ConstSFVec2f)field);
	  	case fieldTypeConstSFVec3f		: return new ConstSFVec3f((ConstSFVec3f)field);
	  	case fieldTypeConstSFNode		: return new ConstSFNode((ConstSFNode)field);
		//case fieldTypeConstMFBool	: return new ConstMFBool((ConstMFBool)field);
		case fieldTypeConstMFColor		: return new ConstMFColor((ConstMFColor)field);
		case fieldTypeConstMFFloat		: return new ConstMFFloat((ConstMFFloat)field);
		case fieldTypeConstMFInt32		: return new ConstMFInt32((ConstMFInt32)field);
		case fieldTypeConstMFRotation	: return new ConstMFRotation((ConstMFRotation)field);
		case fieldTypeConstMFString	: return new ConstMFString((ConstMFString)field);
		case fieldTypeConstMFTime		: return new ConstMFTime((ConstMFTime)field);
	  	case fieldTypeConstMFVec2f		: return new ConstMFVec2f((ConstMFVec2f)field);
	  	case fieldTypeConstMFVec3f		: return new ConstMFVec3f((ConstMFVec3f)field);
	  	case fieldTypeConstMFNode		: return new ConstMFNode((ConstMFNode)field);
		}
		Debug.warning("Node.createCopyField");
		Debug.warning("\tCouldn't create a copy field of " + field);
		return null;
	}

	public Field createFieldFromString(String fieldType) {

		if (fieldType.compareTo("SFBool") == 0)
			return new SFBool(true);
		else if (fieldType.compareTo("SFColor") == 0)
			return new SFColor(0.0f, 0.0f, 0.0f);
		else if (fieldType.compareTo("SFFloat") == 0)
			return new SFFloat(0.0f);
		else if (fieldType.compareTo("SFInt32") == 0)
			return new SFInt32(0);
		else if (fieldType.compareTo("SFRotation") == 0)
			return new SFRotation(0.0f, 0.0f, 1.0f, 0.0f);
		else if (fieldType.compareTo("SFString") == 0)
			return new SFString();
		else if (fieldType.compareTo("SFTime") == 0)
			return new SFTime(0.0);
		else if (fieldType.compareTo("SFVec2f") == 0)
			return new SFVec2f(0.0f, 0.0f);
		else if (fieldType.compareTo("SFVec3f") == 0)
			return new SFVec3f(0.0f, 0.0f, 0.0f);

		if (fieldType.compareTo("ConstSFBool") == 0)
			return new ConstSFBool(true);
		else if (fieldType.compareTo("ConstSFColor") == 0)
			return new ConstSFColor(0.0f, 0.0f, 0.0f);
		else if (fieldType.compareTo("ConstSFFloat") == 0)
			return new ConstSFFloat(0.0f);
		else if (fieldType.compareTo("ConstSFInt32") == 0)
			return new ConstSFInt32(0);
		else if (fieldType.compareTo("ConstSFRotation") == 0)
			return new ConstSFRotation(0.0f, 0.0f, 1.0f, 0.0f);
		else if (fieldType.compareTo("ConstSFString") == 0)
			return new ConstSFString("");
		else if (fieldType.compareTo("ConstSFTime") == 0)
			return new ConstSFTime(0.0);
		else if (fieldType.compareTo("ConstSFVec2f") == 0)
			return new ConstSFVec2f(0.0f, 0.0f);
		else if (fieldType.compareTo("ConstSFVec3f") == 0)
			return new ConstSFVec3f(0.0f, 0.0f, 0.0f);

		if (fieldType.compareTo("MFColor") == 0)
			return new MFColor();
		else if (fieldType.compareTo("MFFloat") == 0)
			return new MFFloat();
		else if (fieldType.compareTo("MFInt32") == 0)
			return new MFInt32();
		else if (fieldType.compareTo("MFRotation") == 0)
			return new MFRotation();
		else if (fieldType.compareTo("MFString") == 0)
			return new MFString();
		else if (fieldType.compareTo("MFTime") == 0)
			return new MFTime();
		else if (fieldType.compareTo("MFVec2f") == 0)
			return new MFVec2f();
		else if (fieldType.compareTo("MFVec3f") == 0)
			return new MFVec3f();

		if (fieldType.compareTo("ConstMFColor") == 0)
			return new ConstMFColor();
		else if (fieldType.compareTo("ConstMFFloat") == 0)
			return new ConstMFFloat();
		else if (fieldType.compareTo("ConstMFInt32") == 0)
			return new ConstMFInt32();
		else if (fieldType.compareTo("ConstMFRotation") == 0)
			return new ConstMFRotation();
		else if (fieldType.compareTo("ConstMFString") == 0)
			return new ConstMFString();
		else if (fieldType.compareTo("ConstMFTime") == 0)
			return new ConstMFTime();
		else if (fieldType.compareTo("ConstMFVec2f") == 0)
			return new ConstMFVec2f();
		else if (fieldType.compareTo("ConstMFVec3f") == 0)
			return new ConstMFVec3f();

		return null;
	}
}