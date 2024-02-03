public class SensorJoystick extends Module {
	
	Joystick	joy;
	int		port;
	
	float 	trans[] = new float[2];
	int		button;
	
	public void initialize() {
		joy = null;
		
		port = -1;
		
		String portName = getStringValue();
		if (portName != null) {
			if (portName.equalsIgnoreCase("PORT1") == true)
				port = 0;
			if (portName.equalsIgnoreCase("PORT2") == true)
				port = 1;
		}

		if (0 <= port) {		
			try {
				joy = new Joystick(port);
			}
			catch (SecurityException se) {}
			catch (UnsatisfiedLinkError ule) {}
		}
	}
	
	public void shutdown() {
	}

	public void processData(ModuleNode inNode[], ModuleNode exeNode) {
		if (joy == null)
			return;
		
		button = joy.getButtons();
		if ((button & Joystick.BUTTON1) != 0)
			sendOutNodeValue(0, "BUTTON1");
		else if ((button & Joystick.BUTTON2) != 0)
			sendOutNodeValue(0, "BUTTON2");
		else if ((button & Joystick.BUTTON3) != 0)
			sendOutNodeValue(0, "BUTTON3");
		else if ((button & Joystick.BUTTON4) != 0)
			sendOutNodeValue(0, "BUTTON4");
		else
			sendOutNodeValue(0, "NONE");
		
		trans[0] = joy.getXPos();
		trans[1] = joy.getYPos();
		sendOutNodeValue(1, trans);
	}
	
}
