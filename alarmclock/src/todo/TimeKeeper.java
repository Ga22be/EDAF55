package todo;

import se.lth.cs.realtime.semaphore.MutexSem;

public class TimeKeeper {
	
	private MutexSem mutex;
	private int currentHH = 0;
	private int currentMM = 0;
	private int currentSS = 0;
	private int alarmTime = 0;
	private boolean alarmSet = false;
	private boolean beeping = false;
	private int beeps = 0;
	
	public TimeKeeper() {
		mutex = new MutexSem();
	}
	
	public int getTime() {
		mutex.take();
		int temp = currentHH * 10000 + currentMM * 100 + currentSS;
		mutex.give();
		return temp;
	}
	
	public void setTime(int time) {
		mutex.take();
		currentHH = time / 10000;
		currentMM = time / 100 - currentHH * 100;
		currentSS = time - currentHH * 10000 - currentMM * 100;
		mutex.give();
	}
	
	public void incrememnt() {
		mutex.take();
		currentSS++;
		if(currentSS == 60) {
			currentSS = 0;
			currentMM++;
		}
		if(currentMM == 60) {
			currentMM = 0;
			currentHH++;
		}
		if(currentHH == 24) {
			currentHH = 0;
		}
		//System.out.println("New time: " + currentHH + ":" + currentMM + ":" + currentSS);
		mutex.give();
	}
	
	public void activateBeeping() {
		mutex.take();
		beeping = true;
		mutex.give();
	}
	
	public boolean isBeeping() {
		mutex.take();
		if (beeping && beeps++ < 20) {
			// Beeping comences
		} else {
			beeping = false;
			beeps = 0;			
		}
		boolean temp = beeping;
		mutex.give();
		return temp;
	}
	
	public void setAlarmTime(int alarmTime) {
		mutex.take();
		this.alarmTime = alarmTime;
		mutex.give();
	}
	
	public void resetAlarm() {
		if(beeping) {
			beeping = false;
			beeps = 0;
		}
	}
	
	public boolean isAlarm() {
		mutex.take();
		boolean temp = alarmTime == getTime() && alarmSet;
		mutex.give();
		return temp;
	}
	
	public void setAlarmState(boolean alarmSet) {
		mutex.take();
		this.alarmSet = alarmSet;
		mutex.give();
	}

}
