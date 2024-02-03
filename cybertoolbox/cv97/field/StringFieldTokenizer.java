/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : StringFieldTokenizer.java
*
******************************************************************/

package cv97.field;

import java.io.*;
import java.util.*;

public class StringFieldTokenizer extends Object {
	
	public final static String[] getTokens(String value, String whitespaceChars, String wordChars) {
		FieldTokenizer stream = new FieldTokenizer(value, whitespaceChars, wordChars);
		Vector tokenBuffer = new Vector();
		
		try {
			stream.nextToken();
			while (stream.ttype != stream.TT_EOF) {
				switch (stream.ttype) {
				case stream.TT_WORD: 
					{
						tokenBuffer.addElement(new String(stream.sval));
					}
					break;
				case stream.TT_NUMBER:
					{
						if ((stream.nval % 1.0) == 0.0)
							tokenBuffer.addElement(Integer.toString((int)stream.nval));
						else
							tokenBuffer.addElement(Double.toString(stream.nval));
					}
					break;
				}
				stream.nextToken();
			}
		}
		catch (IOException e) {
			return null;
		}
		
		String tokens[] = new String[tokenBuffer.size()];
		for (int n=0; n<tokenBuffer.size(); n++) {
			tokens[n] = (String)tokenBuffer.elementAt(n);
		}
		
		return tokens;
	}	

	public final static String[] getTokens(String value) {
		return getTokens(value, ", ", "%");
	}	

}
