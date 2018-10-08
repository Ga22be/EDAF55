package lift;

public class PersonThread extends Thread {
	// Person wishing to travel
	private LiftMonitor mon;
	
	private int id;
	private int currentFloor;
	private int targetFloor;
	
	public PersonThread(LiftMonitor monitor, int id) {
		super("Person-" + id);
		mon = monitor;
		this.id = id;
	}
	
	public void run() {
		while (true) {
			long sleep = (long)(Math.random() * 46000);
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			do {
				currentFloor = (int) Math.round(Math.random() * 6);
				targetFloor = (int) Math.round(Math.random() * 6);
			} while (currentFloor == targetFloor);
			System.out.println(currentFloor + "-->" + targetFloor);
			mon.travel(currentFloor, targetFloor);
			System.out.println("Trip finished, sleeping");
		}
	}
	
}
