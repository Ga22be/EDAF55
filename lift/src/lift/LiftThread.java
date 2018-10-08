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
		while (true) {
			//mon.arrived();
			int next = mon.startMove();
			mon.moveLift(currentFloor, next);
			currentFloor = next;
			mon.stopMove();
			long sleep = (long)(Math.random() * 500);
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
