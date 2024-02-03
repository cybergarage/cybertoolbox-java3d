/******************************************************************
*
*	Copyright (C) Satoshi Konno 1999
*
*	File : Fastrak.java
*
******************************************************************/

public class Fastrak extends Polhemus {

	public Fastrak(String deviceName, int baudrate) {
		super(deviceName, baudrate);
	}

	public Fastrak(int device, int baudrate) {
		super(device, baudrate);
	}

	public int readActiveReceivers() {
		int nReceiver = 0;
		write(activeStationStateCommand);
		byte data[] = read(9).getBytes();
		if (data.length == 9) {
			for (int n=0; n<4; n++) {
				if (data[n+3] == '1')
			    nReceiver++;	
			}
		}
		return nReceiver;
	}

	public void setReceiverOutputFormat() {
	    for (int n=1; n<=4; n++) {
			String commSetRecvN = "O" + n + ",2,0,4,1\r";
			write(commSetRecvN);
	    }
	}

	public int getDeviceDataLength() {
		return 48;
	}

	public int getDevicePositionDataOffset() {
		return 3;
	}

	public int getDeviceRotationDataOffset() {
		return 25;
	}
}
