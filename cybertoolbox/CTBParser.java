/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	CBFParser.java
*
******************************************************************/

import java.util.Stack;

public abstract class CTBParser {

	public final static int FORMATTYPE_NONE	= 0;
	public final static int FORMATTYPE_CBF	= 1;
	public final static int FORMATTYPE_CDF	= 2;
	
	private World			mWorld;
	private Stack			mObjectStack	= new Stack();
	private LinkedList	mEventList		= new LinkedList();
	private LinkedList	mDiagramList	= new LinkedList();
	private int				mFormatType;
	private String			mVersionString	= null;
	
	public CTBParser () {
		setWorld(null);
		setFormatType(FORMATTYPE_NONE);
		setVersionString(null);
	}
	
	public void setWorld(World world) {
		mWorld = world;
	}
	
	public World getWorld() {
		return mWorld;
	}

	public void setFormatType(int type) {
		mFormatType = type;
	}
	
	public int getFormatType() {
		return mFormatType;
	}

	public void setVersionString(String ver) {
		mVersionString = ver;
	}
	
	public String getVersionString() {
		return mVersionString;
	}
	
	public void pushObject(Object obj)
	{
		mObjectStack.push(obj);
	}

	public Object popObject()
	{
		return mObjectStack.pop();
	}

	public Object getCurrentObject() {
		return mObjectStack.peek();
	}
	
	public Event getCurrentEvent() {
		return (Event)getCurrentObject();
	}

	public Diagram getCurrentDiagram() {
		return (Diagram)getCurrentObject();
	}

	public ModuleInfo getCurrentModuleInfo() {
		return (ModuleInfo)getCurrentObject();
	}

	///////////////////////////////////////////////
	//	Event
	///////////////////////////////////////////////
	
	public boolean addEvent(Event event) {
		mEventList.addNode(event);
		return true;
	}
	
	public int getNEvents() {
		return mEventList.getNNodes();
	}
	
	public Event getEvents() {
		return (Event)mEventList.getNodes();
	}
	
	public Event getEvent(int n) {
		return (Event)mEventList.getNode(n);
	}

	///////////////////////////////////////////////
	//	Diagram
	///////////////////////////////////////////////
	
	public boolean addDiagram(Diagram event) {
		mDiagramList.addNode(event);
		return true;
	}
	
	public int getNDiagrams() {
		return mDiagramList.getNNodes();
	}
	
	public Diagram getDiagrams() {
		return (Diagram)mDiagramList.getNodes();
	}
	
	public Diagram getDiagram(int n) {
		return (Diagram)mDiagramList.getNode(n);
	}

	///////////////////////////////////////////////
	//	ModuleInfo
	///////////////////////////////////////////////

	public class ModuleInfo extends Object {
	
		String	mClassName;
		String	mTypeName;
		String	mName;
		String	mValue;
		int		mx, my;
		Diagram	mInsideDiagram;
						
		public ModuleInfo() {
			setClassName(null);
			setTypeName(null);
			setInsideDiagram(null);
		}
		
		public void setClassName(String name) {
			mClassName = name;
		}
		
		public String getClassName() {
			return mClassName;
		}
		
		public void setTypeName(String name) {
			mTypeName = name;
		}
		
		public String getTypeName() {
			return mTypeName;
		}

		public void setName(String name) {
			mName = name;
		}
		
		public String getName() {
			return mName;
		}
		
		public void setValue(String value) {
			mValue = value;
		}
		
		public String getValue() {
			return mValue;
		}
		
		public void setPosition(int x, int y) {
			mx = x;
			my = y;
		}

		public void setXPosition(int x) {
			mx = x;
		}

		public void setYPosition(int y) {
			my = y;
		}
		
		public int getXPosition() {
			return mx;
		}
		
		public int getYPosition() {
			return my;
		}

		public void setInsideDiagram(Diagram dgm) {
			mInsideDiagram = dgm;
		}
		
		public Diagram getInsideDiagram() {
			return mInsideDiagram;
		}
	}

	abstract public void	Input() throws ParseException;
}
