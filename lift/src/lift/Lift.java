package lift;

public class Lift {

	public static void main(String[] args) {
		// Initialize system
		LiftView view = new LiftView();
		
		LiftMonitor monitor = new LiftMonitor(view);
		
		LiftThread lift = new LiftThread(monitor);
		int people = 20;
		PersonThread persons[] = new PersonThread[people];
		for (int i = 0; i < people; i++) {
			persons[i] = new PersonThread(monitor, i);
			persons[i].start();
		}
		
		lift.start();
	}
	
}
