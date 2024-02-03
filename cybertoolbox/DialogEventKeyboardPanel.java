/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogEventPickupPanel.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.*;

public class DialogEventKeyboardPanel extends DialogEventPanel {
	
	private KeyboardComponent mKeyboardComponent = null;
	
	public DialogEventKeyboardPanel(World world) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		mKeyboardComponent = new KeyboardComponent();
		add(mKeyboardComponent);
	}

	public DialogEventKeyboardPanel(World world, Event event) {
		this(world);
		setKey(event.getOptionString());
	}

	public void setKey(String key) {
		mKeyboardComponent.setKey(key);
	}
	
	public String getKey() {
		return mKeyboardComponent.getKey();
	}

	public String getOptionString() {
		return getKey();
	}
	
	public class KeyboardComponent extends JComboBox {

		public KeyboardComponent() {
			//setMaximumRowCount(2);		
			
			for (int c=KeyEvent.VK_0; c<=KeyEvent.VK_9; c++) 
				addItem(KeyEvent.getKeyText(c));
			
			addItem(KeyEvent.getKeyText(KeyEvent.VK_PLUS));
			addItem(KeyEvent.getKeyText(KeyEvent.VK_MINUS));
			
			for (int c=KeyEvent.VK_A; c<=KeyEvent.VK_Z; c++) 
				addItem(KeyEvent.getKeyText(c));
			
			for (int c=KeyEvent.VK_F1; c<=KeyEvent.VK_F12; c++) 
				addItem(KeyEvent.getKeyText(c));
			
			addItem(KeyEvent.getKeyText(KeyEvent.VK_UP));
			addItem(KeyEvent.getKeyText(KeyEvent.VK_DOWN));
			addItem(KeyEvent.getKeyText(KeyEvent.VK_LEFT));
			addItem(KeyEvent.getKeyText(KeyEvent.VK_RIGHT));
			
			setBorder(new TitledBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Keybaord")));
		}

		public void setKey(String keyName) {
			if (keyName == null)
				return;
			for (int n=0; n<getItemCount(); n++) {
				String itemName = (String)getItemAt(n);
				if (itemName.equals(keyName) == true) {
					setSelectedIndex(n);
					return;
				}
			}
		}
	
		public String getKey() {
			return (String)getSelectedItem();
		}
	}
	
}
		
