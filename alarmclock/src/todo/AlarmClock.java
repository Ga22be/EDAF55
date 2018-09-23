package todo;
import done.*;

public class AlarmClock extends Thread {

	private static ClockInput	input;
	private static ClockOutput	output;
	private static TimeKeeper	keeper;

	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
		keeper = new TimeKeeper();
	}

	// The AlarmClock thread is started by the simulator. No
	// need to start it by yourself, if you do you will get
	// an IllegalThreadStateException. The implementation
	// below is a simple alarmclock thread that beeps upon 
	// each keypress. To be modified in the lab.
	public void run() {
		// Create two threads!
		Thread time = new TimeThread(output, keeper);
		Thread control = new ControlThread(input, keeper);
		time.start();
		control.start();
	}
}
