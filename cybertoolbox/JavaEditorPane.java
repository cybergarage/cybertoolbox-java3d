/******************************************************************
*
*	CyberToolbox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File : JavaEditorPane.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

public class JavaEditorPane extends JEditorPane {

	private JavaEditorKit	mJavaEditorKit;
	
	public void setJavaEditorKit(JavaEditorKit kit) {
		mJavaEditorKit = kit;
	}
	
	public JavaEditorKit getJavaEditorKit() {
		return mJavaEditorKit;
	}

	public JavaEditorPane() {

		Debug.message("JavaEditorPane");
		
		Document doc;
		
		doc = getDocument();
		
		Debug.message("    doc = " + doc);
		
		JavaEditorKit kit = new JavaEditorKit();

		doc = getDocument();
		
		Debug.message("    doc = " + doc);
		
		setJavaEditorKit(kit);
		
		setEditorKitForContentType("text/java", kit);
		setContentType("text/java");
		setBackground(Color.white);
		setFont(new Font("Courier", 0, 12));
		setEditable(true);

		// PENDING(prinz) This should have a customizer and
		// be serialized.  This is a bogus initialization.
		JavaContext styles = mJavaEditorKit.getStylePreferences();
		Style s;
		s = styles.getStyleForScanValue(JavaToken.COMMENT.getScanValue());
		StyleConstants.setForeground(s, new Color(102, 153, 153));
		s = styles.getStyleForScanValue(JavaToken.STRINGVAL.getScanValue());
		StyleConstants.setForeground(s, new Color(102, 153, 102));
		Color keyword = new Color(102, 102, 255);
		for (int code = 70; code <= 130; code++) {
			s = styles.getStyleForScanValue(code);
			if (s != null) {
			StyleConstants.setForeground(s, keyword);
			}
		}

		setFont(new Font("Monospaced", Font.PLAIN, 14));
		setTabSize(4);
		Debug.message("    JavaEditorPane.TabSize = " + getTabSize());
		
		Debug.message("    JavaEditorPane.doc = " + doc);

		doc.addUndoableEditListener(kit.getUndoHandler());

		Debug.message("    JavaEditorPane.doc = " + doc);
	}

	public void setText(String t) {
		Debug.message("setText");
		JavaEditorKit kit = getJavaEditorKit();
		UndoableEditListener undoHandler = kit.getUndoHandler();
		Document oldDoc = getDocument();
		Debug.message("    setText.oldDoc = " + oldDoc);
		int tabSize = 4;
		if(oldDoc != null) {
			oldDoc.removeUndoableEditListener(undoHandler);
			tabSize = getTabSize(oldDoc);
		}
		super.setText(t);
		Document newDoc = getDocument();
		Debug.message("    setText.newDoc = " + newDoc);
		setTabSize(newDoc, tabSize);
		newDoc.addUndoableEditListener(undoHandler);
	}
	
	public void setTabSize(Document doc, int size) {
		Debug.message("setTabSize");
		Debug.message("    setTabSize.doc = " + doc);
		if (doc != null) {
			int oldTabSize = getTabSize();
			Debug.message("    setTabSize.oldTabSize = " + oldTabSize);
			doc.putProperty(PlainDocument.tabSizeAttribute, new Integer(size));
			firePropertyChange("TabSize", oldTabSize, size);
		}
	}
	
	public void setTabSize(int size) {
		setTabSize(getDocument(), size);
	}

	public int getTabSize(Document doc) {
		int size = 8;
		if (doc != null) {
			Integer i = (Integer) doc.getProperty(PlainDocument.tabSizeAttribute);
			if (i != null) {
				size = i.intValue();
			}
		}
		return size;
	}

	public int getTabSize() {
		return getTabSize(getDocument());
	}
}
