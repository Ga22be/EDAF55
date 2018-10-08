package lift;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Occurs;

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
	
	synchronized public int startMove() {
		while((waitEntry[here] > 0 && load < 4) || waitExit[here] != 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	synchronized public void travel(int current, int target) {
		System.out.println("New trip planned");
		waitEntry[current]++;
		view.drawLevel(current, waitEntry[current]);
		System.out.println("Place taken on start level");
		while (here != next  || current != here || load == 4) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Lift arrived");
		view.drawLift(current, ++load);
		view.drawLevel(current, --waitEntry[here]);
		waitExit[target]++;
		System.out.println("Entered lift");
		while (here != target) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Lift arrived to target floor");
		view.drawLift(target, --load);
		waitExit[target]--;
		
		notifyAll();
	}
	
	private boolean emptySystem() {
		boolean emptySystem = true;
		for (int i : waitEntry) {
			if (i > 0) {
				emptySystem = false;
				//goto would be nice -> end
				break;
			}
		}
		for (int j : waitExit) {
			if (j > 0) {
				emptySystem = false;
				break;
			}
		}
		end: return emptySystem;
	}


}
