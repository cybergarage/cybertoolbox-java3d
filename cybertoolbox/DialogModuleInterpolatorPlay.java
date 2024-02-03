/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogModuleInterpolatorPlay.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import cv97.*;
import cv97.node.*;

public class DialogModuleInterpolatorPlay extends Dialog {
	
	private DialogModuleInterpolatorNodeComboBox	mInterpNodeCombo			= null;
	private DialogValueFieldComponent				mStartPosComponent		= null;
	private DialogValueFieldComponent				mPlayStepComponent		= null;
	private DialogValueFieldComponent				mPlayIntervalComponent	= null;
	private PlayModeComponent							mPlayModeComponent		= null;
	
	public DialogModuleInterpolatorPlay(Frame parentFrame, Module module) {
		super(parentFrame, "Module Interpolator::Play");
		
		SceneGraph sg = module.getSceneGraph();
		
		mInterpNodeCombo			= new DialogModuleInterpolatorNodeComboBox(sg);
		mStartPosComponent		= new DialogValueFieldComponent("Start Position (0 - 1)");
		mPlayStepComponent		= new DialogValueFieldComponent("Play Step");
		mPlayIntervalComponent	= new DialogValueFieldComponent("Play Interval (sec)");
		mPlayModeComponent		= new PlayModeComponent(module);
		
		JComponent dialogComponent[] = new JComponent[5];
		dialogComponent[0] = mInterpNodeCombo;
		dialogComponent[1] = mStartPosComponent;
		dialogComponent[2] = mPlayStepComponent;
		dialogComponent[3] = mPlayIntervalComponent;
		dialogComponent[4] = mPlayModeComponent;
		setComponents(dialogComponent);

		mStartPosComponent.setValue(0);
		mPlayStepComponent.setValue(0.1);
		mPlayIntervalComponent.setValue(0.1);
		
		setOptionValue(module);
	}

	public void setOptionValue(Module module) {
		String moduleValue[] = module.getStringValues();
		if (moduleValue != null && moduleValue.length == 6) {
			mInterpNodeCombo.setNode(moduleValue[0]);
			try {
				mStartPosComponent.setValue(Double.parseDouble(moduleValue[1]));
				mPlayStepComponent.setValue(Double.parseDouble(moduleValue[2]));
				mPlayIntervalComponent.setValue(Double.parseDouble(moduleValue[3]));
			}
			catch (NumberFormatException  nfe) {}
		}
	}

	public String getOptionValue() {
		StringBuffer value = new StringBuffer();
		value.append(mInterpNodeCombo.getNode() + ",");
		value.append(mStartPosComponent.getValue() + ",");
		value.append(mPlayStepComponent.getValue() + ",");
		value.append(mPlayIntervalComponent.getValue() + ",");
		value.append((mPlayModeComponent.isCotinuous() ? "true" : "false") + ",");
		value.append((mPlayModeComponent.isOscillateBox() ? "true" : "false"));
		return value.toString();
	}
	
	private class PlayModeComponent extends JPanel {
	
		JCheckBox					mCotinuousBox;
		JCheckBox					mOscillateBox;
					
		public PlayModeComponent(Module module) {
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			setBorder(BorderFactory.createTitledBorder("Play Mode"));

			mCotinuousBox = new JCheckBox("Cotinuous");
			mCotinuousBox.setSelected(false);
			
			mOscillateBox = new JCheckBox("Oscillate");
			mOscillateBox.setSelected(false);

			Dimension spaceDim = new Dimension(10,1);
			
			add(Box.createRigidArea(spaceDim));
			add(mCotinuousBox);	
			add(Box.createRigidArea(spaceDim));
			add(mOscillateBox);	
			add(Box.createRigidArea(spaceDim));

			String moduleValue[] = module.getStringValues();
			if (moduleValue != null && moduleValue.length == 6) {
				mCotinuousBox.setSelected(moduleValue[4].equalsIgnoreCase("true"));
				mOscillateBox.setSelected(moduleValue[5].equalsIgnoreCase("true"));
			}
		}
		
		public boolean isCotinuous() {
			return mCotinuousBox.isSelected();
		}

		public boolean isOscillateBox() {
			return mOscillateBox.isSelected();
		}
	}
}
		
