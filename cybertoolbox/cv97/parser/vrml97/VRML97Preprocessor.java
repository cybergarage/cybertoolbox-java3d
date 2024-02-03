/******************************************************************
*
*	CyberVRML97 for Java
*
*	Copyright (C) Satoshi Konno 1997-1998
*
*	File : VRML97Preprocessor.java
*
******************************************************************/

package cv97.parser.vrml97;

import java.io.*;

import cv97.util.*;

public class VRML97Preprocessor {

	public VRML97Preprocessor() { 
		setOkFlag(false);
	}
	
	public VRML97Preprocessor(File file) throws FileNotFoundException, IOException { 
		open(file);
	}

	public VRML97Preprocessor(String file) throws FileNotFoundException, IOException { 
		open(file);
	}

	////////////////////////////////////////////////
	//	PROTO
	////////////////////////////////////////////////

	private LinkedList	mVRML97ProtoList = new LinkedList();
	
	public void addVRML97Proto(VRML97Proto proto) {
		mVRML97ProtoList.addNode(proto);
	}
	
	public VRML97Proto getVRML97Protos() {
		return (VRML97Proto)mVRML97ProtoList.getNodes();
	}
	
	public VRML97Proto getVRML97Proto(String name) {
		for (VRML97Proto proto=getVRML97Protos(); proto != null; proto=proto.next()) {
			String protoName = proto.getName();
			if (protoName.compareTo(name) == 0)
				return proto;
		}
		return null;
	}
	
	////////////////////////////////////////////////
	//	Flag 
	////////////////////////////////////////////////

	private boolean		mOK	= false;

	public void setOkFlag(boolean flag) {
		mOK = flag;
	}
	
	public boolean isOk() {
		return mOK;
	}
	
	public String getTempFilename() {
		return "__temp__filename__";
	}
	
	////////////////////////////////////////////////
	//	getInputStream (Result)
	////////////////////////////////////////////////
	
	public InputStream	mInputStream	= null;

	public InputStream getInputStream() throws FileNotFoundException {
		if (isOk()) {
			mInputStream= new FileInputStream(getTempFilename());
			return mInputStream;
		}
		else
			throw new FileNotFoundException(getTempFilename());
	}

	////////////////////////////////////////////////
	//	close
	////////////////////////////////////////////////
	
	public void close() throws IOException {
		if (mInputStream == null)
			throw new IOException(getTempFilename());
		mInputStream.close();
	}

	////////////////////////////////////////////////
	//	delete
	////////////////////////////////////////////////
	
	public boolean delete() throws IOException {
		close();
		File file = new File(getTempFilename());
		return file.delete();
	}
				
	////////////////////////////////////////////////
	//	open
	////////////////////////////////////////////////

	public InputStream open(File file) throws FileNotFoundException, IOException { 
		setOkFlag(false);
		replace(file);
		return getInputStream();
	}
	
	public InputStream open(String file) throws FileNotFoundException, IOException { 
		setOkFlag(false);
		replace(file);
		return getInputStream();
	}
				
	////////////////////////////////////////////////
	//	Replace
	////////////////////////////////////////////////

	public void replace(File file) throws FileNotFoundException, IOException {
		InputStream inputStream = new FileInputStream(file);
		Reader r = new BufferedReader(new InputStreamReader(inputStream));
		VRML97ProtoTokenizer st = new VRML97ProtoTokenizer(r);
		replace(st);
		inputStream.close();
	}

	public void replace(String file) throws FileNotFoundException, IOException {
		InputStream inputStream = new FileInputStream(file);
		Reader r = new BufferedReader(new InputStreamReader(inputStream));
		VRML97ProtoTokenizer st = new VRML97ProtoTokenizer(r);
		replace(st);
   		inputStream.close();
	}

	public String getParameterValue(String fieldName, VRML97ProtoTokenizer stream) throws IOException {
		if (fieldName.compareTo("SFBool") == 0) {
			stream.nextToken(); String value = stream.sval;
			return value;
		}
		else if (fieldName.compareTo("SFFloat") == 0) {
			stream.nextToken();	Double value = new Double(stream.nval);
			return value.toString();
		}
		else if (fieldName.compareTo("SFColor") == 0) {
			stream.nextToken();	double r = stream.nval;
			stream.nextToken();	double g = stream.nval;
			stream.nextToken();	double b = stream.nval;
			return r + " " + g + " " + b;
		}
		else if (fieldName.compareTo("SFImage") == 0) { // Not supported
			System.out.println("PROTO SFImage is not suppoted !!");
			return null;
		}
		else if (fieldName.compareTo("SFInt32") == 0) {
			stream.nextToken();	Integer value = new Integer((int)stream.nval);
			return value.toString();
		}
		else if (fieldName.compareTo("SFNode") == 0) { // Not supported
			System.out.println("PROTO SFNode is not suppoted !!");
			return null;
		}
		else if (fieldName.compareTo("SFString") == 0) {
			stream.nextToken(); String value = stream.sval;
			return value;
		}
		else if (fieldName.compareTo("SFTime") == 0) {
			stream.nextToken();	Double value = new Double(stream.nval);
			return value.toString();
		}
		else if (fieldName.compareTo("SFVec2f") == 0) {
			stream.nextToken();	double x = stream.nval;
			stream.nextToken();	double y = stream.nval;
			return x + " " + y;
		}
		else if (fieldName.compareTo("SFVec3f") == 0) {
			stream.nextToken();	double x = stream.nval;
			stream.nextToken();	double y = stream.nval;
			stream.nextToken();	double z = stream.nval;
			return x + " " + y + " " + z;
		}
		
		return null;
	}

