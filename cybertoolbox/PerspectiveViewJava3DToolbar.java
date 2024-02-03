/******************************************************************
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998
*
*	File:	PerspectiveViewJava3DToolbar.java
*
******************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;

import cv97.*;
import cv97.node.*;
import cv97.util.*;
import cv97.j3d.*;

public class PerspectiveViewJava3DToolbar extends CtbViewerToolbar implements Constants {

	public PerspectiveViewJava3DToolbar(Frame frame, World world) {
		super(frame, world, false);
	}

}

