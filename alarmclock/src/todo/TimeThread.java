package todo;

import done.ClockOutput;

public class TimeThread extends Thread {
	
	private static ClockOutput	output;
	private static TimeKeeper	keeper;
	
	public TimeThread(ClockOutput co, TimeKeeper tKeeper) {
		output = co;
		keeper = tKeeper;
	}
	
	public void run() {
		long timeNill = System.currentTimeMillis();
		while(true) {
			timeNill+=1000;
			try {
//				monitorSleep(1000);
				sleep(timeNill-System.currentTimeMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			keeper.incrememnt();
			if(keeper.isAlarm()) {
				keeper.activateBeeping();
			}
			if(keeper.isBeeping()) {
				output.doAlarm();
			}
			output.showTime(keeper.getTime());
		}
	}
	
	private synchronized void monitorSleep(long timeout) throws InterruptedException {
		long tf = System.currentTimeMillis()+timeout;
		while ((timeout=tf-System.currentTimeMillis())>0) wait(timeout);
	}

}
