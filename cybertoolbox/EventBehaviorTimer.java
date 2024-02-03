/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1999
*
*	File:	EventBehaviorTimer.java
*
******************************************************************/

public class EventBehaviorTimer extends EventBehavior {
		
	private Event	timerEvent;
	private int		sleepTime;
	private int		frameCnt;

	public EventBehaviorTimer(Event event) {
		setEvent(event);
		timerEvent = event;
		
		sleepTime = -1;
		try {
			String timerTimeString = event.getOptionString();
			sleepTime = (int)(Double.parseDouble(timerTimeString) * 1000.0);
		} catch (NumberFormatException nfe) {};
	}

	public void initialize() {
		frameCnt = 0;
	}
	
	public boolean processStimulus() {
		if (timerEvent == null)
			return false;
		
		if (sleepTime < 0)
			return false;
			
		//yield();
		try {
			getThread().sleep(sleepTime);
		} catch (InterruptedException e) {}
		
		timerEvent.run();
		
		return true;
	}
	
}