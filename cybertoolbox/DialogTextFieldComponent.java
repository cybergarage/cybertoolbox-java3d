/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogTextFieldComponent.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class DialogTextFieldComponent extends JPanel {
	
	private JTextField mValueField = null;
		
	public DialogTextFieldComponent(String title) {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(title));
		mValueField = new JTextField();
		mValueField.setHorizontalAlignment(JTextField.LEFT);
		add(mValueField, BorderLayout.NORTH);
		setMaximumSize(new Dimension(300, 64));
	}
		
	public void setText(String text) {
		mValueField.setText(text);
	}

	public String getText() {
		return mValueField.getText();
	}

	public void addActionListener(ActionListener l) {
		mValueField.addActionListener(l);
	}

	public void setEnabled(boolean enabled) {
		mValueField.setEnabled(enabled);
	}
}
