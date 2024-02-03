/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : MFieldSingleValueTokenizer.java
*
******************************************************************/

package cv97.field;

import java.io.*;
import java.util.*;

public class MFieldSingleValueTokenizer {

	public final static String[] getTokens(String value) {
		return StringFieldTokenizer.getTokens(value, ", ", "%");
	}	

}
