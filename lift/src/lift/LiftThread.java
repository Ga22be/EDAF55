package lift;

public class LiftThread extends Thread {
	// The lift
	private LiftMonitor mon;
	private int currentFloor = 0;
	
	public LiftThread(LiftMonitor monitor) {
		super("Lift");
		mon = monitor;
	}
	
	public void run() {
		try {		
			while (true) {
				int next = mon.startMove();
				mon.moveLift(currentFloor, next);
				currentFloor = next;
				mon.stopMove();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
