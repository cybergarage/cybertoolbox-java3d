/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	DialogWorldAbout.java
*
******************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import cv97.*;
import cv97.node.*;

public class DialogWorldAbout extends Dialog {
	
	public DialogWorldAbout(Frame parentFrame) {
		super(parentFrame, "About", true);
		JComponent dialogComponent[] = new JComponent[5];
		dialogComponent[0] = new CyberToolboxPanel();
		dialogComponent[1] = new ReleaseDate();
		dialogComponent[2] = new CopyRightPanel();
		dialogComponent[3] = new WebPanel();
		dialogComponent[4] = new MailPanel();
		setComponents(dialogComponent);
	}

	private class CyberToolboxPanel extends JPanel {
		public CyberToolboxPanel() {
			setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
			setBorder(new EmptyBorder(5, 5, 5, 5));
			add(new JLabel(new ImageIcon(World.getImageDirectory() + "cybertoolbox.gif")));
			add(new JLabel("CyberToolbox for Java3D Release " + World.RELEASE_NUMBER));
		}
 	}

	private class ReleaseDate extends JPanel {
		public ReleaseDate() {
			setLayout(new FlowLayout(FlowLayout.CENTER, 10,2));
//			setBorder(new EmptyBorder(5, 5, 5, 5));
			add(new JLabel("Release Date : " + World.RELEASE_DATE));
		}
 	}

	private class CopyRightPanel extends JPanel {
		public CopyRightPanel() {
			setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2));
//			setBorder(new EmptyBorder(5, 5, 5, 5));
			add(new JLabel("Copyright (C) 1998 Satoshi Konno, All right reserved."));
		}
 	}

	private class WebPanel extends JPanel {
		public WebPanel() {
			setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2));
//			setBorder(new EmptyBorder(5, 5, 5, 5));
			add(new JLabel("http://www.cyber.koganei.tokyo.jp"));
		}
 	}

	private class MailPanel extends JPanel {
		public MailPanel() {
			setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2));
//			setBorder(new EmptyBorder(5, 5, 5, 5));
			add(new JLabel("skonno@cyber.koganei.tokyo.jp"));
		}
 	}
}
		
