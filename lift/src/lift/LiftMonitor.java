package lift;

public class LiftMonitor {
	// Do monitoring
	private static int LIFT_CAPACITY = 4;
	private static int FLOORS = 7;
	
	private LiftView view;
	private int direction = 1;
	
	private int here;			// If here!=next , here (floor number) tells from which floor
								// the lift is moving and next to which floor it is moving.
	private int next;			// If here==next , the lift is standing still on the floor
								// given by here.
	private int[] waitEntry;	// The number of persons waiting to enter the lift at the
								// various floors.
	private int[] waitExit; 	// The number of persons (inside the lift) waiting to leave
								// the lift at the various floors.
	private int load; 			// The number of people currently occupying the lift
	
	public LiftMonitor(LiftView view) {
		this.view = view;
		waitEntry = new int[FLOORS];
		waitExit = new int[FLOORS];
	}
	 
	public void moveLift(int current, int target) {
		view.moveLift(current, target);
	}
	
	synchronized public int startMove() throws InterruptedException {
		while((waitEntry[here] > 0 && load < LIFT_CAPACITY) || waitExit[here] != 0 || emptySystem()) {
			wait();
		}
		return next += direction;			
	}
		
	synchronized public void stopMove() {
		here += direction;
		if (here == 6 || here == 0) {
			direction *= -1;
		}
		notifyAll();
	}
	
	synchronized public void travel(int current, int target) throws InterruptedException {
		waitEntry[current]++;
		view.drawLevel(current, waitEntry[current]);
		notifyAll();
		while (here != next  || current != here || load == LIFT_CAPACITY) {
			wait();
		}
		view.drawLift(current, ++load);
		view.drawLevel(current, --waitEntry[here]);
		waitExit[target]++;
		notifyAll();
		while (here != next  || here != target) {
			wait();
		}
		view.drawLift(target, --load);
		waitExit[target]--;
		
		notifyAll();
	}
	
	private boolean emptySystem() {
		for (int i : waitEntry) {
			if (i > 0) {
				return false;
			}
		}
		for (int j : waitExit) {
			if (j > 0) {
				return false;
			}
		}
		return true;
	}


}
