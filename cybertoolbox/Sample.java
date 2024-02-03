/******************************************************************
*
*	Copyright (C) Satoshi Konno 1999
*
*	File : Sample.java
*
******************************************************************/

public class Sample {

	public static void main(String args[]) {
		Polhemus sensor = new Fastrak(Polhemus.SERIALPORT1, 19200);
		System.out.println("ActiveNReceiver = " + sensor.getActiveReceivers());
		float pos[] = new float[3];
		float ori[] = new float[3];
	    for (int n=0; n<100; n++) {
			sensor.getPosition(1, pos);
			sensor.getOrientation(1, ori);
			System.out.println("R1 : " + pos[0] + ", " + pos[1] + ", " + pos[2] + ", "  + ori[0] + ", " + ori[1] + ", " + ori[2]);
		};
	}

}