	public boolean addProtoParameter(VRML97ProtoParameterList paramList, VRML97ProtoTokenizer stream) throws IOException {
		stream.nextToken();	
		if (stream.ttype != stream.TT_WORD)
			return false;
		String fieldName = stream.sval;
		
		stream.nextToken();	
		if (stream.ttype != stream.TT_WORD)
			return false;
		String name = stream.sval;
		
		String value = getParameterValue(fieldName, stream);
		if (name != null && value != null) {
			paramList.addParameter(fieldName, name, value);
			return true;
		}
		
		return false;
	}

	public boolean addProtoParameters(VRML97ProtoParameterList paramList, VRML97ProtoTokenizer stream)  throws IOException {
		stream.nextToken();
		while (stream.ttype != stream.TT_EOF) {
			if (stream.ttype == stream.TT_WORD) {
				if (stream.sval.compareTo("[") == 0) 
					break;
			}
			stream.nextToken();
		}

		if (stream.ttype == stream.TT_EOF)
			return false;
					
		stream.nextToken();
		while (stream.ttype != stream.TT_EOF) {
			if (stream.ttype == stream.TT_WORD) {
				if (stream.sval.compareTo("]") == 0) 
					return true;
				else if (stream.sval.compareTo("field") == 0) { 
					if (addProtoParameter(paramList, stream) == false)
						return false;
				}
			}
			stream.nextToken();
		}
		
		return false;
	}
			
	public VRML97Proto createProto(VRML97ProtoTokenizer stream) throws IOException {
		stream.nextToken();
		String protoName = stream.sval;
		VRML97Proto proto = new VRML97Proto(protoName);

		if (addProtoParameters(proto.getParameterList(), stream) == false)
			return null;

		int nest = 0;
		
		stream.nextToken();
		while (stream.ttype != stream.TT_EOF) {
			if (stream.ttype == stream.TT_WORD) {
				if (stream.sval.compareTo("{") == 0) { 
					nest++;
					break;
				}
			}
			stream.nextToken();
		}

		stream.nextToken();
		while (stream.ttype != stream.TT_EOF && 0 < nest) {
			switch (stream.ttype) {
			case stream.TT_NUMBER:
				proto.addToken(stream.nval);
				break;
			case stream.TT_WORD:
				if (stream.sval.compareTo("{") == 0) 
					nest++;
				if (stream.sval.compareTo("}") == 0) 
					nest--;
				if (0 < nest)
					proto.addToken(stream.sval);
				break;
			case stream.TT_EOL:
				proto.addToken("\n");
				break;
			}
			stream.nextToken();
		}
		
		return proto;
	}
	
	public String getVRML97ProtoString(VRML97Proto proto, VRML97ProtoTokenizer stream) throws IOException {
		VRML97ProtoParameterList paramList = new VRML97ProtoParameterList();

		stream.nextToken();
		while (stream.ttype != stream.TT_EOF) {
			if (stream.ttype == stream.TT_WORD) {
				if (stream.sval.compareTo("{") == 0) 
					break;
			}
			stream.nextToken();
		}

		if (stream.ttype == stream.TT_EOF)
			return null;
					
		stream.nextToken();
		while (stream.ttype != stream.TT_EOF) {
			if (stream.ttype == stream.TT_WORD) {
				if (stream.sval.compareTo("}") == 0)
					return proto.getString(paramList);
				else {
					String name = stream.sval;
					VRML97ProtoParameter protoParam = proto.getParameter(name);
					if (protoParam != null) {
						String fieldName = protoParam.getType();
						String value = getParameterValue(fieldName, stream);
						paramList.addParameter(fieldName, name, value);
					}
				}
			}
			stream.nextToken();
		}
		
		return null;
	}
	
	public void replace(VRML97ProtoTokenizer stream) {

		FileOutputStream outputStream = null;
		
		try {
			outputStream = new FileOutputStream(getTempFilename());
		}
		catch (IOException e) {
			System.out.println("Couldn't close a temporary file(" + getTempFilename() + ") !!");
		}
		
		PrintWriter printStream = new PrintWriter(outputStream);

		try {
			int nToken = 0;
			while (stream.nextToken() != StreamTokenizer.TT_EOF) {
				switch (stream.ttype) {
				case stream.TT_NUMBER:
					double dvalue = stream.nval;
					if ((dvalue % 1.0) == 0.0) {
						int ivalue = (int)dvalue;
						printStream.print(ivalue + " ");
					}
					else
						printStream.print(dvalue + " ");
					nToken++;
					break;
				case stream.TT_WORD:
					if (stream.sval.compareTo("PROTO") == 0) { 
						VRML97Proto proto = createProto(stream);
						if (proto != null) {
							addVRML97Proto(proto);
							String protoString = proto.getString(null);
							if (protoString != null) {
								printStream.println(protoString);
							}
						}
					}
					else {
						VRML97Proto proto = getVRML97Proto(stream.sval);
						if (proto != null) {
							String protoString = getVRML97ProtoString(proto, stream);
							if (protoString != null) {
								printStream.println(protoString);
							}
						}
						else {
							printStream.print(stream.sval + " ");
							nToken++;
						}
					}
					break;
				case stream.TT_EOL:
					if (0 < nToken)
						printStream.println("");
					nToken = 0;
					break;
				}
			}
		}
		catch (IOException e) {
			System.out.println("Couldn't read a next token !!");
		}

		try {
			if (printStream != null) {
				printStream.flush();
				printStream.close();
			}
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}
		catch (IOException e) {
			System.out.println("Couldn't close a file(" + getTempFilename() + ") !!");
		}
		
		setOkFlag(true);
	}
};
