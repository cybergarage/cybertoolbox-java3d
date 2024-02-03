/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1998-1998
*
*	File:	DoubleValueTokenizer.java
*
******************************************************************/

import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.IOException;
import java.util.Vector;

public class StringValueTokenizer extends StreamTokenizer {
	
	public StringValueTokenizer(String string) { 
		super(new StringReader(string));
		wordChars('_', '_');
		whitespaceChars(',', ',');
	}
		
	public String[] getValues() {
		Vector tokenBuffer = new Vector();
		try {
			nextToken();
			while (ttype != TT_EOF) {
				switch (ttype) {
				case TT_NUMBER: 
					{
						if ((nval % 1.0) == 0.0)
							tokenBuffer.addElement(Integer.toString((int)nval));
						else
							tokenBuffer.addElement(Double.toString(nval));
					}
					break;
				case TT_WORD:
					{
						tokenBuffer.addElement(new String(sval));
					}
					break;
				}
				nextToken();
			}
		}
		catch (IOException e) {
			return null;
		}
		
		if (tokenBuffer.size() == 0)
			return null;
			
		String values[] = new String[tokenBuffer.size()];
		for (int n=0; n<tokenBuffer.size(); n++) 
			values[n] = (String)tokenBuffer.elementAt(n);
	
		return values;
	}	
} 
	
