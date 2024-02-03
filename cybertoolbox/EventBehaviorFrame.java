/******************************************************************
*
*	CyberToolBox for Java3D
*
*	Copyright (C) Satoshi Konno 1999
*
*	File:	Behavior.java
*
******************************************************************/

public class EventBehaviorFrame extends EventBehavior {
		
	private Event	frameEvent;
	private int		frameCnt;

	public EventBehaviorFrame(Event event) {
		setEvent(event);
		frameEvent = event;
	}

	public void initialize() {
		frameCnt = 1;
	}
	
	public boolean processStimulus() {
		if (frameEvent == null)
			return false;
				
		//yield();
		try {
			getThread().sleep(100);
		} catch (InterruptedException e) {}
		
		frameEvent.run(Integer.toString(frameCnt));
		
		frameCnt++;
			
		return true;
	}
	
}