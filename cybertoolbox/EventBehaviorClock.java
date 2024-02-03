/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1999
*
*	File:	EventBehaviorClock.java
*
******************************************************************/

public class EventBehaviorClock extends EventBehavior {
		
	private Event	clockEvent;
	private long	sleepTime;
	private int		frameCnt;

	public EventBehaviorClock(Event event) {
		setEvent(event);
		clockEvent = event;
		
		sleepTime = -1;
		try {
			String clockTimeString = event.getOptionString();
			sleepTime = (long)(Double.parseDouble(clockTimeString) * 1000.0);
		} catch (NumberFormatException nfe) {};
	}

	public void initialize() {
		frameCnt = 1;
	}
	
	public boolean processStimulus() {
		if (clockEvent == null)
			return false;
		
		if (sleepTime < 0)
			return false;
			
		//yield();
		try {
			getThread().sleep(sleepTime);
		} catch (InterruptedException e) {}
		
		clockEvent.run(Integer.toString(frameCnt));
		
		frameCnt++;
			
		return true;
	}
	
}