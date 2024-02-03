/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : Switch.java
*
******************************************************************/

package cv97.node;

import java.io.PrintWriter;
import cv97.*;
import cv97.field.*;

public class SwitchNode extends GroupingNode {

	private String	whichChoiceFieldName	= "whichChoice";
	private String	choiceExposedField		= "choice";
	
	public SwitchNode() {
		super(false, false);
		setHeaderFlag(false);
		setType(switchTypeName);

		// whichChoice field
		SFInt32 whichChoice = new SFInt32(-1);
		addField(whichChoiceFieldName, whichChoice);

		// choice exposedField
		MFNode choice = new MFNode();
		addExposedField(choiceExposedField, choice);
	}

	public SwitchNode(SwitchNode node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	//	whichChoice
	////////////////////////////////////////////////

	public void setWhichChoice(int value) {
		SFInt32 whichChoice = (SFInt32)getField(whichChoiceFieldName);
		whichChoice.setValue(value);
	}

	public void setWhichChoice(String value) {
		SFInt32 whichChoice = (SFInt32)getField(whichChoiceFieldName);
		whichChoice.setValue(value);
	}

	public int getWhichChoice() {
		SFInt32 whichChoice = (SFInt32)getField(whichChoiceFieldName);
		return whichChoice.getValue();
	}

	////////////////////////////////////////////////
	//	choice
	////////////////////////////////////////////////

	public MFNode getChoiceField() {
		return (MFNode)getExposedField(choiceExposedField);
	}

	public void updateChoiceField() {
		MFNode choiceField = getChoiceField();
		choiceField.removeAllValues();
		for (Node node=getChildNodes(); node != null; node=node.next()) 
			choiceField.addValue(node);
	}

	////////////////////////////////////////////////
	//	abstract functions
	////////////////////////////////////////////////
	
	public boolean isChildNodeType(Node node){
		if (node.isCommonNode() || node.isBindableNode() ||node.isInterpolatorNode() || node.isSensorNode() || node.isGroupingNode() || node.isSpecialGroupNode())
			return true;
		else
			return false;
	}

	public void initialize() {
		super.initialize();
		updateChoiceField();
		//calculateBoundingBox();
	}

	public void uninitialize() {
	}

	public void update() {
	}

	////////////////////////////////////////////////
	//	Infomation
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) {
		printStream.println(indentString + "\t" + "whichChoice " + getWhichChoice());
	}
}
