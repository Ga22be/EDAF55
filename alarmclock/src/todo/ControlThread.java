package todo;

import done.ClockInput;
import se.lth.cs.realtime.semaphore.Semaphore;

public class ControlThread extends Thread {
	
	private static ClockInput	input;
	private static Semaphore	sem;
	private static TimeKeeper	keeper;
	
	public ControlThread(ClockInput ci, TimeKeeper tKeeper) {
		input = ci;
		keeper = tKeeper;
		sem = input.getSemaphoreInstance();
	}
	
	public void run() {
//		System.out.println("Control loop running");
		int prevState = 0;
		while(true) {
			sem.take();
			int state = input.getChoice();
			keeper.resetAlarm();
			// Check if set time has been invoked
			if (state != prevState && prevState == ClockInput.SET_TIME) {
				keeper.setTime(input.getValue());
			} 
			// Check if set alarm has been invoked
			else if (state != prevState && prevState == ClockInput.SET_ALARM) {
				keeper.setAlarmTime(input.getValue());
			} 
			// Check if default show time
			else if (state == ClockInput.SHOW_TIME) {
				// Do nothing?
			}
			// update alarm active state
			keeper.setAlarmState(input.getAlarmFlag());
			prevState = state;
		}
	}
}
