/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	SensorFastrak.java
*
----------------------------------------------------------------*/

public class SensorFastrak extends Module {

	static Fastrak fastrak[] =  new Fastrak[4];
	
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
				port = Fastrak.SERIALPORT1;
			if (portName.equalsIgnoreCase("SERIAL2") == true)
				port = Fastrak.SERIALPORT2;
			if (portName.equalsIgnoreCase("SERIAL3") == true)
				port = Fastrak.SERIALPORT3;
			if (portName.equalsIgnoreCase("SERIAL4") == true)
				port = Fastrak.SERIALPORT4;
		}

		try {
			baud = (int)Double.parseDouble(value[1]);
			receiver = (int)Double.parseDouble(value[2]);
		}
		catch (NumberFormatException nfe) {}
		
		Debug.message("Sensor::Fastrak");
		Debug.message("\tport = " + value[0]);
		Debug.message("\tbaud = " + value[1]);
		Debug.message("\treceiver = " + value[2]);
		Debug.message("\tport = " + port);
		Debug.message("\tbaud = " + baud);
		Debug.message("\treceiver = " + receiver);

//		if (receiver == 1) {
			if (0 <= port) {
				if (fastrak[port] == null) 
					fastrak[port] = new Fastrak(port, baud);
			}
//		}
	}

	public void shutdown() {
		if (port < 0)
			return;
			
		if (fastrak[port] == null)
			return;
			
//		if (receiver == 1) {
			fastrak[port].close();
			fastrak[port] = null;
//		}
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		if (port < 0)
			return;
			
		if (fastrak[port] == null)
			return;
			
		if (receiver < 1)
			return;

		fastrak[port].getPosition(receiver, pos);
		sendOutNodeValue(0, pos);
			
		fastrak[port].getOrientation(receiver, rot);
		rot[0] = (float)Math.toRadians(rot[0]);
		rot[1] = (float)Math.toRadians(rot[1]);
		rot[2] = (float)Math.toRadians(rot[2]);
		sendOutNodeValue(1, rot);
	}

}
