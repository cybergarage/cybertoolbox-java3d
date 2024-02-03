/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : PixelTexture.java
*
******************************************************************/

package cv97.node;

import java.util.Vector;
import java.lang.String;
import java.io.PrintWriter;
import cv97.*;
import cv97.field.*;

public class PixelTextureNode extends TextureNode {
	
	//// Exposed Field ////////////////
	private String	imageExposedFieldName	= "image";

	//// Field ////////////////
	private String	repeatSFieldName		= "repeatS";
	private String	repeatTFieldName		= "repeatT";

	public PixelTextureNode() {
		setHeaderFlag(false);
		setType(pixelTextureTypeName);

		///////////////////////////
		// Exposed Field 
		///////////////////////////

		// image field
		SFImage image = new SFImage();
		addExposedField(imageExposedFieldName, image);

		///////////////////////////
		// Field 
		///////////////////////////

		// repeatS field
		SFBool repeatS = new SFBool(true);
		addField(repeatSFieldName, repeatS);

		// repeatT field
		SFBool repeatT = new SFBool(true);
		addField(repeatTFieldName, repeatT);
	}

	public PixelTextureNode(PixelTextureNode node) {
		this();
		setFieldValues(node);
	}

	////////////////////////////////////////////////
	//	RepeatS
	////////////////////////////////////////////////
	
	public void setRepeatS(boolean value) {
		SFBool repeatS = (SFBool)getField(repeatSFieldName);
		repeatS.setValue(value);
	}

	public void setRepeatS(String value) {
		SFBool repeatS = (SFBool)getField(repeatSFieldName);
		repeatS.setValue(value);
	}
	
	public boolean getRepeatS() {
		SFBool repeatS = (SFBool)getField(repeatSFieldName);
		return repeatS.getValue();
	}

	////////////////////////////////////////////////
	//	RepeatT
	////////////////////////////////////////////////
	
	public void setRepeatT(boolean value) {
		SFBool repeatT = (SFBool)getField(repeatTFieldName);
		repeatT.setValue(value);
	}

	public void setRepeatT(String value) {
		SFBool repeatT = (SFBool)getField(repeatTFieldName);
		repeatT.setValue(value);
	}
	
	public boolean getRepeatT() {
		SFBool repeatT = (SFBool)getField(repeatTFieldName);
		return repeatT.getValue();
	}

	////////////////////////////////////////////////
	// Image
	////////////////////////////////////////////////

	public void addImage(int value) {
		SFImage image = (SFImage)getExposedField(imageExposedFieldName);
		image.addValue(value);
	}

	public void setImages(String value) {
		SFImage image = (SFImage)getExposedField(imageExposedFieldName);
		image.addValue(value);
	}

	public int getNImages() {
		SFImage image = (SFImage)getExposedField(imageExposedFieldName);
		return image.getSize();
	}
	
	public int getImage(int index) {
		SFImage image = (SFImage)getExposedField(imageExposedFieldName);
		return image.get1Value(index);
	}

	////////////////////////////////////////////////
	//	abstract functions
	////////////////////////////////////////////////
	
	public boolean isChildNodeType(Node node){
		return false;
	}

	public void initialize() {
		super.initialize();
	}

	public void uninitialize() {
	}

	public void update() {
	}

	////////////////////////////////////////////////
	//	Imagemation
	////////////////////////////////////////////////

	public void outputContext(PrintWriter printStream, String indentString) {
		SFBool repeatS = (SFBool)getField(repeatSFieldName);
		SFBool repeatT = (SFBool)getField(repeatTFieldName);
		SFImage image = (SFImage)getExposedField(imageExposedFieldName);
		
		int imgSize = image.getSize();
		if (3 < imgSize) { 
			int width		= image.get1Value(0);
			int height		= image.get1Value(1);
			int compSize	= image.get1Value(2);
			
			printStream.println(indentString + "\t" + "image " + width + " " + height + " " + compSize);

			int linePixels = 0;
			for (int n=3; n<imgSize; n++) {
				
				if (linePixels == 0)
					printStream.print(indentString + "\t\t");

				printStream.print("0x" + Integer.toHexString(image.get1Value(n)) + " ");

				linePixels++;
				
				if (16 < linePixels || n == (imgSize-1)) {
					printStream.println("");
					linePixels = 0;
				}
			}
		}
		
		printStream.println(indentString + "\t" + "repeatS " + repeatS );
		printStream.println(indentString + "\t" + "repeatT " + repeatT );
	}
}
