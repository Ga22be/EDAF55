package lift;

public class PersonThread extends Thread {
	// Person wishing to travel
	private LiftMonitor mon;
	
	private int currentFloor;
	private int targetFloor;
	
	public PersonThread(LiftMonitor monitor, int id) {
		super("Person-" + id);
		mon = monitor;
	}
	
	public void run() {
		try {
			while (true) {
				long sleep = (long)(Math.random() * 46000);
				Thread.sleep(sleep);
				do {
					currentFloor = (int) Math.round(Math.random() * 6);
					targetFloor = (int) Math.round(Math.random() * 6);
				} while (currentFloor == targetFloor);
				System.out.println(currentFloor + "-->" + targetFloor);
				mon.travel(currentFloor, targetFloor);
				System.out.println("Trip finished, sleeping");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
