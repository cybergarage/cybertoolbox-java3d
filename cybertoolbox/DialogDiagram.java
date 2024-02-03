/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogDiagram.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import cv97.*;
import cv97.field.*;

public class DialogDiagram extends Dialog {
	
	private DialogTextFieldComponent	mValueFieldComponent		= null;
	private JComboBox						mDataFlowLineComponent	= null;
	
	public DialogDiagram(Frame parentFrame, Diagram dgm, int dataflowLineType) {
		super(parentFrame, "Diagram");
		
		mValueFieldComponent = new DialogTextFieldComponent("Name");
		mValueFieldComponent.setText(dgm.getName());

		mDataFlowLineComponent = new JComboBox();
		mDataFlowLineComponent.setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Dataflow Line")));
		mDataFlowLineComponent.addItem("Straight");
		mDataFlowLineComponent.addItem("Zigzag");
		if (dataflowLineType == Diagram.LINE_STYLE_STRAIGHT)
			mDataFlowLineComponent.setSelectedIndex(0);
		else
			mDataFlowLineComponent.setSelectedIndex(1);
			
		JComponent dialogComponent[] = new JComponent[2];
		dialogComponent[0] = mValueFieldComponent;
		dialogComponent[1] = mDataFlowLineComponent;
		setComponents(dialogComponent);
	}
	
	public String getName() {
		return mValueFieldComponent.getText();
	}

	public int getDataflowLineStyle() {
		String lineType = (String)mDataFlowLineComponent.getSelectedItem();
		if (lineType.equals("Straight") == true)
			return Diagram.LINE_STYLE_STRAIGHT;
		return Diagram.LINE_STYLE_ZIGZAG;
	}
}

