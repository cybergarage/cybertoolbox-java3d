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

public class DoubleValueTokenizer extends StreamTokenizer {
	
	public DoubleValueTokenizer(String string) { 
		super(new StringReader(string));
	}
		
	public double[] getValues() {
		Vector tokenBuffer = new Vector();
		try {
			nextToken();
			while (ttype != TT_EOF) {
				switch (ttype) {
				case TT_NUMBER:
					tokenBuffer.addElement(new Double(nval));
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
			
		double values[] = new double[tokenBuffer.size()];
		for (int n=0; n<tokenBuffer.size(); n++) 
			values[n] = ((Double)tokenBuffer.elementAt(n)).doubleValue();
	
		return values;
	}	
} 
	
