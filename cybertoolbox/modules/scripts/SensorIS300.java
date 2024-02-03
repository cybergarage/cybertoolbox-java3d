/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	SensorIS300.java
*
----------------------------------------------------------------*/

public class SensorIS300 extends Module {

	static IS300 is300[] =  new IS300[4];
	
	int				port;
	int				baud;
	int				receiver;
	
	float				pos[] = new float[3];			 	
	float				rot[] = new float[3];
				 	
	public void initialize() {
		port = -1;
		baud = -1;
		receiver = -1;
				
		String value[] = getStringValues();
		
		if (value == null)
			return;
			
		if (value.length != 3)
			return;
			
		String portName = value[0];
		if (portName != null) {
			if (portName.equalsIgnoreCase("SERIAL1") == true)
				port = IS300.SERIALPORT1;
			if (portName.equalsIgnoreCase("SERIAL2") == true)
				port = IS300.SERIALPORT2;
			if (portName.equalsIgnoreCase("SERIAL3") == true)
				port = IS300.SERIALPORT3;
			if (portName.equalsIgnoreCase("SERIAL4") == true)
				port = IS300.SERIALPORT4;
		}

		try {
			baud = (int)Double.parseDouble(value[1]);
			receiver = (int)Double.parseDouble(value[2]);
		}
		catch (NumberFormatException nfe) {}
		
		Debug.message("Sensor::IS300");
		Debug.message("\tport = " + value[0]);
		Debug.message("\tbaud = " + value[1]);
		Debug.message("\treceiver = " + value[2]);
		Debug.message("\tport = " + port);
		Debug.message("\tbaud = " + baud);
		Debug.message("\treceiver = " + receiver);

//		if (receiver == 1) {
			if (0 <= port) {
				if (is300[port] == null) 
					is300[port] = new IS300(port, baud);
			}
//		}
	}

	public void shutdown() {
		if (port < 0)
			return;
			
		if (is300[port] == null)
			return;
			
//		if (receiver == 1) {
			is300[port].close();
			is300[port] = null;
//		}
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		if (port < 0)
			return;
			
		if (is300[port] == null)
			return;
			
		if (receiver < 1)
			return;

		is300[port].getOrientation(receiver, rot);
		rot[0] = (float)Math.toRadians(rot[0]);
		rot[1] = (float)Math.toRadians(rot[1]);
		rot[2] = (float)Math.toRadians(rot[2]);
		sendOutNodeValue(0, rot);
	}

}
