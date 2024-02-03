/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : VRML97ProtoTokenizer.java
*
******************************************************************/

package cv97.parser.vrml97;

import java.io.*;

public class VRML97ProtoTokenizer extends StreamTokenizer {

	public VRML97ProtoTokenizer(Reader reader) { 
		super(reader);
		initializeTokenizer();
	}

	public void initializeTokenizer() {
		eolIsSignificant(true);
		commentChar('#');
		wordChars ('{', '}');
		wordChars ('[', ']');
		wordChars (',', ',');
		wordChars ('"', '"');
		wordChars ('_', '_');
	}
};
