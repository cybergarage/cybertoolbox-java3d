/*----------------------------------------------------------------
*
*	CyberToolBox for Java
*
*	Copyright (C) Satoshi Konno 1998-1999
*
*	File:	SensorMouse.java
*
----------------------------------------------------------------*/

public class SensorMagellan extends Module {

	Magellan			magellan;
	int				port;
	int				button;
	int				trans[] = new int[3];			 	
	float				rot[] = new float[3];
				 	
	public void initialize() {
		magellan = null;
		port = -1;
		
		String portName = getStringValue();
		if (portName != null) {
			if (portName.equalsIgnoreCase("SERIAL1") == true)
				port = Magellan.SERIALPORT1;
			if (portName.equalsIgnoreCase("SERIAL2") == true)
				port = Magellan.SERIALPORT2;
			if (portName.equalsIgnoreCase("SERIAL3") == true)
				port = Magellan.SERIALPORT3;
			if (portName.equalsIgnoreCase("SERIAL4") == true)
				port = Magellan.SERIALPORT4;
		}
		
		if (0 <= port) {
			magellan = new Magellan(port);
			magellan.start();
		}
	}

	public void shutdown() {
		if (magellan == null)
			return;
			
		magellan.stop();
		magellan.close();
		magellan = null;
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		if (magellan == null)
			return;
			
		int button = magellan.getButton();
		if ((button & Magellan.BUTTON1) != 0)
			sendOutNodeValue(0, "BUTTON1");
		else if ((button & Magellan.BUTTON2) != 0)
			sendOutNodeValue(0, "BUTTON2");
		else if ((button & Magellan.BUTTON3) != 0)
			sendOutNodeValue(0, "BUTTON3");
		else if ((button & Magellan.BUTTON4) != 0)
			sendOutNodeValue(0, "BUTTON4");
		else if ((button & Magellan.BUTTON5) != 0)
			sendOutNodeValue(0, "BUTTON5");
		else if ((button & Magellan.BUTTON6) != 0)
			sendOutNodeValue(0, "BUTTON6");
		else if ((button & Magellan.BUTTON7) != 0)
			sendOutNodeValue(0, "BUTTON7");
		else if ((button & Magellan.BUTTON8) != 0)
			sendOutNodeValue(0, "BUTTON8");
		else if ((button & Magellan.BUTTONA) != 0)
			sendOutNodeValue(0, "BUTTONA");
		else
			sendOutNodeValue(0, "NONE");
			
		magellan.getTranslation(trans);
		sendOutNodeValue(1, trans);

		magellan.getRotation(rot);
		sendOutNodeValue(2, rot);
	}

}
